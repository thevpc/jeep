package net.thevpc.jeep.editorkits;

import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenConfigBuilder;
import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.core.DefaultJeep;
import net.thevpc.jeep.core.JTokenState;
import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.core.tokens.JTokenPatternOrder;
import net.thevpc.jeep.core.tokens.SimpleTokenPattern;
import net.thevpc.jeep.editor.ColorResource;
import net.thevpc.jeep.editor.JSyntaxKit;
import net.thevpc.jeep.editor.JSyntaxStyle;
import net.thevpc.jeep.editor.JSyntaxStyleManager;
import net.thevpc.jeep.impl.JEnumDefinition;
import net.thevpc.jeep.impl.JEnumTypeRegistry;
import net.thevpc.jeep.impl.tokens.JTokenizerImpl;
import net.thevpc.jeep.impl.tokens.RegexpBasedTokenPattern;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static final int TT_P1 = -1114;
    public static final int TT_P2 = -1115;
    public static final int TT_P3 = -1116;
    public static final int TT_P4 = -1117;
    public static final int TT_P5 = -1118;
    public static final int TT_P6 = -1119;
    public static final int TT_P7 = -1120;
    public static final int TT_P8 = -1121;
    public static final int TT_P9 = -1122;

    public static final int TT_S1 = -1123;
    public static final int TT_S2 = -1124;
    public static final int TT_S3 = -1125;
    public static final int TT_S4 = -1126;
    public static final int TT_S5 = -1127;
    public static final int TT_S6 = -1128;
    public static final int TT_S7 = -1129;
    public static final int TT_S8 = -1130;
    public static final int TT_S9 = -1131;
    private static JContext langContext;


    public NTFJSyntaxKit() {
        super();
        JContext jContext = getSingleton();
        JSyntaxStyleManager styles = new NTFJSyntaxStyleManager();
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
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P9, "#p9"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{10}[^\n\r#]+[#]{10}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P8, "#p9"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{9}[^\n\r#]+[#]{9}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P7, "#p8"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{8}[^\n\r#]+[#]{8}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P6, "#p7"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{7}[^\n\r#]+[#]{7}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P5, "#p6"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{6}[^\n\r#]+[#]{6}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P4, "#p5"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{5}[^\n\r#]+[#]{5}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P3, "#p4"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{4}[^\n\r#]+[#]{4}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P2, "#p3"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{3}[^\n\r#]+[#]{3}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_P1, "#p2"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("[#]{2}[^\n\r#]+[#]{2}")));
            config.addPatterns(new RegexpBasedTokenPattern(new JTokenDef(TT_CODE, "code"), JTokenPatternOrder.ORDER_OPERATOR,
                    Pattern.compile("```((?!```).)*```", Pattern.DOTALL)));

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

    public JSyntaxStyle getStyleAnsi256Foreground(int i) {
        if (i < 0 || i > 255) {
            return getStyles().getTokenIdStyle(0);
        }
        Ansi256Colors.AnsiColor cc = Ansi256Colors.COLORS[i];
        return new JSyntaxStyle("P256_" + i, ColorResource.of(new Color(
                cc.getR(), cc.getG(), cc.getB()
        )), JSyntaxStyle.PLAIN);
    }

    public JSyntaxStyle getStyleAnsi256Background(int i) {
        if (i < 0 || i > 255) {
            return getStyles().getTokenIdStyle(0);
        }
        Ansi256Colors.AnsiColor cc = Ansi256Colors.COLORS[i];
        ColorResource col = ColorResource.of(new Color(
                cc.getR(), cc.getG(), cc.getB()
        ));
        return new JSyntaxStyle("P256_" + i, null, JSyntaxStyle.PLAIN)
                .setFillColor(col);
    }

    public static class LangState extends JTokenState {

        public static final int STATE_DEFAULT = 1;

        public static final JEnumDefinition<LangState> _ET = JEnumTypeRegistry.INSTANCE
                .register(LangState.class)
                .addConstIntFields(LangState.class, f -> f.getName().startsWith("STATE_"));

        private LangState(JEnumDefinition type, String name, int value) {
            super(type, name, value);
        }

        public static class Enums {

            public static final LangState STATE_DEFAULT = _ET.valueOf("STATE_DEFAULT");
        }
    }

    private class NTFJSyntaxStyleManager extends JSyntaxStyleManager {
        public JSyntaxStyle getTokenIdStyleSpecialOrNull(String name, String number, JToken token) {
            switch (name) {
                case "": {
                    try {
                        int ii = Integer.parseInt(number);
                        if (ii >= 0 && ii <= 255) {
                            return getStyleAnsi256Foreground(ii);
                        }
                    } catch (Exception ex) {
                        //
                    }
                    return super.getTokenIdStyle(0);
                }
                case "/": {
                    return STYLE_ITALIC;
                }
                case "_": {
                    return STYLE_UNDERLINE;
                }
                case "%": {
                    return STYLE_ITALIC; //blink
                }
                case "!": {
                    return STYLE_BOLD;//reversed
                }
                case "+": {
                    return STYLE_BOLD;//reversed
                }
                case "-": {
                    return STYLE_CROSS_OUT;//reversed
                }
                case "f":
                case "foreground": {
                    int ii = Integer.parseInt(number);
                    if (ii >= 0 && ii <= 255) {
                        return getStyleAnsi256Foreground(ii);
                    }
                    return null;
                }
                case "b":
                case "background": {
                    try {
                        int ii = Integer.parseInt(number);
                        if (ii >= 0 && ii <= 255) {
                            return getStyleAnsi256Background(ii);
                        }
                    } catch (Exception ex) {
                        //
                    }
                    return null;
                }
                case "p":
                case "primary": {
                    try {
                        int ii = Integer.parseInt(number);
                        if (ii >= 0 && ii <= 9) {
                            return super.getTokenIdStyle(-TT_P1 + ii);
                        }
                    } catch (Exception ex) {
                        //
                    }
                    return null;
                }
                case "s":
                case "secondary": {
                    try {
                        int ii = Integer.parseInt(number);
                        if (ii >= 1 && ii <= 9) {
                            return getBackgroundStyle(ii);
                        }
                        return getBackgroundStyle(1);
                    } catch (Exception ex) {
                        //
                    }
                    return null;
                }
                case "underlined": {
                    return STYLE_UNDERLINE;
                }
                case "italic": {
                    return STYLE_ITALIC;
                }
                case "striked": {
                    return STYLE_CROSS_OUT;
                }
                case "reversed": {
                    return STYLE_BOLD;
                }
                case "bold": {
                    return STYLE_BOLD;
                }
                case "blink": {
                    return STYLE_BOLD;
                }
                case "error": {
                    return STYLE_ERROR;
                }
                case "warn": {
                    return STYLE_WARN;
                }
                case "info": {
                    return STYLE_INFO;
                }
                case "config": {
                    return STYLE_TITLE4;
                }
                case "comments": {
                    return STYLE_COMMENTS;
                }
                case "string": {
                    return STYLE_STRINGS;
                }
                case "number": {
                    return STYLE_NUMBERS;
                }
                case "date": {
                    return STYLE_TEMPORALS;
                }
                case "kw":
                case "keyword":
                {
                    return STYLE_KEYWORDS;
                }
                case "option": {
                    return STYLE_KEYWORDS2;
                }
                case "input": {
                    return STYLE_STRINGS2;
                }
                case "separator": {
                    return STYLE_SEPARATORS;
                }
                case "operator": {
                    return STYLE_OPERATORS;
                }
                case "success": {
                    return STYLE_SUCCESS;
                }
                case "fail": {
                    return STYLE_ERROR;
                }
                case "danger": {
                    return STYLE_ERROR;
                }
                case "var": {
                    return STYLE_KEYWORDS2;
                }
                case "pale": {
                    return STYLE_COMMENTS;
                }
                case "path": {
                    return STYLE_STRINGS2;
                }
                case "version": {
                    return STYLE_TITLE7;
                }
                case "title": {
                    int ii = Integer.parseInt(number);
                    if (ii >= 1 && ii <= 9) {
                        return getTitleStyle(ii);
                    }
                    return getTitleStyle(1);
                }
                case "fx":
                case "foregroundx": {
                    try {
                        int ii = Integer.parseInt(number, 16);
                        if (ii >= 0) {
                            Ansi256Colors.AnsiColor cc = new Ansi256Colors.AnsiColor(ii);
                            return new JSyntaxStyle("FX24_" + ii, ColorResource.of(new Color(
                                    cc.getR(), cc.getG(), cc.getB()
                            )), JSyntaxStyle.PLAIN);
                        }
                    } catch (Exception ex) {
                        //
                    }
                    return null;
                }
                case "bx":
                case "backgroundx": {
                    try {
                        int ii = Integer.parseInt(number, 16);
                        if (ii >= 0) {
                            Ansi256Colors.AnsiColor cc = new Ansi256Colors.AnsiColor(ii);
                            ColorResource col = ColorResource.of(new Color(
                                    cc.getR(), cc.getG(), cc.getB()
                            ));
                            return new JSyntaxStyle("BX24_" + ii, null, JSyntaxStyle.PLAIN)
                                    .setFillColor(col);
                        }
                    } catch (Exception ex) {
                        //
                    }
                    return null;
                }
            }
            return null;
        }

        public JSyntaxStyle getTokenIdStyleByCodeOrNull(String n, JToken token) {
            switch (n) {
                case "/":
                case "_":
                case "%":
                case "!":
                case "+":
                case "-":
                case "p":
                case "primary":
                case "s":
                case "secondary":
                case "underlined":
                case "italic":
                case "striked":
                case "reversed":
                case "bold":
                case "blink":
                case "error":
                case "warn":
                case "info":
                case "config":
                case "comments":
                case "string":
                case "number":
                case "date":
                case "keyword":
                case "option":
                case "input":
                case "separator":
                case "operator":
                case "success":
                case "fail":
                case "danger":
                case "var":
                case "pale":
                case "path":
                case "version":
                case "title":
                {
                    return getTokenIdStyleSpecialOrNull(n, "0", token);
                }
            }
            String intVal = extract(n, "(?<v>^[0-9]{1,3}$)", "v");
            if (intVal != null) {
                return getTokenIdStyleSpecialOrNull("", intVal, token);
            }
            Matcher a;

            a = Pattern.compile("^(?<s>(fx|foregroundx|bx|backgroundx))(?<i>[0-9a-fA-F]{6})$").matcher(n.toLowerCase());
            if (a.find()) {
                String gn = a.group("s");
                String gi = a.group("i");
                return getTokenIdStyleSpecialOrNull(gn.toUpperCase(), gi, token);
            }

            a = Pattern.compile("^(?<s>[^0-9]+)(?<i>[0-9]{1,3})$").matcher(n);
            if (a.find()) {
                String gn = a.group("s");
                String gi = a.group("i");
                return getTokenIdStyleSpecialOrNull(gn.toLowerCase(), gi, token);
            }
            return null;
        }


        @Override
        public JSyntaxStyle getTokenIdStyle(JToken token) {
//                System.out.println(token.image + " : " + token.id());
            switch (token.def.ttype) {
                case TT_P1: {
                    String s = token.image.substring(2, token.image.length() - 4);
                    if (s.length() > 0) {
                        char c = s.charAt(0);
                        if (c == ':') {
                            int i = indexOfAnyChar(s, 1, ' ', '\t', ':');
                            String n = null;
                            if (i > 0) {
                                n = s.substring(1, i);
                                JSyntaxStyle d = getTokenIdStyleByCodeOrNull(n, token);
                                if (d != null) {
                                    return d;
                                }
                            }
                        }
                        return STYLE_TITLE1;
                    }
                    break;
                }
                case TT_CODE: {
                    String s = token.image.substring(3, token.image.length() - 6);
                    int i = indexOfAnyChar(s, 3, ' ', '\t', '\n', '\r');
                    if (i >= 0) {
                        String n = s.substring(0, i);
                        JSyntaxStyle d = getTokenIdStyleSpecialOrNull(n, "", token);
                        if (d != null) {
                            return d;
                        }
                    }
                    return STYLE_CODE;
                }
                case JTokenType.TT_KEYWORD: {
                    switch (token.def.idName) {
                        case "true":
                        case "false": {
                            return STYLE_BOOLEAN_LITERALS;
                        }
                        default: {
                            return STYLE_KEYWORDS;
                        }
                    }
                }
                case JTokenType.TT_BLOCK_COMMENTS:
                case JTokenType.TT_LINE_COMMENTS: {
                    return STYLE_COMMENTS;
                }
                case JTokenType.TT_STRING: {
                    return STYLE_STRINGS;
                }
                case JTokenType.TT_NUMBER: {
                    return STYLE_NUMBERS;
                }
                case JTokenType.TT_OPERATOR: {
                    return STYLE_OPERATORS;
                }
                case JTokenType.TT_GROUP_SEPARATOR:
                case JTokenType.TT_SEPARATOR: {
                    return STYLE_SEPARATORS;
                }
                case JTokenType.TT_REGEX: {
                    return STYLE_REGEXPS;
                }
                case JTokenType.TT_TEMPORAL: {
                    return STYLE_TEMPORALS;
                }
                case TT_STAR1: {
                    return STYLE_ITALIC;
                }
                case TT_STAR2: {
                    return STYLE_BOLD;
                }
                case TT_STAR3: {
                    return STYLE_BOLD_ITALIC;
                }
                case TT_TITLE1:{
                    return STYLE_TITLE1;
                }
                case TT_TITLE2:
                case TT_P2: {
                    return STYLE_TITLE2;
                }
                case TT_TITLE3:
                case TT_P3: {
                    return STYLE_TITLE3;
                }
                case TT_TITLE4:
                case TT_P4: {
                    return STYLE_TITLE4;
                }
                case TT_TITLE5:
                case TT_P5: {
                    return STYLE_TITLE5;
                }
                case TT_TITLE6:
                case TT_P6: {
                    return STYLE_TITLE6;
                }
                case TT_TITLE7:
                case TT_P7: {
                    return STYLE_TITLE7;
                }
                case TT_TITLE8:
                case TT_P8: {
                    return STYLE_TITLE8;
                }
                case TT_TITLE9:
                case TT_P9: {
                    return STYLE_TITLE9;
                }
                case TT_S1: {
                    return STYLE_BG1;
                }
                case TT_S2: {
                    return STYLE_BG2;
                }
                case TT_S3: {
                    return STYLE_BG3;
                }
                case TT_S4: {
                    return STYLE_BG4;
                }
                case TT_S5: {
                    return STYLE_BG5;
                }
                case TT_S6: {
                    return STYLE_BG6;
                }
                case TT_S7: {
                    return STYLE_BG7;
                }
                case TT_S8: {
                    return STYLE_BG8;
                }
                case TT_S9: {
                    return STYLE_BG9;
                }

                case TT_PRE: {
                    return STYLE_PRE;
                }
            }
            return super.getTokenIdStyle(token);
        }

        private String extract(String s, String pattern, String group) {
            Matcher a = Pattern.compile(pattern).matcher(s);
            if (a.find()) {
                return a.group(group);
            }
            return null;
        }

        private int indexOfAnyChar(String s, int from, char... chars) {
            char[] charArray = s.toCharArray();
            for (int i = from; i < charArray.length; i++) {
                char c = charArray[i];
                for (char c2 : chars) {
                    if (c == c2) {
                        return i;
                    }
                }
            }
            return -1;
        }
    }
}
