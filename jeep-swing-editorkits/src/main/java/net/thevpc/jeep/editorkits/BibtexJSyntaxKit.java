package net.thevpc.jeep.editorkits;

import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JOperatorPrecedences;
import net.thevpc.jeep.JTokenConfigBuilder;
import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.core.DefaultJeep;
import net.thevpc.jeep.core.JTokenState;
import net.thevpc.jeep.core.tokens.*;
import net.thevpc.jeep.editor.ColorResource;
import net.thevpc.jeep.editor.JSyntaxKit;
import net.thevpc.jeep.editor.JSyntaxStyle;
import net.thevpc.jeep.editor.JSyntaxStyleManager;
import net.thevpc.jeep.impl.JEnumDefinition;
import net.thevpc.jeep.impl.JEnumTypeRegistry;
import net.thevpc.jeep.impl.tokens.JTokenizerImpl;
import net.thevpc.jeep.impl.tokens.JavaNumberTokenEvaluator;
import net.thevpc.jeep.impl.tokens.RegexpBasedTokenPattern;

import java.awt.*;
import java.util.regex.Pattern;

public class BibtexJSyntaxKit extends JSyntaxKit {

    public static final int OFFSET_LEFT_PARENTHESIS = 80;
    public static final int OFFSET_RIGHT_CURLY_BRACKET = 88;
    public static final int OFFSET_COMMA = 90;
    public static final int TOKEN_AT = -300;
    public static final JTokenDef TOKEN_DEF_AT = new JTokenDef(
            TOKEN_AT,
            "@def",
            TOKEN_AT, "@def", "@def");

    public static final int TOKEN_CURL_TEXT = -301;
    public static final JTokenDef TOKEN_DEF_CURL_TEXT = new JTokenDef(
            TOKEN_CURL_TEXT,
            "curl-text",
            TOKEN_CURL_TEXT, "{text}", "{text}");

    public static class LangState extends JTokenState {

        public static final int STATE_DEFAULT = 1;

        public static final JEnumDefinition<LangState> _ET = JEnumTypeRegistry.INSTANCE
                .register(LangState.class)
                .addConstIntFields(LangState.class, f -> f.getName().startsWith("STATE_"));

        public static class Enums {

            public static final LangState STATE_DEFAULT = _ET.valueOf("STATE_DEFAULT");
        }

        private LangState(JEnumDefinition type, String name, int value) {
            super(type, name, value);
        }
    }

