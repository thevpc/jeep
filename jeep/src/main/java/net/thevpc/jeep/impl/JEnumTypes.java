package net.thevpc.jeep.impl;

public final class JEnumTypes {

    private JEnumTypes() {
    }

    public static <T extends JEnum> JEnumDefinition<T> find(String enumType) {
        return JEnumTypeRegistry.INSTANCE.find(enumType);
    }

    public static JEnumDefinition get(String enumType) {
        return JEnumTypeRegistry.INSTANCE.get(enumType);
    }

    public static <T extends JEnum> JEnumDefinition<T> of(Class<T> enumClass) {
        return JEnumTypeRegistry.INSTANCE.of(enumClass);
    }

    public static <T extends JEnum> JEnumDefinition<T> ofInts(Class<T> enumClass) {
        return JEnumTypeRegistry.INSTANCE.ofInts(enumClass);
    }

    public static <T extends JEnum> JEnumDefinition<T> register(Class<T> enumClass) {
        return JEnumTypeRegistry.INSTANCE.register(enumClass);
    }

    public static <T extends JEnum> JEnumDefinition<T> register(String enumType, Class<T> enumClass) {
        return JEnumTypeRegistry.INSTANCE.register(enumType, enumClass);
    }

}
