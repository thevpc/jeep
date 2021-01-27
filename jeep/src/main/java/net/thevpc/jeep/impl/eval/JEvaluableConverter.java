package net.thevpc.jeep.impl.eval;

import net.thevpc.jeep.JConverter;
import net.thevpc.jeep.JEvaluable;
import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.*;

public class JEvaluableConverter implements JEvaluable {
    private final JConverter argConverter;
    private final JEvaluable value;

    public JEvaluableConverter(JConverter argConverter, JEvaluable value) {
        this.argConverter = argConverter;
        this.value = value;
    }

    @Override
    public JType type() {
        return argConverter.targetType().getType();
    }

    @Override
    public Object evaluate(JInvokeContext context) {
        return argConverter.convert(value.evaluate(context), context);
    }

    public JConverter getArgConverter() {
        return argConverter;
    }

    public JEvaluable getValue() {
        return value;
    }
}
