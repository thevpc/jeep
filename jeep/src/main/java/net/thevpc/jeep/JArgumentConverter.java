package net.thevpc.jeep;

public interface JArgumentConverter {
    JType argumentType();
    Object getArgument(int index,Object[] allArguments, JContext context);
}
