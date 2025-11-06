package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JAnnotationInstance;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.JArray;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.core.types.AbstractJField;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrFieldLength extends AbstractJField {
    private JType arrayType;
    private JTypes types;
    private List<JAnnotationInstance> annotations = new ArrayList<>();
    private JModifierList modifiers = new DefaultJModifierList();

    public ArrFieldLength(JType arrayType, JTypes types) {
        this.arrayType = arrayType;
        this.types = types;
    }

    @Override
    public String name() {
        return "length";
    }

    @Override
    public JType type() {
        return JTypeUtils.forInt(types);
    }

    @Override
    public Object get(Object instance) {
        if (instance instanceof JArray) {
            return ((JArray) instance).length();
        }
        return Array.getLength(instance);
    }

    @Override
    public void set(Object instance, Object value) {
        throw new IllegalStateException("Unmodifiable field");
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public JType getDeclaringType() {
        return arrayType;
    }

    @Override
    public List<JAnnotationInstance> getAnnotations() {
        return annotations;
    }

    @Override
    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public JTypes getTypes() {
        return arrayType.getTypes();
    }
}
