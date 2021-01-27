package net.thevpc.jeep;

public interface JInvokeContext {
    JEvaluator getEvaluator();

    JCallerInfo  getCallerInfo();

    JTypedValue getInstance();

    <T> T getExtra();

    Object evaluateArg(int index);

    Object evaluate(JNode node);

    Object evaluate(JEvaluable node);

    Object[] evaluate(JEvaluable[] node);

    JContext getContext();

    JEvaluable[] getArguments();

    String getName();

    JType[] getArgumentTypes();

    JInvokeContextBuilder builder();
}
