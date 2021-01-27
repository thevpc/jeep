package net.thevpc.jeep;

public interface JNodeVisitor {
    default void startVisit(JNode node){}

    default void endVisit(JNode node){}
}
