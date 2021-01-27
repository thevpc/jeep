/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

/**
 * @author thevpc
 */
public class JNodeWord extends JNodeStatement {

    private final String name;

    public JNodeWord(String name) {
        super();
        this.name = name;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_SYNTACTIC;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.types().forName(Object.class);
//    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }


}
