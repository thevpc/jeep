package net.thevpc.jeep.core;

import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypedValue;

public class DefaultJTypedValue implements JTypedValue {
    private Object value;
    private JType type;

    public DefaultJTypedValue(Object value, JType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JType getType() {
        return type;
    }
}
