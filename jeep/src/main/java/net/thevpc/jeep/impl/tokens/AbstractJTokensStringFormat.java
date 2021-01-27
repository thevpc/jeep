package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.JToken;

public abstract class AbstractJTokensStringFormat implements JTokensStringFormat{
    @Override
    public String format(Iterable<JToken> tokensb){
        StringBuilder sb=new StringBuilder();
        for (JToken token : tokensb) {
            sb.append(format(token));
        }
        return sb.toString();
    }
}
