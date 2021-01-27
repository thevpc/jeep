package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;

public class DefaultJInvokeContextBuilder implements JInvokeContextBuilder {
    private JEvaluator evaluator;
    private JTypedValue instance;
    private JContext context;
    private JEvaluable[] arguments;
    private String name;
    private JCallerInfo callerInfo;
    private Object extra;

    public DefaultJInvokeContextBuilder() {
    }
    public DefaultJInvokeContextBuilder(JContext context, JEvaluator evaluator, JTypedValue instance, JEvaluable[] arguments, String name,JCallerInfo callerInfo,Object extra) {
        this.context = context;
        this.instance = instance;
        this.evaluator = evaluator;
        this.arguments = arguments;
        this.name = name;
        this.callerInfo = callerInfo;
        this.extra = extra;
    }


    public DefaultJInvokeContextBuilder(JInvokeContextBuilder other) {
        set(other);
    }

    public DefaultJInvokeContextBuilder(JInvokeContext other) {
        set(other);
    }

    public JInvokeContextBuilder set(JInvokeContextBuilder other){
        if(other!=null){
            setEvaluator(other.getEvaluator());
            setContext(other.getContext());
            setInstance(other.getInstance());
            setArguments(other.getArguments());
            setName(other.getName());
            setCallerInfo(other.getCallerInfo());
            setExtra(other.getExtra());
        }
        return this;
    }
    public JInvokeContextBuilder set(JInvokeContext other){
        if(other!=null){
            setEvaluator(other.getEvaluator());
            setContext(other.getContext());
            setInstance(other.getInstance());
            setArguments(other.getArguments());
            setName(other.getName());
            setCallerInfo(other.getCallerInfo());
            setExtra(other.getExtra());
        }
        return this;
    }



    @Override
    public JTypedValue getInstance() {
        return instance;
    }

    @Override
    public JEvaluator getEvaluator() {
        return evaluator;
    }

    @Override
    public JContext getContext() {
        return context;
    }

    @Override
    public JEvaluable[] getArguments() {
        return arguments;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DefaultJInvokeContextBuilder setEvaluator(JEvaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public DefaultJInvokeContextBuilder setInstance(JTypedValue instance) {
        this.instance = instance;
        return this;
    }

    public DefaultJInvokeContextBuilder setContext(JContext context) {
        this.context = context;
        return this;
    }

    public DefaultJInvokeContextBuilder setArguments(JEvaluable[] arguments) {
        this.arguments = arguments;
        return this;
    }

    public DefaultJInvokeContextBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public JCallerInfo getCallerInfo() {
        return callerInfo;
    }

    @Override
    public JInvokeContextBuilder setCallerInfo(JCallerInfo callerInfo) {
        this.callerInfo = callerInfo;
        return this;
    }

    @Override
    public <T> T getExtra() {
        return (T) extra;
    }

    @Override
    public DefaultJInvokeContextBuilder setExtra(Object extra) {
        this.extra = extra;
        return this;
    }

    @Override
    public JInvokeContext build() {
        return new DefaultJInvokeContext(context, evaluator, instance, arguments, name,callerInfo,extra);
    }
}
