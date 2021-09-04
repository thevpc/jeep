package net.thevpc.jeep.msg;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class ExceptionMessage implements Message {

    private Level level;
    private String message;
    private Throwable error;

    public ExceptionMessage(Level level, String message, Throwable error) {
        if(level==null){
            level=Level.SEVERE;
        }
        this.error=error;
        if (message == null) {
            if (error != null) {
                message = error.getMessage();
                if (message == null || message.length() < 3) {
                    message = error.toString();
                }
            }
        }
        this.message = message;
        this.level = level;
    }

    public Throwable getError() {
        return error;
    }
    

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public String getText() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
