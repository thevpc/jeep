package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JAnnotationField;
import net.thevpc.jeep.JAnnotationInstance;
import net.thevpc.jeep.JAnnotationInstanceField;
import net.thevpc.jeep.JMethod;
import net.thevpc.jeep.core.types.DefaultJAnnotationObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultJAnnotationInstance implements JAnnotationInstance {
    private final DefaultJAnnotationType annotationType;
    private final DefaultJAnnotationObject ajo;
    private JAnnotationInstanceField[] fields;
    private Map<String, JAnnotationInstanceField> fieldsMap = new HashMap<>();

    public DefaultJAnnotationInstance(DefaultJAnnotationObject ajo,DefaultJAnnotationType annotationType) {
        this.ajo = ajo;
        this.annotationType = annotationType;
    }

    @Override
    public String getName() {
        return annotationType.getName();
    }

    @Override
    public JAnnotationInstanceField[] getFields() {
        if (fields == null) {
            JMethod[] declaredMethods = annotationType.getDeclaredMethods();
            List<JAnnotationInstanceField> fields = new ArrayList<>();
            for (JMethod declaredMethod : declaredMethods) {
                if (
                        declaredMethod.isPublic()
                                && declaredMethod.getArgTypes().length == 0
                                && !declaredMethod.getName().equals("hashCode")
                                && !declaredMethod.getName().equals("toString")
                                && !declaredMethod.getName().equals("annotationType")
                ) {
                    try {
                        JAnnotationField annotationField = annotationType.getAnnotationField(declaredMethod.getName());
                        Object dv = ajo.getDefaultValues().get(declaredMethod.getName());
                        if (dv == null) {
                            dv = annotationField.getDefaultValue();
                        }
                        fields.add(new DefaultJAnnotationInstanceField(
                                annotationField,
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
        return ajo;
    }
}
