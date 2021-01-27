package net.thevpc.jeep.core;

import net.thevpc.jeep.JTokenIdFilter;

public abstract class AbstractJTokenIdFilter implements JTokenIdFilter {
    public boolean isIdentifierStart(char cc) {
        return Character.isJavaIdentifierPart(cc);
    }

    public boolean isIdentifierPart(char cc) {
        return Character.isJavaIdentifierPart(cc);
    }
}
