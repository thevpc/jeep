package net.thevpc.jeep;

import java.util.List;

public interface JCompletion {
    void setCompilationUnit(JCompilationUnit compilationUnit);

    void setCompilationUnit(String compilationUnitText, String sourceName);

    JLocationContext findLocation(int caretOffset);

    List<JCompletionProposal> findProposals(int caretOffset, int level);
}
