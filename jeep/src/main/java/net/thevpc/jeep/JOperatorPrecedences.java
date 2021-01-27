package net.thevpc.jeep;

import java.util.HashMap;
import java.util.Map;

public class JOperatorPrecedences {
    public static final String OPERATORS = "-+$=<>#~%~/*|&!^#:.;`\\\"'?";
    /**
     * none
     */
    public static final int PRECEDENCE_0 = 10;
    /**
     * ',' ';'
     */
    public static final int PRECEDENCE_1 = 100;
    /**
     * ' ' '='
     */
    public static final int PRECEDENCE_2 = 200;
    /**
     * '&lt;' '&gt;' '#' '!'
     */
    public static final int PRECEDENCE_3 = 300;
    /**
     * '{@literal &}' '|' '~'
     */
    public static final int PRECEDENCE_4 = 400;
    /**
     * '+' '-'
     */
    public static final int PRECEDENCE_5 = 500;
    /**
     * '*' '/'
     */
    public static final int PRECEDENCE_6 = 600;
    /**
     * '^' '$' ':'
     */
    public static final int PRECEDENCE_7 = 700;
    /**
     * none
     */
    public static final int PRECEDENCE_8 = 800;
    /**
     * none
     */
    public static final int PRECEDENCE_9 = 900;
    /**
     * none
     */
    public static final int PRECEDENCE_10 = 1000;
    public static final int PRECEDENCE_11 = 1100;
    public static final int PRECEDENCE_12 = 1200;
    public static final int PRECEDENCE_13 = 1300;
    public static final int PRECEDENCE_14 = 1400;
    public static final int PRECEDENCE_15 = 1500;
    public static final int PRECEDENCE_16 = 1600;
    public static final int PRECEDENCE_17 = 1700;
    public static final int PRECEDENCE_18 = 1800;
    public static final int PRECEDENCE_19 = 1900;
    public static final int PRECEDENCE_20 = 2000;
    public static final int PRECEDENCE_MAX = 2002;
    public static final Map<JOperator, Integer> defaultPrecedences = new HashMap<>();

    static {
        defaultPrecedences.put(JOperator.infix("."), PRECEDENCE_15);

        defaultPrecedences.put(JOperator.postfix("++"), PRECEDENCE_14);
        defaultPrecedences.put(JOperator.postfix("--"), PRECEDENCE_14);

        defaultPrecedences.put(JOperator.prefix("++"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("--"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("+"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("-"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("!"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("~"), PRECEDENCE_13);

        defaultPrecedences.put(JOperator.infix("*"), PRECEDENCE_12);
        defaultPrecedences.put(JOperator.infix("/"), PRECEDENCE_12);
        defaultPrecedences.put(JOperator.infix("%"), PRECEDENCE_12);

        defaultPrecedences.put(JOperator.infix("+"), PRECEDENCE_11);
        defaultPrecedences.put(JOperator.infix("-"), PRECEDENCE_11);

        defaultPrecedences.put(JOperator.infix("<<"), PRECEDENCE_10);
        defaultPrecedences.put(JOperator.infix(">>"), PRECEDENCE_10);
        defaultPrecedences.put(JOperator.infix(">>>"), PRECEDENCE_10);

        defaultPrecedences.put(JOperator.infix("<"), PRECEDENCE_9);
        defaultPrecedences.put(JOperator.infix("<="), PRECEDENCE_9);
        defaultPrecedences.put(JOperator.infix(">"), PRECEDENCE_9);
        defaultPrecedences.put(JOperator.infix(">="), PRECEDENCE_9);

        defaultPrecedences.put(JOperator.infix("=="), PRECEDENCE_8);
        defaultPrecedences.put(JOperator.infix("!="), PRECEDENCE_8);
        defaultPrecedences.put(JOperator.infix("<>"), PRECEDENCE_8);

        defaultPrecedences.put(JOperator.infix("&"), PRECEDENCE_7);

        defaultPrecedences.put(JOperator.infix("^"), PRECEDENCE_6);

        defaultPrecedences.put(JOperator.infix("|"), PRECEDENCE_5);

        defaultPrecedences.put(JOperator.infix("&&"), PRECEDENCE_4);

        defaultPrecedences.put(JOperator.infix("||"), PRECEDENCE_3);

        defaultPrecedences.put(JOperator.infix("="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("+="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("-="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("*="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("/="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("%="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("->"), PRECEDENCE_0+20);
        defaultPrecedences.put(JOperator.infix(","), PRECEDENCE_0+10);
        defaultPrecedences.put(JOperator.infix(";"), PRECEDENCE_0);
    }

    public static int getDefaultPrecedenceOrNull(JOperator op) {
        return defaultPrecedences.get(op);
    }

    public static int getDefaultPrecedence(JOperator op) {
        Integer a = defaultPrecedences.get(op);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("No Default Precedence for " + op);
    }
}
