package net.thevpc.jeep.core.types;

import net.thevpc.jeep.JType;

import java.util.Map;

public class DefaultJAnnotationObject extends DefaultJObject{
    private Map<String,Object> defaultValues;

    public DefaultJAnnotationObject(JType type, Map<String, Object> defaultValues) {
        super(type);
        this.defaultValues = defaultValues;
    }

    public Map<String, Object> getDefaultValues() {
        return defaultValues;
    }
}
