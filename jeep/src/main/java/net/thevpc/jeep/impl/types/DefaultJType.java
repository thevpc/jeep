package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.host.AbstractJType;
import net.thevpc.jeep.core.types.DefaultJField;
import net.thevpc.jeep.core.types.DefaultJObject;
import net.thevpc.jeep.core.types.DefaultJStaticObject;
import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.impl.types.host.HostJArray;
import net.thevpc.jeep.util.ImplicitValue;
import net.thevpc.jeep.util.JTypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;

public class DefaultJType extends AbstractJType {
    private String rawName;
    private String simpleRawName;
    private String name;
    //    private int arrayDimension;
    private String simpleName;
    private String packageName;
    private JType superType;
    private JConstructor defaultConstructor;
    public ImplicitValue.MapForListImplicitValue<String, JAnnotationField> annotationFields = new ImplicitValue.MapForListImplicitValue<>(x -> x.getName());
    public ImplicitValue.UniqueListImplicitValue<String, JType> interfaces = new ImplicitValue.UniqueListImplicitValue<>(x -> x.getName());
    public ImplicitValue.MapForListImplicitValue<String, JField> fields = new ImplicitValue.MapForListImplicitValue<>(JField::name);
    public ImplicitValue.MapForListImplicitValue<JSignature, JMethod> methods = new ImplicitValue.MapForListImplicitValue<>(x -> x.getSignature().toNoVarArgs());
    public ImplicitValue.MapForListImplicitValue<JSignature, JConstructor> constructors = new ImplicitValue.MapForListImplicitValue<>(x -> x.getSignature().toNoVarArgs());
    public ImplicitValue.MapForListImplicitValue<String, JType> innerTypes = new ImplicitValue.MapForListImplicitValue<>(x -> x.getName());
    public ImplicitValue.MapForListImplicitValue<String, JAnnotationInstance> annotations = new ImplicitValue.MapForListImplicitValue<>(x -> x.getName());
    public ImplicitValue.MapForListImplicitValue<String, JModifier> modifiers = new ImplicitValue.MapForListImplicitValue<>(x -> x.name());

    private List<String> exports = new ArrayList<>();
    private JInvoke instanceInitializer;
    private JInvoke staticInitializer;
    private JStaticObject staticObject = new DefaultJStaticObject(this);
    private Object defaultValue;
    private JType declaringType;
    private JDeclaration declaration;
    private String sourceName;

    private JType rawType;
    private JTypeVariable[] typeParameters = new JTypeVariable[0];
    private JTypeKind kind;
    private Type hostType;
    private List<Runnable> onPostRegisterList = new ArrayList<>();
    private JType[] actualTypeArguments = new JType[0];
    private int arrayDimension;
    private JType arrayRootType;
    private JType arrayComponentType;
    private boolean parametrized;

    public DefaultJType(JType arrayRootType,int arrayDimension,JTypes types) {
        this(null,JTypeKind.CLASS, null, new JType[0],null,arrayRootType,arrayDimension,types);
    }

    public DefaultJType(JType rootRaw, JType[] actualTypeArguments,JType declaringType,JTypes types) {
        this(null,JTypeKind.CLASS, rootRaw, actualTypeArguments,declaringType,null,0,types);
    }

    public DefaultJType(String name, JTypeKind kind,JType declaringType,JTypes types) {
        this(name,kind, null, new JType[0],declaringType,null,0,types);
    }

