package net.thevpc.jeep;

import java.util.List;

public interface JField {
    String name();

    JType type();

    Object get(Object instance);

    void set(Object instance, Object value);

    boolean isPublic();

    boolean isStatic();

    boolean isFinal();

    JType getDeclaringType();
    List<JAnnotationInstance> getAnnotations() ;

    List<JModifier> getModifiers();

    JTypes getTypes();

}
