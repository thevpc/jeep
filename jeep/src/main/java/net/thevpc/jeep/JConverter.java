package net.thevpc.jeep;

public interface JConverter {
    JTypePattern originalType();
    JTypePattern targetType();

    double weight();
    Object convert(Object value, JInvokeContext context);
}
