package net.thevpc.jeep.msg;

import java.text.MessageFormat;
import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class JFormattedMessage implements Message {
    private Level level;
    private String message;
    private Object[] args;

    public JFormattedMessage(Level level, String message, Object[] args) {
        this.level= level;
        this.message = message;
        this.args = args;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public String getText() {
        return MessageFormat.format(message ,args);
    }

    @Override
    public String toString() {
        return MessageFormat.format(message ,args);
    }
}
