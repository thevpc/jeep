package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JType;

import java.util.LinkedHashMap;

public class JVersionedType {
    private String name;
    private JType defaultType;
    private LinkedHashMap<String, JType> byVersion = new LinkedHashMap<>();

    public JVersionedType(JType defaultType) {
        this.name=defaultType.getName();
        add(defaultType);
    }

    public boolean add(JType type) {
        if (type == null) {
            return false;
        }
        String v = type.getVersion();
        JType o = byVersion.get(v);
        if (o == null) {
            byVersion.put(v, type);
            if (defaultType == null) {
                defaultType = type;
            }
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public JType get(String version) {
        return byVersion.get(version);
    }

    public JType getDefaultType() {
        return defaultType;
    }

    public boolean contains(JType type) {
        String v = type.getVersion();
        return byVersion.containsKey(v);
    }
}
