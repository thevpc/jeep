package net.thevpc.jeep;

public interface JString {
    String name();
    int startLineNumber();
    int startColumnNumber();
    long startCharacterNumber();

    int endLineNumber();
    int endColumnNumber();
    long endCharacterNumber();

}
