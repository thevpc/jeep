package net.thevpc.jeep.impl.types.host;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.DefaultJModifierList;
import net.thevpc.jeep.impl.types.DefaultJRawMethod;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.impl.JTypesSPI;

import java.util.*;

public abstract class AbstractJType extends JTypeBase implements JType {
    protected boolean primitiveType;
    protected String version;
    private JTypes types;
    private JType boxedType = null;
    private JType unboxedType = null;
    private LinkedHashMap<String, JMethod[]> methodsByName = new LinkedHashMap<>();

    public AbstractJType(JTypes types) {
        this.types = types;
    }

    public JType getUnboxedType() {
        return unboxedType;
    }


    @Override
    public String getVersion() {
        return version;
    }
    public boolean isInterface() {
        return ((JTypesSPI)getTypes()).isInterfaceType(this);
    }

    public AbstractJType setUnboxedType(JType unboxedType) {
        this.unboxedType = unboxedType;
        return this;
    }
    @Override
    public boolean isPublic() {
        return ((JTypesSPI)getTypes()).isPublicType(this);
    }

    @Override
    public boolean isStatic() {
        return ((JTypesSPI)getTypes()).isStaticType(this);
    }

    public JType getBoxedType() {
        return boxedType;
    }

    public AbstractJType setBoxedType(JType boxedType) {
        this.boxedType = boxedType;
        return this;
    }

    public AbstractJType setPrimitiveType(boolean primitiveType) {
        this.primitiveType = primitiveType;
        return this;
    }

    @Override
    public JTypeName typeName() {
        return getTypes().parseName(getName());
    }


    @Override
    public JTypes getTypes() {
        return types;
    }

    public JTypesSPI types2() {
        return (JTypesSPI) getTypes();
    }

    /**
     * simple name including declaring type
     *
     * @return simple name including declaring type
     */
    @Override
    public String getSimpleName2() {
        JType d = getDeclaringType();
        if (d == null) {
            return getName();
        } else {
            return d.getSimpleName2() + '.' + getName();
        }
    }

    @Override
    public Object cast(Object o) {
        if (o == null) {
            return o;
        }
        JType y = getTypes().typeOf(o);
        if (isAssignableFrom(y)) {
            return o;
        }
        throw new ClassCastException("Cannot cast " + y + " to " + this);
    }

    @Override
    public JType boxed() {
        if(!isPrimitive()){
            return this;
        }
        JType bt = getBoxedType();
        if(bt!=null){
            return bt;
        }
        return this;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return primitiveType;
    }

    @Override
    public boolean isInstance(Object instance) {
        if (instance == null) {
            return !isPrimitive();
        }
        return isAssignableFrom(getTypes().typeOf(instance));
    }

    @Override
    public boolean isAssignableFrom(JTypePattern other) {
        if (other.isType()) {
            return isAssignableFrom(other.getType());
        }
        JType[] o = other.getLambdaArgTypes();
        JSignature a = JTypeUtils.extractLambdaArgTypesOrNull(this, o.length);
        if (a != null) {
            return a.acceptAndExpand(o) != null;
        }
        return false;
    }

    @Override
    public boolean isAssignableFrom(JType other) {
        if (getName().equals(other.getName())) {
            return true;
        }
        if (isPrimitive() || other.isPrimitive()) {
            if (getName().equals("void") || other.getName().equals("void")) {
                return false;
            }
        }
        if (getRawType().getName().equals(other.getRawType().getName())) {
            if (isRawType() || other.isRawType()) {
                return true;
            } else if (this.isParametrizedType() && other.isParametrizedType()) {
                JType t1 = this;
                JType t2 = other;
                JType[] actualTypeArguments1 = t1.getActualTypeArguments();
                JType[] actualTypeArguments2 = t2.getActualTypeArguments();
                for (int i = 0; i < actualTypeArguments1.length; i++) {
                    JType s1 = actualTypeArguments1[i];
                    JType s2 = actualTypeArguments2[i];
                    if (!s1.isAssignableFrom(s2)) {
                        return false;
                    }
                }
                return true;
            } else {
                throw new JFixMeLaterException();
            }
        }
        for (JType parent : other.getParents()) {
            if (isAssignableFrom(parent)) {
                return true;
            }
        }
        if (JTypeUtils.isNullType(other)) {
            if (isNullable()) {
                return true;
            }
        }
        if (JTypeUtils.isObjectType(this)) {
            return true;
        }
        return false;
    }


    @Override
    public JType toPrimitive() {
        if(isPrimitive()){
            return this;
        }
        return getUnboxedType();
    }



