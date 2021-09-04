package net.thevpc.jeep;


import net.thevpc.jeep.source.JTextSource;

public interface JCompilationUnit {
    JTextSource getSource();

    JNode getAst();

    void setAst(JNode newNode);
}
