package net.thevpc.jeep;

import net.thevpc.jeep.impl.functions.JSignature;

public interface JRawConstructor extends JConstructor {
    JSignature getGenericSignature();
}
