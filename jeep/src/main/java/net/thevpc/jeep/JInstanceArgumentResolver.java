package net.thevpc.jeep;

public interface JInstanceArgumentResolver {
    JTypedValue getInstance(Object actualInstance,Object[] arguments);
}
