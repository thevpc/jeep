package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.JTokenType;

public class RegexPattern extends StringPattern{
    public static final JTokenDef REGEXP=new JTokenDef(
            JTokenId.REGEX,
            "REGEX",
            JTokenType.TT_REGEX,
            "TT_REGEX",
            "<regexp>"
    );
    public RegexPattern(String start, String end) {
        super(REGEXP, start, end);
    }
}
