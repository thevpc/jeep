package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.impl.tokens.JNamedImage;
import net.thevpc.jeep.JTokenType;

public class SeparatorsPattern extends WordListPattern {

    public SeparatorsPattern(String name, int startId, String... words) {
        super(validateName(name), startId, JTokenPatternOrder.ORDER_OPERATOR, JTokenType.Enums.TT_GROUP_SEPARATOR, true, words);
    }

    public SeparatorsPattern(String name, int startId, JTokenPatternOrder order,String... words) {
        super(validateName(name), startId, validateOrder(order), JTokenType.Enums.TT_GROUP_SEPARATOR, true, words);
    }

    public SeparatorsPattern(String name, int startId, JTokenPatternOrder order, JTokenType ttype, String... words) {
        super(validateName(name), startId, order, ttype, true, words);
    }

    public SeparatorsPattern(String name, int startId, JTokenType ttype, String... words) {
        super(validateName(name), startId, validateOrder(null), ttype, true, words);
    }

//    public SeparatorsPattern(String name, int startId, JTokenType ttype, JNamedImage... words) {
//        super(validateName(name), startId, ORDER_OPERATOR, ttype, words);
//    }
//    public SeparatorsPattern(String name, int startId, int order,JTokenType ttype, JNamedImage... words) {
//        super(validateName(name), startId, validateOrder(order), ttype, words);
//    }

    public SeparatorsPattern(String name, JTokenPatternOrder order,JTokenType ttype, JNamedImage... words) {
        super(validateName(name), words[0].getPreferredId(), validateOrder(order), ttype, true, words);
    }

    private static String validateName(String name) {
        return name == null ? "Separators" : name;
    }

    private static JTokenPatternOrder validateOrder(JTokenPatternOrder order) {
        return order==null?JTokenPatternOrder.ORDER_OPERATOR:order;
    }
}
