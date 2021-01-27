package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.vars.AbstractJVar;

public class JFunctionJVar extends AbstractJVar {

    private final JFunction fct;
    private final JContext context;

    public JFunctionJVar(JFunction fct, JContext context) {
        this.fct = fct;
        this.context = context;
    }

    @Override
    public JType undefinedType() {
        return JTypeUtils.forObject(context.types());
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String name() {
        return fct.getName();
    }

    @Override
    public JType type() {
        return fct.getReturnType();
//                context.types().forName(Object.class);
    }

//    @Override
//    public JType getEffectiveType(JContext context) {
//        return fct.returnType();
//    }

    @Override
    public Object getValue(JInvokeContext context) {
        return fct.invoke(context
//                new DefaultJInvokeContext(
//                        context.getContext(),
//                        context.getContext().evaluators().newEvaluator(),//???
//                        null,
//                        new JEvaluable[0],
//                        null,null
//                )
        );
    }

    @Override
    public JVar setValue(Object value, JInvokeContext context) {
        throw new UnsupportedOperationException("Not supported");
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
