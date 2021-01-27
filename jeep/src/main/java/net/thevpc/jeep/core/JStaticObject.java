package net.thevpc.jeep.core;

import net.thevpc.jeep.JType;

public interface JStaticObject {
    JType type();
    Object get(String name);
    void set(String name,Object o);
}
