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
public class JNodeEnd extends JNodeStatement {

    public static final JNodeEnd INSTANCE = new JNodeEnd();

    public JNodeEnd() {
        super();
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_END;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.types().forName(Void.class);
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        return null;
//    }

    public String toString() {
        return "end";
    }


}
