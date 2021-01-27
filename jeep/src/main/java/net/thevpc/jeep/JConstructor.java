package net.thevpc.jeep;

public interface JConstructor extends JInvokable {
    JType[] getArgTypes();

    String[] getArgNames();

    JTypeVariable[] getTypeParameters();

    JType getDeclaringType();
    JTypes getTypes() ;

}
