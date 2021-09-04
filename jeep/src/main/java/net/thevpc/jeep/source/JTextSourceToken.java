package net.thevpc.jeep.source;

public interface JTextSourceToken {

    JTextSource getSource();

    int getStartLineNumber();

    int getStartColumnNumber();

    int getStartCharacterNumber();

    int getEndLineNumber();

    int getEndColumnNumber();

    int getEndCharacterNumber();

    JTextSourceToken copy();

    long getTokenNumber();
}

