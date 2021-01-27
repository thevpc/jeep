package net.thevpc.jeep;

public interface JTypeOrVariable {
    String getName();
    boolean isVar();
    JTypeVariable toVar();
}
