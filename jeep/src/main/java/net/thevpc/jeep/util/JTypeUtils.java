package net.thevpc.jeep.util;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.AbstractJConverter;
import net.thevpc.jeep.core.types.JTypeNameBounded;
import net.thevpc.jeep.impl.CastJConverter;
import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.common.textsource.log.JMessageList;

import java.util.*;

public class JTypeUtils {
    private static final JConverter[] ARR02 = new JConverter[0];
    private static final int SYNTHETIC = 0x00001000;

    private static Map<TypeConvertersCacheKey2, JConverter[]> cache_getTypeImplicitConversions2 = new HashMap<>();
    public static boolean isJavaPrimitiveType(String name) {
        if (name != null) {
            switch (name) {
                case "boolean":
                case "byte":
                case "short":
                case "char":
                case "int":
                case "long":
                case "float":
                case "double": {
                    return true;
                }
            }
        }
        return false;
    }

    public static JType[] buildRawType(JType[] rt, JDeclaration declaration) {
        JType[] rr = new JType[rt.length];
        for (int i = 0; i < rr.length; i++) {
            rr[i] = buildRawType(rt[i], declaration);
        }
        return rr;
    }

    public static JType[] buildActualType(JType[] rt, JDeclaration declaration) {
        JType[] rr = new JType[rt.length];
        for (int i = 0; i < rr.length; i++) {
            rr[i] = buildActualType(rt[i], declaration);
        }
        return rr;
    }

    public static JType[] buildParentType(JType[] rt, JDeclaration declaration) {
        JType[] rr = new JType[rt.length];
        for (int i = 0; i < rr.length; i++) {
            rr[i] = buildParentType(rt[i], declaration);
        }
        return rr;
    }

    /**
     * replaces the generic var with its upper bound
     *
     * @param type        type to replace
     * @param declaration context declaration
     * @return type that replaces the generic var with its upper bound
     */
    public static JType buildRawType(JType type, JDeclaration declaration) {
        if (type == null) {
            return null;
        }
        JTypesSPI typesSPI = (JTypesSPI) type.getTypes();
        if (type instanceof JParameterizedType) {
            JParameterizedType jpt = (JParameterizedType) type;
            JType[] jTypes = jpt.getActualTypeArguments();
            if (jTypes.length > 0) {
                JType[] jTypes2 = buildRawType(jTypes, declaration);
                for (int i = 0; i < jTypes2.length; i++) {
                    if (jTypes2[i] != jTypes[i]) {
                        return JTypesSPI.getRegisteredOrRegister(typesSPI.createParameterizedType0(
                                jpt.getRawType(),
                                jTypes2,
                                jpt.getDeclaringType()), type.getTypes());
                    }
                }
            }
            return type;
        } else if (type instanceof JRawType) {
            return type;
        } else if (type instanceof JTypeVariable) {
            JDeclaration d = declaration;
            while (d != null) {
                if (d instanceof JMethod) {
                    for (JTypeVariable jTypeVariable : ((JMethod) d).getTypeParameters()) {
                        if (jTypeVariable.getName().equals(type.getName())) {
                            JType c = firstCommonSuperType(jTypeVariable.upperBounds());
                            if (c == null) {
                                c = forObject(type.getTypes());
                            }
                            return c;
                        }
                    }
                } else if (d instanceof JConstructor) {
                    for (JTypeVariable jTypeVariable : ((JConstructor) d).getTypeParameters()) {
                        if (jTypeVariable.getName().equals(type.getName())) {
                            JType c = firstCommonSuperType(jTypeVariable.upperBounds());
                            if (c == null) {
                                c = forObject(type.getTypes());
                            }
                            return c;
                        }
                    }
                } else if (d instanceof JType) {
                    for (JTypeVariable jTypeVariable : ((JType) d).getTypeParameters()) {
                        if (jTypeVariable.getName().equals(type.getName())) {
                            JType c = firstCommonSuperType(jTypeVariable.upperBounds());
                            if (c == null) {
                                c = forObject(type.getTypes());
                            }
                            return c;
                        }
                    }
                } else {
                    throw new JShouldNeverHappenException();
                }
                d = d.getDeclaration();
            }
            return type;
        } else if (type instanceof JArrayType) {
            JArrayType ta = (JArrayType) type;
            JType c = buildRawType(ta.rootComponentType(), declaration);
            return JTypesSPI.getRegisteredOrRegister(
                    typesSPI.createArrayType0(c, ta.arrayDimension())
                    , c.getTypes());
        } else {
            return type;
        }
    }

