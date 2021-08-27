package net.thevpc.jeep.editorkits;

import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JOperatorPrecedences;
import net.thevpc.jeep.JTokenConfigBuilder;
import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.core.DefaultJeep;
import net.thevpc.jeep.core.JTokenState;
import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.core.tokens.JTokenPatternOrder;
import net.thevpc.jeep.core.tokens.JavaIdPattern;
import net.thevpc.jeep.core.tokens.SeparatorsPattern;
import net.thevpc.jeep.editor.JSyntaxKit;
import net.thevpc.jeep.editor.JSyntaxStyleManager;
import net.thevpc.jeep.impl.JEnumDefinition;
import net.thevpc.jeep.impl.JEnumTypes;
import net.thevpc.jeep.impl.tokens.JTokenizerImpl;
import net.thevpc.jeep.impl.tokens.JavaNumberTokenEvaluator;

public class SqlJSyntaxKit extends JSyntaxKit {

    public static final int OFFSET_LEFT_PARENTHESIS = 80;
    public static final int OFFSET_COMMA = 90;
    public static final int OFFSET_RIGHT_CURLY_BRACKET = 88;

    public static class LangState extends JTokenState {

        public static final int STATE_DEFAULT = 1;

        public static final JEnumDefinition<LangState> _ET = JEnumTypes.of(LangState.class);

        public static class Enums {

            public static final LangState STATE_DEFAULT = _ET.valueOf("STATE_DEFAULT");
        }

        private LangState(JEnumDefinition type, String name, int value) {
            super(type, name, value);
        }
    }

    private static JContext langContext;

    public SqlJSyntaxKit() {
        super();
        JContext jContext = getSingleton();
        JSyntaxStyleManager styles = new JSyntaxStyleManager();
        for (JTokenDef o : jContext.tokens().tokenDefinitions()) {
            switch (o.ttype) {
                case JTokenType.TT_KEYWORD: {
                    switch (o.idName) {
                        case "int":
                        case "void":
                        case "boolean":
                        case "char":
                        case "byte":
                        case "short":
                        case "long":
                        case "float":
                        case "double": {
                            styles.setTokenIdStyle(o.id, STYLE_PRIMITIVE_TYPES);
                            break;
                        }
                        case "select":
                        case "insert":
                        case "update":
                        case "delete":
                        case "into":
                        case "from":
                        case "group":
                        case "by":
                        case "having":
                        case "order":
                        case "inner":
                        case "join":
                        case "left":
                        case "outer":
                        case "where":
                        case "and":
                        case "or":
                        case "not":
                        case "in":
                        case "exists":
                        case "cross":
                        case "set":
                        {
                            styles.setTokenIdStyle(o.id, STYLE_PRIMITIVE_TYPES);
                            break;
                        }
                        case "true":
                        case "false":
                        case "null": {
                            styles.setTokenIdStyle(o.id, STYLE_BOOLEAN_LITERALS);
                            break;
                        }
                        default: {
                            styles.setTokenIdStyle(o.id, STYLE_KEYWORDS);
                        }
                    }
                    break;
                }
                case JTokenType.TT_BLOCK_COMMENTS:
                case JTokenType.TT_LINE_COMMENTS: {
                    styles.setTokenIdStyle(o.id, STYLE_COMMENTS);
                    break;
                }
                case JTokenType.TT_STRING: {
                    styles.setTokenIdStyle(o.id, STYLE_STRINGS);
                    break;
                }
                case JTokenType.TT_NUMBER: {
                    styles.setTokenIdStyle(o.id, STYLE_NUMBERS);
                    break;
                }
                case JTokenType.TT_OPERATOR: {
                    styles.setTokenIdStyle(o.id, STYLE_OPERATORS);
                    break;
                }
                case JTokenType.TT_GROUP_SEPARATOR:
                case JTokenType.TT_SEPARATOR: {
                    styles.setTokenIdStyle(o.id, STYLE_SEPARATORS);
                    break;
                }
                case JTokenType.TT_REGEX: {
                    styles.setTokenIdStyle(o.id, STYLE_REGEXPS);
                    break;
                }
                case JTokenType.TT_TEMPORAL: {
                    styles.setTokenIdStyle(o.id, STYLE_TEMPORALS);
                    break;
                }
            }
        }
        setJcontext(jContext);
        setStyles(styles);
        //setCompletionSupplier(new HLJCompletionSupplier(jContext));
    }

