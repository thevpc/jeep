package net.thevpc.jeep;

public interface JInvokeContextBuilder {
    JTypedValue getInstance();

    JEvaluator getEvaluator();

    JInvokeContextBuilder setEvaluator(JEvaluator value);

    JInvokeContextBuilder setInstance(JTypedValue instance);

    JContext getContext();

    JInvokeContextBuilder setContext(JContext context);

    JEvaluable[] getArguments();

    <T> T getExtra();

    JInvokeContextBuilder setArguments(JEvaluable[] args);

    String getName();

    JInvokeContextBuilder setName(String name);

    JCallerInfo getCallerInfo();

    JInvokeContextBuilder setCallerInfo(JCallerInfo callerInfo);

    JInvokeContextBuilder setExtra(Object extra);

    JInvokeContext build();
}
