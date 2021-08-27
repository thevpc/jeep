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
import net.thevpc.jeep.impl.JEnumTypeRegistry;
import net.thevpc.jeep.impl.tokens.JTokenizerImpl;
import net.thevpc.jeep.impl.tokens.JavaNumberTokenEvaluator;
import net.thevpc.jeep.impl.tokens.RegexpBasedTokenPattern;

import java.util.regex.Pattern;

public class TexJSyntaxKit extends JSyntaxKit {

    public static final int OFFSET_LEFT_PARENTHESIS = 80;
    public static final int OFFSET_RIGHT_CURLY_BRACKET = 88;
    public static final int OFFSET_COMMA = 90;

    public static final int TOKEN_TEX_COMMAND = -300;
    public static final JTokenDef TOKEN_DEF_TEX_COMMAND = new JTokenDef(
            TOKEN_TEX_COMMAND,
            "tex-command",
            TOKEN_TEX_COMMAND, "\\tex", "\\tex");

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
    public TexJSyntaxKit() {
        super();
        JContext jContext = getSingleton();
        JSyntaxStyleManager styles = new JSyntaxStyleManager();
        for (JTokenDef o : jContext.tokens().tokenDefinitions()) {
            switch (o.ttype) {
                case JTokenType.TT_KEYWORD: {
                    switch (o.imageLayout) {
                        case "\\part":{
                            styles.setTokenIdStyle(o.id, STYLE_TITLE1);
                            break;
                        }
                        case "\\chapter":{
                            styles.setTokenIdStyle(o.id, STYLE_TITLE2);
                            break;
                        }
                        case "\\section":{
                            styles.setTokenIdStyle(o.id, STYLE_TITLE3);
                            break;
                        }
                        case "\\subsection":{
                            styles.setTokenIdStyle(o.id, STYLE_TITLE4);
                            break;
                        }
                        case "\\subsubsection":{
                            styles.setTokenIdStyle(o.id, STYLE_TITLE5);
                            break;
                        }
                        case "\\paragraph":{
                            styles.setTokenIdStyle(o.id, STYLE_TITLE6);
                            break;
                        }
                        case "\\subparagraph":{
                            styles.setTokenIdStyle(o.id, STYLE_TITLE7);
                            break;
                        }
                        case "true":
                        case "false": {
                            styles.setTokenIdStyle(o.id, STYLE_BOOLEAN_LITERALS);
                            break;
                        }
//                        case "article":case "book":case "booklet"
//                                :case "conference":case "inbook":case "incollection":
//                                    case "manual":case "mastersthesis":case "misc":
//                                        case "phdthesis":case "proceedings":case "techreport":case "unpublished":{
//                            styles.setTokenIdStyle(o.id, keywords2);
//                            break;
//                        }
                        default: {
                            styles.setTokenIdStyle(o.id, STYLE_KEYWORDS);
                            break;
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
                    styles.setTokenIdStyle(o.id, STYLE_STRING);
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
                case TOKEN_TEX_COMMAND: {
                    styles.setTokenIdStyle(o.id, STYLE_DIRECTIVES);
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
                    .setParseDoubleQuotesString(false)
                    .setParseSimpleQuotesString(false)
                    .setBlockComments("\\begin{comment}","\\end{comment}")
            ;
            config.setIdPattern(new JavaIdPattern());
            config.addPatterns(new SeparatorsPattern("Separators1", OFFSET_LEFT_PARENTHESIS, JTokenType.Enums.TT_GROUP_SEPARATOR,
                    "(", ")", "[", "]", "{", "}")
            );
            config.addPatterns(new SeparatorsPattern("Separators3", OFFSET_COMMA,
                    JTokenPatternOrder.valueOf(JTokenPatternOrder.ORDER_OPERATOR.getValue() - 1, "BEFORE_OPERATOR"),
                    JTokenType.Enums.TT_SEPARATOR,
                    ",", ";", ":")
            );
            config.addPatterns(
                    new RegexpBasedTokenPattern(TOKEN_DEF_TEX_COMMAND, JTokenPatternOrder.ORDER_IDENTIFIER,
                            Pattern.compile("\\\\[a-zA-Z]+")
                    )
            );

            config.setNumberEvaluator(new JavaNumberTokenEvaluator());

            config.addKeywords("\\part","\\chapter","\\section","\\subsection","\\subsubsection","\\paragraph","\\subparagraph");
            config.addKeywords("\\documentclass","\\usepackage","\\title","\\author","\\date","\\tableofcontents");
            config.addKeywords("\\begin","\\end","\\usepackage","\\title","\\author","\\date");
            config.addKeywords("\\if","\\else","\\ifdraft","\\ifnew","\\drafttrue","\\draftfalse","\\iffalse","\\iftrue");
            //    \ { }
            config.addKeywords("\\$","\\#","\\%","\\&","\\~","\\~","\\_","\\^","\\\\","\\{","\\}");

            langContext.tokens().setConfig(config);

            langContext.operators().declareCStyleOperators();

            langContext.operators().declareBinaryOperators(JOperatorPrecedences.PRECEDENCE_1, "=","&","~","_","^");

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
