package net.thevpc.jeep.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class JEnumTypeRegistry {

    public static final JEnumTypeRegistry INSTANCE = new JEnumTypeRegistry();

    private final Map<String, JEnumDefinition> names = new HashMap<>();

    public <T extends JEnum> JEnumDefinition<T> find(String enumType) {
        return names.get(enumType);
    }

    public JEnumDefinition get(String enumType) {
        JEnumDefinition e = find(enumType);
        if (e == null) {
            throw new NoSuchElementException("enum " + enumType + " not found");
        }
        return e;
    }

    public <T extends JEnum> JEnumDefinition<T> of(Class<T> enumClass) {
        return ofInts(enumClass);
    }
    
    public <T extends JEnum> JEnumDefinition<T> ofInts(Class<T> enumClass) {
        return register(enumClass).addInts(enumClass);
    }
    
    public <T extends JEnum> JEnumDefinition<T> register(Class<T> enumClass) {
        return register(enumClass.getName(), enumClass);
    }

    public <T extends JEnum> JEnumDefinition<T> register(String enumType, Class<T> enumClass) {
        if (names.containsKey(enumType)) {
            throw new IllegalArgumentException("enum " + enumType + " already registered");
        }
        JEnumDefinition value = new JEnumDefinition(enumType, enumClass);
        names.put(enumType, value);
        return value;
    }

}
