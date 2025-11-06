package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.host.AbstractJType;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.util.ImplicitValue;

import java.util.ArrayList;
import java.util.List;

public class NullJType extends AbstractJType {
    private String name;
    public ImplicitValue.MapForListImplicitValue<String, JAnnotationInstance> annotations = new ImplicitValue.MapForListImplicitValue<>(x -> x.getName());
    public ImplicitValue.MapForListImplicitValue<String, JModifier> modifiers = new ImplicitValue.MapForListImplicitValue<>(x -> x.name());
    public NullJType(JTypes types) {
        super(types);
        name="null";
        modifiers.add(DefaultJModifierList.PUBLIC);
    }

    @Override
    public JType[] getActualTypeArguments() {
        return new JType[0];
    }

    @Override
    public String getRawName() {
        return getName();
    }

    @Override
    public String getSimpleRawName() {
        return getName();
    }

    @Override
    public String gname() {
        return name;
    }

    @Override
    public boolean isRaw() {
        return true;
    }

    @Override
    public void setInterfaces(JType[] array) {

    }

    @Override
    public void setSuperType(JType tt) {

    }

    @Override
    public void addInterface(JType interfaceType) {

    }

    @Override
    public JConstructor addConstructor(JConstructor constructor, boolean redefine) {
        return constructor;
    }

    @Override
    public JTypeKind getKind() {
        return JTypeKind.CLASS;
    }

    @Override
    public int arrayDimension() {
        return 0;
    }

    @Override
    public JType rootComponentType() {
        return null;
    }

    @Override
    public JType componentType() {
        return null;
    }

    @Override
    public JArray asArray(Object o) {
        throw new UnsupportedOperationException("not an array.");
    }

    @Override
    public Object newArray(int... len) {
        throw new UnsupportedOperationException("not an array.");
    }

    @Override
    public boolean isParametrizedType() {
        return false;
    }

    @Override
    public JType getRawType() {
        return this;
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JStaticObject getStaticObject() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSimpleName() {
        return getName();
    }

    @Override
    public JType getSuperType() {
        return null;
    }

    @Override
    public JType[] getInterfaces() {
        return new JType[0];
    }
    @Override
    public JType toPrimitive() {
        return this;
    }

    @Override
    public JConstructor findDefaultConstructorOrNull() {
        return null;
    }

    @Override
    public JConstructor getDefaultConstructor() {
        return null;
    }

    @Override
    public JConstructor[] getDeclaredConstructors() {
        return new JConstructor[0];
    }

    @Override
    public JField findDeclaredFieldOrNull(String fieldName) {
        return null;
    }

//    @Override
//    public JType parametrize(JType... parameters) {
//        return this;
//    }

    @Override
    public JMethod[] getDeclaredMethods() {
        return new JMethod[0];
    }

    @Override
    public JMethod findDeclaredMethodOrNull(JSignature sig) {
        return null;
    }

    @Override
    public JConstructor findDeclaredConstructorOrNull(JSignature sig) {
        return null;
    }


    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public JType getDeclaringType() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public String[] getExports() {
        return new String[0];
    }

    @Override
    public JField[] getDeclaredFields() {
        return new JField[0];
    }

    @Override
    public JType[] getDeclaredInnerTypes() {
        return new JType[0];
    }

    @Override
    public JDeclaration getDeclaration() {
        return null;
    }

    @Override
    public boolean isNullable() {
        return true;
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        return this;
    }

    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(this,count), getTypes()
        );
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
    public boolean isInterface() {
        return false;
    }

    @Override
    public List<JAnnotationInstance> getAnnotations() {
        return annotations.values();
    }

    @Override
    public List<JModifier> getModifiers() {
        return modifiers.values();
    }

    @Override
    public String getSourceName() {
        return "<runtime>";
    }

    @Override
    public void addAnnotation(JAnnotationInstance jAnnotationInstance) {
        annotations.add(jAnnotationInstance);
    }
}
