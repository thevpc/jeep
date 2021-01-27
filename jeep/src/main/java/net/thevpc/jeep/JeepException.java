package net.thevpc.jeep;

public class JeepException extends RuntimeException{
    public JeepException() {
    }

    public JeepException(String message) {
        super(message);
    }

    public JeepException(String message, Throwable cause) {
        super(message, cause);
    }

    public JeepException(Throwable cause) {
        super(cause);
    }

    public JeepException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
