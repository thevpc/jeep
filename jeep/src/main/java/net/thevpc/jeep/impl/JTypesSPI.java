package net.thevpc.jeep.impl;

import net.thevpc.jeep.*;

import java.lang.reflect.Type;

public interface JTypesSPI {
    static JType getAlreadyRegistered(String name, JTypes jtypes) {
        JTypes types = jtypes;
        while (types.parent() != null) {
            types = types.parent();
        }
        return ((JTypesSPI) types).getRegistered(name);
    }

    static JType getRegisteredOrRegister(JType type, JTypes jtypes) {
        JType old = getAlreadyRegistered(type.getName(), jtypes);
        if (old != null) {
            return old;
        }
        JTypes types = jtypes;
        while (types.parent() != null) {
            types = types.parent();
        }
        ((JTypesSPI) types).registerType(type);
        return type;
    }

    JType getRegisteredOrAliasCurrent(String jt, boolean checkAliases, boolean checkTypes);

    JType getRegisteredOrAlias(String jt, boolean checkAliases, boolean checkTypes);

    JType getRegisteredOrAlias(String jt);

    JType getRegistered(String jt);

    void registerType(JType jt);

    JType createArrayType0(JType root, int dim);

    JType createHostType0(Class hostType);
    JType createNullType0();


    JType createHostType0(String name);

    JType createMutableType0(String name, JTypeKind kind);

    JType createVarType0(String name, JType[] lowerBounds, JType[] upperBounds, JDeclaration declaration);

    JParameterizedType createParameterizedType0(JType rootRaw, JType[] parameters, JType declaringType);

    JType forHostType(Type ctype, JDeclaration declaration);

    JType[] forHostType(Type[] names, JDeclaration declaration);

    ClassLoader hostClassLoader();

    boolean isPublicType(JType c);

    boolean isPublicConstructor(JConstructor c);

    boolean isPublicMethod(JMethod c);

    boolean isSyntheticMethod(JMethod c);

    boolean isPublicField(JField c);

    boolean isStaticType(JType c);

    boolean isStaticMethod(JMethod c);

    boolean isStaticField(JField c);

    boolean isInterfaceType(JType c);

    boolean isAbstractMethod(JMethod c);

    boolean isFinalField(JField c);

}
