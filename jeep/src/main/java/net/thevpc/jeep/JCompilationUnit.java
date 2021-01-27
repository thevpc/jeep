package net.thevpc.jeep;

import net.thevpc.common.textsource.JTextSource;

public interface JCompilationUnit {
    JTextSource getSource();

    JNode getAst();

    void setAst(JNode newNode);
}
