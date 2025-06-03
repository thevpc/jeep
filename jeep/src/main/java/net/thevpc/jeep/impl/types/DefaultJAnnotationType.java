//package net.thevpc.jeep.impl.types;
//
//import net.thevpc.jeep.*;
//import net.thevpc.jeep.*;
//import net.thevpc.jeep.core.types.DefaultJAnnotationObject;
//
//import java.util.*;
//
//public class DefaultJAnnotationType extends DefaultJType implements JAnnotationType {
//    private JAnnotationField[] annotationFields;
//    private Map<String, JAnnotationField> annotationFieldsMap = new HashMap<>();
//
//    public DefaultJAnnotationType(String name, JTypeKind kind, JTypes types) {
//        super(name, kind,types);
//    }
//
//    @Override
//    public JAnnotationField[] getAnnotationFields() {
//        if (annotationFields == null) {
//            JMethod[] declaredMethods = getDeclaredMethods();
//            List<JAnnotationField> fields = new ArrayList<>();
//            for (JMethod declaredMethod : declaredMethods) {
//                if (
//                        declaredMethod.isPublic()
//                                && declaredMethod.getArgTypes().length == 0
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
//            if(annotationField.getName().equals(name)){
//                return annotationField;
//            }
//        }
//        throw new NoSuchElementException("no annotation field named "+name);
//    }
//
//    @Override
//    public JAnnotationInstance newAnnotationInstance(Map<String, Object> properties) {
//        DefaultJAnnotationObject ajo=new DefaultJAnnotationObject(this,properties);
//        return new DefaultJAnnotationInstance(ajo,this);
//    }
//
//}
