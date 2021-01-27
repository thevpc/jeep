package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.util.JTokenUtils;
import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenPattern;
import net.thevpc.jeep.JTokenizerState;
import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.core.tokens.JTokenPatternOrder;

public abstract class AbstractTokenPattern implements JTokenPattern {

    private JTokenPatternOrder order;
    private final String name;
    private String stateName;
    private int stateId;

    public AbstractTokenPattern(JTokenPatternOrder order, String name) {
        this.order = order;
        this.name = name;
    }

    public void bindToState(JTokenizerState state) {
        this.stateId = state.getId();
        this.stateName = state.getName();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "TokenPattern[" + name + "]{"
                + "order=" + order
                + '}';
    }

    protected void fill(JTokenDef from, JToken to) {
        JTokenUtils.fillToken(from, to);
    }

    @Override
    public JTokenPatternOrder order() {
        return order;
    }

    @Override
    public String dump() {
        return toString();
    }
    
}
