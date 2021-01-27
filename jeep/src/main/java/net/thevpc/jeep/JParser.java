package net.thevpc.jeep;

public interface JParser<T extends JNode> {
    JCompilationUnit compilationUnit();

    T parse();

}
