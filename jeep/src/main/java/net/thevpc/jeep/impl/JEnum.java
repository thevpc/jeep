package net.thevpc.jeep.impl;

import java.util.Objects;

public class JEnum {

    private final String type;
    private final String name;
    private final int value;

    protected JEnum(JEnumDefinition type, String name, int value) {
        this.type = type.getName();
        this.name = name;
        this.value = value;
        type.register(this);
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JEnum JEnum = (JEnum) o;
        return value == JEnum.value
                && Objects.equals(type, JEnum.type)
                && Objects.equals(name, JEnum.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, value);
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
