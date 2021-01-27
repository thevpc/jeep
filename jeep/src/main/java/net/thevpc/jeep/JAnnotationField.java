package net.thevpc.jeep;

public interface JAnnotationField {
    JAnnotationType getAnnotationType();

    String getName();
    Object getDefaultValue();
}