    private static JContext langContext;
    public BibtexJSyntaxKit() {
        super();
        JContext jContext = getSingleton();
        JSyntaxStyleManager styles = new JSyntaxStyleManager();
        JSyntaxStyle keywords = new JSyntaxStyle(ColorResource.of(UI_KEY_RESERVED_WORD, Color.decode("#735db7")), JSyntaxStyle.BOLD);
        JSyntaxStyle keywords2 = new JSyntaxStyle(ColorResource.of(UI_KEY_RESERVED_WORD2, Color.decode("#735db7")), JSyntaxStyle.BOLD);
        JSyntaxStyle comments = new JSyntaxStyle(ColorResource.of(UI_KEY_COMMENTS, Color.DARK_GRAY), JSyntaxStyle.ITALIC);
        JSyntaxStyle strings = new JSyntaxStyle(ColorResource.of(UI_KEY_LITERAL_STRING, Color.decode("#2b9946")), JSyntaxStyle.BOLD);
        JSyntaxStyle strings2 = new JSyntaxStyle(ColorResource.of(UI_KEY_LITERAL_STRING2, Color.decode("#2b9946")), JSyntaxStyle.PLAIN);
        JSyntaxStyle numbers = new JSyntaxStyle(ColorResource.of(UI_KEY_LITERAL_NUMBER, Color.RED.darker()), JSyntaxStyle.PLAIN);
        JSyntaxStyle operators = new JSyntaxStyle(ColorResource.of(UI_KEY_OPERATOR, Color.cyan.darker()), JSyntaxStyle.PLAIN);
        JSyntaxStyle separators = new JSyntaxStyle(ColorResource.of(UI_KEY_SEPARATOR, Color.red), JSyntaxStyle.PLAIN);
        JSyntaxStyle regexs = new JSyntaxStyle(ColorResource.of(UI_KEY_LITERAL_REGEXP, Color.MAGENTA.darker()), JSyntaxStyle.PLAIN);
        JSyntaxStyle temporals = new JSyntaxStyle(ColorResource.of(UI_KEY_LITERAL_DATE, Color.pink.darker()), JSyntaxStyle.PLAIN);
        JSyntaxStyle directive = new JSyntaxStyle(ColorResource.of(UI_KEY_DIRECTIVE, Color.pink.darker()), JSyntaxStyle.PLAIN);
        JSyntaxStyle primitiveTypes = new JSyntaxStyle(ColorResource.of(UI_KEY_TYPE_PRIMITIVE, Color.decode("#aa557f")), JSyntaxStyle.BOLD);
        JSyntaxStyle trueFalseLiterals = new JSyntaxStyle(ColorResource.of(UI_KEY_LITERAL_BOOLEAN, Color.decode("#f1a100")), JSyntaxStyle.BOLD);
        for (JTokenDef o : jContext.tokens().tokenDefinitions()) {
            switch (o.ttype) {
                case JTokenType.TT_KEYWORD: {
                    switch (o.idName) {
                        case "true":
                        case "false": {
                            styles.setTokenIdStyle(o.id, trueFalseLiterals);
                            break;
                        }
                        case "article":case "book":case "booklet"
                                :case "conference":case "inbook":case "incollection":
                                    case "manual":case "mastersthesis":case "misc":
                                        case "phdthesis":case "proceedings":case "techreport":case "unpublished":{
                            styles.setTokenIdStyle(o.id, keywords2);
                            break;
                        }
                        default: {
                            styles.setTokenIdStyle(o.id, keywords);
                            break;
                        }
                    }
                    break;
                }
                case JTokenType.TT_BLOCK_COMMENTS:
                case JTokenType.TT_LINE_COMMENTS: {
                    styles.setTokenIdStyle(o.id, comments);
                    break;
                }
                case JTokenType.TT_STRING: {
                    styles.setTokenIdStyle(o.id, strings);
                    break;
                }
                case JTokenType.TT_NUMBER: {
                    styles.setTokenIdStyle(o.id, numbers);
                    break;
                }
                case JTokenType.TT_OPERATOR: {
                    styles.setTokenIdStyle(o.id, operators);
                    break;
                }
                case JTokenType.TT_GROUP_SEPARATOR:
                case JTokenType.TT_SEPARATOR: {
                    styles.setTokenIdStyle(o.id, separators);
                    break;
                }
                case JTokenType.TT_REGEX: {
                    styles.setTokenIdStyle(o.id, regexs);
                    break;
                }
                case JTokenType.TT_TEMPORAL: {
                    styles.setTokenIdStyle(o.id, temporals);
                    break;
                }
                case TOKEN_AT: {
                    styles.setTokenIdStyle(o.id, directive);
                    break;
                }
                case TOKEN_CURL_TEXT: {
                    styles.setTokenIdStyle(o.id, strings2);
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
                    .setParsetInfinity(false)
                    .setParseWhitespaces(true)
                    .setParseDoubleQuotesString(true)
                    .setParseSimpleQuotesString(true)
//                    .setParseBashStyleLineComments()
            ;
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
                    ",", ";", ":")
            );
            config.addPatterns(
                    new RegexpBasedTokenPattern(TOKEN_DEF_AT, JTokenPatternOrder.ORDER_IDENTIFIER,
                            Pattern.compile("@[a-zA-Z]+")
                    )
            );
            config.addPatterns(
                    new RegexpBasedTokenPattern(TOKEN_DEF_CURL_TEXT, JTokenPatternOrder.ORDER_STRING,
                            Pattern.compile("[{][^{}=,]+[}]")
                    )
            );

            config.setNumberEvaluator(new JavaNumberTokenEvaluator());

            config.addKeywords("author", "title", "journal", "year", "number", "pages", "month", "note", "volume", "publisher", "volume","editor");
            config.addKeywords("series", "address", "edition","month","isbn","booktitle","organization","chapter","institution","howpublished");
            config.addKeywords("article", "book","booklet","conference","inbook","incollection","manual","mastersthesis","misc","phdthesis","proceedings","techreport","unpublished");

            langContext.tokens().setConfig(config);

            langContext.operators().declareCStyleOperators();

            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_1, "=");

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
