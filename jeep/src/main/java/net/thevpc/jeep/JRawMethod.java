package net.thevpc.jeep;

import net.thevpc.jeep.impl.functions.JSignature;

public interface JRawMethod extends JMethod {
    JSignature getGenericSignature();
    JType getGenericReturnType();
}
