package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JAnnotationField;
import net.thevpc.jeep.JAnnotationInstanceField;

public class DefaultJAnnotationInstanceField implements JAnnotationInstanceField {
    private JAnnotationField annotationField;
    private String name;
    private Object value;
    private Object defaultValue;

    public DefaultJAnnotationInstanceField(JAnnotationField annotationField, String name, Object value, Object defaultValue) {
        this.annotationField = annotationField;
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    @Override
    public JAnnotationField getAnnotationField() {
        return annotationField;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
}
