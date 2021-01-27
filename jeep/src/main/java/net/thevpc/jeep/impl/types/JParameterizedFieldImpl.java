package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.types.AbstractJField;

public class JParameterizedFieldImpl extends AbstractJField implements JParameterizedField {
    private JField rawField;
    private JParameterizedType declaringType;
    private JType fieldType;
    public JParameterizedFieldImpl(JRawField rawField, JParameterizedType declaringType) {
        this.rawField=rawField;
        this.declaringType=declaringType;
        this.fieldType= JTypeUtils.buildActualType(rawField.genericType(),declaringType);
    }

    @Override
    public String name() {
        return rawField.name();
    }

    @Override
    public JType type() {
        return fieldType;
    }

    @Override
    public Object get(Object instance) {
        return rawField.get(instance);
    }

    @Override
    public void set(Object instance, Object value) {
        rawField.set(instance,value);
    }

    @Override
    public boolean isPublic() {
        return rawField.isPublic();
    }

    @Override
    public boolean isStatic() {
        return rawField.isStatic();
    }

    @Override
    public boolean isFinal() {
        return rawField.isFinal();
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return rawField.getAnnotations();
    }

    @Override
    public JModifierList getModifiers() {
        return rawField.getModifiers();
    }

    @Override
    public JTypes getTypes() {
        return rawField.getTypes();
    }
}
