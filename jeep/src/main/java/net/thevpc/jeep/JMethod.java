package net.thevpc.jeep;

public interface JMethod extends JInvokable {
    JTypes getTypes();

    String getName();

    boolean isPublic();

    JType getDeclaringType();

    JType[] getArgTypes();

    String[] getArgNames();

    boolean isAbstract();

    boolean isStatic();

    boolean isSynthetic();

    boolean isDefault();

    /**
     * annotation method default value
     * @return
     */
    Object getDefaultValue();

    JTypeVariable[] getTypeParameters();

    JMethod parametrize(JType... parameters);

}