    /**
     * replaces the generic var with its upper bound
     *
     * @param type        type to replace
     * @param declaration context declaration
     * @return type that replaces the generic var with its upper bound
     */
    public static JType buildActualType(JType type, JDeclaration declaration) {
        JTypes types = type.getTypes();
        JTypesSPI typesSPI = (JTypesSPI) types;
        if (type instanceof JParameterizedType) {
            JParameterizedType jpt = (JParameterizedType) type;
            JType[] jTypes = jpt.getActualTypeArguments();
            if (jTypes.length > 0) {
                JType[] jTypes2 = buildActualType(jTypes, declaration);
                for (int i = 0; i < jTypes2.length; i++) {
                    if (jTypes2[i] != jTypes[i]) {
                        return JTypesSPI.getRegisteredOrRegister(
                                ((JTypesSPI) type.getTypes()).createParameterizedType0(
                                        jpt.getRawType(),
                                        jTypes2, jpt.getDeclaringType()), type.getTypes())
                                ;
                    }
                }
            }
            return type;
        } else if (type instanceof JRawType) {
            return type;
        } else if (type instanceof JTypeVariable) {
            JDeclaration d = declaration;
            while (d != null) {
                if (d instanceof JMethod) {
                    if (d instanceof JParameterizedMethod) {
                        JParameterizedMethod pt = (JParameterizedMethod) d;
                        JType[] aTypes = pt.getActualParameters();
                        if (aTypes.length > 0) {
                            JMethod rm = pt.getRawMethod();
                            while ((rm instanceof JParameterizedMethod)) {
                                rm = ((JParameterizedMethod) rm).getRawMethod();
                            }
                            JTypeVariable[] typeParameters = rm.getTypeParameters();
                            for (int i = 0; i < typeParameters.length; i++) {
                                JTypeVariable jTypeVariable = typeParameters[i];
                                if (jTypeVariable.getName().equals(type.getName())) {
                                    if (i >= aTypes.length) {
                                        throw new ArrayIndexOutOfBoundsException(i);
                                    }
                                    return aTypes[i];
                                }
                            }
                        }
                    }
                } else if (d instanceof JConstructor) {
                    if (d instanceof JParameterizedConstructor) {
                        JParameterizedConstructor pt = (JParameterizedConstructor) d;
                        JConstructor rm = pt.getRawConstructor();
                        JTypeVariable[] typeParameters = rm.getTypeParameters();
                        for (int i = 0; i < typeParameters.length; i++) {
                            JTypeVariable jTypeVariable = typeParameters[i];
                            if (jTypeVariable.getName().equals(type.getName())) {
                                return pt.getActualParameters()[i];
                            }
                        }
                    }
                } else if (d instanceof JType) {
                    if (d instanceof JParameterizedType) {
                        JParameterizedType tt = (JParameterizedType) d;
                        JType rawType = tt.getRawType();
                        JTypeVariable[] jTypeVariables = rawType.getTypeParameters();
                        for (int i = 0; i < jTypeVariables.length; i++) {
                            JTypeVariable jTypeVariable = jTypeVariables[i];
                            if (jTypeVariable.getName().equals(type.getName())) {
                                return tt.getActualTypeArguments()[i];
                            }
                        }
                    }
                } else {
                    throw new JShouldNeverHappenException();
                }
                d = d.getDeclaration();
            }
            return type;
        } else if (type instanceof JArrayType) {
            JArrayType ta = (JArrayType) type;
            JType c = buildRawType(ta.rootComponentType(), declaration);
            return JTypesSPI.getRegisteredOrRegister(
                    typesSPI.createArrayType0(c, ta.arrayDimension()), types);
        } else {
            return type;
        }
    }

