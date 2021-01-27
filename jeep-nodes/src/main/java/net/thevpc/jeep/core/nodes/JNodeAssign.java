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
public class JNodeAssign extends JNodeStatement {

    private JDefaultNode name;
    private JDefaultNode value;

    public JNodeAssign(JDefaultNode name, JDefaultNode val) {
        super();
        this.name = name;
        this.value = val;
    }

    public JNodeAssign setName(JDefaultNode name) {
        this.name = name;
        return this;
    }

    public JNodeAssign setValue(JDefaultNode value) {
        this.value = value;
        return this;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_ASSIGN;
    }

    public JDefaultNode getName() {
        return name;
    }

    public JDefaultNode getValue() {
        return value;
    }

//    @Override
//    public JType getType(JContext context) {
//        return value.getType(context);
//    }


//    @Override
//    public Object evaluate(JContext context) {
//        context.debug("##EXEC ASSIGN "+name);
//        Object v = context.evaluate(value);
//        context.vars().setValue(name, v);
//        return v;
//    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

}
