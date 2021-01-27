package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.JToken;

public class JTokensStringFormatPlain extends AbstractJTokensStringFormat {
    public static final JTokensStringFormat INSTANCE=new JTokensStringFormatPlain();

    @Override
    public String formatDocument(Iterable<JToken> tokensb){
        return format(tokensb);
    }

    @Override
    public String format(JToken token){
        if(token==null){
            return "";
        }
        return token.image;
    }
}
