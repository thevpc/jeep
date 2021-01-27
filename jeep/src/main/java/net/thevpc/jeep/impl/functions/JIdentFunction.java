package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.core.JFunctionBase;

public class JIdentFunction extends JFunctionBase {
    public JIdentFunction(String name, JType type) {
        super(name, type,new JType[0],false,"<runtime>");
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        return icontext.evaluate(icontext.getArguments()[0]);
    }

}
