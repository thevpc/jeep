/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.jeep;

/**
 * @author thevpc
 */
public class JEvalException extends JeepException {

    public JEvalException(Throwable cause) {
        super(cause);
    }

    public JEvalException(String message, Throwable cause) {
        super(message, cause);
    }

    public JEvalException(String message) {
        super(message);
    }

    public JEvalException() {
    }

}
