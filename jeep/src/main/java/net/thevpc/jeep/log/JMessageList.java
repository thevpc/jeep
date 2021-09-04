package net.thevpc.jeep.log;


import net.thevpc.jeep.source.JTextSourceToken;

public interface JMessageList extends Iterable<JSourceMessage>{
    void info(String id, String group, JTextSourceToken token, String message);

    void error(String id, String group, JTextSourceToken token, String message);

    void warn(String id, String group, JTextSourceToken token, String message);

    void add(JSourceMessage message);

    void addAll(Iterable<JSourceMessage> messages);

    JMessageList clear();

    void cerror(String id, String group, JTextSourceToken token, String message, Object... params);

    void cinfo(String id, String group, JTextSourceToken token, String message, Object... params);

    void jerror(String id, String group, JTextSourceToken token, String message, Object... params);

    void jinfo(String id, String group, JTextSourceToken token, String message, Object... params);

    void jwarn(String id, String group, JTextSourceToken token, String message, Object... params);

    void cwarn(String id, String group, JTextSourceToken token, String message, Object... params);

}
