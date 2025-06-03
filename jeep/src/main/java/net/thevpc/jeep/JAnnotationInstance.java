package net.thevpc.jeep;

public interface JAnnotationInstance {
    String getName();
    JAnnotationInstanceField[] getFields();
    JAnnotationInstanceField getField(String name);
}