    public DefaultJType(String name, JTypeKind kind, JType rootRaw, JType[] actualTypeArguments,JType declaringType, JType arrayRootType,int arrayDimension,JTypes types) {
        super(types);
        if(rootRaw!=null){
            this.parametrized=true;
        }
        this.declaringType = declaringType;
        if(arrayDimension==0) {
            if(parametrized) {
                this.name=rootRaw.getRawName()+TypeNameBuilder.actualTypeArguments(actualTypeArguments);
                this.rawName=rootRaw.getRawName();
                this.simpleRawName=rootRaw.getSimpleRawName();
                this.simpleName=rootRaw.getRawName()+TypeNameBuilder.actualTypeArguments(actualTypeArguments);
                this.packageName=rootRaw.getPackageName();
                this.kind=rootRaw.getKind();
                this.rawType = rootRaw;
                this.rawName =  rootRaw.getRawName();
            }else{
                this.kind = kind;
                this.name = name;
                int r = name.lastIndexOf('.');
                if (r < 0) {
                    simpleName = name;
                    simpleRawName = name;
                    packageName = "";
                } else {
                    packageName = name.substring(0, r);
                    simpleName = name.substring(r + 1);
                    simpleRawName = simpleName;
                }
                if(simpleName.contains(">")){
                    throw new IllegalArgumentException("Error");
                }
                this.rawType = this;
                this.rawName =  name;
            }
            if (parametrized) {
                this.actualTypeArguments = actualTypeArguments==null?new JType[0]:actualTypeArguments;
            }else if(this.actualTypeArguments!=null && this.actualTypeArguments.length>0){
                throw new IllegalArgumentException("invalid actualTypeArguments "+Arrays.toString(actualTypeArguments));
            }
            addOnPostRegister(()->{
                if (isParametrizedType()) {
                    JType sType = this.rawType.getSuperType();
                    setSuperType(sType == null ? null : JTypeUtils.buildParentType(sType, this));
                }
            });
        }else{
            this.arrayRootType = arrayRootType;
            this.arrayDimension = arrayDimension;
            if (arrayRootType.isArray()) {
                throw new IllegalStateException("Invalid Array with dimension ==0");
            }
            StringBuilder fb = new StringBuilder(arrayRootType.getName().length() + 2 * arrayDimension);
            StringBuilder sb = new StringBuilder(arrayRootType.getSimpleName().length() + 2 * arrayDimension);
            fb.append(arrayRootType.getName());
            sb.append(arrayRootType.getSimpleName());
            for (int i = 0; i < arrayDimension; i++) {
                fb.append("[]");
                sb.append("[]");
            }
            this.name = fb.toString();
            this.simpleName = sb.toString();
            this.simpleRawName = arrayRootType.getSimpleRawName();
            this.rawName = arrayRootType.getRawName();
            this.rawType=arrayRootType.isRawType() ? this : arrayRootType.getRawType().toArray(arrayDimension);
            this.fields.addSupplier(new Supplier<List<JField>>() {
                @Override
                public List<JField> get() {
                    List<JField> m = new ArrayList<>();
                    m.add(new ArrFieldLength(DefaultJType.this, getTypes()));
                    return m;
                }
            });
            this.arrayComponentType = arrayDimension == 1 ? arrayRootType :
                    JTypesSPI.getRegisteredOrRegister(types2().createArrayType0(arrayRootType, arrayDimension - 1),
                            getTypes()
                    );
            addOnPostRegister(()-> {
                setSuperType(JTypeUtils.forObject(getTypes()));
            });
        }
        if(rootRaw != null){
            fields.addSupplier(new Supplier<List<JField>>() {
                @Override
                public List< JField> get() {
                    List<JField> _fields = new ArrayList<>();
                    for (JField i : getRawType().getDeclaredFields()) {
                        JParameterizedFieldImpl m = new JParameterizedFieldImpl((JRawField) i, DefaultJType.this);
                        _fields.add(m);
                    }
                    return  _fields;
                }
            });
            methods.addSupplier(new Supplier<List<JMethod>>() {
                @Override
                public List<JMethod> get() {
                    List<JMethod> _methods = new ArrayList<>();
                    for (JMethod jMethod : getRawType().getDeclaredMethods()) {
                        JParameterizedMethodImpl m = new JParameterizedMethodImpl(jMethod, new JType[0], DefaultJType.this);
                        _methods.add(m);
                    }
                    return  _methods;
                }
            });
            constructors.addSupplier(new Supplier<List<JConstructor>>() {
                @Override
                public List<JConstructor> get() {
                    List<JConstructor> _constructors = new ArrayList<>();
                    for (JConstructor i : getRawType().getDeclaredConstructors()) {
                        JParameterizedConstructorImpl m = new JParameterizedConstructorImpl(i, new JType[0], DefaultJType.this);
                        _constructors.add(m);
                    }
                    return  _constructors;
                }
            });
            innerTypes.addSupplier(new Supplier<List<JType>>() {
                @Override
                public List<JType> get() {
                    List<JType> _innerTypes = new ArrayList<>();
                    for (JType item : rootRaw.getDeclaredInnerTypes()) {
                        if (item.isStatic()) {
                            _innerTypes.add(item);
                        } else {
                            JType f = types2().findOrRegisterParameterizedType(item,
                                    new JType[0],
                                    DefaultJType.this);
                            _innerTypes.add(f);
                        }
                    }
                    return _innerTypes;
                }
            });
            interfaces.addSupplier(new Supplier<List<JType>>() {
                @Override
                public List<JType> get() {
                    return Arrays.asList(JTypeUtils.buildParentType(getRawType().getInterfaces(), DefaultJType.this));
                }
            });
        }
    }

