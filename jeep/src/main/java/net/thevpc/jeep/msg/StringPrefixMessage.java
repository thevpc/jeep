package net.thevpc.jeep.msg;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class StringPrefixMessage implements Message {
    private String prefix;
    private Message message;

    public StringPrefixMessage(String prefix, Message message) {

        this.message = message;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public Level getLevel() {
        return message.getLevel();
    }

    @Override
    public String getText() {
        return prefix+message.getText();
    }

    public String toString(){
        return getText();
    }
}
