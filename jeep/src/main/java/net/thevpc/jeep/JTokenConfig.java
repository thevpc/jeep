package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.SeparatorsPattern;

import java.util.List;
import java.util.Set;

public interface JTokenConfig {
    Set<String> getOperators();

    JTokenConfig setOperators(Set<String> operators);

    boolean isCaseSensitive();

    JTokenConfig setCaseSensitive(boolean caseSensitive);

    Set<String> getKeywords();

    JTokenConfig setKeywords(Set<String> keywords);

    JTokenPattern getIdPattern();

    JTokenConfig setIdPattern(JTokenPattern pattern);

    boolean isParseWhitespaces();
    boolean isAcceptIntNumber();

    JTokenConfig setAcceptIntNumber(boolean acceptIntNumber);

    JTokenConfig setParseWhitespaces(boolean parseWhitespaces);

    boolean isAcceptFloatNumber();

    JTokenConfig setAcceptFloatNumber(boolean acceptFloatNumber);

    boolean isDoubleQuote();

    JTokenConfig setDoubleQuote(boolean quoteDbl);

    // writes

    boolean isAcceptInfinity();

    JTokenConfig setAcceptInfinity(boolean acceptInfinity);

    boolean isSimpleQuote();

    JTokenConfig setSimpleQuote(boolean quoteSmp);

    boolean isAntiQuote();

    JTokenConfig setAntiQuote(boolean quoteAnti);

    String getLineComment();

    JTokenConfig setLineComment(String lineComment);

    String getBlockCommentStart();

    JTokenConfig setBlockCommentStart(String blockCommentStart);

    String getBlockCommentEnd();

    JTokenConfig setBlockCommentEnd(String blockCommentEnd);

    boolean isReadOnly();

    char[] getNumberSuffixes();

    JTokenEvaluator getNumberEvaluator();

    JTokenConfig setAll(JTokenConfig other);

    List<JTokenPattern> getPatterns();

    JTokenConfig setPatterns(List<JTokenPattern> other);

    JTokenConfig addSeparators(SeparatorsPattern... patterns);

    JTokenConfig addPatterns(JTokenPattern... patterns);

    JTokenConfig addPattern(JTokenPattern pattern);

    JTokenConfig addKeywords(String... keywords);

    JTokenConfig addOperator(String operator);

    JTokenConfig addOperators(String... operators);

    JTokenConfig setCStyleBlockComments();

    JTokenConfig setCStyleComments();

    JTokenConfig setBlockComments(String blockCommentStart, String blockCommentEnd);

    JTokenConfigDefinition readOnly();

    JTokenConfigBuilder builder();
}
