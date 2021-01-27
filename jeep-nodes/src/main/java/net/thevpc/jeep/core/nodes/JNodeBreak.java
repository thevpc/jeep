/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

/**
 * @author thevpc
 */
public class JNodeBreak extends JNodeStatement {

    private final String name;

    public JNodeBreak(String name) {
        super();
        this.name = name;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_BREAK;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.types().forName(Void.class);
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        throw new JBreakException(name);
//    }

    public String toString() {
        return "break" + (name == null ? "" : (" " + name));
    }

}
