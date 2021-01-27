/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.nodes.JNodeTokens;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.*;
import net.thevpc.common.textsource.log.JSourceMessage;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author thevpc
 */
public class DefaultJParser<T extends JNode,O extends JExpressionOptions<?>> implements JParser<T> {

    private JTokenizer tokenizer;
    private JContext context;
    private JParserNodeFactory<T> nodeFactory;
    private JCompilationUnit compilationUnit;
    private Stack<T> declarationContexts = new Stack<>();
    private O defaultExpressionOptions;

    public DefaultJParser(JTokenizer tokenizer, JCompilationUnit compilationUnit, JContext context) {
        this(tokenizer, compilationUnit, context,null);
    }

    public DefaultJParser(JTokenizer tokenizer, JCompilationUnit compilationUnit, JContext context, JParserNodeFactory<T> nodeFactory) {
        this.context = context;
        this.compilationUnit = compilationUnit;
        this.tokenizer = tokenizer;
        this.nodeFactory = nodeFactory==null?(JParserNodeFactory<T>) new SimpleNodeNodeFactory():nodeFactory;
    }


    public O getDefaultExpressionOptions() {
        return defaultExpressionOptions;
    }

    public DefaultJParser<T,O> setDefaultExpressionOptions(O defaultExpressionOptions) {
        this.defaultExpressionOptions = defaultExpressionOptions;
        return this;
    }

    public JParserNodeFactory<T> getNodeFactory() {
        return nodeFactory;
    }

