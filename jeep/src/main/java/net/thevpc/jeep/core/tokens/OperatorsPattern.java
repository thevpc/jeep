package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.impl.tokens.JNamedImage;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.JTokenType;
import static net.thevpc.jeep.core.tokens.WordListPattern.toJTypedImages;

public class OperatorsPattern extends WordListPattern {

    public OperatorsPattern(String... words) {
        this(null,words);
    }
    
    public OperatorsPattern(String name, String[] words) {
        super("Operators", JTokenId.OFFSET_OPERATORS, JTokenPatternOrder.ORDER_OPERATOR,
                toJTypedImages(JTokenId.OFFSET_OPERATORS, JTokenType.Enums.TT_OPERATOR, words)
        );
    }

    public OperatorsPattern(String name, JNamedImage[] words) {
        super("Operators", JTokenId.OFFSET_OPERATORS, JTokenPatternOrder.ORDER_OPERATOR,
                toJTypedImages(JTokenId.OFFSET_OPERATORS, JTokenType.Enums.TT_OPERATOR, words)
        );
    }

//    public OperatorsPattern(String name, int startId, String typeName, JTypedImage... words) {
//        super(startId, ORDER_OPERATOR, typeName, words);
//    }

    public OperatorsPattern(String name, int startId, JTokenType ttype, String... words) {
        super(validateName(name), startId, JTokenPatternOrder.ORDER_OPERATOR, ttype, words);
    }

    public OperatorsPattern(String name, int startId, JTokenType ttype, JTokenPatternOrder order, String... words) {
        super(validateName(name), startId, order, ttype, words);
    }

    public OperatorsPattern(String name, JTokenType ttype, JNamedImage... words) {
        super(validateName(name), words[0].getPreferredId(), JTokenPatternOrder.ORDER_OPERATOR, ttype, words);
    }

    private static String validateName(String name) {
        return name == null ? "Operators" : name;
    }
}
