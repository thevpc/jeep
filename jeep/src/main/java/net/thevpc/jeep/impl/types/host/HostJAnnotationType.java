//package net.thevpc.jeep.impl.types.host;
//
//import net.thevpc.jeep.JAnnotationField;
//import net.thevpc.jeep.JAnnotationInstance;
//import net.thevpc.jeep.JAnnotationType;
//import net.thevpc.jeep.JTypes;
//import net.thevpc.jeep.*;
//import net.thevpc.jeep.impl.types.DefaultJAnnotationField;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.util.*;
//
//public class HostJAnnotationType extends HostJRawType implements JAnnotationType {
//    private JAnnotationField[] annotationFields;
//    private Map<String, JAnnotationField> annotationFieldsMap = new HashMap<>();
//
//    public HostJAnnotationType(Class hostType, JTypes types) {
//        super(hostType, types);
//    }
//
//    @Override
//    public JAnnotationField[] getAnnotationFields() {
//        if (annotationFields == null) {
//            Method[] declaredMethods = getHostType().getDeclaredMethods();
//            List<JAnnotationField> fields = new ArrayList<>();
//            for (Method declaredMethod : declaredMethods) {
//                if (
//                        Modifier.isPublic(declaredMethod.getModifiers())
//                                && declaredMethod.getParameterCount() == 0
//                                && !declaredMethod.getName().equals("hashCode")
//                                && !declaredMethod.getName().equals("toString")
//                                && !declaredMethod.getName().equals("annotationType")
//                ) {
//                    DefaultJAnnotationField f = new DefaultJAnnotationField(
//                            declaredMethod.getName(),
//                            declaredMethod.getDefaultValue(),
//                            this
//                    );
//                    fields.add(f);
//                    annotationFieldsMap.put(f.getName(), f);
//                }
//            }
//            this.annotationFields = fields.toArray(new JAnnotationField[0]);
//        }
//        return new JAnnotationField[0];
//    }
//
//    @Override
//    public JAnnotationField getAnnotationField(String name) {
//        for (JAnnotationField annotationField : getAnnotationFields()) {
//            if (annotationField.getName().equals(name)) {
//                return annotationField;
//            }
//        }
//        throw new NoSuchElementException("no annotation field named " + name);
//    }
//
//    @Override
//    public JAnnotationInstance newAnnotationInstance(Map<String, Object> properties) {
//        return new HostJAnnotationInstance(properties, this);
//    }
//
//}
