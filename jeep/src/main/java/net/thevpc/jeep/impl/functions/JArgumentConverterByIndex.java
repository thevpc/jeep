package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JArgumentConverter;
import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JType;

public class JArgumentConverterByIndex implements JArgumentConverter {
    private int newIndex;
    private JType newType;

    public JArgumentConverterByIndex(int newIndex, JType newType) {
        this.newIndex = newIndex;
        this.newType = newType;
    }

    @Override
    public JType argumentType() {
        return newType;
    }

    @Override
    public Object getArgument(int index, Object[] allArguments, JContext context) {
        return allArguments[newIndex];
    }

    public int getNewIndex() {
        return newIndex;
    }

    public JType getNewType() {
        return newType;
    }
}
