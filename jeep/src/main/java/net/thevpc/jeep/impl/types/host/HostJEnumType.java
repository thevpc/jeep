package net.thevpc.jeep.impl.types.host;

import net.thevpc.jeep.JEnumType;
import net.thevpc.jeep.JTypes;

public class HostJEnumType extends HostJRawType implements JEnumType {
    public HostJEnumType(Class hostType, JTypes types) {
        super(hostType, types);
    }
}
