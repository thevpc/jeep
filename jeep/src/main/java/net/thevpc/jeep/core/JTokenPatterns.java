package net.thevpc.jeep.core;

import net.thevpc.jeep.JTokenEvaluator;
import net.thevpc.jeep.JTokenPattern;
import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.core.tokens.*;
import net.thevpc.jeep.impl.tokens.DollarVarPattern;
import net.thevpc.jeep.impl.tokens.JTokenId;

public class JTokenPatterns {
    private JTokenPatterns() {
    }

    public static JTokenPattern forJavaId() {
        return new JavaIdPattern();
    }

    public static JTokenPattern forDollarVar() {
        return new DollarVarPattern();
    }

    public static JTokenPattern forNumber(boolean acceptIntNumber, boolean acceptFloatNumber, boolean acceptInfinity, char[] numberSuffixes, JTokenEvaluator numberEvaluator) {
        return new NumberPattern(null, acceptIntNumber, acceptFloatNumber, acceptInfinity, numberSuffixes, numberEvaluator);
    }

    public static JTokenPattern forIntNumber() {
        return new NumberPattern(null, true, false, false, new char[]{'l', 'L'}, null);
    }

    public static JTokenPattern forFloatNumber() {
        return new NumberPattern(null, true, true, true, new char[]{'f', 'F'}, null);
    }

    public static JTokenPattern forCstyleBlockComments() {
        return new BlockCommentsPattern(new JTokenDef(
                JTokenId.BLOCK_COMMENTS,
                "BLOCK_COMMENTS",
                JTokenType.TT_BLOCK_COMMENTS,
                "TT_BLOCK_COMMENTS",
                "/**/"
        ), "/*", "*/");
    }

    public static JTokenPattern forCstyleLineComments() {
        return new LineCommentsPattern(new JTokenDef(
                JTokenId.LINE_COMMENTS,
                "LINE_COMMENTS",
                JTokenType.TT_LINE_COMMENTS,
                "LINE_COMMENTS",
                "//"
        ), "//");
    }

    public static JTokenPattern forStringDoubleQuoted() {
        return new StringPattern(StringPattern.INFO_DOUBLE_QUOTES, '\"', '\\', true);
    }

    public static JTokenPattern forStringSimpleQuoted() {
        return new StringPattern(StringPattern.INFO_SIMPLE_QUOTES, '\'', '\\', true);
    }

    public static JTokenPattern forStringAntiQuoted() {
        return new StringPattern(StringPattern.INFO_ANTI_QUOTES, '\'', '\\', true);
    }

    public static JTokenPattern forWhitespaces() {
        return new WhitespacePattern();
    }

    public static JTokenPattern forOperators(String... ops) {
        return new OperatorsPattern();
    }

    public static JTokenPattern forKeywords(boolean caseSensitive,String... words) {
        return new KeywordsPattern(caseSensitive, words);
    }

    public static SeparatorsPattern forSeparators(String... seps) {
        return new SeparatorsPattern("Separators", JTokenId.OFFSET_SEPARATORS, seps);
    }
}
