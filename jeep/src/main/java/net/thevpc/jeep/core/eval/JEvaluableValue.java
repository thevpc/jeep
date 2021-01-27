package net.thevpc.jeep.core.eval;

import net.thevpc.jeep.JEvaluable;
import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JType;

public class JEvaluableValue implements JEvaluable {
    private Object object;
    private JType type;

    public JEvaluableValue(Object object, JType type) {
        this.object = object;
        this.type = type;
    }

    @Override
    public JType type() {
        return type;
    }

    @Override
    public Object evaluate(JInvokeContext context) {
        return object;
    }

    public Object getObject() {
        return object;
    }
}
