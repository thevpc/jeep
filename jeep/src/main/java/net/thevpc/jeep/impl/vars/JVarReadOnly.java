package net.thevpc.jeep.impl.vars;

import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JVar;

public abstract class JVarReadOnly extends AbstractJVar {

    private String name;
    private JType _type;

    public JVarReadOnly(String name, JType type) {
        this.name = name;
        this._type = type;
    }

    @Override
    public JType type() {
        return _type;
    }

    @Override
    public JType undefinedType() {
        return type();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public JVar setValue(Object value, JInvokeContext context) {
        throw new IllegalArgumentException("read only var " + name());
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
        throw new IllegalArgumentException("read only var " + name());
    }
}
