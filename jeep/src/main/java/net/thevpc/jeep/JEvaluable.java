package net.thevpc.jeep;

public interface JEvaluable {
    JType type();
    Object evaluate(JInvokeContext context);
}

