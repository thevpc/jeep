package net.thevpc.jeep;

import net.thevpc.jeep.impl.functions.JSignature;

public interface JInvokable extends JDeclaration{

    Object invoke(JInvokeContext context);

    JSignature getSignature();

    JType getReturnType();

    default JType getGenericReturnType() {
        return getReturnType();
    }

    String getName();

    boolean isPublic();
}
