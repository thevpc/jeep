package net.thevpc.jeep.core.types;

import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.impl.types.DefaultJAnnotationInstanceList;
import net.thevpc.jeep.impl.types.DefaultJModifierList;
import net.thevpc.jeep.impl.types.JAnnotationInstanceList;
import net.thevpc.jeep.impl.types.JModifierList;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.util.JeepReflectUtils;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JRawField;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.core.JObject;

public class DefaultJField extends AbstractJField implements JRawField {
    private String name;
    private JType declaringType;
    private JType genericType;
    private JType type;
    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();
    private JModifierList modifiers = new DefaultJModifierList();

    public DefaultJField() {
    }

    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public boolean isFinal() {
        return ((JTypesSPI)getTypes()).isFinalField(this);
    }

    public DefaultJField setName(String name) {
        this.name = name;
        return this;
    }



    public DefaultJField setDeclaringType(JType declaringType) {
        this.declaringType = declaringType;
        return this;
    }

    public JType getType() {
        return type;
    }

    public DefaultJField setGenericType(JType genericType) {
        this.genericType = genericType;
        this.type= JTypeUtils.buildRawType(genericType, getDeclaringType());
        return this;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public JType type() {
        return type;
    }

    @Override
    public Object get(Object instance) {
        if(isStatic()){
            return declaringType.getStaticObject().get(name);
        }else if(instance instanceof JObject){
            return ((JObject)instance).get(name());
        }else{
            return JeepReflectUtils.getInstanceFieldValue(instance,name);
        }
    }

    @Override
    public void set(Object instance, Object value) {
        if(isStatic()){
            getDeclaringType().getStaticObject().set(name(),value);
        }else if(instance instanceof JObject){
            ((JObject)instance).set(name(),value);
        }else{
            JeepReflectUtils.setInstanceFieldValue(instance,name,value);
        }
    }

    @Override
    public boolean isPublic() {
        return ((JTypesSPI)getTypes()).isPublicField(this);
    }

    @Override
    public boolean isStatic() {
        return ((JTypesSPI)getTypes()).isStaticField(this);
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

    @Override
    public JType genericType() {
        return genericType;
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    @Override
    public JTypes getTypes() {
        return getDeclaringType().getTypes();
    }
}