    @Override
    public String getSimpleRawName() {
        return simpleRawName;
    }

    @Override
    public boolean isParametrizedType() {
        return parametrized;
    }

    @Override
    public boolean isArray() {
        return arrayComponentType!=null;
    }

    public boolean isAssignableFrom(JType other) {
        if(isArray()) {
            if(other.isArray()){
                if(arrayDimension()==other.arrayDimension()){
                    return rootComponentType().isAssignableFrom(other.rootComponentType());
                }
            }
        }
        return super.isAssignableFrom(other);
    }

    @Override
    public JType componentType() {
        return arrayComponentType;
    }

    public int arrayDimension() {
        return arrayDimension;
    }

    @Override
    public JType rootComponentType() {
        return arrayRootType;
    }



    @Override
    public boolean isRaw() {
        return getRawType() == this;
    }

    @Override
    public Object newArray(int... len) {
        JType rootComp = rootComponentType();
        if (rootComp instanceof DefaultJType && ((DefaultJType)rootComp).getHostType()!=null) {
            Type ht =((DefaultJType)rootComp).getHostType();
            return Array.newInstance((Class<?>) ht, len);
        } else {
            int len0 = len[0];
            JType jType = componentType();
            DefaultJArray aa = new DefaultJArray(new Object[len0], jType);
            if (len.length > 1) {
                JType jTypea = jType;
                int[] len2 = Arrays.copyOfRange(len, 0, len.length - 1);
                for (int i = 0; i < len0; i++) {
                    aa.set(i, jTypea.newArray(len2));
                }
            }
            return aa;
        }
    }

    @Override
    public JArray asArray(Object o) {
        if (o instanceof JArray) {
            return (JArray) o;
        }
        return new HostJArray(o, componentType());
    }

    public JAnnotationField[] getAnnotationFields() {
        return annotationFields.values().toArray(new JAnnotationField[0]);
    }

    @Override
    public JAnnotationField getAnnotationField(String name) {
        for (JAnnotationField annotationField : getAnnotationFields()) {
            if (annotationField.getName().equals(name)) {
                return annotationField;
            }
        }
        throw new NoSuchElementException("no annotation field named " + name);
    }

    public void addAnnotationField(JAnnotationField f) {
        annotationFields.add(f);
    }

    public Type getHostType() {
        return hostType;
    }

    public DefaultJType setHostType(Type hostType) {
        this.hostType = hostType;
        return this;
    }

    public DefaultJType setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public void addOnPostRegister(Runnable r) {
        onPostRegisterList.add(r);
    }

    public List<Runnable> getOnPostRegisterList() {
        return onPostRegisterList;
    }

    @Override
    public void onPostRegister() {
//        System.out.println("start onPostRegister "+getName());
        super.onPostRegister();
        for (Iterator<Runnable> iterator = onPostRegisterList.iterator(); iterator.hasNext(); ) {
            Runnable runnable = iterator.next();
            runnable.run();
            iterator.remove();
        }
//        System.out.println("end   onPostRegister "+getName());
    }

