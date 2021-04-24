/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.SeparatorsPattern;

import java.util.*;

/**
 * @author thevpc
 */
public final class JTokenConfigDefinition implements Cloneable, JTokenConfig {

    public static final JTokenConfigDefinition DEFAULT = new JTokenConfigDefinition(null);

    private final boolean acceptIntNumber;
    private final boolean acceptFloatNumber;
    private final boolean acceptInfinity;
    private final boolean quoteDbl;
    private final boolean quoteSmp;
    private final boolean quoteAnti;
    private final boolean parseWhitespaces;
    private final String lineComment;
    private final String blockCommentStart;
    private final String blockCommentEnd;
    private final Set<String> operators;
    private final Set<String> keywords;
    private final List<JTokenPattern> patterns;
    private final boolean caseSensitive;
    private final JTokenPattern idPattern;
    private char[] numberSuffixes = null;
    private JTokenEvaluator numberEvaluator = null;

    public JTokenConfigDefinition(JTokenConfigBuilder other) {
        if (other != null) {
            this.acceptIntNumber = other.isParsetIntNumber();
            this.acceptFloatNumber = other.isParseFloatNumber();
            this.numberSuffixes = other.getNumberSuffixes() == null ? new char[0] : Arrays.copyOf(other.getNumberSuffixes(), other.getNumberSuffixes().length);
            this.numberEvaluator = other.getNumberEvaluator();
            this.acceptInfinity = other.isParsetInfinity();
            this.quoteDbl = other.isParseDoubleQuotesString();
            this.quoteSmp = other.isParseSimpleQuotesString();
            this.quoteAnti = other.isParseAntiQuotesString();
            this.lineComment = other.getLineComment();
            this.blockCommentStart = other.getBlockCommentStart();
            this.blockCommentEnd = other.getBlockCommentEnd();
            this.operators = Collections.unmodifiableSet(new HashSet<>(other.getOperators()));
            this.keywords = Collections.unmodifiableSet(new HashSet<>(other.getKeywords()));
            this.caseSensitive = other.isCaseSensitive();
            this.idPattern = other.getIdPattern();
            this.patterns = Collections.unmodifiableList(new ArrayList<>(other.getPatterns()));
            this.parseWhitespaces = other.isParseWhitespaces();
        } else {
            this.acceptIntNumber = false;
            this.acceptFloatNumber = false;
            this.numberSuffixes = new char[0];
            this.numberEvaluator = null;
            this.acceptInfinity = false;
            this.quoteDbl = false;
            this.quoteSmp = false;
            this.quoteAnti = false;
            this.lineComment = null;
            this.blockCommentStart = null;
            this.blockCommentEnd = null;
            this.operators = Collections.emptySet();
//                    Collections.unmodifiableSet(
//                    JTokenConfigBuilder._defaultOperators()
//            );
            this.keywords = Collections.emptySet();
            this.patterns = Collections.emptyList();
            this.caseSensitive = true;
            this.idPattern = null;
            this.parseWhitespaces = false;
//                    JTokenConfigBuilder.DEFAULT_ID_PATTERN;
        }
    }

    @Override
    public boolean isParseWhitespaces() {
        return parseWhitespaces;
    }

    @Override
    public char[] getNumberSuffixes() {
        return (numberSuffixes == null || numberSuffixes.length == 0) ? numberSuffixes : Arrays.copyOf(numberSuffixes, numberSuffixes.length);
    }

    @Override
    public JTokenEvaluator getNumberEvaluator() {
        return numberEvaluator;
    }

    @Override
    public List<JTokenPattern> getPatterns() {
        return patterns;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public JTokenConfigBuilder builder() {
        return new JTokenConfigBuilder(this);
    }

    public JTokenConfigDefinition copy() {
        try {
            return (JTokenConfigDefinition) this.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("ShouldNeverHappen");
        }
    }

    public Set<String> getOperators() {
        return operators;
    }

    @Override
    public Set<String> getKeywords() {
        return keywords;
    }

    public JTokenPattern getIdPattern() {
        return idPattern;
    }

    public boolean isParsetIntNumber() {
        return acceptIntNumber;
    }

    public boolean isParseFloatNumber() {
        return acceptFloatNumber;
    }

    public boolean isParsetInfinity() {
        return acceptInfinity;
    }

    public boolean isParseDoubleQuotesString() {
        return quoteDbl;
    }

    public boolean isParseSimpleQuotesString() {
        return quoteSmp;
    }

    public boolean isParseAntiQuotesString() {
        return quoteAnti;
    }

    public String getLineComment() {
        return lineComment;
    }

    public String getBlockCommentStart() {
        return blockCommentStart;
    }

    public String getBlockCommentEnd() {
        return blockCommentEnd;
    }

    public boolean isReadOnly() {
        return true;
    }

    @Override
    public JTokenConfigDefinition readOnly() {
        return this;
    }

    // exceptions for writes
    private JTokenConfig throwReadOnly() {
        throw new JParseException("This is a read only copy of Token configuration");
    }

    @Override
    public JTokenConfig setAll(JTokenConfig other) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addKeywords(String... keywords) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setKeywords(Set<String> keywords) {
        return null;
    }

    @Override
    public JTokenConfig setParseAntiQuotesString(boolean quoteAnti) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setOperators(Set<String> operators) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParseIntNumber(boolean acceptIntNumber) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParseWhitespaces(boolean parseWhitespaces) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParseFloatNumber(boolean acceptFloatNumber) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setBlockCommentEnd(String blockCommentEnd) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParseCStyleBlockComments() {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParseBashStyleLineComments() {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParseCStyleLineComments() {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setBlockComments(String blockCommentStart, String blockCommentEnd) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setCaseSensitive(boolean caseSensitive) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setIdPattern(JTokenPattern pattern) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setLineComment(String lineComment) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setBlockCommentStart(String blockCommentStart) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParsetInfinity(boolean acceptInfinity) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParseDoubleQuotesString(boolean quoteDbl) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setParseSimpleQuotesString(boolean quoteSmp) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addPatterns(JTokenPattern... patterns) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addPattern(JTokenPattern pattern) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setPatterns(List<JTokenPattern> other) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addOperator(String operator) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addOperators(String... operators) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addSeparators(SeparatorsPattern... patterns) {
        return throwReadOnly();
    }
}
