package net.thevpc.jeep.editorkits;

import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.editor.JSyntaxKit;
import net.thevpc.jeep.editor.JSyntaxStyle;
import net.thevpc.jeep.editor.JSyntaxStyleManager;

import java.util.regex.Pattern;
import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JTokenConfigBuilder;
import net.thevpc.jeep.core.DefaultJeep;
import net.thevpc.jeep.core.JTokenState;
import net.thevpc.jeep.core.tokens.JTokenPatternOrder;
import net.thevpc.jeep.core.tokens.SimpleTokenPattern;
import net.thevpc.jeep.editor.ColorResource;
import net.thevpc.jeep.impl.JEnumDefinition;
import net.thevpc.jeep.impl.JEnumTypeRegistry;
import net.thevpc.jeep.impl.tokens.JTokenizerImpl;
import net.thevpc.jeep.impl.tokens.RegexpBasedTokenPattern;

public class NTFJSyntaxKit extends JSyntaxKit {

    public static final int OFFSET_LEFT_PARENTHESIS = 80;
    public static final int OFFSET_RIGHT_CURLY_BRACKET = 88;
    public static final int OFFSET_COMMA = 90;
    public static final int TT_STAR1 = -1100;
    public static final int TT_STAR2 = -1101;
    public static final int TT_STAR3 = -1102;
    public static final int TT_TITLE1 = -1103;
    public static final int TT_TITLE2 = -1104;
    public static final int TT_TITLE3 = -1105;
    public static final int TT_TITLE4 = -1106;
    public static final int TT_TITLE5 = -1107;
    public static final int TT_TITLE6 = -1108;
    public static final int TT_TITLE7 = -1109;
    public static final int TT_TITLE8 = -1110;
    public static final int TT_TITLE9 = -1111;
    public static final int TT_PRE = -1112;
    public static final int TT_CODE = -1113;
//    public static final int TT_ANY = -1114;

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


    public NTFJSyntaxKit() {
        super();
        JContext jContext = getSingleton();
        JSyntaxStyleManager styles = new JSyntaxStyleManager();
        JSyntaxStyle bold = new JSyntaxStyle("BOLD",null, JSyntaxStyle.BOLD);
        JSyntaxStyle italic = new JSyntaxStyle("ITALIC",null, JSyntaxStyle.ITALIC);
        JSyntaxStyle boldItalic = new JSyntaxStyle("BOLD_ITALIC",null, JSyntaxStyle.BOLD | JSyntaxStyle.ITALIC);
        JSyntaxStyle pre = new JSyntaxStyle("PRE",ColorResource.of("OptionPane.warningDialog.titlePane.shadow;ToolWindowTitleBarUI.background.active.end;Tree.selectionBackground"), JSyntaxStyle.PLAIN).setFillColor(ColorResource.of("#fff5b9"));
        JSyntaxStyle code = new JSyntaxStyle("CODE",ColorResource.of("OptionPane.warningDialog.titlePane.shadow;ToolWindowTitleBarUI.background.active.end;Tree.selectionBackground"), JSyntaxStyle.PLAIN).setFillColor(ColorResource.of("#fae0ff"));
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
                case TT_STAR1: {
                    styles.setTokenIdStyle(o.id, italic);
                    break;
                }
                case TT_STAR2: {
                    styles.setTokenIdStyle(o.id, bold);
                    break;
                }
                case TT_STAR3: {
                    styles.setTokenIdStyle(o.id, boldItalic);
                    break;
                }
                case TT_TITLE1: {
                    styles.setTokenIdStyle(o.id, TITLE1);
                    break;
                }
                case TT_TITLE2: {
                    styles.setTokenIdStyle(o.id, TITLE2);
                    break;
                }
                case TT_TITLE3: {
                    styles.setTokenIdStyle(o.id, TITLE3);
                    break;
                }
                case TT_TITLE4: {
                    styles.setTokenIdStyle(o.id, TITLE4);
                    break;
                }
                case TT_TITLE5: {
                    styles.setTokenIdStyle(o.id, TITLE5);
                    break;
                }
                case TT_TITLE6: {
                    styles.setTokenIdStyle(o.id, TITLE6);
                    break;
                }
                case TT_TITLE7: {
                    styles.setTokenIdStyle(o.id, TITLE7);
                    break;
                }
                case TT_TITLE8: {
                    styles.setTokenIdStyle(o.id, TITLE8);
                    break;
                }
                case TT_TITLE9: {
                    styles.setTokenIdStyle(o.id, TITLE9);
                    break;
                }
                case TT_PRE: {
                    styles.setTokenIdStyle(o.id, pre);
                    break;
                }
                case TT_CODE: {
                    styles.setTokenIdStyle(o.id, code);
                    break;
                }
            }
        }
        setJcontext(jContext);
        setStyles(styles);
        //setCompletionSupplier(new HLJCompletionSupplier(jContext));
    }

    private static JContext getSingleton() {
        if (langContext == null) {
            langContext = new DefaultJeep();

            JTokenConfigBuilder config = langContext.tokens().config().builder();
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_STAR3, "**"), JTokenPatternOrder.ORDER_OPERATOR, Pattern.compile(
                    "|([*]{3}((?![*]{3}).)+[*]{3})|([_]{3}[^_][_]{3})"
            )));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_STAR2, "**"), JTokenPatternOrder.ORDER_OPERATOR, Pattern.compile(
                    "|([*]{2}((?![*]{2}).)+[*]{2})|([_]{2}[^_][_]{2})"
            )));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_STAR1, "*"), JTokenPatternOrder.ORDER_OPERATOR, Pattern.compile(
                    "([*][^*]+[*])|([_][^_][_])"
            )));
