package net.thevpc.jeep.source;

public interface JTextSourcePosition {
    int getCurrentRowNumber();

    int getCurrentColumnNumber();

    int getCurrentCharNumber();

    int getCurrentTokenNumber();

    JTextSourcePosition readOnly();
}