    public static JType buildParentType(JType type, JDeclaration declaration) {
        JTypes types = type.getTypes();
        JTypesSPI typesSPI = (JTypesSPI) types;
        if (type instanceof JParameterizedType) {
            JParameterizedType jpt = (JParameterizedType) type;
            JType[] jTypes = jpt.getActualTypeArguments();
            if (jTypes.length > 0) {
                JType[] jTypes2 = buildParentType(jTypes, declaration);
                for (int i = 0; i < jTypes2.length; i++) {
                    if (jTypes2[i] != jTypes[i]) {
                        return JTypesSPI.getRegisteredOrRegister(
                                typesSPI.createParameterizedType0(
                                        jpt.getRawType(),
                                        jTypes2, jpt.getDeclaringType()), types);
                    }
                }
            }
            return type;
        } else if (type instanceof JRawType) {
            //example : List<X>
            JTypeVariable[] jTypeVariables = type.getTypeParameters();
            if(jTypeVariables.length>0) {
                //example ArrayList<Y>
                if (declaration instanceof JParameterizedType) {
                    //example ArrayList<Y=int>
                    JParameterizedType jpt =(JParameterizedType)declaration;
                    JType[] jTypes = jpt.getActualTypeArguments();
                    if (jTypes.length > 0) {
                        JType[] jTypes2 = buildParentType(jTypes, declaration);
                        for (int i = 0; i < jTypes2.length; i++) {
                            if (jTypes2[i] != jTypes[i]) {
                                return JTypesSPI.getRegisteredOrRegister(
                                        typesSPI.createParameterizedType0(
                                                jpt.getRawType(),
                                                jTypes2, jpt.getDeclaringType()), types);
                            }
                        }
                        return typesSPI.createParameterizedType0(
                                type,
                                jTypes2, jpt.getDeclaringType());
                    }
//                    JType[] jTypes = jpt.actualTypeArguments();
//                    if (jTypes.length > 0) {
//                        JType rr = jpt.rawType();
//                    }
                }
            }
            return type;
        } else if (type instanceof JTypeVariable) {
            JDeclaration d = declaration;if(d==declaration)return type;
            while (d != null) {
                if (d instanceof JMethod) {
                    if (d instanceof JParameterizedMethod) {
                        JParameterizedMethod pt = (JParameterizedMethod) d;
                        JMethod rm = pt.getRawMethod();
                        JTypeVariable[] typeParameters = rm.getTypeParameters();
                        for (int i = 0; i < typeParameters.length; i++) {
                            JTypeVariable jTypeVariable = typeParameters[i];
                            if (jTypeVariable.getName().equals(type.getName())) {
                                return pt.getActualParameters()[i];
                            }
                        }
                    }
                } else if (d instanceof JConstructor) {
                    if (d instanceof JParameterizedConstructor) {
                        JParameterizedConstructor pt = (JParameterizedConstructor) d;
                        JConstructor rm = pt.getRawConstructor();
                        JTypeVariable[] typeParameters = rm.getTypeParameters();
                        for (int i = 0; i < typeParameters.length; i++) {
                            JTypeVariable jTypeVariable = typeParameters[i];
                            if (jTypeVariable.getName().equals(type.getName())) {
                                return pt.getActualParameters()[i];
                            }
                        }
                    }
                } else if (d instanceof JType) {
                    if (d instanceof JParameterizedType) {
                        JParameterizedType tt = (JParameterizedType) d;
                        JType rawType = tt.getRawType();
                        JTypeVariable[] jTypeVariables = rawType.getTypeParameters();
                        for (int i = 0; i < jTypeVariables.length; i++) {
                            JTypeVariable jTypeVariable = jTypeVariables[i];
                            if (jTypeVariable.getName().equals(type.getName())) {
                                return tt.getActualTypeArguments()[i];
                            }
                        }
                    }
                } else {
                    throw new JShouldNeverHappenException();
                }
                d = d.getDeclaration();
            }
            return type;
        } else if (type instanceof JArrayType) {
            JArrayType ta = (JArrayType) type;
            JType c = buildRawType(ta.rootComponentType(), declaration);
            return JTypesSPI.getRegisteredOrRegister(
                    typesSPI.createArrayType0(c, ta.arrayDimension())
                    , c.getTypes()
            );
        } else {
            return type;
        }
    }

    public static JType firstCommonSuperType(JType[] allTypes) {
        JType c = null;
        for (JType t : allTypes) {
            if (c == null) {
                c = t;
            } else {
                c = t.firstCommonSuperType(c);
            }
        }
        return c;
    }

    public static JType firstCommonSuperType(JType type1, JType type2, JTypes types) {
        JType tobj = forObject(types);
        JType tvoid = forVoid(types);
        JType tnull = forNull(types);
        if (type1 == null) {
            return type2;
        }
        if (type2 == null) {
            return type1;
        }
        if (type1.equals(type2)) {
            return type1;
        }

        if (!type1.isNullable() && tnull.equals(type2)) {
            return type1.boxed();
        }

        if (!type2.isNullable() && tnull.equals(type1)) {
            return type2.boxed();
        }

        if (type1.equals(tvoid) || type2.equals(tvoid)) {
            return tvoid;
        }
        if (type1.equals(tobj) || type2.equals(tobj)) {
            return tobj;
        }
        if (type1.isAssignableFrom(type2)) {
            return type1;
        }
        if (type2.isAssignableFrom(type1)) {
            return type2;
        }
        return firstCommonSuperType(type1.getSuperType(), type2, types);
    }

