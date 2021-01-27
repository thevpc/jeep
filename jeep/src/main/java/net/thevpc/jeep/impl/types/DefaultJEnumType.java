package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JEnumType;
import net.thevpc.jeep.JTypeKind;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.*;

public class DefaultJEnumType extends DefaultJType implements JEnumType {
    public DefaultJEnumType(String name, JTypeKind kind, JTypes types) {
        super(name, kind,types);
    }
}
