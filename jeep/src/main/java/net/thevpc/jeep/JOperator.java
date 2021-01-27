package net.thevpc.jeep;

import java.util.Objects;

public final class JOperator {
    private final String name;
    private final JOperatorType type;

    public JOperator(String name, JOperatorType type) {
        this.name = name;
        this.type = type;
    }

    public static JOperator prefix(String name) {
        return new JOperator(name, JOperatorType.PREFIX_UNARY);
    }

    public static JOperator special(String name) {
        return new JOperator(name, JOperatorType.SPECIAL);
    }
    public static JOperator list(String name) {
        return new JOperator(name, JOperatorType.LIST);
    }

    public static JOperator postfix(String name) {
        return new JOperator(name, JOperatorType.POSTFIX_UNARY);
    }

    public static JOperator implicitList() {
        return new JOperator("", JOperatorType.LIST);
    }

    public static JOperator infix(String name) {
        return new JOperator(name, JOperatorType.INFIX_BINARY);
    }

    public String name() {
        return name;
    }

    public JOperatorType type() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JOperator jOperator = (JOperator) o;
        return Objects.equals(name, jOperator.name) &&
                type == jOperator.type;
    }

    @Override
    public String toString() {
        switch (type){
            case INFIX_BINARY:{
                return "infix("+name+")";
            }
            case PREFIX_UNARY:{
                return "prefix("+name+")";
            }
            case POSTFIX_UNARY:{
                return "postfix("+name+")";
            }
            default:{
                return type+"("+name+")";
            }
        }
    }
}
