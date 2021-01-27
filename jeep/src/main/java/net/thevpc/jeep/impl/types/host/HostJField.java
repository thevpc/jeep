package net.thevpc.jeep.impl.types.host;

import net.thevpc.jeep.impl.types.DefaultJModifierList;
import net.thevpc.jeep.impl.types.JModifierList;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.util.JeepPlatformUtils;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.core.types.AbstractJField;
import net.thevpc.jeep.JRawField;
import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.impl.types.*;
import net.thevpc.jeep.impl.types.DefaultJAnnotationInstanceList;
import net.thevpc.jeep.impl.types.JAnnotationInstanceList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class HostJField extends AbstractJField implements JRawField {
    private JType declaringType;
    private Field field;
    private JType fieldType;
    private JType genericFieldType;
    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();
    private JModifierList modifiers = new DefaultJModifierList();

    public HostJField(Field field, JType actualType, JType declaringType) {
        this.declaringType = declaringType;
        this.field = field;
        this.genericFieldType = ((JTypesSPI) getTypes()).forHostType(field.getGenericType(), declaringType);
        this.fieldType = JTypeUtils.buildRawType(this.genericFieldType, declaringType);
        JeepPlatformUtils.setAccessibleWorkaround(field);
        applyAnnotations(field.getAnnotations());
        applyModifiers(field.getModifiers());
    }

    protected void applyAnnotations(Annotation[] annotations) {
        DefaultJAnnotationInstanceList list = (DefaultJAnnotationInstanceList) getAnnotations();
        for (Annotation annotation : annotations) {
            list.add(new HostJAnnotationInstance(annotation,getDeclaringType().getTypes().forName(annotation.getClass().getName())));
        }
    }

    protected void applyModifiers(int modifiers) {
        DefaultJModifierList modifiersList = (DefaultJModifierList) getModifiers();
        modifiersList.addJavaModifiers(modifiers);
    }

    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    public JModifierList getModifiers() {
        return modifiers;
    }

    public JTypes getTypes(){
        return declaringType.getTypes();
    }

    public String name() {
        return field.getName();
    }

    @Override
    public JType type() {
        return fieldType;
    }

    @Override
    public JType genericType() {
        return genericFieldType;
    }

    @Override
    public Object get(Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void set(Object instance, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

}
