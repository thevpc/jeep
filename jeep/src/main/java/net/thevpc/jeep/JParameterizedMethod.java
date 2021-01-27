package net.thevpc.jeep;

public interface JParameterizedMethod extends JMethod {
    JMethod getRawMethod();
    JType[] getActualParameters();
}
