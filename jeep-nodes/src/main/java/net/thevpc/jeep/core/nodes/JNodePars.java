/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

/**
 *
 * @author thevpc
 */
public class JNodePars extends JNodeStatement {

    private JDefaultNode[] items;

    public JNodePars(JDefaultNode[] items) {
        super();
        this.items = items;
    }

    public JDefaultNode[] getItems() {
        return items;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_PARS;
    }

//    @Override
//    public JType getType(JContext context) {
//        return JeepPlatformUtils.forObject(context.types());
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        Object o = null;
//        context.debug("##EXEC (" + item + ")");
//        o = item.evaluate(context);
//        return o;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("(");
        for (int i = 0; i < items.length; i++) {
            JDefaultNode item = items[i];
            if(i>0) {
                sb.append(",");
            }
            sb.append(item.toString());
        }
        sb.append(")");
        return sb.toString();
    }

}
