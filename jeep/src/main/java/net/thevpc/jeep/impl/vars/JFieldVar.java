package net.thevpc.jeep.impl.vars;

import net.thevpc.jeep.JField;
import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JVar;
import net.thevpc.jeep.*;

public class JFieldVar extends AbstractJVar {

    private final JField field;
    private final Object instance;
    private final boolean readOnly;

    public JFieldVar(Object instance, JField field, boolean readOnly) {
        this.field = field;
        this.instance = instance;
        this.readOnly = readOnly;
    }

    @Override
    public JType undefinedType() {
        return field.type();
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String name() {
        return field.name();
    }

    @Override
    public JType type() {
        return field.type();
    }

    @Override
    public Object getValue(JInvokeContext context) {
        return field.get(instance);
    }

    @Override
    public JVar setValue(Object value, JInvokeContext context) {
        if (readOnly) {
            throw new UnsupportedOperationException("Not supported");
        }
        field.set(instance, value);
        return this;
    }

    @Override
    public boolean isDefinedValue() {
        return true;
    }

    @Override
    public boolean isUndefinedValue() {
        return false;
    }

    @Override
    public JVar setUndefinedValue() {
        throw new UnsupportedOperationException("Not supported");
    }
}