//
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE9, "#9"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{9}\\).*")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE8, "#8"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{8}\\).*")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE7, "#7"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{7}\\).*")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE6, "#6"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{6}\\).*")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE5, "#5"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{5}\\).*")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE4, "#4"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{4}\\).*")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE3, "#3"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{3}\\).*")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE2, "#2"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{2}\\).*")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE1, "#"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]\\).*")));
//
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE9, "#9"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{10}[^#]+[#]{10}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE8, "#9"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{9}[^#]+[#]{9}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE7, "#8"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{8}[^#]+[#]{8}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE6, "#7"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{7}[^#]+[#]{7}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE5, "#6"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{6}[^#]+[#]{6}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE4, "#5"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{5}[^#]+[#]{5}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE3, "#4"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{4}[^#]+[#]{4}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE2, "#3"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{3}[^#]+[#]{3}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_TITLE1, "#2"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("^( )*[#]{2}[^#]+[#]{2}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_CODE, "code"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("```((?!```).)*```",Pattern.DOTALL)));

            config.setIdPattern(new SimpleTokenPattern() {
                @Override
                public boolean accept(CharSequence prefix, char c) {
                    return !Character.isWhitespace(c);
                }
            });

            config
                    .setParseWhitespaces(true)
                    .setParseDoubleQuotesString(true)
                    .setParseSimpleQuotesString(true);
//            config.addPatterns(new SeparatorsPattern("Separators1", OFFSET_LEFT_PARENTHESIS, JTokenType.Enums.TT_GROUP_SEPARATOR,
//                    "(", ")", "[", "]", "{", "}")
//            );
//
//            config.addPatterns(new SeparatorsPattern("Separators3", OFFSET_COMMA,
//                    JTokenPatternOrder.valueOf(JTokenPatternOrder.ORDER_OPERATOR.getValue() - 1, "BEFORE_OPERATOR"),
//                    JTokenType.Enums.TT_SEPARATOR,
//                    ",", ";", ":", "->", "@")
//            );

            langContext.tokens().setConfig(config);

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
