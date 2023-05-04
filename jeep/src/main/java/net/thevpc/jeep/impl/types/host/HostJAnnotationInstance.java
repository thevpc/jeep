package net.thevpc.jeep.impl.types.host;

import net.thevpc.jeep.JAnnotationInstance;
import net.thevpc.jeep.JAnnotationInstanceField;
import net.thevpc.jeep.JAnnotationType;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.impl.types.DefaultJAnnotationInstanceField;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.*;

public class HostJAnnotationInstance implements JAnnotationInstance, InvocationHandler {
    private Map<String, Object> properties;
    private JAnnotationInstanceField[] fields;
    private Map<String, JAnnotationInstanceField> fieldsMap = new HashMap<>();
    private JAnnotationType annotationType;
    private Annotation object;
    private Class hostType;

    public HostJAnnotationInstance(Map<String, Object> properties, JAnnotationType annotationType) {
        this.properties = properties;
        this.annotationType = annotationType;
        hostType = ((HostJRawType) annotationType).getHostType();
        this.object = (Annotation) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{hostType}, this);
    }

    @Override
    public String getName() {
        return annotationType.getName();
    }

    public HostJAnnotationInstance(Annotation annotation, JType annotationType) {
        this.object = annotation;
        if(annotation instanceof JAnnotationType) {
            this.annotationType = (JAnnotationType) annotationType;
        }else if(annotationType.getInterfaces().length==1 && annotationType.getInterfaces()[0] instanceof JAnnotationType){
            this.annotationType = (JAnnotationType) annotationType.getInterfaces()[0];
        }else{
            throw new IllegalArgumentException("Unsupported");
        }
        this.hostType = annotation.annotationType();
    }

    public JAnnotationInstanceField getField(String n) {
        getFields();
        JAnnotationInstanceField f = fieldsMap.get(n);
        if (f == null) {
            throw new NoSuchElementException("Field not found " + n);
        }
        return f;
    }

    @Override
    public JAnnotationInstanceField[] getFields() {
        if (fields == null) {
            Method[] declaredMethods = hostType.getDeclaredMethods();
            List<JAnnotationInstanceField> fields = new ArrayList<>();
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
                        if (properties != null) {
                            dv = properties.get(declaredMethod.getName());
                            if (dv == null) {
                                dv = declaredMethod.getDefaultValue();
                            }
                        } else {
                            dv = declaredMethod.invoke(getObject());
                        }
                        fields.add(new DefaultJAnnotationInstanceField(
                                annotationType.getAnnotationField(declaredMethod.getName()),
                                declaredMethod.getName(),
                                dv,
                                declaredMethod.getDefaultValue()
                        ));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            this.fields = fields.toArray(new JAnnotationInstanceField[0]);
            for (JAnnotationInstanceField field : fields) {
                fieldsMap.put(field.getName(), field);
            }
        }
        return fields;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (args.length == 0) {
            switch (method.getName()) {
                case "equals": {
                    return proxy.equals(args[0]);
                }
                case "hashCode": {
                    return hostType.hashCode() + Objects.hash(args);
                }
                case "toString": {
                    StringBuilder sb = new StringBuilder();
                    sb.append("@");
                    sb.append(hostType.getName());
                    JAnnotationInstanceField[] fields = getFields();
                    if (fields.length > 0) {
                        sb.append("(");
                        for (int i = 0; i < fields.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            Object v = fields[i].getValue();
                            sb.append(v);
                        }
                        sb.append(")");
                    }
                    return sb.toString();
                }
                case "annotationType": {
                    return hostType;
                }
                default: {
                    return getField(method.getName()).getValue();
                }
            }
        } else if (args.length == 1) {
            switch (method.getName()) {
                case "equals": {
                    return proxy.equals(args[0]);
                }
            }
        }
        throw new NoSuchElementException("Method not found " + method);
    }


}
