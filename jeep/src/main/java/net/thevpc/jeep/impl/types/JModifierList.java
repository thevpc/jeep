package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JModifier;

import java.util.List;

public interface JModifierList {
    JModifier[] toArray();
    <T> T[] toArray(Class<T> cls);
    List<JModifier> toList();
    boolean contains(JModifier a);
}
