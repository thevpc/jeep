package net.thevpc.jeep;

public interface JParameterizedConstructor extends JConstructor {
    JConstructor getRawConstructor();
    JType[] getActualParameters();
}
