package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JInvoke;
import net.thevpc.jeep.JType;

public class JFunctionLocal extends JFunctionFromInvoke {
    public JFunctionLocal(String name, JType returnType, JType[] argTypes, boolean varArgs, JInvoke handler) {
        super(name, returnType, argTypes, varArgs, handler);
    }
}
