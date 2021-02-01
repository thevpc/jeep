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
public final class JTokenConfigBuilder implements Cloneable, JTokenConfig {
//    public static final JTokenPattern DEFAULT_ID_PATTERN = new JavaIdPattern();
    //            Pattern.compile("[^\\s0-9\\[\\]{}()*+/~&=$£%'\"-]([^\\s\\[\\]{}()*+/~&=$£%'\"-])*");
    private boolean parsetIntNumber = false;
    private boolean parseWhitespaces = false;
    private boolean parseFloatNumber = false;
    private boolean parsetInfinity = false;
    private char[] numberSuffixes = null;
    private JTokenEvaluator numberEvaluator = null;
    private boolean parseDoubleQuotesString = false;
    private boolean parseSimpleQuotesString = false;
    private boolean parseAntiQuotesString = false;
    private String lineComment = null;
    private String blockCommentStart = null;
    private String blockCommentEnd = null;
    private Set<String> operators = new HashSet<>();
    private List<JTokenPattern> patterns = new ArrayList<>();
    private Set<String> keywords = new HashSet<>();
    private JTokenPattern idPattern = null;//DEFAULT_ID_PATTERN;
    private JTokenConfigDefinition readOnlyCopy;
    private boolean caseSensitive = true;
    public JTokenConfigBuilder() {
    }

    public JTokenConfigBuilder(JTokenConfig other) {
        setAll(other);
    }

//    public static Set<String> _defaultOperators() {
//        return new HashSet<>(Arrays.asList("+", "-", "*", "/", "^", "=", "<", ">", "!", "&", "|"));
//    }

    public JTokenConfigBuilder copy() {
        try {
            return (JTokenConfigBuilder) this.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("ShouldNeverHappen");
        }
    }

    public JTokenConfigBuilder unsetAll(){
        parsetIntNumber = false;
        parseFloatNumber = false;
        parsetInfinity = false;
        numberSuffixes = null;
        numberEvaluator = null;
        parseDoubleQuotesString = false;
        parseSimpleQuotesString = false;
        parseAntiQuotesString = false;
        lineComment = null;
        blockCommentStart = null;
        blockCommentEnd = null;
        operators.clear();;
        patterns.clear();
        keywords.clear();
        idPattern = null;
        readOnlyCopy=null;
        caseSensitive = true;
        parseWhitespaces = false;
        return this;
    }

    @Override
    public boolean isParseWhitespaces() {
        return parseWhitespaces;
    }

    @Override
    public JTokenConfig setParseWhitespaces(boolean parseWhitespaces) {
        this.parseWhitespaces = parseWhitespaces;
        return this;
    }

    @Override
    public Set<String> getOperators() {
        return operators;
    }

    @Override
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    @Override
    public Set<String> getKeywords() {
        return keywords;
    }

    @Override
    public JTokenPattern getIdPattern() {
        return idPattern;
    }

    @Override
    public boolean isParsetIntNumber() {
        return parsetIntNumber;
    }

    @Override
    public boolean isParseFloatNumber() {
        return parseFloatNumber;
    }

    @Override
    public boolean isParseDoubleQuotesString() {
        return parseDoubleQuotesString;
    }

    @Override
    public boolean isParsetInfinity() {
        return parsetInfinity;
    }

    @Override
    public boolean isParseSimpleQuotesString() {
        return parseSimpleQuotesString;
    }

    @Override
    public boolean isParseAntiQuotesString() {
        return parseAntiQuotesString;
    }

    @Override
    public String getLineComment() {
        return lineComment;
    }

    @Override
    public String getBlockCommentStart() {
        return blockCommentStart;
    }