    @Override
    public JMethod[] getDeclaredMethods(String name) {
        JMethod[] jMethods = methodsByName.get(name);
        if (jMethods == null) {
            List<JMethod> n = new ArrayList<>();
            for (JMethod value : getDeclaredMethods()) {
                if (value.getName().equals(name)) {
                    n.add(value);
                }
            }
            methodsByName.put(name, jMethods = n.toArray(new JMethod[0]));
        }
        return jMethods;
    }






//    @Override
//    public JMethod findMethodMatchOrNull(JSignature signature, JContext context) {
//        JMethod[] possibleMethods = declaredMethods(new String[]{signature.name()}, signature.argsCount(), true);
//        return (JMethod) context.functions().resolveBestMatch(possibleMethods, null, signature.argTypes());
//    }

//    @Override
//    public JConstructor findConstructorMatchOrNull(JSignature signature, JContext context) {
//        JConstructor[] possibleMethods = declaredConstructors();
//        return (JConstructor) context.functions().resolveBestMatch(possibleMethods, null, signature.argTypes());
//    }

//    @Override
//    public JMethod findMethodMatch(JSignature signature, JContext context) {
//        JMethod a = findMethodMatchOrNull(signature,context);
//        if (a == null) {
//            throw new IllegalArgumentException("Method not found : " + name() + "." + signature);
//        }
//        return a;
//    }

//    @Override
//    public JConstructor findConstructorMatch(JSignature signature, JContext context) {
//        JConstructor a = findConstructorMatchOrNull(signature,context);
//        if (a == null) {
//            throw new IllegalArgumentException("Constructor not found : " + name() + "." + signature);
//        }
//        return a;
//    }




    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractJType that = (AbstractJType) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public String toString() {
        if (isPrimitive()) {
            return getName();
        }
        JTypeKind k = getKind();
        if(k==null){
            k=JTypeKind.CLASS;
        }
        return k.getName().toLowerCase()+" " + getName();
    }

    @Override
    public boolean isRawType() {
        return getRawType() == this;
    }



    @Override
    public boolean isVar() {
        return false;
    }


    @Override
    public JTypeVariable toVar() {
        throw new ClassCastException();
    }


    @Override
    public JMethod findDeclaredMethodOrNull(JSignature sig) {
        for (JMethod s : getDeclaredMethods(sig.name())) {
            if (s.getSignature().equals(sig)) {
                return s;
            }
        }
        return null;
    }


    @Override
    public JConstructor findDeclaredConstructorOrNull(JSignature sig) {
        for (JConstructor s : getDeclaredConstructors()) {
            if (s.getSignature().equals(sig)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        JType[] jTypes = getActualTypeArguments();
        JType[] y = new JType[jTypes.length];
        boolean modified = false;
        for (int i = 0; i < y.length; i++) {
            y[i] = jTypes[i].replaceParameter(name, param);
            modified |= (!y[i].getName().equals(jTypes[i].getName()));
        }
        if (modified) {
            return types2().findOrRegisterParameterizedType(getRawType(), y, this);
        } else {
            return this;
        }
    }

    @Override
    public JDeclaration getDeclaration() {
        return getDeclaringType();
    }

    @Override
    public JType findDeclaredInnerTypeOrNull(String name) {
        for (JType jType : getDeclaredInnerTypes()) {
            if (jType.getRawType().getName().equals(name)) {
                return jType;
            }
        }
        return null;
    }

    public void onPostRegister() {

    }

    public JMethod addMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations, boolean redefine) {
        JMethod old = findDeclaredMethodOrNull(signature.toNoVarArgs());
        if (old != null) {
            if (redefine) {
                //old.dispose();
            } else {
                throw new IllegalArgumentException("Method already registered " + getName() + "." + signature);
            }
        }
        JMethod m = createMethod(signature, argNames, returnType, handler, modifiers, annotations);
        addMethod(m);
        return m;
    }

    public void addMethod(JMethod m) {
        throw new IllegalArgumentException("Unsupported");
    }

    public JMethod createMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations) {
        DefaultJRawMethod m = new DefaultJRawMethod();
        m.setArgNames(argNames);
        m.setDeclaringType(this);
        ((DefaultJModifierList) m.getModifiers()).addAll(modifiers);
        if(annotations!=null){
            m.getAnnotations().addAll(Arrays.asList(annotations));
        }
        m.setGenericReturnType(returnType);
        m.setHandler(handler);
        m.setGenericSignature(signature);
        return m;
    }

    @Override
    public JAnnotationField[] getAnnotationFields() {
        return new JAnnotationField[0];
    }

    @Override
    public JAnnotationField getAnnotationField(String name) {
        return null;
    }

    @Override
    public boolean isNullable() {
        return !isPrimitive();
    }


    @Override
    public JType toArray(int count) {
        JType r = rootComponentType();
        if(r==null){
            r=this;
        }
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(r,count)
                , getTypes());
    }

    @Override
    public JType parametrize(JType... parameters) {
        if (parameters.length == 0) {
            throw new IllegalArgumentException("Invalid zero parameters count");
        }
        JTypeVariable[] vars = getTypeParameters();
        if (vars.length != parameters.length) {
            throw new IllegalArgumentException("Invalid parameters count. expected "+vars.length+" but got "+parameters.length);
        }
        return JTypesSPI.getRegisteredOrRegister(
                types2().findOrRegisterParameterizedType(this, parameters,
                        getDeclaringType()), getTypes());
    }
}
