package net.thevpc.jeep.core;

import net.thevpc.jeep.JInvoke;
import net.thevpc.jeep.JInvokeContext;

public class EmptyJInvoke implements JInvoke {
    public static final EmptyJInvoke INSTANCE=new EmptyJInvoke();

    private EmptyJInvoke() {
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return null;
    }
}
