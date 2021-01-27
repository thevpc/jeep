package net.thevpc.jeep.core;

import net.thevpc.jeep.impl.functions.AbstractJInvokable;
import net.thevpc.jeep.JFunction;
import net.thevpc.jeep.JTypes;

public abstract class AbstractJFunction extends AbstractJInvokable implements JFunction {
    private JTypes types;

    public AbstractJFunction(JTypes types) {
        this.types = types;
    }

    @Override
    public JTypes getTypes() {
        return types;
    }
}
