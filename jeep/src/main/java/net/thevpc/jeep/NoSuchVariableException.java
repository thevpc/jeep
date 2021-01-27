/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.jeep;

/**
 * @author thevpc
 */
public class NoSuchVariableException extends JEvalException {
    private String varName;

    public NoSuchVariableException(String varName) {
        super("JVar " + varName + " not found");
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }


}