    public static JTypePattern firstCommonSuperTypePattern(JTypePattern typePattern1, JTypePattern typePattern2, JTypes types) {
        if (typePattern1 == null) {
            return typePattern2;
        }
        if (typePattern2 == null) {
            return typePattern1;
        }
        JType tobj = forObject(types);
        if (typePattern1.isType() && typePattern2.isType()) {
            JType type1 = typePattern1.getType();
            JType type2 = typePattern1.getType();
            return JTypePattern.of(firstCommonSuperType(type1, type2, types));
        } else if (typePattern1.isLambda() && typePattern2.isType()) {
            JType type2 = typePattern1.getType();
            JMethod[] m = type2.getDeclaredMethods();
            if (m.length == 1) {
                if (m[0].isPublic() && !m[0].isStatic()) {
                    JType[] type2ArgTypes = m[0].getArgTypes();
                    JType[] type1ArgTypes = typePattern1.getLambdaArgTypes();
                    if (type2ArgTypes.length == type1ArgTypes.length) {
                        for (int i = 0; i < type2ArgTypes.length; i++) {
                            if (type2ArgTypes[i].isAssignableFrom(type1ArgTypes[i])) {
                                //ok
                            } else {
                                return JTypePattern.of(tobj);
                            }
                        }
                        return JTypePattern.of(type2);
                    }
                }
            }
            return JTypePattern.of(tobj);
        } else if (typePattern1.isType() && typePattern2.isLambda()) {
            JType type1 = typePattern1.getType();
            JMethod[] m = type1.getDeclaredMethods();
            if (m.length == 1) {
                if (m[0].isPublic() && !m[0].isStatic()) {
                    JType[] type1ArgTypes = m[0].getArgTypes();
                    JType[] type2ArgTypes = typePattern2.getLambdaArgTypes();
                    if (type1ArgTypes.length == type2ArgTypes.length) {
                        for (int i = 0; i < type1ArgTypes.length; i++) {
                            if (type1ArgTypes[i].isAssignableFrom(type2ArgTypes[i])) {
                                //ok
                            } else {
                                return JTypePattern.of(tobj);
                            }
                        }
                        return JTypePattern.of(type1);
                    }
                }
            }
            return JTypePattern.of(tobj);
        } else {
            //both lambda
            JType[] type1ArgTypes = typePattern1.getLambdaArgTypes();
            JType[] type2ArgTypes = typePattern2.getLambdaArgTypes();
            JType type1RetType = typePattern1.getType();
            JType type2RetType = typePattern2.getType();
            if (type1ArgTypes.length == type2ArgTypes.length) {
                if (type1ArgTypes.length == 0) {
                    return typePattern1;
                }
                JType[] ret = new JType[type1ArgTypes.length];
                for (int i = 0; i < type1ArgTypes.length; i++) {
                    ret[i] = firstCommonSuperType(type1ArgTypes[i], type2ArgTypes[i], types);
                }
                return JTypePattern.of(ret,firstCommonSuperType(type1RetType,type2RetType,types));
            }
            return JTypePattern.of(tobj);
        }
    }

    public static JType forVoid(JTypes types) {
        return types.forName("void");
    }

    public static JType forByte(JTypes types) {
        return types.forName("byte");
    }

    public static JType forShort(JTypes types) {
        return types.forName("short");
    }

    public static JType forChar(JTypes types) {
        return types.forName("char");
    }

    public static JType forBoolean(JTypes types) {
        return types.forName("boolean");
    }

    public static boolean isBooleanResolvableType(JType type) {
        return
                type.getName().equals("boolean")
                ||type.getName().equals("java.lang.Boolean")
                ;
    }

    public static boolean isBooleanResolvableType(JTypePattern type) {
        return type.isType() && isBooleanResolvableType(type.getType());
    }
    
    public static boolean isVoid(JTypePattern type) {
        return type.isType() && type.getType().getName().equals("void");
    }

    public static boolean isVoid(JType type) {
        return type.getName().equals("void");
    }

    public static boolean isIntResolvableType(JType type) {
        return
                type.getName().equals("int")
                ||type.getName().equals("java.lang.Integer")
                ;
    }

