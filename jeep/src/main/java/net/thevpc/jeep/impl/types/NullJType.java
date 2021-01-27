package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.host.AbstractJType;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.impl.JTypesSPI;

public class NullJType extends AbstractJType {
    private String name;
    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();
    private JModifierList modifiers = new DefaultJModifierList();
    public NullJType(JTypes types) {
        super(types);
        name="null";
        ((DefaultJModifierList)modifiers).add(DefaultJModifierList.PUBLIC);
    }

//    @Override
//    public JType[] actualTypeArguments() {
//        return new JType[0];
//    }

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
    public String simpleName() {
        return getName();
    }

//    @Override
//    public JType toArray(int count) {
//        return getRegisteredOrRegister(new NullJType(dim+count, types()));
//    }

//    @Override
//    public Object newArray(int... len) {
//        if (len.length == 0) {
//            throw new IllegalArgumentException("zero len");
//        }
//        int len0 = len[0];
//        JType jType = componentType();
//        return Array.newInstance(Object.class,len);
//    }



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
    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    @Override
    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public String getSourceName() {
        return "<runtime>";
    }
}
