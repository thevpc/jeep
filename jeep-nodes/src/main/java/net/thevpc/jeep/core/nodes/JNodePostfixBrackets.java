/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;


/**
 * @author thevpc
 */
public class JNodePostfixBrackets extends JNodeStatement {

    private JDefaultNode base;
    private JNodeBrackets items;

    public JNodePostfixBrackets(JDefaultNode base, JNodeBrackets items) {
        super();
        this.base = base;
        this.items = items;
    }

    public JDefaultNode getBase() {
        return base;
    }

    public void setBase(JDefaultNode base) {
        this.base = base;
    }

    public void setItems(JNodeBrackets items) {
        this.items = items;
    }

    public JNodeBrackets getItems() {
        return items;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_BRACKETS_POSTFIX;
    }

//    @Override
//    public JType getType(JContext context) {
//        throw new IllegalArgumentException("Unsupported");
//        //return items[0].getType(context).toArray();
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        context.debug("##EXEC [" + item + "]");
//        return context.functions().evaluate("[", item);
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(base==null){
            sb.append("?");
        }else{
            sb.append(base.toString());
        }
        sb.append("[");
        JDefaultNode[] items = this.items.getItems();
        for (int i = 0; i < items.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            JDefaultNode item = items[i];
            sb.append(item.toString());
        }
        sb.append("]");
        return sb.toString();
    }

}
