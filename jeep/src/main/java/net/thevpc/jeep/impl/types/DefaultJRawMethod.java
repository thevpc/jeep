package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.util.ImplicitValue;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DefaultJRawMethod extends AbstractJMethod implements JRawMethod{
    private JInvoke handler;
    private JType declaringType;
    private JType returnType;
    private JType genericReturnType;
    private JTypeVariable[] typeParameters=new JTypeVariable[0];
    private JSignature signature;
    private String[] argNames;
    private JSignature genericSignature;
    private Object defaultValue;
    public ImplicitValue.MapForListImplicitValue<String, JAnnotationInstance> annotations = new ImplicitValue.MapForListImplicitValue<>(x -> x.getName());
    public ImplicitValue.MapForListImplicitValue<String, JModifier> modifiers = new ImplicitValue.MapForListImplicitValue<>(x -> x.name());
    private String sourceName;
    private Method hostMethod;
    private boolean defaultMethod;

    public DefaultJRawMethod() {
    }

    public Method getHostMethod() {
        return hostMethod;
    }

    public DefaultJRawMethod setHostMethod(Method hostMethod) {
        this.hostMethod = hostMethod;
        return this;
    }

    @Override
    public JTypes getTypes() {
        return getDeclaringType().getTypes();
    }

    @Override
    public List<JAnnotationInstance> getAnnotations() {
        return annotations.values();
    }

    @Override
    public JType[] getArgTypes() {
        JSignature s = getSignature();
        return s==null?null:s.argTypes();
    }


    @Override
    public String[] getArgNames() {
        return argNames;
    }

    public DefaultJRawMethod setArgNames(String[] argNames) {
        this.argNames = argNames;
        return this;
    }

    @Override
    public JSignature getSignature() {
        return signature;
    }

    @Override
    public JSignature getGenericSignature() {
        return genericSignature;
    }

    @Override
    public JType getGenericReturnType() {
        return genericReturnType;
    }


    public List<JModifier> getModifiers() {
        return modifiers.values();
    }

    public JInvoke getHandler() {
        return handler;
    }

    public DefaultJRawMethod setHandler(JInvoke handler) {
        this.handler = handler;
        return this;
    }

    public JType getDeclaringType() {
        return declaringType;
    }

    public DefaultJRawMethod setDeclaringType(JType declaringType) {
        this.declaringType = declaringType;
        return this;
    }

    public DefaultJRawMethod setGenericSignature(JSignature signature) {
        this.genericSignature = signature;
        this.signature=JSignature.of(genericSignature.name(),
                JTypeUtils.buildRawType(genericSignature.argTypes(),this),genericSignature.isVarArgs());
        return this;
    }

    public DefaultJRawMethod setGenericReturnType(JType returnType) {
        this.genericReturnType = returnType;
        this.returnType = JTypeUtils.buildRawType(returnType,this);
        return this;
    }

    @Override
    public JType getReturnType() {
        return returnType;
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return handler.invoke(context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultJRawMethod that = (DefaultJRawMethod) o;
        return Objects.equals(declaringType, that.declaringType) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declaringType, signature);
    }

    public JTypeVariable[] getTypeParameters() {
        return typeParameters;
    }

    public DefaultJRawMethod setTypeParameters(JTypeVariable[] typeParameters) {
        this.typeParameters = typeParameters;
        return this;
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return new JParameterizedMethodImpl(
                this,parameters, getDeclaringType()
        );
    }

    public boolean isDefault() {
        return defaultMethod;
//                getDeclaringType().isInterface()
//                && isPublic() && !isAbstract() && !isStatic();
    }

    public DefaultJRawMethod setDefaultMethod(boolean defaultMethod) {
        this.defaultMethod = defaultMethod;
        return this;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    public DefaultJRawMethod setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void addModifiers(JModifier... jModifiers) {
        modifiers.addAll(Arrays.asList(jModifiers));
    }
    public void addAnnotation(JAnnotationInstance jAnnotationInstance){
        annotations.add(jAnnotationInstance);
    }

}
