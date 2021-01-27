package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JAnnotationField;
import net.thevpc.jeep.JAnnotationType;

public class DefaultJAnnotationField implements JAnnotationField {
    private JAnnotationType annotationType;
    private String name;
    private Object defaultValue;

    public DefaultJAnnotationField(String name, Object defaultValue, JAnnotationType annotationType) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.annotationType = annotationType;
    }

    @Override
    public JAnnotationType getAnnotationType() {
        return annotationType;
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
