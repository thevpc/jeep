package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.DefaultJTokenFormat;
import net.thevpc.jeep.core.tokens.JTokenColumnFormat;
import net.thevpc.jeep.core.tokens.JTokenDef;

public interface JTokenFormat {
    JTokenFormat COLUMNS=new JTokenColumnFormat();
    JTokenFormat DEFAULT=new DefaultJTokenFormat();
    String format(JToken token) ;
    String format(JTokenDef token) ;
}
