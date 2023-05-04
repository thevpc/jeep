package net.thevpc.jeep;

public interface JTypeNameOrVariable {
    String name();
    JTypeNameOrVariable withSimpleName();
    JTypeNameOrVariable toArray();
}
