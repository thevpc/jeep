package net.thevpc.jeep.impl.types.host;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.core.types.DefaultJField;
import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.*;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.util.JeepPlatformUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HostHelper {
    private static final int[] MODIFIER_FLAGS = {
            Modifier.PUBLIC, Modifier.PRIVATE, Modifier.PROTECTED,
            Modifier.STATIC, Modifier.FINAL, Modifier.SYNCHRONIZED,
            Modifier.VOLATILE, Modifier.TRANSIENT, Modifier.NATIVE,
            Modifier.INTERFACE, Modifier.ABSTRACT, Modifier.STRICT
    };
    private static final JModifier[] MODIFIER_INSTANCES = {
            DefaultJModifierList.PUBLIC, DefaultJModifierList.PRIVATE, DefaultJModifierList.PROTECTED,
            DefaultJModifierList.STATIC, DefaultJModifierList.FINAL, DefaultJModifierList.SYNCHRONIZED,
            DefaultJModifierList.VOLATILE, DefaultJModifierList.TRANSIENT, DefaultJModifierList.NATIVE,
            DefaultJModifierList.INTERFACE, DefaultJModifierList.ABSTRACT, DefaultJModifierList.STRICT
    };

    public static JModifier[] toJModifiers(int modifiers) {
        List<JModifier> all = new ArrayList<>();
        for (int i = 0; i < MODIFIER_FLAGS.length && modifiers != 0; i++) {
            int flag = MODIFIER_FLAGS[i];
            if ((modifiers & flag) != 0) {
                all.add(MODIFIER_INSTANCES[i]);
                modifiers &= ~flag; // clear the bit
            }
        }
        return all.toArray(new JModifier[0]);
    }


    public static DefaultJType createHostType(Class hostType, JTypes types) {
        JTypeKind kind = JTypeKind.CLASS;
        if (hostType.isEnum()) {
            kind = JTypeKind.ENUM;
        } else if (hostType.isInterface()) {
            kind = JTypeKind.INTERFACE;
        } else if (hostType.isAnnotation()) {
            kind = JTypeKind.ANNOTATION;
        } else if (Throwable.class.isAssignableFrom(hostType)) {
            kind = JTypeKind.EXCEPTION;
        }
        DefaultJType jt = new DefaultJType(hostType.getName(), kind, types);
        jt.setHostType(hostType);
        Package p = hostType.getPackage();
        jt.setPackageName(p == null ? null : p.getName());
        jt.addModifiers(toJModifiers(hostType.getModifiers()));

        jt.setPrimitiveType(hostType.isPrimitive());
        StringBuilder gname = new StringBuilder(hostType.getName());
        jt.addOnPostRegisterList(new Runnable() {
            @Override
            public void run() {
                TypeVariable[] tp = hostType.getTypeParameters();
                JTypeVariable[] r = new JTypeVariable[tp.length];
                for (int i = 0; i < r.length; i++) {
                    r[i] = (JTypeVariable) ((JTypesSPI) types).forHostType(tp[i], jt);
                    if (i == 0) {
                        gname.append("<" + r[i].getName());
                    } else {
                        gname.append("," + r[i].getName());
                    }
                    if (i == r.length - 1) {
                        gname.append(">");
                    }
                }
                jt.setTypeParameters(r);
                Set<String> exportsList = new LinkedHashSet<>();
                for (JTypesResolver resolver : types.resolvers()) {
                    String[] strings = resolver.resolveTypeExports(hostType);
                    if (strings != null) {
                        for (String string : strings) {
                            if (string != null) {
                                exportsList.add(string);
                            }
                        }
                    }
                }
                jt.addExports(exportsList.toArray(new String[0]));

                for (Field declaredField : hostType.getDeclaredFields()) {
                    JField f = createHostField(declaredField, jt);
                    jt.addField(f);
                }

                for (Method item : hostType.getDeclaredMethods()) {
                    jt.addMethod(createHostMethod(item, jt));
                }

                for (Class item : hostType.getDeclaredClasses()) {
                    JRawType f = (JRawType) ((JTypesSPI) types).forHostType(item, jt);
                    jt.addInnerType(f);
                }
                for (Constructor item : hostType.getDeclaredConstructors()) {
                    JConstructor f = createHostConstructor(item, jt);
                    jt.addConstructor(f, true);
                }

                if (jt.isPrimitive()) {
                    jt.setBoxedType(
                            ((JTypesSPI) types).forHostType(JeepPlatformUtils.toBoxingType(hostType), jt)
                    );
                } else {
                    Class p = JeepPlatformUtils.REF_TO_PRIMITIVE_TYPES.get((Class) hostType);
                    jt.setUnboxedType(p == null ? null : ((JTypesSPI) types).forHostType(p, jt));
                }

                if (hostType instanceof Class) {
                    //TODO FIX ME
//            Type s = ((Class) hostType).getGenericSuperclass();
//            return s == null ? null : htypes().forName(s, this);
                    Class s = hostType.getSuperclass();
                    jt.setSuperType(s == null ? null : ((JTypesSPI) types).forHostType(s, jt));
                } else {
                    //TODO fix me
                    JType rc = jt.getRawType();
                    JType superclass = rc.getSuperType();
                    jt.setSuperType(superclass);
                }

                if (hostType instanceof Class) {
//            Type[] interfaces = ((Class) hostType).getGenericInterfaces();
//            JType[] ii = new JType[interfaces.length];
//            for (int i = 0; i < ii.length; i++) {
//                ii[i] = htypes().forName(interfaces[i], this);
//            }
//            return ii;
                    Class[] interfaces = hostType.getInterfaces();
                    JType[] ii = new JType[interfaces.length];
                    for (int i = 0; i < ii.length; i++) {
                        jt.addInterface(((JTypesSPI) types).forHostType(interfaces[i], jt));
                    }
                } else {
                    JType rc = jt.getRawType();
                    JType[] superInterfaces = rc.getInterfaces();
                    for (JType si : superInterfaces) {
                        jt.addInterface(si);

                    }
                }
                switch (jt.getName()) {
                    case "char":
                        jt.setDefaultValue('\0');
                    case "boolean":
                        jt.setDefaultValue(false);
                    case "byte":
                        jt.setDefaultValue((byte) 0);
                        return;
                    case "short":
                        jt.setDefaultValue((short) 0);
                    case "int":
                        jt.setDefaultValue(0);
                    case "long":
                        jt.setDefaultValue(0L);
                    case "float":
                        jt.setDefaultValue(0.0f);
                    case "double":
                        jt.setDefaultValue(0.0);
                        return;
                    default:
                        jt.setDefaultValue(null);
                }

                Class dc = hostType.getDeclaringClass();
                if (dc == null) {
                    jt.setDeclaringType(null);
                } else {
                    jt.setDeclaringType(((JTypesSPI) types).forHostType(dc, jt));
                }
                jt.setStaticObject(new JStaticObject() {
                    @Override
                    public JType type() {
                        return jt;
                    }

                    @Override
                    public Object get(String name) {
                        try {
                            Field field = hostType.getField(name);
                            JeepPlatformUtils.setAccessibleWorkaround(field);
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                            throw new IllegalArgumentException("Field not accessible " + type().getName() + "." + name);
                        } catch (NoSuchFieldException e) {
                            throw new IllegalArgumentException("Field not found " + type().getName() + "." + name);
                        }
                    }

                    @Override
                    public void set(String name, Object o) {
                        try {
                            Field field = ((Class) hostType).getField(name);
                            JeepPlatformUtils.setAccessibleWorkaround(field);
                            field.set(null, o);
                        } catch (IllegalAccessException e) {
                            throw new IllegalArgumentException("Field not accessible " + type().getName() + "." + name);
                        } catch (NoSuchFieldException e) {
                            throw new IllegalArgumentException("Field not found " + type().getName() + "." + name);
                        }
                    }
                });

                if (hostType.isAnnotation()) {
                    Method[] declaredMethods = hostType.getDeclaredMethods();
                    for (Method declaredMethod : declaredMethods) {
                        if (
                                Modifier.isPublic(declaredMethod.getModifiers())
                                        && declaredMethod.getParameterCount() == 0
                                        && !declaredMethod.getName().equals("hashCode")
                                        && !declaredMethod.getName().equals("toString")
                                        && !declaredMethod.getName().equals("annotationType")
                        ) {
                            DefaultJAnnotationField f = new DefaultJAnnotationField(
                                    declaredMethod.getName(),
                                    declaredMethod.getDefaultValue(),
                                    jt
                            );
                            jt.addAnnotationField(f);
                        }
                    }
                }
                for (Annotation annotation : hostType.getAnnotations()) {
                    JType jType = types.forName(annotation.getClass().getName());
                    jt.addAnnotation(createAnnotationInstance(annotation, (JRawType) jType));
                }
            }
        });
        return jt;
    }

    private static JAnnotationInstance createAnnotationInstance(Annotation annotation, JRawType jType) {
        DefaultJAnnotationInstance i=new DefaultJAnnotationInstance();
        i.setName(annotation.annotationType().getName());
        i.setAnnotationType(jType);
        //i.setHostAnnotation(annotation);
        JType annType = ((JTypesSPI) jType.getTypes()).forHostType(annotation.annotationType(),null);
        Method[] declaredMethods = annotation.annotationType().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (
                    Modifier.isPublic(declaredMethod.getModifiers())
                            && declaredMethod.getParameterCount() == 0
                            && !declaredMethod.getName().equals("hashCode")
                            && !declaredMethod.getName().equals("toString")
                            && !declaredMethod.getName().equals("annotationType")
            ) {
                try {
                    Object dv = null;
                    dv = declaredMethod.invoke(annotation);
                    i.add(new DefaultJAnnotationInstanceField(
                            annType.getAnnotationField(declaredMethod.getName()),
                            declaredMethod.getName(),
                            dv,
                            declaredMethod.getDefaultValue()
                    ));
                } catch (Exception e) {
                    Logger.getLogger(HostHelper.class.getName()).log(Level.SEVERE, e.toString());
                }
            }
        }
        return i;
    }

    private static JConstructor createHostConstructor(Constructor constructor, DefaultJType declaringType) {
        DefaultJConstructor m = new DefaultJConstructor();
        m.setHostConstructor(constructor);
        for (Annotation annotation : constructor.getAnnotations()) {
            JType jType = declaringType.getTypes().forName(annotation.getClass().getName());
            m.addAnnotation(createAnnotationInstance(annotation, (JRawType) jType));
        }
        m.addModifiers(toJModifiers(constructor.getModifiers()));
        JSignature genericSig = null;
        for (JTypesResolver resolver : declaringType.getTypes().resolvers()) {
            String s = resolver.resolveConstructorSignature(constructor);
            if (s != null) {
                genericSig = JSignature.of(declaringType.getTypes(), s);
                break;
            }
        }
        if (genericSig == null) {
            JSig sigAnn = (JSig) constructor.getAnnotation(JSig.class);
            if (sigAnn != null) {
                genericSig = JSignature.of(declaringType.getTypes(), sigAnn.value());
            }
        }

        JType[] jeepParameterTypes = ((JTypesSPI) declaringType.getTypes()).forHostType(constructor.getGenericParameterTypes(), m);
        List<String> argNamesList = new ArrayList<>();
        try {
            for (Parameter parameter : constructor.getParameters()) {
                String an = parameter.getName();
                if (an == null) {
                    //just skip
                    throw new IllegalArgumentException();
                }
                argNamesList.add(an);
            }
        } catch (Exception ex) {
            //ignore
        }

        String[] argNames = null;
        if (argNamesList.size() == jeepParameterTypes.length) {
            argNames = argNamesList.toArray(new String[0]);
        } else {
            argNames = new String[jeepParameterTypes.length];
            for (int i = 0; i < argNames.length; i++) {
                argNames[i] = "arg" + (i + 1);
            }
        }
        m.setArgNames(argNames);
        JTypeVariable[] typeParameters = null;
        typeParameters = Arrays.stream(((JTypesSPI) declaringType.getTypes()).forHostType(constructor.getTypeParameters(), m)).toArray(JTypeVariable[]::new);
        JSignature rawSig = null;
        if (genericSig != null) {
            //add some match checking
            if (genericSig.argTypes().length != jeepParameterTypes.length) {
                throw new IllegalArgumentException("Method parameters mismatch");
            }
            for (int i = 0; i < jeepParameterTypes.length; i++) {
                if (!jeepParameterTypes[i].typeName().name().equals(genericSig.argType(i).getName())) {
                    throw new IllegalArgumentException("Method parameters mismatch");
                }
            }
            rawSig = JSignature.of(constructor.getName(), JTypeUtils.buildRawType(genericSig.argTypes(), m), constructor.isVarArgs());
        } else {
            genericSig = new JSignature(constructor.getName(), jeepParameterTypes, constructor.isVarArgs());
            rawSig = JSignature.of(constructor.getName(), JTypeUtils.buildRawType(genericSig.argTypes(), m), constructor.isVarArgs());//just for test
//            rawSig=new JSignature(method.getName(), declaringType.types().forName(method.getParameterTypes()), method.isVarArgs());
        }
        m.setGenericSignature(genericSig);
        m.setHandler(new JInvoke() {
            @Override
            public Object invoke(JInvokeContext context) {
                try {
                    constructor.setAccessible(true);
                    JEvaluable[] rargs = context.getArguments();
                    Object[] eargs = new Object[rargs.length];
                    for (int i = 0; i < eargs.length; i++) {
                        eargs[i] = rargs[i].evaluate(context);
                    }
                    return constructor.newInstance(context.getInstance(), eargs);
                } catch (IllegalAccessException ex) {
                    throw new IllegalArgumentException(ex);
                } catch (InvocationTargetException e) {
                    Throwable c = e.getCause();
                    if (c instanceof RuntimeException) {
                        throw (RuntimeException) c;
                    }
                    throw new RuntimeException(c);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return m;
    }

    private static JRawMethod createHostMethod(Method declaredMethod, DefaultJType declaringType) {
        DefaultJRawMethod m = new DefaultJRawMethod();
        m.setHostMethod(declaredMethod);
        for (Annotation annotation : declaredMethod.getAnnotations()) {
            JType jType = declaringType.getTypes().forName(annotation.getClass().getName());
            m.addAnnotation(createAnnotationInstance(annotation, (JRawType) jType));
        }
        m.addModifiers(toJModifiers(declaredMethod.getModifiers()));
        m.setDefaultMethod(declaredMethod.isDefault());
        JSignature genericSig = null;
        for (JTypesResolver resolver : declaringType.getTypes().resolvers()) {
            String s = resolver.resolveMethodSignature(declaredMethod);
            if (s != null) {
                genericSig = JSignature.of(declaringType.getTypes(), s);
                break;
            }
        }
        if (genericSig == null) {
            JSig sigAnn = declaredMethod.getAnnotation(JSig.class);
            if (sigAnn != null) {
                genericSig = JSignature.of(declaringType.getTypes(), sigAnn.value());
            }
        }

        JType[] jeepParameterTypes = ((JTypesSPI) declaringType.getTypes()).forHostType(declaredMethod.getGenericParameterTypes(), m);
        List<String> argNamesList = new ArrayList<>();
        try {
            for (Parameter parameter : declaredMethod.getParameters()) {
                String an = parameter.getName();
                if (an == null) {
                    //just skip
                    throw new IllegalArgumentException();
                }
                argNamesList.add(an);
            }
        } catch (Exception ex) {
            //ignore
        }

        String[] argNames = null;
        if (argNamesList.size() == jeepParameterTypes.length) {
            argNames = argNamesList.toArray(new String[0]);
        } else {
            argNames = new String[jeepParameterTypes.length];
            for (int i = 0; i < argNames.length; i++) {
                argNames[i] = "arg" + (i + 1);
            }
        }
        m.setArgNames(argNames);
        m.setGenericReturnType(((JTypesSPI) declaringType.getTypes()).forHostType(declaredMethod.getGenericReturnType(), m));
        JTypeVariable[] typeParameters = null;
        typeParameters = Arrays.stream(((JTypesSPI) declaringType.getTypes()).forHostType(declaredMethod.getTypeParameters(), m)).toArray(JTypeVariable[]::new);
        JSignature rawSig = null;
        if (genericSig != null) {
            //add some match checking
            if (genericSig.argTypes().length != jeepParameterTypes.length) {
                throw new IllegalArgumentException("Method parameters mismatch");
            }
            for (int i = 0; i < jeepParameterTypes.length; i++) {
                if (!jeepParameterTypes[i].typeName().name().equals(genericSig.argType(i).getName())) {
                    throw new IllegalArgumentException("Method parameters mismatch");
                }
            }
            rawSig = JSignature.of(declaredMethod.getName(), JTypeUtils.buildRawType(genericSig.argTypes(), m), declaredMethod.isVarArgs());
        } else {
            genericSig = new JSignature(declaredMethod.getName(), jeepParameterTypes, declaredMethod.isVarArgs());
            rawSig = JSignature.of(declaredMethod.getName(), JTypeUtils.buildRawType(genericSig.argTypes(), m), declaredMethod.isVarArgs());//just for test
//            rawSig=new JSignature(method.getName(), declaringType.types().forName(method.getParameterTypes()), method.isVarArgs());
        }
        m.setGenericSignature(genericSig);
        m.setHandler(new JInvoke() {
            @Override
            public Object invoke(JInvokeContext context) {
                try {
                    declaredMethod.setAccessible(true);
                    JEvaluable[] rargs = context.getArguments();
                    Object[] eargs = new Object[rargs.length];
                    for (int i = 0; i < eargs.length; i++) {
                        eargs[i] = rargs[i].evaluate(context);
                    }
                    return declaredMethod.invoke(context.getInstance(), eargs);
                } catch (IllegalAccessException ex) {
                    throw new IllegalArgumentException(ex);
                } catch (InvocationTargetException e) {
                    Throwable c = e.getCause();
                    if (c instanceof RuntimeException) {
                        throw (RuntimeException) c;
                    }
                    throw new RuntimeException(c);
                }
            }
        });
        return m;
    }

    public static JField createHostField(Field declaredField, DefaultJType jt) {
        DefaultJField djf = new DefaultJField();
        djf.setDeclaringType(jt);
        djf.setGenericType(((JTypesSPI) jt.getTypes()).forHostType(declaredField.getGenericType(), jt));
        for (Annotation annotation : declaredField.getAnnotations()) {
            JType jType = jt.getTypes().forName(annotation.getClass().getName());
            djf.addAnnotation(createAnnotationInstance(annotation, (JRawType) jType));
        }
        djf.addModifiers(toJModifiers(declaredField.getModifiers()));
        djf.setSetter(new JRawField.Setter() {
            @Override
            public void set(JRawField field, Object instance, Object value) {
                try {
                    declaredField.setAccessible(true);
                    declaredField.set(instance, value);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        djf.setGetter(new JRawField.Getter() {

            @Override
            public Object get(JRawField field, Object instance) {
                try {
                    declaredField.setAccessible(true);
                    return declaredField.get(instance);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return djf;
    }
}