    @Override
    public String getBlockCommentEnd() {
        return blockCommentEnd;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    public char[] getNumberSuffixes() {
        return numberSuffixes;
    }

    public JTokenConfigBuilder setNumberSuffixes(char[] numberSuffixes) {
        this.numberSuffixes = numberSuffixes;
        return this;
    }

    public JTokenEvaluator getNumberEvaluator() {
        return numberEvaluator;
    }

    public JTokenConfigBuilder setNumberEvaluator(JTokenEvaluator numberEvaluator) {
        this.numberEvaluator = numberEvaluator;
        return this;
    }

    @Override
    public JTokenConfig setAll(JTokenConfig other) {
        if (other != null) {
            this.parsetIntNumber = other.isParsetIntNumber();
            this.parseFloatNumber = other.isParseFloatNumber();
            this.numberSuffixes = other.getNumberSuffixes()==null?null:Arrays.copyOf(other.getNumberSuffixes(),other.getNumberSuffixes().length);
            this.numberEvaluator = other.getNumberEvaluator();
            this.parsetInfinity = other.isParsetInfinity();
            this.parseDoubleQuotesString = other.isParseDoubleQuotesString();
            this.parseSimpleQuotesString = other.isParseSimpleQuotesString();
            this.parseAntiQuotesString = other.isParseAntiQuotesString();
            this.lineComment = other.getLineComment();
            this.blockCommentStart = other.getBlockCommentStart();
            this.blockCommentEnd = other.getBlockCommentEnd();
            this.operators = new HashSet<>(other.getOperators());
            this.keywords = new HashSet<>(other.getKeywords());
            this.patterns = new ArrayList<>(other.getPatterns());
            this.caseSensitive = other.isCaseSensitive();
            this.idPattern = other.getIdPattern();
            this.parseWhitespaces = other.isParseWhitespaces();
        }
        return this;
    }

    @Override
    public List<JTokenPattern> getPatterns() {
        return patterns;
    }

    public JTokenPattern getPattern(String name){
        return getPatterns().stream().filter(x -> x.name().equals(name)).findFirst().get();
    }
    
    public void removePattern(JTokenPattern p){
        patterns.remove(p);
    }

    @Override
    public JTokenConfig addSeparators(SeparatorsPattern... patterns) {
        return addPatterns(patterns);
    }

    @Override
    public JTokenConfig addPatterns(JTokenPattern... patterns) {
        if (patterns != null) {
            for (JTokenPattern pattern : patterns) {
                addPattern(pattern);
            }
        }
        return this;
    }

    @Override
    public JTokenConfig addPattern(JTokenPattern pattern) {
        if (pattern != null) {
            this.patterns.add(pattern);
        }
        return this;
    }

    @Override
    public JTokenConfig setPatterns(List<JTokenPattern> other) {
        this.patterns.clear();
        if (other != null) {
            for (JTokenPattern tokenPattern : other) {
                addPattern(tokenPattern);
            }
        }
        return this;
    }


    @Override
    public JTokenConfig addKeywords(String... keywords) {
        for (String keyword : keywords) {
            if (keyword != null) {
                getKeywords().add(keyword);
            }
        }
        return this;
    }

    @Override
    public JTokenConfig setKeywords(Set<String> keywords) {
        this.keywords = keywords == null ? new HashSet<>() : keywords;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfigBuilder addOperator(String s) {
        if (s != null) {
            operators.add(s);
        }
        return this;
    }

    @Override
    public JTokenConfigBuilder addOperators(String... s) {
        for (String s1 : s) {
            addOperator(s1);
        }
        return this;
    }

    @Override
    public JTokenConfig setOperators(Set<String> operators) {
        this.operators = operators == null ? new HashSet<>() : operators;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setParsetIntNumber(boolean parseIntNumber) {
        this.parsetIntNumber = parseIntNumber;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setParseFloatNumber(boolean parseFloatNumber) {
        this.parseFloatNumber = parseFloatNumber;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setBlockCommentEnd(String blockCommentEnd) {
        this.blockCommentEnd = blockCommentEnd;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setParseCStyleBlockComments() {
        return setBlockComments("/*", "*/");
    }

    @Override
    public JTokenConfig setParseCStyleLineComments() {
        return setLineComment("//");
    }

    @Override
    public JTokenConfig setBlockComments(String blockCommentStart, String blockCommentEnd) {
        if (blockCommentStart == null || blockCommentStart.length() == 0) {
            blockCommentStart = null;
        }
        if (blockCommentEnd == null || blockCommentEnd.length() == 0) {
            blockCommentEnd = null;
        }
        if ((blockCommentStart == null) != (blockCommentEnd == null)) {
            throw new IllegalArgumentException("invalid Block comment separators " + blockCommentStart + " " + blockCommentEnd);
        }
        this.blockCommentStart = blockCommentStart;
        this.blockCommentEnd = blockCommentEnd;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setIdPattern(JTokenPattern pattern) {
        this.idPattern = pattern;
//        this.pattern = pattern == null ? DEFAULT_ID_PATTERN : pattern;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setLineComment(String lineComment) {
        if (lineComment == null || lineComment.length() == 0) {
            lineComment = null;
        }
        this.lineComment = lineComment;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setBlockCommentStart(String blockCommentStart) {
        this.blockCommentStart = blockCommentStart;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setParsetInfinity(boolean parsetInfinity) {
        this.parsetInfinity = parsetInfinity;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setParseDoubleQuotesString(boolean doubleQuote) {
        this.parseDoubleQuotesString = doubleQuote;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setParseSimpleQuotesString(boolean quoteSmp) {
        this.parseSimpleQuotesString = quoteSmp;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setParseAntiQuotesString(boolean antiQuote) {
        this.parseAntiQuotesString = antiQuote;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfigDefinition readOnly() {
        if (readOnlyCopy == null) {
            readOnlyCopy = new JTokenConfigDefinition(this);
        }
        return readOnlyCopy;
    }

    @Override
    public JTokenConfigBuilder builder() {
        return new JTokenConfigBuilder(this);
    }
}
