package net.thevpc.jeep.core;

import net.thevpc.jeep.JShouldNeverHappenException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class JExpressionBinaryOptions implements Cloneable{
    private Set<String> excludedBinaryOperators;
    private boolean excludedListOperator;
    private boolean excludedImplicitOperator;

    public JExpressionBinaryOptions copy(){
        try {
            JExpressionBinaryOptions a=(JExpressionBinaryOptions) clone();
            if(a.getExcludedBinaryOperators() !=null){
                a.setExcludedBinaryOperators(new HashSet<>(a.getExcludedBinaryOperators()));
            }
            return a;
        } catch (CloneNotSupportedException e) {
            throw new JShouldNeverHappenException();
        }
    }

    public Set<String> getExcludedBinaryOperators() {
        return excludedBinaryOperators;
    }

    public JExpressionBinaryOptions setExcludedBinaryOperators(String... excludedBinaryOperators) {
        this.excludedBinaryOperators = new LinkedHashSet<>(Arrays.asList(excludedBinaryOperators));
        return this;
    }
    public JExpressionBinaryOptions setExcludedBinaryOperators(Set<String> excludedBinaryOperators) {
        this.excludedBinaryOperators = excludedBinaryOperators;
        return this;
    }

    public boolean isExcludedListOperator() {
        return excludedListOperator;
    }

    public JExpressionBinaryOptions setExcludedListOperator(boolean excludedListOperator) {
        this.excludedListOperator = excludedListOperator;
        return this;
    }

    public boolean isExcludedImplicitOperator() {
        return excludedImplicitOperator;
    }

    public JExpressionBinaryOptions setExcludedImplicitOperator(boolean excludedImplicitOperator) {
        this.excludedImplicitOperator = excludedImplicitOperator;
        return this;
    }
}
