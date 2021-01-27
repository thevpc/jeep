package net.thevpc.jeep.core.eval;

import net.thevpc.jeep.JEvaluable;
import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JNode;
import net.thevpc.jeep.JType;

public class JEvaluableNode implements JEvaluable {
    private final JNode node;
    private final JType type;

    public JEvaluableNode(JNode node,JType type) {
        this.node = node;
        this.type = type;
    }

    @Override
    public JType type() {
        return type;
    }

    public JNode getNode() {
        return node;
    }

    @Override
    public Object evaluate(JInvokeContext context) {
        return context.getEvaluator().evaluate(node,context);
    }
}
