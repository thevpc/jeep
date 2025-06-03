//package net.thevpc.jeep.impl.types.host;
//
//import net.thevpc.jeep.*;
//import net.thevpc.jeep.impl.functions.JSignature;
//import net.thevpc.jeep.impl.types.DefaultJAnnotationInstanceList;
//import net.thevpc.jeep.impl.types.DefaultJModifierList;
//import net.thevpc.jeep.impl.types.JAnnotationInstanceList;
//import net.thevpc.jeep.impl.types.JModifierList;
//import net.thevpc.jeep.util.JeepPlatformUtils;
//import net.thevpc.jeep.core.JStaticObject;
//import net.thevpc.jeep.impl.JTypesSPI;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.*;
//import java.util.LinkedHashMap;
//import java.util.LinkedHashSet;
//import java.util.Map;
//import java.util.Set;
//
//public abstract class HostJRawType extends AbstractJRawType implements JRawType {
//    private Class hostType;
//    //    private JType rawType;
//    private String name;
//    private String gname;
//    private String simpleName;
//    private JConstructor defaultConstructor;
//    private LinkedHashMap<String, JField> fields;
//    private LinkedHashMap<String, JType> innerTypes;
//    private LinkedHashMap<JSignature, JMethod> methods;
//    private LinkedHashMap<JSignature, JConstructor> constructors;
//    private JTypeVariable[] typeParameters = new JTypeVariable[0];
//    private String[] exports = new String[0];
//    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();
//    private JModifierList modifiersList = new DefaultJModifierList();
//    private int modifiers;
//
//    private JStaticObject so = new JStaticObject() {
//        @Override
//        public JType type() {
//            return HostJRawType.this;
//        }
//
//        @Override
//        public Object get(String name) {
//            try {
//                Field field = hostType.getField(name);
//                JeepPlatformUtils.setAccessibleWorkaround(field);
//                return field.get(null);
//            } catch (IllegalAccessException e) {
//                throw new IllegalArgumentException("Field not accessible " + getName() + "." + name);
//            } catch (NoSuchFieldException e) {
//                throw new IllegalArgumentException("Field not found " + getName() + "." + name);
//            }
//        }
//
//        @Override
//        public void set(String name, Object o) {
//            try {
//                Field field = ((Class) hostType).getField(name);
//                JeepPlatformUtils.setAccessibleWorkaround(field);
//                field.set(null, o);
//            } catch (IllegalAccessException e) {
//                throw new IllegalArgumentException("Field not accessible " + getName() + "." + name);
//            } catch (NoSuchFieldException e) {
//                throw new IllegalArgumentException("Field not found " + getName() + "." + name);
//            }
//        }
//    };
//
//    public HostJRawType(Class hostType, JTypes types) {
//        super(types);
//        this.hostType = hostType;
//        this.name = hostType.getCanonicalName();
//        this.simpleName = hostType.getSimpleName();
//        this.gname = name;
//        this.modifiers = hostType.getModifiers();
//
//    }
//
//    @Override
//    public void onPostRegister() {
//        super.onPostRegister();
//        TypeVariable[] tp = getHostType().getTypeParameters();
//        JTypeVariable[] r = new JTypeVariable[tp.length];
//        for (int i = 0; i < r.length; i++) {
//            r[i] = (JTypeVariable) ((JTypesSPI)getTypes()).forHostType(tp[i], this);
//            if (i == 0) {
//                gname += "<" + r[i].getName();
//            } else {
//                gname += "," + r[i].getName();
//            }
//            if (i == r.length - 1) {
//                gname += ">";
//            }
//        }
//        this.typeParameters = r;
//        Set<String> exportsList = new LinkedHashSet<>();
//        for (JTypesResolver resolver : getTypes().resolvers()) {
//            String[] strings = resolver.resolveTypeExports(getHostType());
//            if (strings != null) {
//                for (String string : strings) {
//                    if (string != null) {
//                        exportsList.add(string);
//                    }
//                }
//            }
//        }
//        this.exports = exportsList.toArray(new String[0]);
//        applyModifiers(getHostType().getModifiers());
//        applyAnnotations(getHostType().getAnnotations());
//    }
//
//    protected void applyAnnotations(Annotation[] annotations) {
//        DefaultJAnnotationInstanceList list = (DefaultJAnnotationInstanceList) getAnnotations();
//        for (Annotation annotation : annotations) {
//            JType jType = getTypes().forName(annotation.getClass().getName());
//            list.add(new HostJAnnotationInstance(annotation, jType));
//        }
//    }
//
//    protected void applyModifiers(int modifiers) {
//        DefaultJModifierList modifiersList = (DefaultJModifierList) getModifiers();
//        modifiersList.addJavaModifiers(modifiers);
//    }
//
//    @Override
//    public JModifierList getModifiers() {
//        return modifiersList;
//    }
//
//    @Override
//    public JAnnotationInstanceList getAnnotations() {
//        return annotations;
//    }
//
//    @Override
//    public String getSourceName() {
//        return null;
//    }
//
//    @Override
//    public String gname() {
//        return gname;
//    }
//
//    protected Map<String, JField> _fields() {
//        if (fields == null) {
//            fields = new LinkedHashMap<>();
////            if (hostType instanceof Class) {
//            JTypesSPI types = (JTypesSPI) getTypes();
//            for (Field declaredField : hostType.getDeclaredFields()) {
//                HostJField f = (HostJField) types.createHostField(declaredField);
//                fields.put(f.name(), f);
//            }
////            } else {
////                for (Field declaredField : rawClass().getDeclaredFields()) {
////                    Type gt = declaredField.getGenericType();
////                    JType y = types().forNameOrVar(gt, this);
////                    HostJField f = new HostJField(declaredField, y, this);
////                    fields.put(f.name(), f);
////                }
////            }
//        }
//        return fields;
//    }
//
//    private synchronized Map<JSignature, JMethod> _methods() {
//        if (methods == null) {
//            methods = new LinkedHashMap<>();
//            for (Method item : hostType.getDeclaredMethods()) {
//                HostJRawMethod f = new HostJRawMethod(item, this);
//                methods.put(f.getSignature().toNoVarArgs(), f);
//            }
//        }
//        return methods;
//    }
//
////    @Override
////    public final JType toArray(int count) {
////        if (hostType instanceof Class) {
////            Class t = (Class) hostType;
////            for (int i = 0; i < count; i++) {
////                t = Array.newInstance(t, 0).getClass();
////            }
////            return getRegisteredOrRegister(new HostJRawType(t, types()));
////        } else {
////            HostJRawType h = new HostJRawType(types(), rawType, actualTypeArguments, arrayDimension + count);
////            return getRegisteredOrRegister(h);
////        }
////    }
//
////    @Override
////    public Object newArray(int... len) {
////        if (arrayRootClazz == null) {
////            if (hostType instanceof Class) {
////                return Array.newInstance((Class<?>) hostType, len);
////            }else{
////                return Array.newInstance(rawClass(), len);
////            }
////        } else if (arrayRootClazz instanceof Class) {
////            return Array.newInstance((Class<?>) arrayRootClazz, len);
////        }else{
////            return Array.newInstance(rawClass(), len);
////        }
////    }
//
////    private JType resolveDefaultImplClass() {
////        //this should be handled in more customizable way!!
////        if (List.class.equals(hostType)) {
////            return types().forName(ArrayList.class);
////        }
////        if (Set.class.equals(hostType)) {
////            return types().forName(HashSet.class);
////        }
////        if (Map.class.equals(hostType)) {
////            return types().forName(HashMap.class);
////        }
////        return null;
////    }
//
//    private synchronized Map<String, JType> _innerTypes() {
//        if (innerTypes == null) {
//            innerTypes = new LinkedHashMap<>();
//            for (Class item : hostType.getDeclaredClasses()) {
//                HostJRawType f = (HostJRawType) ((JTypesSPI)getTypes()).forHostType(item,this);
//                innerTypes.put(f.simpleName(), f);
//            }
//        }
//        return innerTypes;
//    }
//
//    private synchronized LinkedHashMap<JSignature, JConstructor> _constructors() {
//        if (constructors == null) {
//            constructors = new LinkedHashMap<>();
////            if (hostType instanceof Class) {
//            for (Constructor item : hostType.getDeclaredConstructors()) {
//                HostJRawConstructor f = new HostJRawConstructor(item, this);
//                constructors.put(f.getSignature().toNoVarArgs(), f);
//            }
////            }
//        }
//        return constructors;
//    }
//
//    @Override
//    public Object cast(Object o) {
//        return hostType.cast(o);
//    }
//
//    @Override
//    public JType boxed() {
//        return ((JTypesSPI)getTypes()).forHostType(JeepPlatformUtils.toBoxingType(hostType),this);
//    }
//
//    @Override
//    public boolean isPrimitive() {
//        return hostType.isPrimitive();
//    }
//
//    @Override
//    public JType toPrimitive() {
//        if (isPrimitive()) {
//            return this;
//        }
//        Class p = JeepPlatformUtils.REF_TO_PRIMITIVE_TYPES.get((Class) hostType);
//        return p == null ? null : ((JTypesSPI)getTypes()).forHostType(p,this);
//    }
//
//    @Override
//    public JMethod findDeclaredMethodOrNull(JSignature sig) {
//        return _methods().get(sig.toNoVarArgs());
//    }
//
//    @Override
//    public synchronized JField findDeclaredFieldOrNull(String fieldName) {
//        return _fields().get(fieldName);
//    }
//
////    @Override
////    public JType toArray(int count) {
////        Class t = clazz;
////        for (int i = 0; i < count; i++) {
////            t = Array.newInstance(t, 0).getClass();
////        }
////        return types.forName(t);
////    }
//
//    @Override
//    public JConstructor findDeclaredConstructorOrNull(JSignature sig) {
//        sig = sig.setName(getName());
//        JConstructor f = _constructors().get(sig.toNoVarArgs());
//        if (f != null) {
//            return f;
//        }
//        return null;
//    }
//
//    @Override
//    public JConstructor findDefaultConstructorOrNull() {
//        if (defaultConstructor == null) {
//            JConstructor defaultConstructor = getDeclaredConstructor(JSignature.of(getName(), new JType[0]));
//            if (defaultConstructor.isPublic()) {
//                this.defaultConstructor = defaultConstructor;
//            }
//        }
//        return this.defaultConstructor;
//    }
//
//    @Override
//    public JType findDeclaredInnerTypeOrNull(String name) {
//        return _innerTypes().get(name);
//    }
//
//    @Override
//    public JTypeVariable[] getTypeParameters() {
//        return typeParameters;
//    }
//
//    @Override
//    public JType getRawType() {
//        return this;
//    }
//
//    @Override
//    public JStaticObject getStaticObject() {
//        return so;
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public String simpleName() {
//        return simpleName;
//    }
//
//
////    @Override
////    public JConstructor addConstructor(JSignature signature, String[] argNames, JInvoke handler, JModifier[] modifiers, JAnnotationInstance[] annotations, boolean redefine) {
////        throw new IllegalArgumentException("Not supported yet");
////    }
////
////    @Override
////    public JField addField(String name, JType type, int modifiers, boolean redefine) {
////        throw new IllegalArgumentException("Not supported yet");
////    }
////
////    @Override
////    public JMethod addMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, int modifiers, boolean redefine) {
////        throw new IllegalArgumentException("Not supported yet");
////    }
//
////    @Override
////    public boolean isAssignableFrom(JType other) {
////        if (other instanceof HostJRawType) {
////            if (clazz instanceof Class && ((HostJRawType) other).clazz instanceof Class) {
////                return ((Class) clazz).isAssignableFrom((Class) ((HostJRawType) other).clazz);
////            }
////        }
////        return false;
////    }
//
//    @Override
//    public boolean isNullable() {
//        return !isPrimitive();
//    }
//
//    @Override
//    public JType getSuperType() {
//        if (hostType instanceof Class) {
//            //TODO FIX ME
////            Type s = ((Class) hostType).getGenericSuperclass();
////            return s == null ? null : htypes().forName(s, this);
//            Class s = hostType.getSuperclass();
//            return s == null ? null : ((JTypesSPI)getTypes()).forHostType(s, this);
//
//        } else {
//            JType rc = getRawType();
//            JType superclass = rc.getSuperType();
//            if (superclass == null) {
//                return superclass;
//            } else {
//                //should i update vars?
//                return superclass;
//            }
//        }
//    }
//
//    @Override
//    public JType[] getInterfaces() {
//        if (hostType instanceof Class) {
////            Type[] interfaces = ((Class) hostType).getGenericInterfaces();
////            JType[] ii = new JType[interfaces.length];
////            for (int i = 0; i < ii.length; i++) {
////                ii[i] = htypes().forName(interfaces[i], this);
////            }
////            return ii;
//            Class[] interfaces = hostType.getInterfaces();
//            JType[] ii = new JType[interfaces.length];
//            for (int i = 0; i < ii.length; i++) {
//                ii[i] = ((JTypesSPI)getTypes()).forHostType(interfaces[i], this);
//            }
//            return ii;
//        } else {
//            JType rc = getRawType();
//            JType[] superInterfaces = rc.getInterfaces();
//            //should i update vars?
//            return superInterfaces;
//        }
//    }
//
//    @Override
//    public JConstructor[] getDeclaredConstructors() {
//        return _constructors().values().toArray(new JConstructor[0]);
//    }
//
//    @Override
//    public JField[] getDeclaredFields() {
//        return _fields().values().toArray(new JField[0]);
//    }
//
//    @Override
//    public JMethod[] getDeclaredMethods() {
//        return _methods().values().toArray(new JMethod[0]);
//    }
//
//    @Override
//    public JType[] getDeclaredInnerTypes() {
//        return _innerTypes().values().toArray(new JType[0]);
//    }
//
//    @Override
//    public Object getDefaultValue() {
//        switch (getName()) {
//            case "char":
//                return '\0';
//            case "boolean":
//                return false;
//            case "byte":
//                return (byte) 0;
//            case "short":
//                return (short) 0;
//            case "int":
//                return 0;
//            case "long":
//                return 0L;
//            case "float":
//                return 0.0f;
//            case "double":
//                return 0.0;
//        }
//        return null;
//    }
//
//    @Override
//    public JType getDeclaringType() {
//        Class dc = this.hostType.getDeclaringClass();
//        if (dc == null) {
//            return null;
//        }
//        return ((JTypesSPI)getTypes()).forHostType(dc, this);
//    }
//
//    @Override
//    public String getPackageName() {
//        Package p = this.hostType.getPackage();
//        if (p == null) {
//            return null;
//        }
//        return p.getName();
//    }
//
//    @Override
//    public String[] getExports() {
//        return exports;
//    }
//
//    @Override
//    public boolean isInterface() {
//        return hostType.isInterface();
//    }
//
//    public Class getHostType() {
//        return hostType;
//    }
//}