    public JTypeKind getKind() {
        return kind;
    }

    public void addAnnotation(JAnnotationInstance jAnnotationInstance) {
        annotations.add(jAnnotationInstance);
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
        return findDeclaredConstructorOrNull(sig, true);
    }

    //@Override
    public JConstructor findDeclaredConstructorOrNull(JSignature sig, boolean createDefault) {
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
            if (dc != null) {
                addConstructor(dc, false);
            }
            return dc;
        }
        return null;
    }

    protected JConstructor createDefaultConstructor() {
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
        if (isParametrizedType()) {
            return rawType.getDeclaration();
        }
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

    /// /            return new HostJArray(o, componentType());
//        }
//        throw new IllegalArgumentException("Not array");
//    }
    @Override
    public JTypeVariable[] getTypeParameters() {
        if (isParametrizedType()) {
            return new JTypeVariable[0];
        }
        return typeParameters;
    }

    @Override
    public JType getRawType() {
        return rawType;
    }

    @Override
    public JStaticObject getStaticObject() {
        if (isParametrizedType()) {
            return rawType.getStaticObject();
        }
        return staticObject;
    }

    public DefaultJType setStaticObject(JStaticObject staticObject) {
        this.staticObject = staticObject;
        return this;
    }

    @Override
    public String getRawName() {
        return rawName;
    }

    public String getName() {
        return String.valueOf(name);
//        StringBuilder sb = new StringBuilder(name);
//        JType[] jTypeVariables = getTypeParameters();
//        if (jTypeVariables.length > 0) {
//            sb.append("<");
//            for (int i = 0; i < jTypeVariables.length; i++) {
//                if (i > 0) {
//                    sb.append(",");
//                }
//                sb.append(jTypeVariables[i].getName());
//            }
//            sb.append(">");
//        }
//        return sb.toString();
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }


    @Override
    public JType getSuperType() {
        return superType;
    }


    public void setInterfaces(JType[] interfaces) {
        this.interfaces.clear();
        this.interfaces.addAll(Arrays.asList(interfaces));
    }

    @Override
    public boolean isNullable() {
        if (isParametrizedType()) {
            return getRawType().isNullable();
        }
        return super.isNullable();
    }

    @Override
    public boolean isPublic() {
        if (isParametrizedType()) {
            return getRawType().isPublic();
        }
        return super.isPublic();
    }

    @Override
    public boolean isStatic() {
        if (isParametrizedType()) {
            return getRawType().isStatic();
        }
        return super.isStatic();
    }

    public void setSuperType(JType superclass) {
        this.superType = superclass;
    }

    public void addInterfaces(JType[] interfaces) {
        this.interfaces.addAll(Arrays.asList(interfaces));
    }

    //    @Override
    public JConstructor addConstructor(JSignature signature, String[] argNames, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations, boolean redefine) {
        JConstructor c = createConstructor(signature, argNames, handler, modifiers, annotations);
        return addConstructor(c, redefine);
    }

    public JConstructor createConstructor(JSignature signature, String[] argNames, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations) {
        signature = signature.setName(getName());
        DefaultJConstructor m = new DefaultJConstructor();
        m.setDeclaringType(this);
        ((DefaultJModifierList) m.getModifiers()).addAll(modifiers);
        m.setArgNames(Arrays.copyOf(argNames, argNames.length));
        if(annotations!=null){
            m.getAnnotations().addAll(Arrays.asList(annotations));
        }
        m.setHandler(handler);
        m.setGenericSignature(signature);
        return m;
    }

    public JConstructor addConstructor(JConstructor constructor, boolean redefine) {
        JSignature signature = constructor.getSignature();
        JConstructor old = findDeclaredConstructorOrNull(signature, false);
        if (old != null) {
            if (redefine) {
                //old.dispose();
            } else {
                throw new IllegalArgumentException("Constructor already registered " + getName() + "." + signature);
            }
        }
        constructors.add(constructor);
        return constructor;
    }

    //    @Override

    public void addField(JField field) {
        ((DefaultJField) field).setDeclaringType(this);
        fields.add(field);
    }

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
        if(annotations!=null) {
            f.getAnnotations().addAll(Arrays.asList(annotations));
        }
        fields.add(f);
        return f;
    }

    //    @Override


