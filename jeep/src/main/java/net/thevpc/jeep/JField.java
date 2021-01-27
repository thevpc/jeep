package net.thevpc.jeep;

import net.thevpc.jeep.impl.types.JAnnotationInstanceList;
import net.thevpc.jeep.impl.types.JModifierList;

public interface JField {
    String name();

    JType type();

    Object get(Object instance);

    void set(Object instance, Object value);

    boolean isPublic();

    boolean isStatic();

    boolean isFinal();

    JType getDeclaringType();
    JAnnotationInstanceList getAnnotations() ;

    JModifierList getModifiers();

    JTypes getTypes();

}
