package net.thevpc.jeep.impl;

import net.thevpc.jeep.JConverter;
import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypePattern;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;

public class CastJConverter implements JConverter {
    private JTypePattern from;
    private JType to;

    public CastJConverter(JTypePattern from, JType to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public double weight() {
        return 2;
    }

    @Override
    public JTypePattern originalType() {
        return from;
    }

    @Override
    public JTypePattern targetType() {
        return JTypePattern.of(to);
    }

    @Override
    public Object convert(Object value, JInvokeContext context) {
        from.getType().boxed().cast(value);
        return to.boxed().cast(value);
    }

    @Override
    public String toString() {
        return "Cast{" +
                JTypeUtils.str(originalType()) +
                "->" + JTypeUtils.str(targetType()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CastJConverter that = (CastJConverter) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        return to != null ? to.equals(that.to) : that.to == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}
