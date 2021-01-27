package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;

public class DefaultJInvokeContext implements JInvokeContext {
    private JEvaluator evaluator;
    private JTypedValue instance;
    private JContext context;
    private JEvaluable[] arguments;
    private String name;
    private JType[] argumentTypes;
    private JCallerInfo callerInfo;
    private Object extra;

    public DefaultJInvokeContext(JContext context, JEvaluator evaluator, JTypedValue instance, JEvaluable[] arguments, String name,JCallerInfo callerInfo,Object extra) {
        this.context = context;
        this.instance = instance;
        this.evaluator = evaluator;
        this.arguments = arguments;
        this.name = name;
        this.argumentTypes = new JType[arguments.length];
        for (int i = 0; i < this.argumentTypes.length; i++) {
            this.argumentTypes[i]=arguments[i].type();
        }
        this.callerInfo = callerInfo;
        this.extra = extra;
    }

    @Override
    public <T> T getExtra() {
        return (T) extra;
    }

    @Override
    public Object evaluateArg(int index) {
        return evaluate(arguments[index]);
    }

    @Override
    public JEvaluator getEvaluator() {
        return evaluator;
    }

    @Override
    public JTypedValue getInstance() {
        return instance;
    }

    @Override
    public Object evaluate(JEvaluable node) {
        return node.evaluate(this);
    }

    @Override
    public Object evaluate(JNode node) {
        return getEvaluator().evaluate(node,this);
    }

    @Override
    public Object[] evaluate(JEvaluable[] node) {
        Object[] a=new Object[node.length];
        for (int i = 0; i < a.length; i++) {
            a[i]=evaluate(node[i]);
        }
        return a;
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
    public JType[] getArgumentTypes() {
        return argumentTypes;
    }

    @Override
    public JInvokeContextBuilder builder() {
        return new DefaultJInvokeContextBuilder(this);
    }

    @Override
    public JCallerInfo getCallerInfo() {
        return callerInfo;
    }
}
