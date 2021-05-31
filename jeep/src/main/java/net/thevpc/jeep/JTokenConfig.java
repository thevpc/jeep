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

    boolean isParsetIntNumber();

    JTokenConfig setParseIntNumber(boolean acceptIntNumber);

    JTokenConfig setParseWhitespaces(boolean parseWhitespaces);

    boolean isParseFloatNumber();

    JTokenConfig setParseFloatNumber(boolean acceptFloatNumber);

    boolean isParseDoubleQuotesString();

    JTokenConfig setParseDoubleQuotesString(boolean quoteDbl);

    // writes
    boolean isParsetInfinity();

    JTokenConfig setParsetInfinity(boolean acceptInfinity);

    boolean isParseSimpleQuotesString();

    JTokenConfig setParseSimpleQuotesString(boolean quoteSmp);

    boolean isParseAntiQuotesString();

    JTokenConfig setParseAntiQuotesString(boolean quoteAnti);

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

    JTokenConfig setParseCStyleBlockComments();

    JTokenConfig setParseCStyleLineComments();
    
    JTokenConfig setParseBashStyleLineComments();

    JTokenConfig setParseXmlStyleBlockComments();

    JTokenConfig setBlockComments(String blockCommentStart, String blockCommentEnd);

    JTokenConfigDefinition readOnly();

    JTokenConfigBuilder builder();
}