    public static boolean isIntResolvableType(JTypePattern type) {
        return type.isType() && isIntResolvableType(type.getType());
    }

    public static JType forInt(JTypes types) {
        return types.forName("int");
    }

    public static JType forCharSequence(JTypes types) {
        return types.forName(CharSequence.class.getName());
    }

    public static JType forList(JTypes types) {
        return types.forName(List.class.getName());
    }

    public static JType forLong(JTypes types) {
        return types.forName("long");
    }

    public static JType forFloat(JTypes types) {
        return types.forName("float");
    }

    public static JType forDouble(JTypes types) {
        return types.forName("double");
    }

    public static JType forObject(JTypes types) {
        return types.forName("java.lang.Object");
    }

    public static JType forString(JTypes types) {
        return types.forName("java.lang.String");
    }

    public static JType forNull(JTypes types) {
        return types.forName("null");
    }

    public static int getAssignationCost(JType parent, JTypePattern childOrLambda) {
        if (childOrLambda == null || (childOrLambda.isType() && JTypeUtils.isNullType(childOrLambda.getType()))) {
            if (parent.isPrimitive()) {
                return -1;
            }
            return 3;
        }
        if (childOrLambda.isType()) {
            JType child = childOrLambda.getType();
            if (parent.equals(child)) {
                return 0;
            }
            if (parent.isAssignableFrom(child)) {
                return 2;
            }
            if (parent.boxed().isAssignableFrom(child.boxed())) {
                return 1;
            }
            JTypes types = parent.getTypes();
            if (types.forName(JNode.class.getName()).isAssignableFrom(parent)) {
                return 2;
            }
            JType c1 = parent.toPrimitive();
            JType c2 = child.toPrimitive();

            if (c1 != null && c2 != null) {
                if (c1.equals(c2)) {
                    return 1;
                }
                JType _boolean = forBoolean(types);
                if (_boolean.equals(c1)) {
                    if (c2.equals(_boolean)) {
                        return 2;
                    }
                }
                JType _byte = forByte(types);
                if (_byte.equals(c1)) {
                    if (c2.equals(_byte)) {
                        return 2;
                    }
                }
                JType _short = forShort(types);
                if (_short.equals(c1)) {
                    if (c2.equals(_byte) || c2.equals(_short)) {
                        return 2;
                    }
                }
                JType _char = forChar(types);
                if (_char.equals(c1)) {
                    if (_char.equals(c2)) {
                        return 2;
                    }
                }
                JType _int = forInt(types);
                if (_int.equals(c1)) {
                    if (c2.equals(_byte) || c2.equals(_short) || c2.equals(_char)
                            || c2.equals(_int)) {
                        return 2;
                    }
                }
                JType _long = forLong(types);
                if (_long.equals(c1)) {
                    if (c2.equals(_byte) || c2.equals(_short) || c2.equals(_char)
                            || c2.equals(_int) || c2.equals(_long)) {
                        return 2;
                    }
                }
                JType _float = forFloat(types);
                if (_float.equals(c1)) {
                    if (c2.equals(_byte) || c2.equals(_short)
                            || c2.equals(_char) || c2.equals(_int)) {
                        return 2;
                    }
                }
                JType _double = forDouble(types);
                if (_double.equals(c1)) {
                    if (c2.equals(_byte) || c2.equals(_short) || c2.equals(_char)
                            || c2.equals(_int) || c2.equals(_long)
                            || c2.equals(_float) || c2.equals(_double)) {
                        return 2;
                    }
                }
            }
        } else {
            JType[] lambdaTypes = childOrLambda.getLambdaArgTypes();
            JSignature i = JTypeUtils.extractLambdaArgTypesOrNull(parent, lambdaTypes.length);
            if(i!=null){
                JType[] b = i.acceptAndExpand(lambdaTypes);
                int c=0;
                for (int j = 0; j < b.length; j++) {
                    int u = getAssignationCost(b[j], JTypePattern.of(lambdaTypes[j]));
                    if(u<0){
                        //should never happen
                        return -1;
                    }
                    c+=u;
                }
                JMethod expectedMethod = extractLambdaMethod(parent);
                JType expectedReturnType = expectedMethod.getReturnType();
                JType lrt = childOrLambda.getLambdaReturnType();
                int u = getAssignationCost(expectedReturnType, JTypePattern.ofTypeOrNull(lrt));
                if(u<0){
                    return -1;
                }
                c+=u;
                return c;
            }
            return -1;
        }
        return -1;
    }

