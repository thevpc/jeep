package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.host.AbstractJRawType;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.types.DefaultJField;
import net.thevpc.jeep.core.types.DefaultJObject;
import net.thevpc.jeep.core.types.DefaultJStaticObject;
import net.thevpc.jeep.core.JStaticObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class DefaultJType extends AbstractJRawType implements JRawType {
    private String name;
    //    private int arrayDimension;
    private String simpleName;
    private String packageName;
    private JType superclass;
    private JConstructor defaultConstructor;
    private List<JType> interfaces = new ArrayList<>();
    private LinkedHashMap<String, JField> fields = new LinkedHashMap<>();
    private LinkedHashMap<String, JType> innerTypes = new LinkedHashMap<>();
    private LinkedHashMap<JSignature, JMethod> methods = new LinkedHashMap<>();
    private LinkedHashMap<JSignature, JConstructor> constructors = new LinkedHashMap<>();
    private List<String> exports = new ArrayList<>();
    private JInvoke instanceInitializer;
    private JInvoke staticInitializer;
    private JStaticObject staticObject = new DefaultJStaticObject(this);
    private Object defaultValue;
    private JDeclaration declaration;
    private String sourceName;

    private JType rawType;
    private JTypeVariable[] typeParameters = new JTypeVariable[0];
    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();
    private JModifierList modifiers = new DefaultJModifierList();
    private JTypeKind kind;

    public DefaultJType(String name, JTypeKind kind, JTypes types) {
        super(types);
        if (name.endsWith("[]")) {
            throw new IllegalStateException();
        }
        this.kind=kind;
        this.name = name;
        this.superclass = JTypeUtils.forObject(types);
        int r = name.lastIndexOf('.');
        if (r < 0) {
            simpleName = name;
            packageName = "";
        } else {
            packageName = name.substring(0, r);
            name = name.substring(r + 1);
            simpleName = name;
        }
        rawType = this;
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Type name cannot be empty");
        }
    }

    public JTypeKind getKind() {
        return kind;
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    @Override
    public String gname() {
        JTypeVariable[] t = getTypeParameters();
        if (t.length > 0) {
            StringBuilder sb = new StringBuilder(getName()).append('<');
            for (int i = 0; i < t.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(t[i].getName());
            }
            sb.append('>');
            return sb.toString();
        } else {
            return getName();
        }
    }

    public JInvoke instanceInitializer() {
        return instanceInitializer;
    }

    public void instanceInitializer(JInvoke instanceInitializer) {
        this.instanceInitializer = instanceInitializer;
    }

    public JInvoke staticInitializer() {
        return staticInitializer;
    }

    public void staticInitializer(JInvoke classInitializer) {
        this.staticInitializer = classInitializer;
    }

    public void setRawType(JType rawType) {
        this.rawType = rawType;
    }

    @Override
    public JMethod findDeclaredMethodOrNull(JSignature sig) {
        JMethod f = methods.get(sig.toNoVarArgs());
        if (f != null) {
            return f;
        }
        return null;
    }

    @Override
    public synchronized JField findDeclaredFieldOrNull(String fieldName) {
        return fields.get(fieldName);
    }

    @Override
    public JConstructor findDeclaredConstructorOrNull(JSignature sig) {
        return findDeclaredConstructorOrNull(sig,true);
    }

    //@Override
    public JConstructor findDeclaredConstructorOrNull(JSignature sig,boolean createDefault) {
        JConstructor f = constructors.get(sig.setName(getName()).toNoVarArgs());
        if (f != null) {
            return f;
        }
        if (sig.argsCount() > 0 && sig.lastArgType().isArray() && sig.isVarArgs()) {
            f = constructors.get(sig.toNoVarArgs());
            if (f != null) {
                return f;
            }
        }
        if (createDefault && sig.argTypes().length == 0 && constructors.isEmpty()) {
            JConstructor dc = createDefaultConstructor();
            if(dc!=null) {
                addConstructor(dc,false);
            }
            return dc;
        }
        return null;
    }

    protected JConstructor createDefaultConstructor(){
        DefaultJConstructor constructor = (DefaultJConstructor) createConstructor(JSignature.of(getName(), new JType[0]),
                new String[0],
                new JInvoke() {
                    @Override
                    public Object invoke(JInvokeContext context) {
                        return new DefaultJObject(DefaultJType.this);
                    }
                }, new JModifier[]{
                        DefaultJModifierList.PUBLIC
                }, new JAnnotationInstance[0]
        );
        constructor.setSourceName("<generated>");
        return constructor;

    }

    public JDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    public JConstructor findDefaultConstructorOrNull() {
        if (defaultConstructor == null) {
            JConstructor defaultConstructor = getDeclaredConstructor(JSignature.of(getName(), new JType[0]));
            if (defaultConstructor.isPublic()) {
                this.defaultConstructor = defaultConstructor;
            }
        }
        return defaultConstructor;
    }

    public DefaultJType setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

//    @Override
//    public JType toArray(int count) {
//        StringBuilder nname = new StringBuilder(name());
//        for (int i = 0; i < count; i++) {
//            nname.append("[]");
//        }
//        DefaultJTypes types = (DefaultJTypes) types();
//        return getRegisteredOrRegister(new DefaultJType(nname.toString(), types));
//    }

//    @Override
//    public Object newArray(int... len) {
//        if (len.length == 0) {
//            throw new IllegalArgumentException("zero len");
//        }
//        int len0 = len[0];
//        JType jType = componentType();
//        DefaultJArray aa = new DefaultJArray(new Object[len0], jType);
//        if (len.length > 1) {
//            int[] len2 = Arrays.copyOfRange(len, 0, len.length - 1);
//            for (int i = 0; i < len0; i++) {
//                aa.set(i, jType.newArray(len2));
//            }
//        }
//        return aa;
//    }

//    @Override
//    public JArray asArray(Object o) {
//        if (isArray()) {
//            if (o instanceof JArray) {
//                return (JArray) o;
//            }
//            throw new IllegalArgumentException("Invalid type");
////            return new HostJArray(o, componentType());
//        }
//        throw new IllegalArgumentException("Not array");
//    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return typeParameters;
    }

    @Override
    public JType getRawType() {
        return rawType;
    }

    @Override
    public JStaticObject getStaticObject() {
        return staticObject;
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder(name);
        JType[] jTypeVariables = getTypeParameters();
        if (jTypeVariables.length > 0) {
            sb.append("<");
            for (int i = 0; i < jTypeVariables.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(jTypeVariables[i].getName());
            }
            sb.append(">");
        }
//        jTypeVariables = actualTypeArguments();
//        if(jTypeVariables.length>0){
//            sb.append("<");
//            for (int i = 0; i < jTypeVariables.length; i++) {
//                if(i>0){
//                    sb.append(",");
//                }
//                sb.append(jTypeVariables[i].name());
//            }
//            sb.append(">");
//        }
        return sb.toString();
    }

    @Override
    public String simpleName() {
        return simpleName;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public JType getSuperType() {
        return superclass;
    }

    public DefaultJType setSuperType(JType superclass) {
        this.superclass = superclass;
        return this;
    }

    public DefaultJType setInterfaces(JType[] interfaces) {
        this.interfaces.clear();
        this.interfaces.addAll(Arrays.asList(interfaces));
        return this;
    }

//    @Override
    public JConstructor addConstructor(JSignature signature, String[] argNames, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations, boolean redefine) {
        JConstructor c = createConstructor(signature, argNames, handler, modifiers, annotations);
        return addConstructor(c,redefine);
    }

    public JConstructor createConstructor(JSignature signature, String[] argNames, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations) {
        signature = signature.setName(getName());
        DefaultJConstructor m = new DefaultJConstructor();
        m.setDeclaringType(this);
        ((DefaultJModifierList)m.getModifiers()).addAll(modifiers);
        m.setArgNames(Arrays.copyOf(argNames,argNames.length));
        ((DefaultJAnnotationInstanceList)m.getAnnotations()).addAll(annotations);
        m.setHandler(handler);
        m.setGenericSignature(signature);
        return m;
    }

    public JConstructor addConstructor(JConstructor constructor, boolean redefine) {
        JSignature signature = constructor.getSignature();
        JConstructor old = findDeclaredConstructorOrNull(signature,false);
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

//    @Override
    public JField addField(String name, JType type, JModifier[] modifiers, JAnnotationInstance[] annotations, boolean redefine) {
        JField old = findDeclaredFieldOrNull(name);
        if (old != null) {
            if (redefine) {
                //old.dispose();
            } else {
                throw new IllegalArgumentException("field already declared : " + getName() + "." + name);
            }
        }
//        if (type == null) {
//            throw new IllegalArgumentException("Field type cannot be null : " + name() + "." + name);
//        }
        DefaultJField f = new DefaultJField();
        f.setDeclaringType(this);
        f.setName(name);
        f.setGenericType(type);
        ((DefaultJModifierList) f.getModifiers()).addAll(modifiers);
        ((DefaultJAnnotationInstanceList) f.getAnnotations()).addAll(annotations);
        fields.put(name, f);
        return f;
    }

//    @Override
    public JMethod createMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations) {
        DefaultJRawMethod m = new DefaultJRawMethod();
        m.setArgNames(argNames);
        m.setDeclaringType(this);
        ((DefaultJModifierList) m.getModifiers()).addAll(modifiers);
        ((DefaultJAnnotationInstanceList) m.getAnnotations()).addAll(annotations);
        m.setGenericReturnType(returnType);
        m.setHandler(handler);
        m.setGenericSignature(signature);
        return m;
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
        JMethod m = createMethod(signature, argNames, returnType, handler, modifiers,annotations);
        methods.put(signature.toNoVarArgs(), m);
        return m;
    }

    public void addInterface(JType interfaceType){
        interfaces.add(interfaceType);
    }

    @Override
    public JType[] getInterfaces() {
        return interfaces.toArray(new JType[0]);
    }

    @Override
    public JConstructor[] getDeclaredConstructors() {
        if(constructors.isEmpty()){
            findDefaultConstructorOrNull();
        }
        return constructors.values().toArray(new JConstructor[0]);
    }

    @Override
    public JField[] getDeclaredFields() {
        return fields.values().toArray(new JField[0]);
    }

    @Override
    public JMethod[] getDeclaredMethods() {
        return methods.values().toArray(new JMethod[0]);
    }

    @Override
    public JType[] getDeclaredInnerTypes() {
        return innerTypes.values().toArray(new JType[0]);
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public JType getDeclaringType() {
        return null;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String[] getExports() {
        return exports.toArray(new String[0]);
    }

    public DefaultJType setDeclaration(JDeclaration declaration) {
        this.declaration = declaration;
        return this;
    }

    public void setTypeParameters(JTypeVariable[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    public DefaultJType setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }
}
