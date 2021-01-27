package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.JTokenType;

public class TemporalPattern extends StringPattern{
    public static final JTokenDef TEMPORAL=new JTokenDef(
            JTokenId.TEMPORAL,
            "TEMPORAL",
            JTokenType.TT_TEMPORAL,
            "TT_TEMPORAL",
            "t\"yyyy-MM-dd\""
    );
    public TemporalPattern(String start, String end) {
        super(TEMPORAL, start, end);
    }
    public TemporalPattern(JTokenDef info, String start, String end) {
        super(info, start, end);
    }
}
