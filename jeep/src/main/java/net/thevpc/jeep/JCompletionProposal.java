package net.thevpc.jeep;

public interface JCompletionProposal extends Comparable<JCompletionProposal> {
    int insertOffset();

    String sortText();

    String insertPrefix();

    String name();

    Object handler();

    int category();

    JTokensString getLhsHtml();

    JTokensString getRhsHtml();
}
