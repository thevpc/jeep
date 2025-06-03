//package net.thevpc.jeep.impl.types.host;
//
//import net.thevpc.jeep.*;
//import net.thevpc.jeep.impl.functions.JSignature;
//import net.thevpc.jeep.impl.types.*;
//import net.thevpc.jeep.util.JTypeUtils;
//import net.thevpc.jeep.util.JeepPlatformUtils;
//import net.thevpc.jeep.impl.JTypesSPI;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Parameter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class HostJRawConstructor extends AbstractJConstructor implements JRawConstructor {
//    private Constructor constructor;
//    private JType declaringType;
//    private JSignature genericSig;
//    private JSignature rawSig;
//    private JTypeVariable[] typeParameters;
//    private String[] argNames;
//    private JType[] argTypes;
//    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();
//    private JModifierList modifiers = new DefaultJModifierList();
//
//    public HostJRawConstructor(Constructor constructor, JType declaringType) {
//        this.constructor = constructor;
//        this.declaringType = declaringType;
//        if (!declaringType.isRawType()) {
//            throw new IllegalStateException("error");
//        }
//        JeepPlatformUtils.setAccessibleWorkaround(constructor);
//        for (JTypesResolver resolver : declaringType.getTypes().resolvers()) {
//            String s = resolver.resolveConstructorSignature(constructor);
//            if (s != null) {
//                genericSig = JSignature.of(declaringType.getTypes(), s);
//                break;
//            }
//        }
//        if (genericSig == null) {
//            JSig sigAnn = (JSig) constructor.getAnnotation(JSig.class);
//            if (sigAnn != null) {
//                genericSig = JSignature.of(declaringType.getTypes(), sigAnn.value());
//            }
//        }
//        JType[] jeepParameterTypes = ((JTypesSPI)getTypes()).forHostType(constructor.getGenericParameterTypes(), this);
//        List<String> argNamesList = new ArrayList<>();
//        try {
//            for (Parameter parameter : constructor.getParameters()) {
//                String an = parameter.getName();
//                if (an == null) {
//                    //just skip
//                    throw new IllegalArgumentException();
//                }
//                argNamesList.add(an);
//            }
//        } catch (Exception ex) {
//            //ignore
//        }
//        this.argTypes = jeepParameterTypes;
//        if (argNamesList.size() == argTypes.length) {
//            this.argNames = argNamesList.toArray(new String[0]);
//        } else {
//            this.argNames = new String[argTypes.length];
//            for (int i = 0; i < this.argNames.length; i++) {
//                this.argNames[i] = "arg" + (i + 1);
//            }
//        }
////        genericReturnType=declaringType.types().forName(constructor.getGenericReturnType());
////        rawReturnType=declaringType.types().forName(constructor.getReturnType());
//        typeParameters = Arrays.stream(((JTypesSPI)getTypes()).forHostType(constructor.getTypeParameters(), this)).toArray(JTypeVariable[]::new);
//        if (genericSig != null) {
//            //add some match checking
//            if (genericSig.argTypes().length != jeepParameterTypes.length) {
//                throw new IllegalArgumentException("Method parameters mismatch");
//            }
//            for (int i = 0; i < jeepParameterTypes.length; i++) {
//                if (!jeepParameterTypes[i].typeName().name().equals(genericSig.argType(i).getName())) {
//                    throw new IllegalArgumentException("Method parameters mismatch");
//                }
//            }
//            rawSig = JSignature.of(constructor.getName(), JTypeUtils.buildRawType(genericSig.argTypes(), this), constructor.isVarArgs());
//        } else {
//            genericSig = new JSignature(constructor.getName(), jeepParameterTypes, constructor.isVarArgs());
//            rawSig = JSignature.of(constructor.getName(), JTypeUtils.buildRawType(genericSig.argTypes(), this), constructor.isVarArgs());//just for test
////            rawSig=new JSignature(method.getName(), declaringType.types().forName(method.getParameterTypes()), method.isVarArgs());
//        }
//        applyAnnotations(constructor.getAnnotations());
//        applyModifiers(constructor.getModifiers());
//    }
//
//    protected void applyAnnotations(Annotation[] annotations) {
//        DefaultJAnnotationInstanceList list = (DefaultJAnnotationInstanceList) getAnnotations();
//        for (Annotation annotation : annotations) {
//            list.add(new HostJAnnotationInstance(annotation,getTypes().forName(annotation.getClass().getName())));
//        }
//    }
//
//    protected void applyModifiers(int modifiers) {
//        DefaultJModifierList modifiersList = (DefaultJModifierList) getModifiers();
//        modifiersList.addJavaModifiers(modifiers);
//    }
//
//    @Override
//    public JType[] getArgTypes() {
//        return argTypes;
//    }
//
//    @Override
//    public String[] getArgNames() {
//        return argNames;
//    }
//
//    public JTypes getTypes() {
//        return declaringType.getTypes();
//    }
//
//    @Override
//    public JSignature getGenericSignature() {
//        return genericSig;
//    }
//
//    @Override
//    public Object invoke(JInvokeContext context) {
//        return null;
//    }
//
//    @Override
//    public JType getDeclaringType() {
//        return declaringType;
//    }
//
//    @Override
//    public JSignature getSignature() {
//        return rawSig;
//    }
//
//
//    @Override
//    public JAnnotationInstanceList getAnnotations() {
//        return annotations;
//    }
//
//    @Override
//    public JModifierList getModifiers() {
//        return modifiers;
//    }
//
//    @Override
//    public JType getReturnType() {
//        return getDeclaringType();
//    }
//
//    @Override
//    public String getName() {
//        return declaringType.getName();
//    }
//
//    @Override
//    public JDeclaration getDeclaration() {
//        return getDeclaringType();
//    }
//
//    @Override
//    public JTypeVariable[] getTypeParameters() {
//        return typeParameters;
//    }
//
//    @Override
//    public String getSourceName() {
//        return "<library>";
//    }
//}
