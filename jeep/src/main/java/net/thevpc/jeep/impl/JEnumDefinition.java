package net.thevpc.jeep.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class JEnumDefinition<T extends JEnum> {

    private final String name;
    private final Class<T> associatedClass;

    public JEnumDefinition(String name, Class<T> associatedClass) {
        this.name = name;
        this.associatedClass = associatedClass;
    }

    public String getName() {
        return name;
    }

    private Map<Integer, T> v2i = new LinkedHashMap<>();
    private Map<String, T> n2i = new LinkedHashMap<>();

    public JEnumDefinition<T> addInts(Class cls) {
        return addConstIntFields(cls, null);
    }

    public JEnumDefinition<T> addConstIntFields(Class cls, Predicate<Field> filter) {
        for (Field field : cls.getFields()) {
            int m = field.getModifiers();
            if (filter == null) {
                filter = (f) -> !f.getName().startsWith("_") && f.getName().equals(f.getName().toUpperCase());
            }
            if (Modifier.isStatic(m)
                    && Modifier.isPublic(m)
                    && Modifier.isFinal(m)
                    && field.getType().equals(int.class)) {
                if (filter == null || filter.test(field)) {
                    try {
                        addIntField(field.getName(), field.getInt(null));
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return this;
    }

    public T find(int value) {
        return v2i.get(value);
    }

    public T find(String name) {
        return n2i.get(name);
    }

    public T valueOf(int value) {
        T i = find(value);
        if (i == null) {
            throw new NoSuchElementException("value " + value + " not found in " + this.name);
        }
        return i;
    }

    public T of(String name) {
        return valueOf(name);
    }
    
    public T valueOf(String name) {
        T i = find(name);
        if (i == null) {
            throw new NoSuchElementException("name " + name + " not found in " + this.name);
        }
        return i;
    }

    public JEnum addIntField(String name, int value) {
        Constructor c;
        try {
            c = associatedClass.getDeclaredConstructor(JEnumDefinition.class, String.class, Integer.TYPE);
            c.setAccessible(true);
            return (JEnum) c.newInstance(this, name, value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to create enum value " + name + "=" + value, ex);
        }
    }

    JEnum register(T enumItem) {
        String name = enumItem.getName();
        int value = enumItem.getValue();
        if (n2i.containsKey(name)) {
            throw new IllegalArgumentException("Already registered name " + name);
        }
        if (v2i.containsKey(value)) {
            throw new IllegalArgumentException("Already registered value " + value);
        }
        v2i.put(value, enumItem);
        n2i.put(name, enumItem);
        return enumItem;
    }

    public String[] names() {
        return n2i.keySet().toArray(new String[0]);
    }

    public int[] values() {
        int[] r = new int[v2i.size()];
        int i = 0;
        for (Integer v : v2i.keySet()) {
            r[i] = v;
        }
        return r;
    }

    public JEnum[] items() {
        return v2i.values().toArray(new JEnum[0]);
    }
}
