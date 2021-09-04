package net.thevpc.jeep.msg;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class StringMessage implements Message {

    private Level level;
    private String message;

    public StringMessage(Level level, String message) {

        this.message = message;
        this.level = level;
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
