package net.thevpc.jeep.core;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.nodes.*;
import net.thevpc.jeep.util.JeepUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.nodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultJParserNodeFactory implements JParserNodeFactory<JDefaultNode> {
    private JContext context;
    private JCompilationUnit compilationUnit;

    public DefaultJParserNodeFactory(JCompilationUnit compilationUnit, JContext context) {
        this.context = context;
        this.compilationUnit = compilationUnit;
    }

    public JCompilationUnit compilationUnit() {
        return compilationUnit;
    }

    public JContext context() {
        return context;
    }

    public JCompilerLog log() {
        return context.log();
    }

    @Override
    public JDefaultNode createLiteralNode(Object literal, JNodeTokens nodeTokens) {
        if(literal==null){
            return new JNodeLiteral(null);
        }
        Object literal2 = literal;
        Object literal3;
        for (JResolver r : context.resolvers().getResolvers()) {
            literal3 = r.implicitConvertLiteral(literal2, context);
            if (literal3 != null) {
                literal2 = literal3;
            }
        }
        JNodeLiteral lnode = new JNodeLiteral(literal2);
        lnode.setStartToken(nodeTokens.getStart());
        return lnode;
    }

    @Override
    public JDefaultNode createParsNode(List<JDefaultNode> children, JNodeTokens nodeTokens) {
        return new JNodePars(children.toArray(new JDefaultNode[0]));
    }

    @Override
    public JDefaultNode createBracesNode(List<JDefaultNode> children, JNodeTokens nodeTokens) {
        return new JNodeBraces(children.toArray(new JDefaultNode[0]));
    }

    @Override
    public JDefaultNode createPrefixUnaryOperatorNode(JToken op, JDefaultNode child, JNodeTokens nodeTokens) {
        return new JNodePrefixUnaryOpCall(op.image, child);
    }

    @Override
    public JDefaultNode createPostfixBracketsNode(JDefaultNode leftBaseChild, JDefaultNode rightIndexChildren, JNodeTokens nodeTokens) {
        return new JNodePostfixBrackets(leftBaseChild, (JNodeBrackets) rightIndexChildren);
    }

    @Override
    public JDefaultNode createPrefixBracketsNode(JDefaultNode leftIndexChildren, JDefaultNode rightBaseChild, JNodeTokens nodeTokens) {
        return new JNodePrefixBrackets(leftIndexChildren, (JNodeBrackets) rightBaseChild);
    }

    @Override
    public JDefaultNode createPostfixParenthesisNode(JDefaultNode leftBaseChild, JDefaultNode rightIndexChild, JNodeTokens nodeTokens) {
        return new JNodePostfixParenthesis(leftBaseChild, (JNodePars) rightIndexChild);
    }

    @Override
    public JDefaultNode createPrefixParenthesisNode(JDefaultNode leftIndexChild, JDefaultNode rightBaseChild, JNodeTokens nodeTokens) {
        return new JNodePrefixParenthesis(leftIndexChild, (JNodePars) rightBaseChild);
    }

    @Override
    public JDefaultNode createPostfixBracesNode(JDefaultNode leftBaseChild, JDefaultNode rightIndexChild, JNodeTokens nodeTokens) {
        return new JNodePostfixBraces(leftBaseChild, rightIndexChild);
    }

    @Override
    public JDefaultNode createPrefixBracesNode(JDefaultNode leftIndexChild, JDefaultNode rightBaseChild, JNodeTokens nodeTokens) {
        return new JNodePrefixBraces(leftIndexChild, rightBaseChild);
    }

    @Override
    public JDefaultNode createBracketsNode(List<JDefaultNode> children, JNodeTokens nodeTokens) {
        return new JNodeBrackets(children.toArray(new JDefaultNode[0]));
    }

    @Override
    public JDefaultNode createPostfixUnaryOperatorNode(JToken name, JDefaultNode argumentChild, JNodeTokens nodeTokens) {
        return new JNodePostfixUnaryOpCall(nodeTokens.getStart().sval, argumentChild);
    }

    @Override
    public JDefaultNode createListOperatorNode(JToken token, List<JDefaultNode> argumentChildren, JNodeTokens nodeTokens) {
        if(argumentChildren.size()==2) {
            JDefaultNode o1 = argumentChildren.get(0);
            JDefaultNode o2 = argumentChildren.get(1);
            if (o2 instanceof JNodeFunctionCall && (((JNodeFunctionCall) o2).getName()).equals(token.image)) {
                JNodeFunctionCall o21 = (JNodeFunctionCall) o2;
                List<JDefaultNode> aa = new ArrayList<>();
                aa.add(o1);
                aa.addAll(Arrays.asList(o21.getArgs()));
                return new JNodeFunctionCall(token.image, aa.toArray(new JDefaultNode[0]));
            } else {
                return new JNodeFunctionCall(token.image, argumentChildren.toArray(new JDefaultNode[0]));
            }
        }else {
            return new JNodeFunctionCall(token.image, argumentChildren.toArray(new JDefaultNode[0]));
        }
    }

    @Override
    public JDefaultNode createIdentifierNode(String name, JNodeTokens nodeTokens) {
        return new JNodeVarName(nodeTokens.getStart().sval);
    }

    @Override
    public JDefaultNode createBinaryOperatorNode(JToken op, JDefaultNode leftChild, JDefaultNode rightChild, JNodeTokens nodeTokens) {
        return new JNodeInfixBinaryOperatorCall(op.image, leftChild, rightChild);
    }

    @Override
    public JDefaultNode createImplicitOperatorNode(JDefaultNode leftChild, JDefaultNode rightChild, JNodeTokens nodeTokens) {
        return createOpNode("", leftChild, rightChild);
    }

    @Override
    public JDefaultNode createAnnotatedNode(JDefaultNode node, JDefaultNode annotations, JNodeTokens nodeTokens) {
        throw new IllegalArgumentException("Not supported");
    }

    protected JDefaultNode createOpNode(String op, JDefaultNode o1, JDefaultNode o2) {
//        if (o2 == null) {
//            o2=new JNodeLiteral(null);
//        }
        // a binary ?
        if (o1 instanceof JNodeFunctionCall) {
            JNodeFunctionCall o10 = (JNodeFunctionCall) o1;
            if (o10.getName().equals(op)) {
                JFunction ff = context.functions().findFunctionMatchOrNull(o10.getName(), JCallerInfo.NO_CALLER);
                if (ff != null && ff.getSignature().isVarArgs()) {
                    if (o2 instanceof JNodeFunctionCall && ((JNodeFunctionCall) o2).getName().equals(op)) {
                        return new JNodeFunctionCall(op,
                                (JDefaultNode[]) JeepUtils.joinArraysAsType(JDefaultNode.class, new Object[]{
                                        ((JNodeFunctionCall) o1).getArgs(), ((JNodeFunctionCall) o2).getArgs()
                                })
                        );
                    } else {
                        return new JNodeFunctionCall(op,
                                (JDefaultNode[]) JeepUtils.joinArraysAsType(JDefaultNode.class,
                                        new Object[]{((JNodeFunctionCall) o1).getArgs(),
                                                new JDefaultNode[]{o2}})
                        );
                    }
                }
            }
        }
        return new JNodeFunctionCall(op, new JDefaultNode[]{o1, o2});
    }
}
