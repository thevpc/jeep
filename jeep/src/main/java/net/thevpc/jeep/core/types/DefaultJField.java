package net.thevpc.jeep.core.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.impl.types.DefaultJModifierList;
import net.thevpc.jeep.impl.types.JModifierList;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.util.JeepReflectUtils;
import net.thevpc.jeep.core.JObject;

import java.util.ArrayList;
import java.util.List;

public class DefaultJField extends AbstractJField implements JRawField {
    private String name;
    private JType declaringType;
    private JType genericType;
    private JType type;
    private List<JAnnotationInstance> annotations = new ArrayList<>();
    private JModifierList modifiers = new DefaultJModifierList();
    private Getter getter = new Getter() {
        @Override
        public Object get(JRawField field, Object instance) {
            if(field.isStatic()){
                return field.getDeclaringType().getStaticObject().get(field.name());
            }else if(instance instanceof JObject){
                return ((JObject)instance).get(name());
            }else{
                return JeepReflectUtils.getInstanceFieldValue(instance,field.name());
            }
        }
    }; ;
    private Setter setter = new Setter() {
        @Override
        public void set(JRawField field, Object instance, Object value) {
            if(field.isStatic()){
                field.getDeclaringType().getStaticObject().set(name(),value);
            }else if(instance instanceof JObject){
                ((JObject)instance).set(name(),value);
            }else{
                JeepReflectUtils.setInstanceFieldValue(instance,field.name(),value);
            }
        }
    }; ;

    public DefaultJField() {
    }

    public JModifierList getModifiers() {
        return modifiers;
    }

    private JTypesSPI typesSpi() {
        return (JTypesSPI) getTypes();
    }

    public DefaultJField setName(String name) {
        this.name = name;
        return this;
    }

    public DefaultJField setGetter(Getter getter) {
        this.getter = getter;
        return this;
    }

    public DefaultJField setSetter(Setter setter) {
        this.setter = setter;
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
    public void addAnnotation(JAnnotationInstance jAnnotationInstance){
        annotations.add(jAnnotationInstance);
    }

    @Override
    public boolean isFinal() {
        return typesSpi().isFinalField(this);
    }


    @Override
    public boolean isPublic() {
        return typesSpi().isPublicField(this);
    }

    @Override
    public boolean isStatic() {
        return typesSpi().isStaticField(this);
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
    public List<JAnnotationInstance> getAnnotations() {
        return annotations;
    }

    @Override
    public JTypes getTypes() {
        return getDeclaringType().getTypes();
    }

    public void addModifiers(JModifier... jModifiers) {
        DefaultJModifierList mm = (DefaultJModifierList) modifiers;
        mm.addAll(jModifiers);
    }
}
