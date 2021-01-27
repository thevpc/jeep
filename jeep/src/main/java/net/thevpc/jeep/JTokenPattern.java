package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.core.tokens.JTokenPatternOrder;

public interface JTokenPattern {

    JTokenPatternOrder order();

    String name();

    JTokenMatcher matcher();

    JTokenDef[] tokenDefinitions();
    
    String dump();
}