//    public JMethod addMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations, boolean redefine) {
//        JMethod old = findDeclaredMethodOrNull(signature.toNoVarArgs());
//        if (old != null) {
//            if (redefine) {
//                //old.dispose();
//            } else {
//                throw new IllegalArgumentException("Method already registered " + getName() + "." + signature);
//            }
//        }
//        JMethod m = createMethod(signature, argNames, returnType, handler, modifiers,annotations);
//        methods.put(signature.toNoVarArgs(), m);
//        return m;
//    }

    public void addMethod(JMethod m) {
        methods.add(m);
    }

    public void addInterface(JType interfaceType) {
        interfaces.add(interfaceType);
    }

    @Override
    public JType[] getInterfaces() {
        return interfaces.value().toArray(new JType[0]);
    }

    @Override
    public JConstructor[] getDeclaredConstructors() {
        if (constructors.isEmpty()) {
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
        if (isParametrizedType()) {
            return getRawType().getDefaultValue();
        }
        return defaultValue;
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

    public DefaultJType setDeclaringType(JType declaringType) {
        this.declaringType = declaringType;
        return this;
    }

    @Override
    public String getPackageName() {
        if (isParametrizedType()) {
            return getRawType().getPackageName();
        }
        return packageName;
    }

    @Override
    public String[] getExports() {
        return exports.toArray(new String[0]);
    }

    public void addExports(String... exports) {
        this.exports.addAll(Arrays.asList(exports));
    }

    public DefaultJType setDeclaration(JDeclaration declaration) {
        this.declaration = declaration;
        return this;
    }

    public void setTypeParameters(JTypeVariable[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public List<JModifier> getModifiers() {
        if (isParametrizedType()) {
            return rawType.getModifiers();
        }
        return modifiers.values();
    }

    public DefaultJType setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public void addInnerType(JType innerType) {
        innerTypes.add(innerType);
    }

    public void addModifiers(JModifier... jModifiers) {
        modifiers.addAll(Arrays.asList(jModifiers));
    }

    public void setActualTypeArguments(JType[] actualTypeArguments) {
        if (actualTypeArguments.length == 0) {
            return;
        }
        this.actualTypeArguments = actualTypeArguments;

        StringBuilder pn = new StringBuilder();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (i == 0) {
                pn.append("<");
            } else {
                pn.append(",");
            }
            pn.append(actualTypeArguments[i].getName());
            if (i == actualTypeArguments.length - 1) {
                pn.append(">");
            }
        }
        if (declaringType == null) {
            this.name = getRawType().getRawName() + pn.toString();
            this.rawName = getRawType().getRawName();
        } else {
            this.name = declaringType.getName() + "." + getRawType().getSimpleName() + pn;
            this.rawName = declaringType.getName() + "." + getRawType().getSimpleName();
        }
        this.simpleName = getRawType().getSimpleName() + pn;
        if(simpleName.contains(">")){
            throw new IllegalArgumentException("Error");
        }
    }

    @Override
    public JType toArray(int count) {
        return super.toArray(count);
    }

    @Override
    public boolean isInterface() {
        if (isParametrizedType()) {
            return rawType.isInterface();
        }
        return super.isInterface();
    }

    @Override
    public String getSourceName() {
        if (isParametrizedType()) {
            return rawType.getSourceName();
        }
        return sourceName;
    }

    @Override
    public List<JAnnotationInstance> getAnnotations() {
        if (isParametrizedType()) {
            return rawType.getAnnotations();
        }
        return annotations.values();
    }


    @Override
    public JType[] getActualTypeArguments() {
        return actualTypeArguments==null?new JType[0] :actualTypeArguments ;
    }
}
