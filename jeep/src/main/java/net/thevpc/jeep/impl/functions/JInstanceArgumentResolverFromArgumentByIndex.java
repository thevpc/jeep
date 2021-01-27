package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JInstanceArgumentResolver;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypedValue;
import net.thevpc.jeep.core.DefaultJTypedValue;

public class JInstanceArgumentResolverFromArgumentByIndex implements JInstanceArgumentResolver {
    private final int instanceArgumentIndex;
    private final JType instanceArgumentType;

    public JInstanceArgumentResolverFromArgumentByIndex(int instanceArgumentIndex, JType instanceArgumentType) {
        this.instanceArgumentIndex = instanceArgumentIndex;
        this.instanceArgumentType = instanceArgumentType;
    }

    @Override
    public JTypedValue getInstance(Object actualInstance, Object[] arguments) {
        return new DefaultJTypedValue(arguments[instanceArgumentIndex], instanceArgumentType);
    }

    public int getInstanceArgumentIndex() {
        return instanceArgumentIndex;
    }

    public JType getInstanceArgumentType() {
        return instanceArgumentType;
    }
}
