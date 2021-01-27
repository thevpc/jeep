/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JContext;
import net.thevpc.jeep.util.JeepUtils;

/**
 *
 * @author thevpc
 */
public class JNodeIf extends JNodeStatement {

    private JDefaultNode condition;
    private JDefaultNode trueBlock;
    private JDefaultNode falseBlock;

    public JNodeIf() {
        super();
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_IF;
    }

    public JDefaultNode getCondition() {
        return condition;
    }

    public void setCondition(JDefaultNode condition) {
        this.condition = condition;
    }

    public JDefaultNode getTrueBlock() {
        return trueBlock;
    }

    public void setTrueBlock(JDefaultNode trueBlock) {
        this.trueBlock = trueBlock;
    }

    public JDefaultNode getFalseBlock() {
        return falseBlock;
    }

    public void setFalseBlock(JDefaultNode falseBlock) {
        this.falseBlock = falseBlock;
    }

    public static boolean evalBoolean(JDefaultNode condition, JContext context,Object extra) {
        boolean ok = false;
        if (condition == null) {
            ok = true;
        } else {
            Object u = context.evaluate(condition, extra);
            if (u instanceof JNodeLiteral) {
                u = ((JNodeLiteral) u).getValue();
            }
            if (u instanceof Boolean) {
                ok = ((Boolean) u).booleanValue();
            }
        }
        return ok;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("if ");
        sb.append(condition).append(JeepUtils.NEWLINE);
        sb.append(trueBlock.toString()).append(JeepUtils.NEWLINE);
        if (falseBlock != null) {
            sb.append("else").append(JeepUtils.NEWLINE);
            sb.append(falseBlock.toString()).append(JeepUtils.NEWLINE);
        }
        sb.append("end");
        return sb.toString();
    }


}
