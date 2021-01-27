package net.thevpc.jeep.core.tokens;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class JTokenPatternOrder implements Comparable<JTokenPatternOrder>{
    private static Map<Integer, JTokenPatternOrder> cached = new HashMap<>();
    public static final JTokenPatternOrder ORDER_LINE_COMMENTS = valueOf(100, "ORDER_LINE_COMMENTS");
    public static final JTokenPatternOrder ORDER_BLOCK_COMMENTS = valueOf(200, "ORDER_BLOCK_COMMENTS");
    public static final JTokenPatternOrder ORDER_STRING = valueOf(300, "ORDER_STRING");
    public static final JTokenPatternOrder ORDER_NUMBER = valueOf(400, "ORDER_NUMBER");
    public static final JTokenPatternOrder ORDER_KEYWORD = valueOf(500, "ORDER_KEYWORD");
    public static final JTokenPatternOrder ORDER_OPERATOR = valueOf(600, "ORDER_OPERATOR");
    public static final JTokenPatternOrder ORDER_IDENTIFIER = valueOf(700, "ORDER_IDENTIFIER");
    public static final JTokenPatternOrder ORDER_WHITESPACE = valueOf(800, "ORDER_WHITESPACE");

    private String name;
    private int value;

    private JTokenPatternOrder(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static JTokenPatternOrder valueOf(int value) {
        return valueOf(value, "ORDER_" + value);
    }

    public static JTokenPatternOrder valueOf(int value, String name) {
        return cached.computeIfAbsent(value, (x) -> new JTokenPatternOrder(name, x));
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JTokenPatternOrder that = (JTokenPatternOrder) o;
        return value == that.value &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

    @Override
    public int compareTo(JTokenPatternOrder o) {
        if(o==null){
            return 1;
        }
        return Integer.compare(getValue(),o.getValue());
    }
}
