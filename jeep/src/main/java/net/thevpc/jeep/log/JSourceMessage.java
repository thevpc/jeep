package net.thevpc.jeep.log;


import net.thevpc.jeep.msg.Message;
import net.thevpc.jeep.msg.Messages;
import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceRange;
import net.thevpc.jeep.source.JTextSourceToken;

import java.util.logging.Level;

public class JSourceMessage {

    private String id;
    private String group;
    private Message message;
    private JTextSourceToken token;

    public static JSourceMessage error(String id, String group, String message) {
        return new JSourceMessage(id, group, null, Messages.text(Level.SEVERE, message));
    }

    public static JSourceMessage warning(String id, String group, String message) {
        return new JSourceMessage(id, group, null, Messages.text(Level.WARNING, message));
    }

    public static JSourceMessage error(String id, String group, JTextSourceToken token, String message) {
        return new JSourceMessage(id, group, token, Messages.text(Level.SEVERE, message));
    }

    public static JSourceMessage info(String id, String group, JTextSourceToken token, String message) {
        return new JSourceMessage(id, group, token, Messages.text(Level.INFO, message));
    }

    public static JSourceMessage warning(String id, String group, JTextSourceToken token, String message) {
        return new JSourceMessage(id, group, token, Messages.text(Level.WARNING, message));
    }
    public static JSourceMessage jerror(String id, String group, String message, Object... params) {
        return new JSourceMessage(id, group, null, Messages.jmessage(Level.SEVERE, message,params));
    }

    public static JSourceMessage jwarning(String id, String group, String message, Object... params) {
        return new JSourceMessage(id, group, null, Messages.jmessage(Level.WARNING, message,params));
    }

    public static JSourceMessage jerror(String id, String group, JTextSourceToken token, String message, Object... params) {
        return new JSourceMessage(id, group, token, Messages.jmessage(Level.SEVERE, message,params));
    }

    public static JSourceMessage jinfo(String id, String group, JTextSourceToken token, String message, Object... params) {
        return new JSourceMessage(id, group, token, Messages.jmessage(Level.INFO, message,params));
    }

    public static JSourceMessage jwarning(String id, String group, JTextSourceToken token, String message, Object... params) {
        return new JSourceMessage(id, group, token, Messages.jmessage(Level.WARNING, message,params));
    }
    public static JSourceMessage cerror(String id, String group, String message, Object... params) {
        return new JSourceMessage(id, group, null, Messages.cmessage(Level.SEVERE, message,params));
    }

    public static JSourceMessage cwarning(String id, String group, String message, Object... params) {
        return new JSourceMessage(id, group, null, Messages.cmessage(Level.WARNING, message,params));
    }

    public static JSourceMessage cerror(String id, String group, JTextSourceToken token, String message, Object... params) {
        return new JSourceMessage(id, group, token, Messages.cmessage(Level.SEVERE, message,params));
    }

    public static JSourceMessage cinfo(String id, String group, JTextSourceToken token, String message, Object... params) {
        return new JSourceMessage(id, group, token, Messages.cmessage(Level.INFO, message,params));
    }

    public static JSourceMessage cwarning(String id, String group, JTextSourceToken token, String message, Object... params) {
        return new JSourceMessage(id, group, token, Messages.cmessage(Level.WARNING, message,params));
    }

    public JSourceMessage(String id, String group, JTextSourceToken token, Message message) {
        this.id = id;
        this.group = group;
        this.message = message;
        this.token = token == null ? null : token.copy();
    }

    public String getGroup() {
        return group;
    }

    public Level getLevel() {
        return message.getLevel();
    }

    public String getId() {
        return id;
    }

    public Message getMessage() {
        return message;
    }

    public JTextSourceToken getToken() {
        return token;
    }

    public JSourceMessage setId(String id) {
        this.id = id;
        return this;
    }

    public JSourceMessage setMessage(Message message) {
        this.message = message;
        return this;
    }

    public JSourceMessage setToken(JTextSourceToken token) {
        this.token = token;
        return this;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean showCompilationSource) {
        StringBuilder s = new StringBuilder();
        JTextSource compilationUnitSource0 = token == null ? null : token.getSource();
        String compilationUnitSource = compilationUnitSource0 == null ? "" : compilationUnitSource0.name();
        if (showCompilationSource) {
            s.append(String.format("%-10s ", compilationUnitSource));
        }
        if (token != null) {
            s.append(String.format("[%4s,%3s] ", (token.getStartLineNumber() + 1), (token.getStartColumnNumber() + 1)));
        } else {
            s.append("           ");
        }
        s.append(String.format("%-6s [%-5s] : %s",
                getLevel().intValue() == Level.SEVERE.intValue() ? "ERROR" : getLevel().toString(), id == null ? "" : id, message.getText()
        ));
        if (token != null && compilationUnitSource0 != null) {
            s.append(toRangeString(token, compilationUnitSource0, false));
        }
        return s.toString();
    }

    public static String toRangeString(JTextSourceToken token, JTextSource source, boolean includeSourceName) {
        StringBuilder s = new StringBuilder();
        if (token != null && source != null) {
            if (includeSourceName) {
                s.append(source.name()).append(":");
            }
            long cn = token.getStartCharacterNumber();
            int window = 80;
            JTextSourceRange range = source.range((int) cn - window, (int) cn + window);
            JTextSourceRange.JRangePointer windowString = range.trim(cn, window);
            s.append("\n   ").append(windowString.getText());
            s.append("\n   ");
            for (int i = 0; i < windowString.getOffset(); i++) {
                s.append(" ");
            }
            s.append("^^^ [Line:").append(token.getStartLineNumber() + 1).append(",Column:").append(token.getStartColumnNumber() + 1).append("]");
        }
        return s.toString();
    }
}
