package net.thevpc.jeep.editorkits;

import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.editor.JSyntaxKit;
import net.thevpc.jeep.editor.JSyntaxStyle;
import net.thevpc.jeep.editor.JSyntaxStyleManager;

import java.awt.*;
import java.util.regex.Pattern;
import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JOperatorPrecedences;
import net.thevpc.jeep.JTokenConfigBuilder;
import net.thevpc.jeep.core.DefaultJeep;
import net.thevpc.jeep.core.JTokenState;
import net.thevpc.jeep.core.tokens.JTokenPatternOrder;
import net.thevpc.jeep.core.tokens.JavaIdPattern;
import net.thevpc.jeep.core.tokens.SeparatorsPattern;
import net.thevpc.jeep.editor.ColorResource;
import net.thevpc.jeep.impl.JEnumDefinition;
import net.thevpc.jeep.impl.JEnumTypeRegistry;
import net.thevpc.jeep.impl.tokens.JTokenizerImpl;
import net.thevpc.jeep.impl.tokens.JavaNumberTokenEvaluator;
import net.thevpc.jeep.impl.tokens.RegexpBasedTokenPattern;

public class ShellLangJSyntaxKit extends JSyntaxKit {

    public static final int OFFSET_LEFT_PARENTHESIS = 80;
    public static final int OFFSET_RIGHT_CURLY_BRACKET = 88;
    public static final int OFFSET_COMMA = 90;
    public static final int TOKEN_DIRECTIVE = -300;
    public static final JTokenDef TOKEN_DEF_DIRECTIVE = new JTokenDef(
            TOKEN_DIRECTIVE,
            "directive",
            TOKEN_DIRECTIVE, "directive", "#...");

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
    private static boolean bash;

    public ShellLangJSyntaxKit(boolean bash) {
        super();
        this.bash = bash;
        JContext jContext = getSingleton();
        JSyntaxStyleManager styles = new JSyntaxStyleManager();
        for (JTokenDef o : jContext.tokens().tokenDefinitions()) {
            switch (o.ttype) {
                case JTokenType.TT_KEYWORD: {
                    switch (o.idName) {
                        case "true":
                        case "false": {
                            styles.setTokenIdStyle(o.id, BOOLEAN_LITERALS);
                            break;
                        }
                        default: {
                            styles.setTokenIdStyle(o.id, KEYWORDS);
                        }
                    }
                    break;
                }
                case JTokenType.TT_BLOCK_COMMENTS:
                case JTokenType.TT_LINE_COMMENTS: {
                    styles.setTokenIdStyle(o.id, COMMENTS);
                    break;
                }
                case JTokenType.TT_STRING: {
                    styles.setTokenIdStyle(o.id, STRINGS);
                    break;
                }
                case JTokenType.TT_NUMBER: {
                    styles.setTokenIdStyle(o.id, NUMBERS);
                    break;
                }
                case JTokenType.TT_OPERATOR: {
                    styles.setTokenIdStyle(o.id, OPERATORS);
                    break;
                }
                case JTokenType.TT_GROUP_SEPARATOR:
                case JTokenType.TT_SEPARATOR: {
                    styles.setTokenIdStyle(o.id, SEPARATORS);
                    break;
                }
                case JTokenType.TT_REGEX: {
                    styles.setTokenIdStyle(o.id, REGEXPS);
                    break;
                }
                case JTokenType.TT_TEMPORAL: {
                    styles.setTokenIdStyle(o.id, TEMPORALS);
                    break;
                }
                case TOKEN_DIRECTIVE: {
                    styles.setTokenIdStyle(o.id, DIRECTIVES);
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
                    .setParseBashStyleLineComments();
            config.setIdPattern(new JavaIdPattern());
            config.addPatterns(new SeparatorsPattern("Separators1", OFFSET_LEFT_PARENTHESIS, JTokenType.Enums.TT_GROUP_SEPARATOR,
                    "(", ")", "[", "]", "{","}","$")
            );

            config.addPatterns(new SeparatorsPattern("Separators3", OFFSET_COMMA,
                    JTokenPatternOrder.valueOf(JTokenPatternOrder.ORDER_OPERATOR.getValue() - 1, "BEFORE_OPERATOR"),
                    JTokenType.Enums.TT_SEPARATOR,
                    ",", ";", ":", "->", "@")
            );
            config.addPatterns(
                    new RegexpBasedTokenPattern(TOKEN_DEF_DIRECTIVE, JTokenPatternOrder.ORDER_IDENTIFIER,
                            Pattern.compile("^#!.*")
                    )
            );

            config.setNumberEvaluator(new JavaNumberTokenEvaluator());

            config.addKeywords("if", "then", "else", "elif", "end", "do", "while", "esac", "fi", "for", "function");
            config.addKeywords("in", "select", "time","until");
            config.addKeywords("done", "case");

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
