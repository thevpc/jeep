package net.thevpc.jeep;

public interface JEvaluator<T> {
    Object evaluate(JNode node, JInvokeContext context);
}
