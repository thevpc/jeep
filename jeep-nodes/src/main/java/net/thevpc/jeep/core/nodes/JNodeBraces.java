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
public class JNodeBraces extends JNodeStatement {

    private JDefaultNode[] items;

    public JNodeBraces(JDefaultNode[] items) {
        super();
        this.items = items;
    }

    public JDefaultNode[] getItems() {
        return items;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_BRACES;
    }
//    @Override
//    public JType getType(JContext context) {
//        return item.getType(context);
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        context.debug("##EXEC {" + item + "}");
//        return context.functions().evaluate("{", item);
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("{\n");
        for (int i = 0; i < items.length; i++) {
            if(i>0){
                sb.append(",");
            }
            JDefaultNode item = items[i];
            sb.append(item);
        }
        sb.append("\t}");
        return sb.toString();
    }

}
