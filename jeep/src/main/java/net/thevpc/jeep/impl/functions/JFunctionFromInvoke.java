package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JInvoke;
import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.core.JFunctionBase;

public class JFunctionFromInvoke extends JFunctionBase {

    private JInvoke handler;

    public JFunctionFromInvoke(String name, JType returnType, JType[] argTypes, boolean varArgs, JInvoke handler) {
        super(name, returnType, argTypes, varArgs, "<unknown-source>");
        this.handler = handler;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        return handler.invoke(icontext);
    }

    public JInvoke getHandler() {
        return handler;
    }

}
