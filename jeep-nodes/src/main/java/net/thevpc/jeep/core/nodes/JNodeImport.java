/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JType;

/**
 *
 * @author thevpc
 */
public class JNodeImport extends JNodeStatement {

    private final String name;
    private JType type;
    private final JDefaultNode value;

    public JNodeImport(String name, JDefaultNode val) {
        super();
        this.name = name;
        this.value = val;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_IMPORT;
    }

    public String getName() {
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
//        Object result = context.evaluate(value);
//        context.vars().declareVar(name,type, result);
//        return result;
//    }

    @Override
    public String toString() {
        return name + "=" + value;
    }


}
