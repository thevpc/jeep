package net.thevpc.jeep.core.editor;

import net.thevpc.jeep.JCompletionProposal;
import net.thevpc.jeep.JTokensString;

import java.util.Objects;

public class DefaultJCompletionProposal implements JCompletionProposal {
    private int category;
    private int insertOffset;
    private String insertPrefix;

    private String name;
    private String sortText;
    private JTokensString lhs;
    private JTokensString rhs;


    private Object handler;

    public DefaultJCompletionProposal(int category, int insertOffset, String insertPrefix, String name, String sortText,
                                      JTokensString lhs,
                                      JTokensString rhs,
                                      Object handler) {
        this.category = category;
        this.insertOffset = insertOffset;
        this.insertPrefix = insertPrefix;
        this.name = name;
        this.sortText = sortText;
        this.lhs = lhs;
        this.rhs = rhs;
        this.handler = handler;
    }

    @Override
    public int category() {
        return category;
    }

    @Override
    public JTokensString getLhsHtml() {
        return lhs;
    }

    @Override
    public JTokensString getRhsHtml() {
        return rhs;
    }

    @Override
    public String sortText() {
        return sortText;
    }

    @Override
    public String insertPrefix() {
        return insertPrefix;
    }

    public int insertOffset() {
        return insertOffset;
    }

    public String name() {
        return name;
    }

    public Object handler() {
        return handler;
    }

    @Override
    public String toString() {
        return String.valueOf(name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultJCompletionProposal that = (DefaultJCompletionProposal) o;
        return insertOffset == that.insertOffset &&
                Objects.equals(insertPrefix, that.insertPrefix) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sortText, that.sortText) &&
                Objects.equals(handler, that.handler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insertOffset, insertPrefix, name, sortText, handler);
    }

    @Override
    public int compareTo(JCompletionProposal o) {
        return this.sortText().compareTo(o.sortText());
    }
}
