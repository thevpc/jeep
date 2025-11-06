package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.types.DefaultJAnnotationObject;

import java.lang.annotation.Annotation;
import java.util.*;

public class DefaultJAnnotationInstance implements JAnnotationInstance {
    private JType annotationType;
    private Map<String, JAnnotationInstanceField> fieldsMap = new HashMap<>();
    private Annotation hostAnnotation;
    private String name;

    public DefaultJAnnotationInstance() {
    }
    public DefaultJAnnotationInstance(String name) {
        this.name=name;
    }


    public JType getAnnotationType() {
        return annotationType;
    }

    public DefaultJAnnotationInstance setAnnotationType(JType annotationType) {
        this.annotationType = annotationType;
        return this;
    }

    public DefaultJAnnotationInstance setName(String name) {
        this.name = name;
        return this;
    }

    public Annotation getHostAnnotation() {
        return hostAnnotation;
    }

    public DefaultJAnnotationInstance setHostAnnotation(Annotation hostAnnotation) {
        this.hostAnnotation = hostAnnotation;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public void add(JAnnotationInstanceField f){
        fieldsMap.put(f.getName(), f);
    }

    @Override
    public JAnnotationInstanceField[] getFields() {
        return fieldsMap.values().toArray(new JAnnotationInstanceField[0]);
    }

    @Override
    public JAnnotationInstanceField getField(String name) {
        return fieldsMap.get(name);
    }

}
