package net.thevpc.jeep;

import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.JAnnotationInstanceList;
import net.thevpc.jeep.impl.types.JModifierList;

import java.util.*;

public class JMutableRawTypeAdapter extends JTypeBase implements JMutableRawType{
    private JRawType base;
    private LinkedHashMap<JSignature, JMethod> methods = new LinkedHashMap<>();
    private LinkedHashMap<JSignature, JConstructor> constructors = new LinkedHashMap<>();
    private List<JType> interfaces = new ArrayList<>();
    private JType superclass;

    public JMutableRawTypeAdapter(JRawType base) {
        this.base = base;
    }

    public JTypesSPI types2() {
        return (JTypesSPI) getTypes();
    }


    @Override
    public String gname() {
        return base.gname();
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
                types2().createParameterizedType0(this, parameters,
                        getDeclaringType()), getTypes());
    }

    @Override
    public JDeclaration getDeclaration() {
        return base.getDeclaration();
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return base.getTypeParameters();
    }

    @Override
    public JType getRawType() {
        return base.getRawType();
    }

    @Override
    public JStaticObject getStaticObject() {
        return base.getStaticObject();
    }

    @Override
    public String getName() {
        return base.getName();
    }

    @Override
    public JTypeName typeName() {
        return base.typeName();
    }

    @Override
    public JTypes getTypes() {
        return base.getTypes();
    }

    @Override
    public String dname() {
        return base.dname();
    }

    @Override
    public String simpleName() {
        return base.simpleName();
    }

    @Override
    public Object cast(Object o) {
        return base.cast(o);
    }

    @Override
    public JType boxed() {
        return base.boxed();
    }

    @Override
    public boolean isPublic() {
        return base.isPublic();
    }

    @Override
    public boolean isStatic() {
        return base.isStatic();
    }

    @Override
    public boolean isNullable() {
        return base.isNullable();
    }

    @Override
    public boolean isPrimitive() {
        return base.isPrimitive();
    }

    @Override
    public boolean isAssignableFrom(JType other) {
        return base.isAssignableFrom(other);
    }

    @Override
    public boolean isAssignableFrom(JTypePattern other) {
        return base.isAssignableFrom(other);
    }

    @Override
    public boolean isInstance(Object instance) {
        return base.isInstance(instance);
    }

    @Override
    public boolean isArray() {
        return base.isArray();
    }

    @Override
    public JType toArray(int count) {
        return base.toArray(count);
    }

    @Override
    public JType getSuperType() {
        if(superclass!=null) {
            return superclass;
        }
        return base.getSuperType();
    }


    @Override
    public JType toPrimitive() {
        return base.toPrimitive();
    }




    @Override
    public JConstructor[] getDeclaredConstructors() {
        return new JConstructor[0];
    }

    @Override
    public JField[] getDeclaredFields() {
        return new JField[0];
    }


    @Override
    public JMethod[] getDeclaredMethods() {
        return new JMethod[0];
    }

    @Override
    public JMethod[] getDeclaredMethods(String name) {
        return new JMethod[0];
    }


    @Override
    public JType[] getDeclaredInnerTypes() {
        return new JType[0];
    }

    @Override
    public JType findDeclaredInnerTypeOrNull(String name) {
        return null;
    }

    @Override
    public JMethod findDeclaredMethodOrNull(String sig) {
        return null;
    }

    @Override
    public JMethod[] getDeclaredMethods(String[] names, int callArgumentsCount, boolean includeParents) {
        return new JMethod[0];
    }

    @Override
    public JMethod[] getDeclaredMethods(boolean includeParents) {
        return new JMethod[0];
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
        return "";
    }

    @Override
    public String[] getExports() {
        return new String[0];
    }

    @Override
    public boolean isRawType() {
        return base.isRawType();
    }


    @Override
    public boolean isInterface() {
        return base.isInterface();
    }

    @Override
    public JModifierList getModifiers() {
        return base.getModifiers();
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return base.getAnnotations();
    }

    @Override
    public String getSourceName() {
        return "";
    }

    @Override
    public boolean isVar() {
        return false;
    }

    @Override
    public JTypeVariable toVar() {
        return null;
    }

    //--------------------

    @Override
    public JType replaceParameter(String name, JType param) {
        return this;
    }

    @Override
    public JMethod findDeclaredMethodOrNull(JSignature sig) {
        JMethod s = base.findDeclaredMethodOrNull(sig);
        if(s==null){
            s = methods.get(sig);
        }
        return s;
    }


    @Override
    public JType[] getInterfaces() {
        LinkedHashSet<JType> ii = new LinkedHashSet<>(
                Arrays.asList(base.getInterfaces())
        );
        ii.addAll(interfaces);
        return ii.toArray(new JType[0]);
    }

    public void addMethod(JMethod m) {
        methods.put(m.getSignature().toNoVarArgs(), m);
    }

    public void addInterface(JType interfaceType) {
        interfaces.add(interfaceType);
    }    @Override
    public JConstructor addConstructor(JConstructor constructor, boolean redefine) {
        JSignature signature = constructor.getSignature();
        JConstructor old = findDeclaredConstructorOrNull(signature);
        if (old != null) {
            if (redefine) {
                //old.dispose();
            } else {
                throw new IllegalArgumentException("Constructor already registered " + getName() + "." + signature);
            }
        }
        constructors.put(signature, constructor);
        return constructor;
    }

}
