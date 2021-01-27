package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JConverter;
import net.thevpc.jeep.util.JTypeUtils;

public class JNodeConverter extends JDefaultNode {

    private final JConverter currentConverter;
    private final JDefaultNode node;

    public JNodeConverter(JDefaultNode node, JConverter currentConverter) {
        super();
        this.currentConverter = currentConverter;
        this.node = node;
    }
    @Override
    public int id() {
        return JNodeDefaultIds.NODE_CONVERTER;
    }

//    @Override
//    public JType getType(JContext context) {
//        return currentConverter.targetType();
//    }


//    @Override
//    public Object evaluate(JContext context) {
//        return currentConverter.convert(node.evaluate(context), context);
//    }

    public JConverter getCurrentConverter() {
        return currentConverter;
    }

    public JDefaultNode getNode() {
        return node;
    }

    public String toString() {
        return "(" + JTypeUtils.str(currentConverter.targetType()) + ")" + node.toString()
                ;
    }

}
