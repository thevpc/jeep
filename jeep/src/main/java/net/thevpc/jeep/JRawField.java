package net.thevpc.jeep;

public interface JRawField extends JField {
    JType genericType();

    interface Getter{
        Object get(JRawField field,Object instance);
    }
    interface Setter{
        void set(JRawField field,Object instance,Object value);
    }
}
