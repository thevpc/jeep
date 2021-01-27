//package net.thevpc.jeep.impl.types;
//
//import net.thevpc.jeep.*;
//import net.thevpc.jeep.core.JStaticObject;
//import net.thevpc.jeep.impl.functions.JSignature;
//
//public class JRawTypeDelegate implements JRawType {
//    private JRawType base;
//
//    public JRawTypeDelegate(JRawType base) {
//        this.base = base;
//    }
//
//    @Override
//    public String gname() {
//        return base.gname();
//    }
//
//    @Override
//    public JType parametrize(JType... parameters) {
//        JType p = base.parametrize(parameters);
//        if (p == base) {
//            return this;
//        }
//        return p;
//    }
//
//    @Override
//    public JDeclaration getDeclaration() {
//        return base.getDeclaration();
//    }
//
//    @Override
//    public JTypeVariable[] getTypeParameters() {
//        return base.getTypeParameters();
//    }
//
//    @Override
//    public JType getRawType() {
//        return base.getRawType();
//    }
//
//    @Override
//    public JStaticObject staticObject() {
//        return base.staticObject();
//    }
//
//    @Override
//    public String getName() {
//        return base.getName();
//    }
//
//    @Override
//    public JTypeName typeName() {
//        return base.typeName();
//    }
//
//    @Override
//    public JTypes types() {
//        return base.types();
//    }
//
//    @Override
//    public String dname() {
//        return base.dname();
//    }
//
//    @Override
//    public String simpleName() {
//        return base.simpleName();
//    }
//
//    @Override
//    public Object cast(Object o) {
//        return base.cast(o);
//    }
//
//    @Override
//    public JType boxed() {
//        JType b = base.boxed();
//        if (b == base) {
//            return this;
//        }
//        return b;
//    }
//
//    @Override
//    public JModifierList getModifiers() {
//        return base.getModifiers();
//    }
//
//    @Override
//    public boolean isPublic() {
//        return base.isPublic();
//    }
//
//    @Override
//    public boolean isStatic() {
//        return base.isStatic();
//    }
//
//    @Override
//    public boolean isNullable() {
//        return base.isNullable();
//    }
//
//    @Override
//    public boolean isPrimitive() {
//        return base.isPrimitive();
//    }
//
//    @Override
//    public boolean isAssignableFrom(JType other) {
//        if(other.getName().equals("null")){
//            if(isNullable()){
//                return true;
//            }
//        }
//        return base.isAssignableFrom(other);
//    }
//
//    @Override
//    public boolean isInstance(Object instance) {
//        return base.isInstance(instance);
//    }
//
//    @Override
//    public boolean isArray() {
//        return base.isArray();
//    }
//
//    @Override
//    public JType toArray(int count) {
//        return base.toArray(count);
//    }
//
//    @Override
//    public JType getSuperType() {
//        return null;
//    }
//
//    @Override
//    public JType firstCommonSuperType(JType other) {
//        return null;
//    }
//
//    @Override
//    public JType toPrimitive() {
//        return null;
//    }
//
//    @Override
//    public JField findMatchedField(String fieldName) {
//        return null;
//    }
//
////    @Override
////    public JMethod findMethodMatchOrNull(JSignature signature, JContext context) {
////        return null;
////    }
////
////    @Override
////    public JConstructor findConstructorMatch(JSignature signature, JContext context) {
////        return null;
////    }
////
////    @Override
////    public JConstructor findConstructorMatchOrNull(JSignature signature, JContext context) {
////        return null;
////    }
////
////    @Override
////    public JMethod findMethodMatch(JSignature signature, JContext context) {
////        return null;
////    }
//
//    @Override
//    public JConstructor addConstructor(JSignature signature, String[] argNames, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations, boolean redefine) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    @Override
//    public JField addField(String name, JType type, int modifiers, boolean redefine) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    @Override
//    public JMethod addMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, int modifiers, boolean redefine) {
//       throw new IllegalArgumentException("Unsupported");
//    }
//
//    @Override
//    public JMethod getDeclaredMethod(String sig) {
//        return null;
//    }
//
//    @Override
//    public JMethod getDeclaredMethod(JSignature sig) {
//        return null;
//    }
//
//    @Override
//    public JMethod findDeclaredMethodOrNull(JSignature sig) {
//        return null;
//    }
//
//    @Override
//    public JConstructor findDeclaredConstructorOrNull(String sig) {
//        return null;
//    }
//
//    @Override
//    public JConstructor getDeclaredConstructor(String sig) {
//        return null;
//    }
//
//    @Override
//    public JType[] getInterfaces() {
//        return new JType[0];
//    }
//
//    @Override
//    public JConstructor getDeclaredConstructor(JSignature sig) {
//        return null;
//    }
//
//    @Override
//    public JConstructor getDeclaredConstructor(JType... parameterTypes) {
//        return null;
//    }
//
//    @Override
//    public JConstructor[] getPublicConstructors() {
//        return new JConstructor[0];
//    }
//
//    @Override
//    public JConstructor findDefaultConstructorOrNull() {
//        return null;
//    }
//
//    @Override
//    public JConstructor getDefaultConstructor() {
//        return null;
//    }
//
//    @Override
//    public JConstructor[] getDeclaredConstructors() {
//        return new JConstructor[0];
//    }
//
//    @Override
//    public JField getDeclaredField(String fieldName) {
//        return null;
//    }
//
//    @Override
//    public JField[] getDeclaredFields() {
//        return new JField[0];
//    }
//
//    @Override
//    public JField findDeclaredFieldOrNull(String fieldName) {
//        return null;
//    }
//
//    @Override
//    public JField getPublicField(String name) {
//        return null;
//    }
//
//    @Override
//    public JMethod[] getPublicMethods() {
//        return new JMethod[0];
//    }
//
//    @Override
//    public JMethod[] getDeclaredMethods() {
//        return new JMethod[0];
//    }
//
//    @Override
//    public JMethod[] getDeclaredMethods(String name) {
//        return new JMethod[0];
//    }
//
//    @Override
//    public JField[] getDeclaredFieldsWithParents() {
//        return new JField[0];
//    }
//
//    @Override
//    public JType[] getDeclaredInnerTypes() {
//        return new JType[0];
//    }
//
//    @Override
//    public JType getDeclaredInnerType(String name) {
//        return null;
//    }
//
//    @Override
//    public JType findDeclaredInnerTypeOrNull(String name) {
//        return null;
//    }
//
//    @Override
//    public JMethod findDeclaredMethodOrNull(String sig) {
//        return null;
//    }
//
//    @Override
//    public JMethod[] getDeclaredMethods(String[] names, int callArgumentsCount, boolean includeParents) {
//        return new JMethod[0];
//    }
//
//    @Override
//    public JMethod[] getDeclaredMethods(boolean includeParents) {
//        return new JMethod[0];
//    }
//
//    @Override
//    public JType[] getParents() {
//        return new JType[0];
//    }
//
//    @Override
//    public JConstructor findDeclaredConstructorOrNull(JSignature sig) {
//        return null;
//    }
//
//    @Override
//    public Object getDefaultValue() {
//        return null;
//    }
//
//    @Override
//    public JType getDeclaringType() {
//        return null;
//    }
//
//    @Override
//    public String getPackageName() {
//        return null;
//    }
//
//    @Override
//    public String[] getExports() {
//        return new String[0];
//    }
//
//    @Override
//    public boolean isRawType() {
//        return false;
//    }
//
//    @Override
//    public JType replaceParameter(String name, JType param) {
//        return null;
//    }
//
//    @Override
//    public boolean isVar() {
//        return false;
//    }
//
//    @Override
//    public JTypeVariable toVar() {
//        return null;
//    }
//
//    @Override
//    public boolean isInterface() {
//        return base.isInterface();
//    }
//
//    @Override
//    public boolean isAssignableFrom(JTypePattern other) {
//        return base.isAssignableFrom(other);
//    }
//
//    @Override
//    public JAnnotationInstanceList getAnnotations() {
//        return base.getAnnotations();
//    }
//
//    @Override
//    public String getSourceName() {
//        return base.getSourceName();
//    }
//}
