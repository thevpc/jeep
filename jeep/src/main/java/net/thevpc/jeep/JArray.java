package net.thevpc.jeep;

import net.thevpc.jeep.core.JObject;

public interface JArray extends JObject {
    JType type();

    JType componentType();

    int length();

    Object get(int index);

    void set(int index, Object value);

    Object value();
}
