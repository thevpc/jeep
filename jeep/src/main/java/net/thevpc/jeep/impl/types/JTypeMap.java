package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JType;

import java.util.LinkedHashMap;
import java.util.Map;

public class JTypeMap {
    private Map<String, ClassAndTemplates> map = new LinkedHashMap<>();

    public boolean add(JType type) {
        String ts = type.getName();
        ClassAndTemplates o = map.get(ts);
        if (o == null) {
            JType rt = type.getRawType();
            String rn = type.getRawName();
            o = new ClassAndTemplates(new JVersionedType(rt), rn);
            map.put(ts, o);
            o.add(type);
            return true;
        }else {
            return o.add(type);
        }
    }

    public JType getRaw(String name) {
        String n = name;
        int i = name.indexOf("<");
        if (i >= 0) {
            n = n.substring(0, i);
        }
        ClassAndTemplates u = map.get(name);
        if (u != null) {
            return u.getRawClass();
        }
        return null;
    }

    public JType getRaw(String name, String version) {
        String n = name;
        int i = name.indexOf("<");
        if (i >= 0) {
            n = n.substring(0, i);
        }
        ClassAndTemplates u = map.get(name);
        if (u != null) {
            return u.getRawClass(version);
        }
        return null;
    }

    public JType get(String name) {
        String n = name;
        int i = name.indexOf("<");
        if (i >= 0) {
            n = n.substring(0, i);
        }
        ClassAndTemplates u = map.get(n);
        if (u != null) {
            return u.getDefaultVersion(name);
        }
        return null;
    }

    public JType get(String name, String version) {
        String n = name;
        int i = name.indexOf("<");
        if (i >= 0) {
            n = n.substring(0, i);
        }
        ClassAndTemplates u = map.get(n);
        if (u != null) {
            return u.getByVersion(name, version);
        }
        return null;
    }

    public boolean contains(JType type) {
        String n = type.getName();
        ClassAndTemplates u = map.get(n);
        if (u != null) {
            return u.getByVersion(n, type.getVersion()) != null;
        }
        return false;
    }

    public boolean contains(String type) {
        String n = type;
        ClassAndTemplates u = map.get(n);
        if (u != null) {
            return u.getDefaultVersion(n) != null;
        }
        return false;
    }

    private static class ClassAndTemplates {
        private JVersionedTypeMap byFullName = new JVersionedTypeMap();
        private String name;
        private JVersionedType rawClass;

        public ClassAndTemplates(JVersionedType rawClass, String name) {
            this.rawClass = rawClass;
            this.name = name;
            this.byFullName.add(rawClass.getDefaultType());
        }

        public JType getDefaultVersion(String fullName) {
            return byFullName.getDefaultVersion(fullName);
        }

        public JType getByVersion(String fullName, String version) {
            return byFullName.getByVersion(fullName, version);
        }

        public boolean add(JType type) {
            boolean a=rawClass.add(type.getRawType());
            a|=byFullName.add(type);
            return a;
        }

        public JVersionedTypeMap getByFullName() {
            return byFullName;
        }

        public String getName() {
            return name;
        }

        public JType getRawClass(String version) {
            return rawClass.get(version);
        }

        public JType getRawClass() {
            return rawClass.getDefaultType();
        }
    }
}
