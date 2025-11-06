package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.host.AbstractJType;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.impl.JTypesSPI;

import java.util.*;

public abstract class AbstractJTypeVariable extends AbstractJType implements JTypeVariable {
    public AbstractJTypeVariable(JTypes types) {
        super(types);
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JType getRawType() {
        return null;
    }

    @Override
    public JStaticObject getStaticObject() {
        return null;
    }

    @Override
    public String getSimpleRawName() {
        return null;
    }

    @Override
    public String getRawName() {
        return null;
    }


    @Override
    public String getSimpleName() {
        return getName();
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public JConstructor addConstructor(JConstructor constructor, boolean redefine) {
        throw new IllegalArgumentException("not allowed addConstructor");
    }

    @Override
    public void addInterface(JType interfaceType) {
        throw new IllegalArgumentException("not allowed addInterface");
    }

    @Override
    public void setSuperType(JType tt) {
        throw new IllegalArgumentException("not allowed setSuperType");
    }

    @Override
    public void setInterfaces(JType[] array) {
        throw new IllegalArgumentException("not allowed setInterfaces");
    }

    @Override
    public boolean isRaw() {
        return false;
    }

    @Override
    public String gname() {
        return getName();
    }

    @Override
    public JType[] getActualTypeArguments() {
        return new JType[0];
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
        throw new UnsupportedOperationException("Not supported array from TypeVariable.");
    }

    @Override
    public Object newArray(int... len) {
        throw new UnsupportedOperationException("Not supported array from TypeVariable.");
    }

    @Override
    public boolean isParametrizedType() {
        return false;
    }

    @Override
    public boolean isNullable() {
        JType[] jTypes = upperBounds();
        for (JType jType : jTypes) {
            if (jType.isNullable()) {
                return true;
            }
        }
        return jTypes.length == 0;
    }

    @Override
    public JType getSuperType() {
        JType jType = JTypeUtils.firstCommonSuperType(upperBounds());
        if (jType == null) {
            jType = JTypeUtils.forObject(getTypes());
        }
        return jType;
    }

    @Override
    public JType[] getInterfaces() {
        LinkedHashSet<JType> infs = new LinkedHashSet<>();
        for (JType ub : upperBounds()) {
            infs.addAll(Arrays.asList(ub.getInterfaces()));
        }
        return infs.toArray(new JType[0]);
    }

    @Override
    public JConstructor[] getDeclaredConstructors() {
        //how to do this by overcoming Java spec?
        return new JConstructor[0];
    }

    @Override
    public JField[] getDeclaredFields() {
        List<JField> a = new ArrayList<>();
        for (JType jType : upperBounds()) {
            a.addAll(Arrays.asList(jType.getDeclaredFields()));
        }
        return a.toArray(new JField[0]);
    }

    @Override
    public JMethod[] getDeclaredMethods() {
        List<JMethod> a = new ArrayList<>();
        for (JType jType : upperBounds()) {
            a.addAll(Arrays.asList(jType.getDeclaredMethods()));
        }
        return a.toArray(new JMethod[0]);
    }

    @Override
    public JType[] getDeclaredInnerTypes() {
        return new JType[0];
    }

    @Override
    public Object getDefaultValue() {
        JType[] upperBounds = upperBounds();
        for (int i = 1, upperBoundsLength = upperBounds.length; i < upperBoundsLength; i++) {
            if (!Objects.equals(upperBounds[i].getDefaultValue(), upperBounds[i - 1].getDefaultValue())) {
                return null;
            }
        }
        if (upperBounds.length >= 1) {
            return upperBounds[0].getDefaultValue();
        }
        return null;
    }

    @Override
    public JType getDeclaringType() {
        JDeclaration d = getDeclaration();
        while (d != null) {
            if (d instanceof JType) {
                return (JType) d;
            }
            d = d.getDeclaration();
        }
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
    public boolean isPrimitive() {
        JType[] jTypes = upperBounds();
        for (JType jType : jTypes) {
            if (!jType.isPrimitive()) {
                return false;
            }
        }
        return jTypes.length == 1;
    }

    @Override
    public boolean isVar() {
        return true;
    }

    @Override
    public JTypeVariable toVar() {
        return this;
    }

    @Override
    public JConstructor getDefaultConstructor() {
        return null;
    }

    @Override
    public JConstructor findDeclaredConstructorOrNull(JSignature sig) {
        return null;
    }

//    @Override
//    public JType[] actualTypeArguments() {
//        return new JType[0];
//    }

//    @Override
//    public JType parametrize(JType... parameters) {
//        return this;
//    }

    @Override
    public JConstructor findDefaultConstructorOrNull() {
        return null;
    }

    @Override
    public boolean isWildcard() {
        return getName().equalsIgnoreCase("?");
    }

    @Override
    public String toString() {
        return getName();
    }

//    @Override
//    public JModifierList getModifiers() {
//        return Modifier.PUBLIC;
//    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public boolean isStatic() {
        return false;
    }
    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(this,count),
                getTypes()
        );
    }

    @Override
    public String getVName() {
        JDeclaration d = getDeclaration();
        return getName()+":"+d;
    }
}
