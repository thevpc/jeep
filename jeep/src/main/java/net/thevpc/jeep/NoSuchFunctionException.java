/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.jeep;

/**
 * @author thevpc
 */
public class NoSuchFunctionException extends JEvalException {
    private String name;

    public NoSuchFunctionException(String name) {
        super("JFunction " + name + " not found");
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
