package net.thevpc.jeep;

public class JShouldNeverHappenException extends RuntimeException{
    public JShouldNeverHappenException() {
        super("Should Never Happen...");
    }

    public JShouldNeverHappenException(String message) {
        super("Should Never Happen... : "+message);
    }

    public JShouldNeverHappenException(Throwable cause) {
        super("Should Never Happen...",cause);
    }
}
