package net.thevpc.jeep;

public interface JNodeFindAndReplace {
    boolean accept(JNode node);
    JNode replace(JNode node);
    default boolean isReplaceFirst(){
        return true;
    }
}