    public static boolean isNullType(JType other) {
        return other.getName().equals("null");
    }

    public static boolean isObjectType(JType type) {
        return type.getName().equals("java.lang.Object");
    }

    public static String getSimpleClassName(JType[] cls) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cls.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(getSimpleClassName(cls[i]));
        }
        return sb.toString();
    }

    public static String getFullClassName(JTypeName[] cls) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cls.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(getFullClassName(cls[i]));
        }
        return sb.toString();
    }

    public static String getFullClassName(JType[] cls) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cls.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(getFullClassName(cls[i]));
        }
        return sb.toString();
    }

    public static String getSimpleClassName(JType cls) {
        if (cls.isArray()) {
            JArrayType ta = (JArrayType) cls;
            return getSimpleClassName(ta.componentType()) + "[]";
        }
        return cls.simpleName();
    }

    public static String getFullClassName(JType cls) {
        if (cls instanceof JTypeVariable) {
            return cls.getName();
        }
        if (cls.isArray()) {
            JArrayType ta = (JArrayType) cls;
            return getFullClassName(ta.componentType()) + "[]";
        }
        return cls.getName();
    }

    public static String getFullClassName(JTypeNameOrVariable cls) {
        if (cls instanceof JTypeNameBounded) {
            return cls.name();
        }
        JTypeName jt = (JTypeName) cls;
        if (jt.isArray()) {
            return getFullClassName(jt.componentType()) + "[]";
        }
        return cls.name();
    }

    public static JConverter createTypeImplicitConversions(JType from, Class to) {
        return createTypeImplicitConversions(
                JTypePattern.of(from),
                JTypePattern.of(from.getTypes().forName(to.getName()))
        );
    }

    public static JConverter createTypeImplicitConversions(JTypePattern from, JTypePattern to) {
        String k = str(from) + "/" + str(to);
        JConverter f = JInvokeUtils.cached_createTypeImplicitConversions.get(k);
        if (f != null) {
            return f;
        }
        if (from.isLambda() || to.isLambda()) {
            throw new IllegalArgumentException("Unsupported implicit conversion from " + str(from) + " to " + str(to));
        }
        JType from1 = from.getType();
        JType to1 = to.getType();
        if (from1.isPrimitive()) {
            from1 = from1.boxed();
        }
        if (to1.isPrimitive()) {
            to1 = to1.boxed();
        }
        if (to1.isAssignableFrom(from1)) {
            return new CastJConverter(from, to.getType());
        }
        JTypes types = to.getType().getTypes();
        if (types.forName(Number.class.getName()).isAssignableFrom(from1)) {
            if (types.forName(Byte.class.getName()).equals(to1)) {
                f = new NumberToByteJConverter(from, to);
            }
            if (types.forName(Short.class.getName()).equals(to1)) {
                f = new NumberToShortJConverter(from, to);
            }
            if (types.forName(Integer.class.getName()).equals(to1)) {
                f = new NumberToIntJConverter(from, to);
            }
            if (types.forName(Long.class.getName()).equals(to1)) {
                f = new NumberToLongJConverter(from, to);
            }
            if (types.forName(Float.class.getName()).equals(to1)) {
                f = new NumberToFloatJConverter(from, to);
            }
            if (types.forName(Double.class.getName()).equals(to1)) {
                f = new NumberToDoubleJConverter(from, to);
            }
        }

        if (f == null) {
            throw new IllegalArgumentException("Unsupported implicit conversion from " + str(from) + " to " + str(to));
        }
        JInvokeUtils.cached_createTypeImplicitConversions.put(k, f);
        return f;
    }

    public static JConverter[] getTypeImplicitConversions(JTypePattern typePattern, boolean acceptSuper) {
        if (typePattern == null) {
//            cache_getTypeImplicitConversions.put(cls, ARR0);
            return ARR02;
        }
        if (typePattern.isLambda()) {
            return ARR02;
        }
        JType cls = typePattern.getType();
        TypeConvertersCacheKey2 ck = new TypeConvertersCacheKey2(typePattern, acceptSuper);
        JTypes types = cls.getTypes();
        JType jobj = forObject(types);
        JConverter[] old = cache_getTypeImplicitConversions2.get(ck);
        if (old != null) {
            return old;
        }
        if (cls.equals(jobj)) {
            cache_getTypeImplicitConversions2.put(ck, ARR02);
            return ARR02;
        }
        JConverterList2 all = new JConverterList2(typePattern, !acceptSuper);
//        if(cls.isPrimitive()){
//            all.add(new BoxConverter(cls));
//        }
        if (cls.isArray()) {
            JArrayType ta = (JArrayType) cls;
            JType rct = ta.rootComponentType();
            if (!rct.isPrimitive() && !jobj.equals(rct)) {
                all.add(new CastJConverter(typePattern,
                        jobj.toArray(ta.arrayDimension())));
            }
        }
        if (forBoolean(types).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Boolean.class));
        } else if (types.forName(Boolean.class.getName()).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Boolean.TYPE));

        } else if (forByte(types).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Byte.class));
            all.add(createTypeImplicitConversions(cls, Short.TYPE));
        } else if (types.forName(Byte.class.getName()).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Byte.TYPE));

        } else if (forShort(types).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Short.class));
            all.add(createTypeImplicitConversions(cls, Integer.TYPE));
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (types.forName(Short.class.getName()).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Short.TYPE));
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (forInt(types).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Integer.class));
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (types.forName(Integer.class.getName()).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Integer.TYPE));
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));

        } else if (forFloat(types).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Float.class));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (types.forName(Float.class.getName()).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));

        } else if (forLong(types).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Long.class));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (types.forName(Long.class.getName()).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));

        } else if (forDouble(types).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Double.class));
        } else if (types.forName(Double.class.getName()).equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        }
        for (JType cls2 : cls.getInterfaces()) {
            all.add(createTypeImplicitConversions(typePattern, JTypePattern.of(cls2)));
        }
        JType s = cls.getSuperType();
        if (s != null) {
            if (acceptSuper) {
                all.add(new CastJConverter(typePattern, cls.getSuperType()));
            }
            JConverter superConversions = createTypeImplicitConversions(typePattern, JTypePattern.of(s));
            all.add(superConversions);
        }
        JConverter[] r = all.toArray();
        cache_getTypeImplicitConversions2.put(ck, r);
        return r;
    }

    public static String str(JTypePattern argType) {
        if (argType == null) {
            return "?";
        } else if (argType.isType()) {
            return getFullClassName(argType.getType());
        } else {
            StringBuilder sb = new StringBuilder("(");
            JType[] t = argType.getLambdaArgTypes();
            for (int i = 0; i < t.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(getFullClassName(t[i]));
            }
            sb.append(")->...");
            return sb.toString();
        }
    }

    public static JType[] typesOrError(JTypePattern[] argTypes) {
        JType[] all = new JType[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            all[i] = argTypes[i].getType();
        }
        return all;
    }

    public static JType[] typesOrNull(JTypePattern[] argTypes) {
        JType[] all = new JType[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            if (argTypes[i].isType()) {
                all[i] = argTypes[i].getType();
            } else {
                return null;
            }
        }
        return all;
    }

    public static String sig(String name, JTypePattern[] argTypes, boolean varArgs) {
        return sig(name, argTypes, varArgs, true);
    }

    public static String sig(String name, JTypePattern[] argTypes, boolean varArgs, boolean withPars) {
        StringBuilder sb = new StringBuilder();
        if (!JStringUtils.isBlank(name)) {
            withPars = true;
            sb.append(name);
        }
        if (withPars) {
            sb.append("(");
        }
        for (int i = 0; i < argTypes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            JTypePattern ati = argTypes[i];
            if (ati.isType()) {
                if (i == argTypes.length - 1 && varArgs) {
                    JArrayType argType = (JArrayType) ati.getType();
                    sb.append(getFullClassName(argType.componentType()));
                    sb.append("...");
                } else {
                    sb.append(getFullClassName(ati.getType()));
                }
            } else {
                sb.append(str(ati));
            }
        }
        if (withPars) {
            sb.append(")");
        }
        return sb.toString();
    }

    public static JTypePattern[] typesOrLambdas(JType[] argumentTypes) {
        JTypePattern[] ret = new JTypePattern[argumentTypes.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = JTypePattern.of(argumentTypes[i]);
        }
        return ret;
    }

    public static JType classOf(JType tv) {
        JRawType raw = (JRawType) tv.getTypes().forName(Class.class.getName());
        return raw.parametrize(tv);
    }

    public static JType forThrowable(JTypes types) {
        return types.forName(Throwable.class.getName());
    }

    public static JType forException(JTypes types) {
        return types.forName(Exception.class.getName());
    }

    public static JType forRuntimeException(JTypes types) {
        return types.forName(RuntimeException.class.getName());
    }

    private static class TypeConvertersCacheKey2 {
        private JTypePattern type;
        private boolean acceptSuper;

        public TypeConvertersCacheKey2(JTypePattern type, boolean acceptSuper) {
            this.type = type;
            this.acceptSuper = acceptSuper;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, acceptSuper);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TypeConvertersCacheKey2 cacheKey = (TypeConvertersCacheKey2) o;
            return acceptSuper == cacheKey.acceptSuper &&
                    Objects.equals(type, cacheKey.type);
        }
    }

    public static class NumberToByteJConverter extends AbstractJConverter {
        public NumberToByteJConverter(JTypePattern from, JTypePattern to) {
            super(from, to, 2);
        }

        @Override
        public Object convert(Object value, JInvokeContext context) {
            if (value == null) {
                return null;
            }
            return ((Number) value).byteValue();
        }
    }

    public static class NumberToShortJConverter extends AbstractJConverter {
        public NumberToShortJConverter(JTypePattern from, JTypePattern to) {
            super(from, to, 2);
        }

        @Override
        public Object convert(Object value, JInvokeContext context) {
            if (value == null) {
                return null;
            }
            return ((Number) value).shortValue();
        }
    }

    public static class NumberToIntJConverter extends AbstractJConverter {
        public NumberToIntJConverter(JTypePattern from, JTypePattern to) {
            super(from, to, 2);
        }

        @Override
        public Object convert(Object value, JInvokeContext context) {
            if (value == null) {
                return null;
            }
            return ((Number) value).intValue();
        }
    }

    public static class NumberToLongJConverter extends AbstractJConverter {
        public NumberToLongJConverter(JTypePattern from, JTypePattern to) {
            super(from, to, 2);
        }

        @Override
        public Object convert(Object value, JInvokeContext context) {
            if (value == null) {
                return null;
            }
            return ((Number) value).longValue();
        }
    }

    public static class NumberToFloatJConverter extends AbstractJConverter {
        public NumberToFloatJConverter(JTypePattern from, JTypePattern to) {
            super(from, to, 2);
        }

        @Override
        public Object convert(Object value, JInvokeContext context) {
            if (value == null) {
                return null;
            }
            return ((Number) value).floatValue();
        }
    }

    public static class NumberToDoubleJConverter extends AbstractJConverter {
        public NumberToDoubleJConverter(JTypePattern from, JTypePattern to) {
            super(from, to, 2);
        }

        @Override
        public Object convert(Object value, JInvokeContext context) {
            if (value == null) {
                return null;
            }
            return ((Number) value).doubleValue();
        }
    }

    public static boolean isSynthetic(int mod) {
        return (mod & SYNTHETIC) != 0;
    }


    public static JSignature extractLambdaArgTypesOrNull(JType type, int expectedArgCount) {
        return extractLambdaArgTypesOrError(type,expectedArgCount,null,null);
    }

    public static JMethod extractLambdaMethod(JType type) {
        JMethod[] jMethods = type.getDeclaredMethods();
        if (jMethods.length > 1) {
            jMethods = Arrays.stream(type.getDeclaredMethods()).filter(x -> !x.isDefault()
                    && x.isPublic()
                    && !x.isSynthetic()
            ).toArray(JMethod[]::new);
        }
        if (jMethods.length == 1) {
            return jMethods[0];
        }
        return null;
    }

    public static JSignature extractLambdaArgTypesOrError(JType type, int expectedArgCount, JToken location, JMessageList log) {
        JMethod jMethod = extractLambdaMethod(type);
        if (jMethod!=null) {
            JType[] jTypes = jMethod.getArgTypes();
            if (expectedArgCount >= 0) {
                if (jMethod.getSignature().isVarArgs()) {
                    if (jTypes.length - 1 <= expectedArgCount) {
                        if (log != null) {
                            log.jerror("X000", null, location, "lambda expression arguments count mismatch " + jTypes.length + "!=" + expectedArgCount);
                        }
                        return null;
                    }
                } else {
                    if (expectedArgCount != jTypes.length) {
                        if (log != null) {
                            log.jerror("X000", null, location, "lambda expression arguments count mismatch " + jTypes.length + "!=" + expectedArgCount);
                        }
                        return null;
                    }
                }
            }
            return jMethod.getSignature();
        } else {
            if (log != null) {
                log.jerror("X000", null, location, "expected functional type as Lambda expression.");
            }
            return null;
        }
    }
}
