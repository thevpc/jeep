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

public class XmlJSyntaxKit extends JSyntaxKit {

    public static final int OFFSET_LEFT_PARENTHESIS = 80;
    public static final int OFFSET_RIGHT_CURLY_BRACKET = 88;
    public static final int OFFSET_COMMA = 90;
    public static final int TOKEN_TAG = -300;
    public static final JTokenDef TOKEN_DEF_TAG = new JTokenDef(
            TOKEN_TAG,
            "@def",
            TOKEN_TAG, "@def", "@def");


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
    public XmlJSyntaxKit() {
        super();
        JContext jContext = getSingleton();
        JSyntaxStyleManager styles = new JSyntaxStyleManager();
        JSyntaxStyle keywords = new JSyntaxStyle("RESERVED_WORD",ColorResource.of(UI_KEY_RESERVED_WORD), JSyntaxStyle.BOLD);
        JSyntaxStyle keywords2 = new JSyntaxStyle("RESERVED_WORD2",ColorResource.of(UI_KEY_RESERVED_WORD2), JSyntaxStyle.BOLD);
        JSyntaxStyle comments = new JSyntaxStyle("COMMENTS",ColorResource.of(UI_KEY_COMMENTS), JSyntaxStyle.ITALIC);
        JSyntaxStyle strings = new JSyntaxStyle("LITERAL_STRING",ColorResource.of(UI_KEY_LITERAL_STRING), JSyntaxStyle.BOLD);
        JSyntaxStyle strings2 = new JSyntaxStyle("LITERAL_STRING2",ColorResource.of(UI_KEY_LITERAL_STRING2), JSyntaxStyle.PLAIN);
        JSyntaxStyle numbers = new JSyntaxStyle("LITERAL_NUMBER",ColorResource.of(UI_KEY_LITERAL_NUMBER), JSyntaxStyle.PLAIN);
        JSyntaxStyle operators = new JSyntaxStyle("OPERATOR",ColorResource.of(UI_KEY_OPERATOR), JSyntaxStyle.PLAIN);
        JSyntaxStyle separators = new JSyntaxStyle("SEPARATOR",ColorResource.of(UI_KEY_SEPARATOR), JSyntaxStyle.PLAIN);
        JSyntaxStyle regexs = new JSyntaxStyle("LITERAL_REGEXP",ColorResource.of(UI_KEY_LITERAL_REGEXP), JSyntaxStyle.PLAIN);
        JSyntaxStyle temporals = new JSyntaxStyle("LITERAL_DATE",ColorResource.of(UI_KEY_LITERAL_DATE), JSyntaxStyle.PLAIN);
        JSyntaxStyle directive = new JSyntaxStyle("DIRECTIVE",ColorResource.of(UI_KEY_DIRECTIVE), JSyntaxStyle.PLAIN);
        JSyntaxStyle primitiveTypes = new JSyntaxStyle("TYPE_PRIMITIVE",ColorResource.of(UI_KEY_TYPE_PRIMITIVE), JSyntaxStyle.BOLD);
        JSyntaxStyle trueFalseLiterals = new JSyntaxStyle("LITERAL_BOOLEAN",ColorResource.of(UI_KEY_LITERAL_BOOLEAN), JSyntaxStyle.BOLD);
        for (JTokenDef o : jContext.tokens().tokenDefinitions()) {
            switch (o.ttype) {
                case JTokenType.TT_KEYWORD: {
                    switch (o.idName) {
                        case "true":
                        case "false": {
                            styles.setTokenIdStyle(o.id, trueFalseLiterals);
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
                case TOKEN_TAG: {
                    styles.setTokenIdStyle(o.id, directive);
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
                    .setParseXmlStyleBlockComments()
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
                    new RegexpBasedTokenPattern(TOKEN_DEF_TAG, JTokenPatternOrder.ORDER_IDENTIFIER,
                            Pattern.compile("<[/]?[a-zA-Z._-]+[ >]")
                    )
            );

            config.setNumberEvaluator(new JavaNumberTokenEvaluator());

            langContext.tokens().setConfig(config);

            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_1, "=");


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
