package net.thevpc.jeep;

import java.util.Map;

public interface JAnnotationType extends JRawType{
    JAnnotationField[] getAnnotationFields();
    JAnnotationField getAnnotationField(String name);

    JAnnotationInstance newInstance(Map<String, Object> properties);
}