    public void setNodeFactory(JParserNodeFactory<T> nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    @Override
    public JCompilationUnit compilationUnit() {
        return compilationUnit;
    }

    public T parse() {
        return parseExpression();
    }

    public JCompilerLog log() {
        return context.log();
    }

    public JContext context() {
        return context;
    }

    public JTokenizer tokenizer() {
        return tokenizer;
    }

//    protected T parseFunctionCall(String name, JToken startToken) {
//        //this is a function
//        List<T> args = parseParsList("function/method call", "function argument", () -> parseExpression());
//        T nn = getNodeFactory().createFunctionCallNode(name, args.toArray(new T[0]), startToken);
//        nn.setStartToken(startToken);
//        return nn;
//    }

    protected String readLongWord() {
        JToken i = peek();
        if (i.isIdentifier()) {
            StringBuilder sb = new StringBuilder();
            sb.append(i.sval);
            return sb.toString();
        }
        return null;
    }

    protected T parseExpressionPars() {
        return parseParenthesis("parenthesis");
    }

    protected T parseExpressionUnaryPrefix(int opPrecedence, O options) {
        JToken token = peek();
        if (token.isEOF()) {
            return null;
        }
        JExpressionUnaryOptions unary = options == null ? null : options.getUnary();
        if (isVisitSupportedPrefixUnaryOperator(token, unary)) {
            skip();
            int prefixOpPrecedence = context.operators().getOperatorPrecedence(JOperator.prefix(token.image));
            T o = parseExpressionBinary(prefixOpPrecedence, options);
            if (o == null) {
                //this was an expression not a prefix
                log().error("X001", null, "expected expression", peek());
            } else {
                return getNodeFactory().createPrefixUnaryOperatorNode(token, o
                        , new JNodeTokens()
                                .setStart(token)
                                .setEnd(o.getEndToken())
                );
            }
        } else if (isVisitSupportedPrefixPars(token, unary)) {
            T n = parsePrefixParsNode(options);
            if (n != null) {
                return n;
            }
        } else if (isVisitSupportedPrefixBrackets(token, unary)) {
            try (JTokenizerSnapshot snapshot = tokenizer().snapshot()) {
                T o1 = parsePrefixBracketsNode(options);
                if(o1==null){
                    //
                }else {
                    T arg = parseExpressionBinary(Integer.MAX_VALUE, options);
                    if (arg == null) {
                        snapshot.rollback();
                    } else {
                        return getNodeFactory().createPrefixBracketsNode(o1, arg,
                                new JNodeTokens()
                                        .setStart(token).setEnd(arg.getEndToken())
                        );
                    }
                }
            }
        } else if (isVisitSupportedPrefixBraces(token, unary)) {
            try (JTokenizerSnapshot snapshot = tokenizer().snapshot()) {
                T o1 = parsePrefixBracesNode(options);
                if(o1==null){
                    return null;
                }
                T o = parseExpressionBinary(Integer.MAX_VALUE, options);
                if (o == null) {
                    snapshot.rollback();
                } else {
                    return getNodeFactory().createPrefixBracesNode(o, o1,
                            new JNodeTokens().setStart(token).setEnd(o.getEndToken()));
                }
            }
        } else if (isVisitSupportedAnnotation(token, unary)) {
            try (JTokenizerSnapshot snapshot = tokenizer().snapshot()) {
                T ann = parseAnnotations(opPrecedence, options);
                if (ann == null) {
                    //ignore, and parse middle later
                } else {
                    T o = parseExpressionBinary(opPrecedence, options);
                    if (o == null) {
                        snapshot.rollback();
                    } else {
                        return getNodeFactory().createAnnotatedNode(o, ann,
                                new JNodeTokens().setStart(token).setEnd(o.getEndToken()));
                    }
                }
            }
        }
        return null;
    }

    protected T parseExpressionUnary(int opPrecedence, O options) {
        JToken token = peek();
        if (token.isEOF()) {
            return null;
        }
        if (token.isImage(")") || token.isImage("]") || token.isImage("}")) {
            return null;
        }
        ParseExpressionUnaryContext ucontext = new ParseExpressionUnaryContext();

        JExpressionUnaryOptions unary = options == null ? null : options.getUnary();
        ucontext.unary = unary;

        T prefixRightAssociative = parseExpressionUnaryPrefix(opPrecedence, options);
        if(prefixRightAssociative!=null){
            return prefixRightAssociative;
        }

        T middle = parseExpressionUnaryTerminal(options);
        if (middle == null) {
            return null;
        }

        while (true) {
            T r = parseExpressionUnarySuffix(opPrecedence, middle, options, ucontext);
            if (r == null) {
                break;
            } else {
                middle = r;
                if (ucontext.doBreak) {
                    break;
                }
            }
        }
        return middle;
    }

    protected T parseAnnotations(int opPrecedence, O options) {
        return null;
    }

    protected boolean isVisitSupportedPrefixBraces(JToken token, JExpressionUnaryOptions unary) {
        return (unary == null || !unary.isExcludedPrefixBraces())
                && token.isImage("{");
    }

    protected boolean isSupportedAnnotation(JExpressionUnaryOptions unary) {
        return (unary == null || !unary.isExcludedAnnotations());
    }

    protected boolean isVisitSupportedAnnotation(JToken token, JExpressionUnaryOptions unary) {
        return isSupportedAnnotation(unary) && token.isImage("@");
    }

    protected boolean isSupportedTerminalBraces(JToken token, JExpressionUnaryOptions unary) {
        return (unary == null || !unary.isExcludedTerminalBraces())
                && token.isImage("{");
    }

    protected boolean isSupportedTerminalBrackets(JToken token, JExpressionUnaryOptions unary) {
        return (unary == null || !unary.isExcludedTerminalBrackets())
                && token.isImage("[");
    }

    protected boolean isSupportedTerminalPars(JToken token, JExpressionUnaryOptions unary) {
        return (unary == null || !unary.isExcludedTerminalParenthesis())
                && token.isImage("(");
    }

    protected boolean isVisitSupportedPrefixBrackets(JToken token, JExpressionUnaryOptions unary) {
        return (unary == null || !unary.isExcludedPrefixBrackets())
                && token.isImage("[");
    }

    protected boolean isVisitSupportedPrefixPars(JToken token, JExpressionUnaryOptions unary) {
        return (unary == null || !unary.isExcludedPrefixParenthesis())
                && token.isImage("(");
    }

    protected boolean isVisitSupportedPrefixUnaryOperator(JToken token, JExpressionUnaryOptions unary) {
        return (unary == null || unary.getExcludedPrefixUnaryOperators() == null
                || !unary.getExcludedPrefixUnaryOperators().contains(token.image))
                && context().operators().isPrefixUnaryOperator(token.image);
    }

    protected T parseExpressionUnarySuffix(int opPrecedence, T middle, O options, ParseExpressionUnaryContext ucontext) {
        JExpressionUnaryOptions unary = ucontext.unary;
        JToken token = next();
        if ((unary == null || unary.getExcludedPostfixUnaryOperators() == null
                || !unary.getExcludedPostfixUnaryOperators().contains(token.image))
                && context().operators().isPostfixUnaryOperator(token.image)) {
            int binaryOpPrecedence = context().operators().getOperatorPrecedence(JOperator.infix(token.image));
            if (binaryOpPrecedence > opPrecedence) {
                ucontext.doBreak = true;
                return getNodeFactory().createPostfixUnaryOperatorNode(token, middle
                        , new JNodeTokens().setStart(middle.getEndToken()).setEnd(token)
                );
            } else {
                pushBack(token);
                return null;
            }
        } else {
            if ((unary == null || !unary.isExcludedPostfixParenthesis())
                    && token.isImage("(")) {
                JToken copy = token.copy();
                pushBack(token);
                return middle = parsePostfixParsNode(middle, copy);
            } else if ((unary == null || !unary.isExcludedPostfixBrackets())
                    && token.isImage("[")) {
                JToken copy = token.copy();
                pushBack(token);
                return middle = parsePostfixBracketsNode(middle, copy);
            } else if ((unary == null || !unary.isExcludedPostfixBraces())
                    && token.isImage("{")) {
                JToken copy = token.copy();
                pushBack(token);
                return middle = parsePostfixBracesNode(middle, copy);
            } else {
                pushBack(token);
                return null;
            }
        }
    }

    public T parsePrefixParsNodePars() {
        return parseParenthesis("cast");
    }

    public T parsePrefixBracketsNode(O options) {
        return parseBrackets();
    }

    public T parsePrefixBracesNode(O options) {
        return parseBraces();
    }

    public T parsePrefixParsNode(O options) {
        JToken t = peek().copy();
        if (!t.isImage("(")) {
            return null;
        }
        try (JTokenizerSnapshot snapshot = tokenizer().snapshot()) {
            T o1 = parsePrefixParsNodePars();
            if (o1 != null && o1.getChildrenNodes().size() == 1) {
                T arg = parseExpressionBinary(Integer.MAX_VALUE, options);
                if (arg != null) {
                    return getNodeFactory().createPrefixParenthesisNode(o1, arg,
                            new JNodeTokens()
                                    .addSeparator(o1.getEndToken())
                                    .setStart(t).setEnd(arg.getEndToken())

                    );
                }
            }
            snapshot.rollback();
        }
        return null;
    }

    public T parsePostfixParsNodePars() {
        return parseParenthesis("function/method call");
    }

    public T parsePostfixParsNode(T middle, JToken copy) {
        T p = parsePostfixParsNodePars();
        return getNodeFactory().createPostfixParenthesisNode(middle, p,
                new JNodeTokens().setStart(middle.getStartToken()).setEnd(p.getEndToken()));
    }

    public T parsePostfixBracketsNode(T middle, JToken copy) {
        T p = parseBrackets();
        return getNodeFactory().createPostfixBracketsNode(middle, p,
                new JNodeTokens()
                        .setStart(middle.getStartToken())
                        .setEnd(p.getEndToken())
        );
    }

    public T parsePostfixBracesNode(T middle, JToken copy) {
        T p = parseBraces();
        return getNodeFactory().createPostfixBracesNode(middle, p,
                new JNodeTokens().setStart(middle.getStartToken()).setEnd(p.getEndToken())
        );
    }

    protected T parseBrackets() {
        JToken startToken = peek().copy();
        JListWithSeparators<T> elements = parseGroupedList("brackets", "brackets element", this::parseExpression, "[", ",", "]", null);
        return getNodeFactory().createBracketsNode(elements.getItems()
                , new JNodeTokens().setStart(elements.getStartToken()).setEnd(elements.getEndToken())
                        .setSeparators(elements.getSeparatorTokens())
        );
    }

    public T parseBraces() {
        JToken startToken = peek().copy();
        JListWithSeparators<T> elements = parseGroupedList("braces", "braces element", this::parseExpression, "{", ",", "}", null);
        return getNodeFactory().createBracesNode(elements.getItems()
                , new JNodeTokens().setStart(elements.getStartToken()).setEnd(elements.getEndToken())
                        .setSeparators(elements.getSeparatorTokens())
        );
    }

    protected T parseAndBuildExpressionBinary(JToken op, T o1, int opPrecedence, O options) {
        T o2 = parseExpressionBinary(opPrecedence, options);
        if (o2 == null) {
            if (peek().isEOF()) {
                log().error("X002", null, "missing right operand for " + op.image, peek());
            } else {
                log().error("X003", null, "invalid right operand for " + op.image, peek());
            }
        }
        JToken s = o1.getStartToken();
        JToken e = o2 != null ? o2.getEndToken() : op;
        return getNodeFactory().createBinaryOperatorNode(op, o1, o2,
                new JNodeTokens().setStart(s).setEnd(e).addSeparator(op)
        );
    }

    protected T parseExpressionBinary(int opPrecedence, O options) {
        T o1 = parseExpressionUnary(opPrecedence, options);
        if (o1 == null) {
            return null;
        }
        JExpressionBinaryOptions binary = options == null ? null : options.getBinary();
        while (true) {
            JToken token = next();
            if (token.isEOF()) {
                return o1;
            }
            if (token.isImage(")")) {
                pushBack(token);
                return o1;
            }
            if (token.isImage("]")) {
                pushBack(token);
                return o1;
            }
            if (token.isImage("}")) {
                pushBack(token);
                return o1;
            }
//            if (token.ttype == '[') {
//                JToken s = o1 != null && o1.token() != null ? o1.token() : token;
//                T jNode = parseExpression();
//                JToken closing = next();
//                if (closing.ttype != ']') {
//                    //pushBack(token);
//                    log().error("X056", "expected ']'", closing);
//                }
//                o1 = getNodeFactory().createBracketsPostfixNode(o1, jNode, s);
//            } else if (token.ttype == '(') {
//                JToken s = o1 != null && o1.token() != null ? o1.token() : token;
//                T jNode = parseExpression();
//                JToken closing = next();
//                if (closing.ttype != ')') {
//                    //pushBack(token);
//                    log().error("X057", "expected ')'", closing);
//                }
//                o1 = getNodeFactory().createParsPostfixNode(o1, jNode, s);
//            } else if (context.operators().isPostfixUnaryOperator(token.image)) {
//                long binaryOpPrecedence = context.operators().getOperatorPrecedence(token.image);
//                if (binaryOpPrecedence > opPrecedence) {
//                    // a binary ?
//                    o1 = getNodeFactory().createSuffixUnaryOperatorNode(o1, token);
//                } else {
//                    pushBack(token);
//                    return o1;
//                }
////            } else if (definition.isListSeparator(token.image)) {
////                tokenizer.pushBack(token);
////                return o1;
            /*} else */
            Integer binaryPrecedence;
            if (isEnabledBinaryOperator(binary, token) && (binaryPrecedence = resolveBinaryOperatorPrecedence(token)) != null) {
                int binaryOpPrecedence = binaryPrecedence;
                if (binaryOpPrecedence > opPrecedence) {
                    o1 = parseAndBuildExpressionBinary(token, o1, binaryOpPrecedence, options);
                } else {
                    pushBack(token);
                    return o1;
                }
            } else if (isEnabledBinaryListOperator(binary) && isListSeparator(token)) {
                o1 = parseAndBuildListOpNodeElement(o1, opPrecedence, token, options);
            } else if (isEnabledBinaryImplicitOperator(binary)
                    && (binaryPrecedence = resolveBinaryImplicitOperatorPrecedence()) != null
            ) {
                //inject implicit op
                int binaryOpPrecedence = binaryPrecedence;
                if (binaryOpPrecedence > opPrecedence) {
                    pushBack(token);
                    JToken s = o1 != null && o1.getStartToken() != null ? o1.getStartToken() : token;
                    T o2 = parseExpressionBinary(binaryOpPrecedence, options);
                    if (o2 == null) {
                        log().error("S004", null, "missing second operand for implicit operator", s);
                    }
                    o1 = getNodeFactory().createImplicitOperatorNode(o1, o2,
                            new JNodeTokens().setStart(s).setEnd(o2 == null ? s : o2.getEndToken())
                    );
                } else {
                    pushBack(token);
                    return o1;
                }
            } else {
                pushBack(token);
                return o1;
            }
        }
    }

    private boolean isEnabledBinaryImplicitOperator(JExpressionBinaryOptions binary) {
        return binary == null || !binary.isExcludedImplicitOperator();
    }

    private boolean isListSeparator(JToken token) {
        return context.operators().isListSeparator(token.image);
    }

    private boolean isEnabledBinaryListOperator(JExpressionBinaryOptions binary) {
        return binary == null || !binary.isExcludedListOperator();
    }

    protected boolean isEnabledBinaryOperator(JExpressionBinaryOptions binary, JToken token) {
        if ((binary == null || binary.getExcludedBinaryOperators() == null || !binary.getExcludedBinaryOperators().contains(token.image))) {
            return true;
        }
        return false;
    }

    protected Integer resolveBinaryImplicitOperatorPrecedence() {
        if (!isSupportedImplicitOperator()) {
            return null;
        }
        return context.operators().getOperatorPrecedence(JOperator.implicitList());
    }

    /**
     * resolve To Binary Operator And Return Precedence.
     * If not a Binary operator return null;
     *
     * @param token
     * @return
     */
    protected Integer resolveBinaryOperatorPrecedence(JToken token) {
        if (context.operators().isBinaryOperator(token.image)) {
            return context.operators().getOperatorPrecedence(JOperator.infix(token.image));
        }
        return null;
    }

    protected T parseAndBuildListOpNodeElement(T o1, int opPrecedence, JToken token, O options) {
        token = token.copy();
        T o2 = parseExpressionBinary(opPrecedence, options);
        JToken s = o1 != null && o1.getStartToken() != null ? o1.getStartToken() : token;
        JToken e = o1 != null && o1.getEndToken() != null ? o1.getEndToken() : token;
        o1 = getNodeFactory().createListOperatorNode(token, Arrays.asList(o1, o2)
                , new JNodeTokens().setStart(s).setEnd(e).addSeparator(token)
        );
        return o1;
    }

    protected boolean isSupportedImplicitOperator() {
        //TODO how to check
        return context.operators().isListSeparator("");
    }

    public T parseExpression() {
        return parseExpression(defaultExpressionOptions);
    }

    public T parseExpression(O options) {
        return parseExpression(-1, options);
    }

    public T parseAnnotations(O options) {
        return parseAnnotations(-1, options);
    }

    public T parseExpression(int opPrecedence, O options) {
        return parseExpressionBinary(opPrecedence, options);
    }

    protected <T1 extends T> JListWithSeparators<T1> parseParsList(String groupName, String elementName, Supplier<T1> elementParser) {
        return parseGroupedList(groupName, elementName, elementParser, "(", ",", ")", null);
    }

    protected <T1 extends T> JListWithSeparators<T1> parseBracketsList(String groupName, String elementName, Supplier<T1> elementParser) {
        return parseGroupedList(groupName, elementName, elementParser, "[", ",", "]", null);
    }

//    protected JToken requireOp(int ttype, String sval) {
//        JToken token = next();
//        if (token.ttype == ttype && token.image.equals(sval)) {
//            return token;
//        }
//        pushBack(token);
//        log().error("X043", "Expected " + sval, token);
//        JToken t2 = token.copy();
//        t2.ttype = ttype;
//        t2.sval = sval;
//        t2.sttype = "<<Error>>";
//        return t2;
//    }

//    protected T[] readUplet(char start, char end, char sep) {
//
//        List<T> all = new ArrayList<>();
//        requireOp(start, String.valueOf(start));
//        JToken peek = peek();
//        if (peek.ttype == end) {
//            requireOp(end, String.valueOf(end));
//        } else {
//            all.add(parseExpression());
//            while (true) {
//                peek = peek();
//                if (peek.ttype == end) {
//                    requireOp(end, String.valueOf(end));
//                    break;
//                } else {
//                    requireOp(sep, String.valueOf(sep));
//                    all.add(parseExpression());
//                }
//            }
//        }
//        return all.toArray(new T[0]);
//    }

    protected T parseCallArgument() {
        return parseExpression();
    }
    protected T parseParenthesis(String name) {
        JToken startToken = peek();
        if (name == null) {
            name = "parenthesis";
        }
        JListWithSeparators<T> elements = parseGroupedList(name, "expression", () -> parseCallArgument(), "(", ",", ")", null);
        if (elements == null) {
            return null;
        }
        return getNodeFactory().createParsNode(elements.getItems()
                , new JNodeTokens().setStart(elements.getStartToken()).setEnd(elements.getEndToken())
                        .setSeparators(elements.getSeparatorTokens())
        );
    }

    /**
     * @param <T1>
     * @param groupName
     * @param elementName
     * @param elementParser
     * @param start
     * @param separator
     * @param end
     * @param silencedMessages when not null, errors are tracked into this list rather than into log()
     * @return
     */
    protected <T1 extends T> JListWithSeparators<T1> parseGroupedList(
            String groupName,
            String elementName,
            Supplier<T1> elementParser,
            String start,
            String separator,
            String end,
            List<JSourceMessage> silencedMessages) {
        if (start != null && start.length() == 0) {
            start = null;
        }
        if (end != null && end.length() == 0) {
            end = null;
        }
        JToken startToken = null;
        JToken endToken = null;
        List<JToken> separators = new ArrayList<>();
        if (start != null) {
            JToken n = next();
            startToken = n.copy();
            endToken = startToken;
            if (!n.image.equals(start)) {
                pushBack(n);
                return null;
            }
        }
        List<T1> list = new ArrayList<>();
        if (separator != null && separator.length() > 0) {
            boolean wasComma = true;
            while (true) {
                JToken n1 = next();
                if (n1.isEOF()) {
                    if (end != null) {
                        if (silencedMessages != null) {
                            silencedMessages.add(JSourceMessage.error("X005", groupName, "expected '" + end + "'", n1));
                        } else {
                            log().error("X005", groupName, "expected '" + end + "'", n1);
                        }
                    }
                    break;
                } else if (n1.image.equals(separator)) {
                    if (wasComma) {
                        if (silencedMessages != null) {
                            silencedMessages.add(JSourceMessage.error("X005", groupName, "expected " + elementName, n1));
                        } else {
                            log().error("X005", groupName, "expected " + elementName, n1);
                        }
                    } else {
                        wasComma = true;
                        endToken = n1.copy();
                    }
                    separators.add(n1);
                    //read next
                } else if (end != null && n1.image.equals(end)) {
                    endToken = n1.copy();
                    break;
                } else {
                    if (!wasComma) {
                        if (end == null) {
                            pushBack(n1);
                            //cannot read more without error
                            break;
                        }
                        if (silencedMessages != null) {
                            silencedMessages.add(JSourceMessage.error("X005", groupName, "expected '" + separator + "'", n1));
                        } else {
                            log().error("X005", groupName, "expected '" + separator + "'", n1);
                        }
                    }
                    wasComma = false;
                    pushBack(n1);
                    T1 t = null;
                    boolean errorReported = false;
                    while (true) {
                        t = elementParser.get();
                        if (t != null) {
                            break;
                        }
//                        t = elementParser.get();
                        JToken toSkip = next();
                        if (!errorReported) {
                            if (silencedMessages != null) {
                                silencedMessages.add(JSourceMessage.error("X005", groupName, "expected " + elementName, toSkip));
                            } else {
                                log().error("X005", groupName, "expected " + elementName, toSkip);
                            }
                            errorReported = true;
                        }
                        if (toSkip.image.equals(separator) || (end != null && toSkip.image.equals(end))) {
                            pushBack(toSkip);
                            break;
                        } else if (toSkip.isEOF()) {
                            break;
                        }
                    }
                    if (t != null) {
                        endToken = t.getEndToken();
                        list.add(t);
                    }

                }
            }
        } else {
            while (true) {
                JToken n1 = next();
                if (n1.isEOF()) {
                    if (end != null) {
                        if (silencedMessages != null) {
                            silencedMessages.add(JSourceMessage.error("X005", groupName, "expected '" + end + "'", n1));
                        } else {
                            log().error("X005", groupName, "expected '" + end + "'", n1);
                        }
                    }
                    break;
                } else if (end != null && n1.image.equals(end)) {
                    endToken = n1.copy();
                    break;
                } else {
                    pushBack(n1);
                    T1 t = null;
                    while (true) {
                        t = elementParser.get();
                        if (t != null) {
                            break;
                        }
                        t = elementParser.get();
                        JToken toSkip = next();
                        if (silencedMessages != null) {
                            silencedMessages.add(JSourceMessage.error("X005", groupName, "expected " + elementName, toSkip));
                        } else {
                            log().error("X005", groupName, "expected " + elementName, toSkip);
                        }
                        if (toSkip.isEOF()) {
                            break;
                        }
                    }
                    if (t != null) {
                        list.add(t);
                    }

                }
            }
        }
        return new DefaultJListWithSeparators<>(list, startToken, separators, endToken);
    }

    public T parseExpressionUnaryTerminal(O options) {
        JToken token = next();
        if (token.isEOF()) {
            return null;
        }
        JExpressionUnaryOptions unary = options == null ? null : options.getUnary();
        if (isSupportedTerminalPars(token, unary)) {
            pushBack(token);
            return parseExpressionPars();
        } else if (isSupportedTerminalBrackets(token, unary)) {
            pushBack(token);
            return parseBrackets();
        } else if (isSupportedTerminalBraces(token, unary)) {
            pushBack(token);
            return parseBraces();
        }
        switch (token.def.ttype) {
            case JTokenType.TT_KEYWORD: {
                switch (token.image) {
                    case "null":
                        return getNodeFactory().createLiteralNode(null,
                                new JNodeTokens().setStart(token).setEnd(token)
                        );
                    case "true":
                        return getNodeFactory().createLiteralNode(true,
                                new JNodeTokens().setStart(token).setEnd(token)
                        );
                    case "false":
                        return getNodeFactory().createLiteralNode(false,
                                new JNodeTokens().setStart(token).setEnd(token)
                        );
                }
                pushBack(token);
                return null;
            }
            case JTokenType.TT_IDENTIFIER: {
                return getNodeFactory().createIdentifierNode(token.sval, new JNodeTokens().setStart(token).setEnd(token));
            }
            case JTokenType.TT_STRING: {
                return getNodeFactory().createLiteralNode(token.sval, new JNodeTokens().setStart(token).setEnd(token));
            }
            case JTokenType.TT_NUMBER: {
                switch (token.def.id) {
                    case JTokenId.NUMBER_INFINITY: {
                        return getNodeFactory().createLiteralNode(Double.POSITIVE_INFINITY,
                                new JNodeTokens().setStart(token).setEnd(token)
                        );
                    }
                    case JTokenId.NUMBER_FLOAT: {
                        //should i test if int ?
                        switch (token.image.charAt(token.image.length() - 1)) {
                            case 'f':
                            case 'F': {
                                return getNodeFactory().createLiteralNode(Float.parseFloat(token.image),
                                        new JNodeTokens().setStart(token).setEnd(token)
                                );
                            }
                        }
                        return getNodeFactory().createLiteralNode(Double.parseDouble(token.image),
                                new JNodeTokens().setStart(token).setEnd(token)
                        );
                    }
                    case JTokenId.NUMBER_INT: {
                        switch (token.image.charAt(token.image.length() - 1)) {
                            case 'l':
                            case 'L': {
                                T i = getNodeFactory().createLiteralNode(Long.parseLong(token.image),
                                        new JNodeTokens().setStart(token).setEnd(token)
                                );
                            }
                        }
                        return getNodeFactory().createLiteralNode(Integer.parseInt(token.image),
                                new JNodeTokens().setStart(token).setEnd(token)
                        );
                    }
                    default: {
                        throw new JShouldNeverHappenException();
                    }
                }
            }
            default: {
                pushBack(token);
                return null;
            }
        }
    }

    ////////////////////////////////////////////////////////////
    protected void skipUntil(Predicate<JToken> t) {
        tokenizer().skipUntil(t);
    }

    protected void skipWhile(Predicate<JToken> t) {
        tokenizer().skipWhile(t);
    }

    public void skip() {
        tokenizer().skip();
    }

    public void skip(int count) {
        tokenizer().skip(count);
    }

    public void pushBackAll(Collection<JToken> t) {
        tokenizer.pushBackAll(t);
    }

    private JToken[] prepare(JToken[] t) {
        for (int i = 0; i < t.length; i++) {
            prepare(t[i]);
        }
        return t;
    }

    private JToken prepare(JToken t) {
        if (t != null) {
            if (t.source == null) {
                t.source = compilationUnit().getSource();
            }
        }
        return t;
    }

    public void pushBack(JToken t) {
        tokenizer.pushBack(t);
    }

    public JToken[] peek(int n) {
        return prepare(tokenizer.peek(n));
    }

    public JToken[] peekIds(int... ids) {
        JToken[] res = new JToken[ids.length];
        for (int i = 0; i < ids.length; i++) {
            JToken[] a = peek(i + 1);
            if (a.length < (i + 1) || a[i].id() != ids[i]) {
                return null;
            }
            res[i] = a[i];
        }
        return res;
    }

    public JToken[] nextIds(int... ids) {
        JToken[] r = peekIds(ids);
        if (r != null) {
            skip(ids.length);
        }
        return r;
    }

    public JToken peek() {
        return prepare(tokenizer.peek());
    }

    public JToken next() {
        return prepare(tokenizer.next());
    }

    protected void pushDeclarationContext(T node) {
        declarationContexts.push(node);
    }

    protected T popDeclarationContext() {
        return declarationContexts.pop();
    }

    protected Stack<T> getDeclarationContexts() {
        return declarationContexts;
    }

    protected static class ParseExpressionUnaryContext {
        protected JExpressionUnaryOptions unary;
        protected boolean doBreak;
    }

}
