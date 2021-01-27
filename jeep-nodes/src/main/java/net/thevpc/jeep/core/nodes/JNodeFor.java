/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.util.JeepUtils;

/**
 * @author thevpc
 */
public class JNodeFor extends JNodeStatement {

    private String name;
    private JDefaultNode from;
    private JDefaultNode to;
    private JDefaultNode by;
    private JDefaultNode block;

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_FOR;
    }

    public JNodeFor() {
        super();
    }

    public JDefaultNode getBlock() {
        return block;
    }

    public void setBlock(JDefaultNode block) {
        this.block = block;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JDefaultNode getFrom() {
        return from;
    }

    public void setFrom(JDefaultNode from) {
        this.from = from;
    }

    public JDefaultNode getTo() {
        return to;
    }

    public void setTo(JDefaultNode to) {
        this.to = to;
    }

    public JDefaultNode getBy() {
        return by;
    }

    public void setBy(JDefaultNode by) {
        this.by = by;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.types().forName(Void.class);
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        context.debug("##EXEC FOR");
//        context.vars().setValue(name, from.evaluate(context));
//        Object o = null;
//        while (true) {
//            JNodeVarName index = context.vars().getName(name);
//            boolean ok = JeepUtils.convertToBoolean(context.functions().evaluate("<=", index, to));
//            context.debug("##EXEC FOR condition=" + ok);
//            if (!ok) {
//                break;
//            }
//            if (block != null) {
//                block.evaluate(context);
//            }
//            JNode by2 = by;
//            if (by2 == null) {
//                Object v = JeepUtils.getIncDefaultValue(o);
//                JType def=context.types().isNull(v)?context.types().forName(Object.class):context.types().typeOf(v);
//                by2 = new JNodeLiteral(v,def);
//            }
//            context.debug("##EXEC FOR, INC " + index);
//            context.vars().setValue(name, context.functions().evaluate("+", index, by2));
//        }
//        return o;
//    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append("for ").append(name).append("=").append(from);
        sb.append(" to ").append(to);
        if (by != null) {
            sb.append(" by ").append(by);
        }
        sb.append("\n");
        sb.append(JeepUtils.indent(block.toString())).append(JeepUtils.NEWLINE);
        sb.append("end");
        return sb.toString();
    }


}
