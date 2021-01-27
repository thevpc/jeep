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
public class JNodeArrayCall extends JDefaultNode {

    private final JDefaultNode base;
    private final JNodeArray arrayIndex;

    public JNodeArrayCall(JDefaultNode base, JNodeArray arrayIndex) {
        super();
        this.base = base;
        this.arrayIndex = arrayIndex;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_ARRAY_CALL;
    }

    public JDefaultNode getBase() {
        return base;
    }

    public JNodeArray getArrayIndex() {
        return arrayIndex;
    }

//    @Override
//    public JType getType(JContext context) {
//        return arrayIndex.getType(context);
//    }

    @Override
    public String toString() {
        return base.toString()+ arrayIndex;
    }

}
