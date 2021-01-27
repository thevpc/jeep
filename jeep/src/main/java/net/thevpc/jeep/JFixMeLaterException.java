package net.thevpc.jeep;

public class JFixMeLaterException extends RuntimeException{
    public JFixMeLaterException() {
        super("FIX ME LATER");
    }

    public JFixMeLaterException(String message) {
        super("FIX ME LATER: "+message);
    }
}