    private JContext getSingleton() {
        if (langContext == null) {
            langContext = new DefaultJeep();

            JTokenConfigBuilder config = langContext.tokens().config().builder();
            config
                    .setParseWhitespaces(true)
                    .setParseIntNumber(true)
                    .setParseFloatNumber(true)
                    .setParsetInfinity(true)
                    .setParseWhitespaces(true)
                    .setParseDoubleQuotesString(true)
                    .setParseSimpleQuotesString(true)
                    .setParseCStyleBlockComments()
                    .setParseCStyleLineComments();
            config.setIdPattern(new JavaIdPattern());
            config.addPatterns(new SeparatorsPattern("Separators1", OFFSET_LEFT_PARENTHESIS, JTokenType.Enums.TT_GROUP_SEPARATOR,
                    "(", ")", "[", "]", "{")
            );
            //this will be handled in a special way
            config.addPatterns(new SeparatorsPattern("Separators2",
                    OFFSET_RIGHT_CURLY_BRACKET,
                    JTokenPatternOrder.ORDER_OPERATOR,
                    JTokenType.Enums.TT_GROUP_SEPARATOR,
                    "}")
            );

            config.addPatterns(new SeparatorsPattern("Separators3", OFFSET_COMMA,
                    JTokenPatternOrder.valueOf(JTokenPatternOrder.ORDER_OPERATOR.getValue() - 1, "BEFORE_OPERATOR"),
                    JTokenType.Enums.TT_SEPARATOR,
                    ",", ";", ":", "->", "@")
            );

            //numbers
            config.setNumberEvaluator(new JavaNumberTokenEvaluator());
            config.setNumberSuffixes(new char[]{'f', 'F', 'l', 'L'});

            config.addKeywords("select","insert","update","delete");
            config.addKeywords("from", "where", "and", "or", "not", "in","into","set","is");
            config.addKeywords("left","right");
            config.addKeywords("exists","having","order","by","group");
            config.addKeywords("inner","outer","cross","join");
            config.addKeywords("begin","end","create","drop","alter","grant","deny");
            config.addKeywords("desc","asc");
            config.addKeywords("case","if","then","else","as","on");
            config.addKeywords("avg","max","min","sum");
            config.addKeywords("like","when","null");
            langContext.tokens().setConfig(config);

            langContext.operators().declareCStyleOperators();

            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_1, "=", "+=", "-=", "*=", "|=", "&=", "~=", "^=", "%=");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_3, "||");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_4, "&&");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_5, "|");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_6, "^");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_7, "&");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_8, "==", "!=");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_9, "<", ">", "<=", ">=");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_10, "<<", "<<<", ">>", ">>>");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_11, "+", "-");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_12, "*", "/", "%");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_13, "++", "--", "~");
            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_15, ".");

            langContext.operators().declareSpecialOperators("...");

            /*
         * this is the default state, handling HL tokens
             */
            JTokenConfigBuilder config_DEFAULT = new JTokenConfigBuilder(langContext.tokens().config());
            langContext.tokens().setFactory((reader, config1, context) -> {
                JTokenizerImpl t = new JTokenizerImpl(reader);
                t.addState(LangState.Enums.STATE_DEFAULT, config_DEFAULT);
                t.pushState(LangState.Enums.STATE_DEFAULT);
                return t;
            });
        }
        return langContext;
    }

}
