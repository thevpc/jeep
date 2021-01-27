package net.thevpc.jeep.core;

import net.thevpc.jeep.JShouldNeverHappenException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class JExpressionUnaryOptions implements Cloneable {
    private Set<String> excludedPrefixUnaryOperators;
    private boolean excludedPrefixParenthesis;
    private boolean excludedPrefixBrackets;
    private boolean excludedPrefixBraces;
    private boolean excludedAnnotations;

    private boolean excludedTerminalParenthesis;
    private boolean excludedTerminalBrackets;
    private boolean excludedTerminalBraces;

    private boolean excludedTerminal;
    private Set<String> excludedPostfixUnaryOperators;
    private boolean excludedPostfixParenthesis;
    private boolean excludedPostfixBrackets;
    private boolean excludedPostfixBraces;

    public JExpressionUnaryOptions copy() {
        try {
            JExpressionUnaryOptions a = (JExpressionUnaryOptions) clone();
            if (a.getExcludedPrefixUnaryOperators() != null) {
                a.setExcludedPrefixUnaryOperators(new HashSet<>(a.getExcludedPrefixUnaryOperators()));
            }
            if (a.getExcludedPostfixUnaryOperators() != null) {
                a.setExcludedPostfixUnaryOperators(new HashSet<>(a.getExcludedPostfixUnaryOperators()));
            }
            return a;
        } catch (CloneNotSupportedException e) {
            throw new JShouldNeverHappenException();
        }
    }

    public boolean isExcludedAnnotations() {
        return excludedAnnotations;
    }

    public JExpressionUnaryOptions setExcludedAnnotations(boolean excludedAnnotations) {
        this.excludedAnnotations = excludedAnnotations;
        return this;
    }

    public Set<String> getExcludedPrefixUnaryOperators() {
        return excludedPrefixUnaryOperators;
    }

    public JExpressionUnaryOptions setExcludedPrefixUnaryOperators(String... excludedPrefixUnaryOperators) {
        this.excludedPrefixUnaryOperators = new LinkedHashSet<>(Arrays.asList(excludedPrefixUnaryOperators));
        return this;
    }

    public JExpressionUnaryOptions setExcludedPrefixUnaryOperators(Set<String> excludedPrefixUnaryOperators) {
        this.excludedPrefixUnaryOperators = excludedPrefixUnaryOperators;
        return this;
    }

    public boolean isExcludedPrefixParenthesis() {
        return excludedPrefixParenthesis;
    }

    public JExpressionUnaryOptions setExcludedPrefixParenthesis(boolean excludedPrefixParenthesis) {
        this.excludedPrefixParenthesis = excludedPrefixParenthesis;
        return this;
    }

    public boolean isExcludedPrefixBrackets() {
        return excludedPrefixBrackets;
    }

    public JExpressionUnaryOptions setExcludedPrefixBrackets(boolean excludedPrefixBrackets) {
        this.excludedPrefixBrackets = excludedPrefixBrackets;
        return this;
    }

    public boolean isExcludedPrefixBraces() {
        return excludedPrefixBraces;
    }

    public JExpressionUnaryOptions setExcludedPrefixBraces(boolean excludedPrefixBraces) {
        this.excludedPrefixBraces = excludedPrefixBraces;
        return this;
    }

    public boolean isExcludedTerminalParenthesis() {
        return excludedTerminalParenthesis;
    }

    public JExpressionUnaryOptions setExcludedTerminalParenthesis(boolean excludedTerminalParenthesis) {
        this.excludedTerminalParenthesis = excludedTerminalParenthesis;
        return this;
    }

    public boolean isExcludedTerminalBrackets() {
        return excludedTerminalBrackets;
    }

    public JExpressionUnaryOptions setExcludedTerminalBrackets(boolean excludedTerminalBrackets) {
        this.excludedTerminalBrackets = excludedTerminalBrackets;
        return this;
    }

    public boolean isExcludedTerminalBraces() {
        return excludedTerminalBraces;
    }

    public JExpressionUnaryOptions setExcludedTerminalBraces(boolean excludedTerminalBraces) {
        this.excludedTerminalBraces = excludedTerminalBraces;
        return this;
    }

    public boolean isExcludedTerminal() {
        return excludedTerminal;
    }

    public JExpressionUnaryOptions setExcludedTerminal(boolean excludedTerminal) {
        this.excludedTerminal = excludedTerminal;
        return this;
    }

    public Set<String> getExcludedPostfixUnaryOperators() {
        return excludedPostfixUnaryOperators;
    }

    public JExpressionUnaryOptions setExcludedPostfixUnaryOperators(String... excludedPostfixUnaryOperators) {
        this.excludedPostfixUnaryOperators = new LinkedHashSet<>(Arrays.asList(excludedPostfixUnaryOperators));
        return this;
    }

    public JExpressionUnaryOptions setExcludedPostfixUnaryOperators(Set<String> excludedPostfixUnaryOperators) {
        this.excludedPostfixUnaryOperators = excludedPostfixUnaryOperators;
        return this;
    }

    public boolean isExcludedPostfixParenthesis() {
        return excludedPostfixParenthesis;
    }

    public JExpressionUnaryOptions setExcludedPostfixParenthesis(boolean excludedPostfixParenthesis) {
        this.excludedPostfixParenthesis = excludedPostfixParenthesis;
        return this;
    }

    public boolean isExcludedPostfixBrackets() {
        return excludedPostfixBrackets;
    }

    public JExpressionUnaryOptions setExcludedPostfixBrackets(boolean excludedPostfixBrackets) {
        this.excludedPostfixBrackets = excludedPostfixBrackets;
        return this;
    }

    public boolean isExcludedPostfixBraces() {
        return excludedPostfixBraces;
    }

    public JExpressionUnaryOptions setExcludedPostfixBraces(boolean excludedPostfixBraces) {
        this.excludedPostfixBraces = excludedPostfixBraces;
        return this;
    }
}
