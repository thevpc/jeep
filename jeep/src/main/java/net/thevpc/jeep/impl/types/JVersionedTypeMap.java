package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JType;

import java.util.LinkedHashMap;
import java.util.Map;

public class JVersionedTypeMap {
    private Map<String, JVersionedType> map = new LinkedHashMap<>();

    public boolean contains(JType type) {
        String ts = type.getName();
        JVersionedType o = map.get(ts);
        if (o == null) {
            return false;
        } else {
            return o.contains(type);
        }
    }

    public boolean add(JType type) {
        String ts = type.getName();
        JVersionedType o = map.get(ts);
        if (o == null) {
            map.put(ts, new JVersionedType(type));
            return true;
        } else {
            return o.add(type);
        }
    }

    public JType getByVersion(String fullName, String version) {
        JVersionedType u = map.get(fullName);
        if (u != null) {
            return u.get(version);
        }
        return null;
    }

    public JType getDefaultVersion(String fullName) {
        JVersionedType u = map.get(fullName);
        if (u != null) {
            return u.getDefaultType();
        }
        return null;
    }
}
