package net.thevpc.jeep.log.impl;

import net.thevpc.jeep.log.JMessageList;
import net.thevpc.jeep.log.JSourceMessage;
import net.thevpc.jeep.source.JTextSourceToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultJMessageList implements JMessageList {
    private List<JSourceMessage> messages = new ArrayList<>();

    @Override
    public void info(String id, String group, JTextSourceToken token, String message) {
        add(JSourceMessage.info(id, group, token, message));
    }

    @Override
    public void error(String id, String group, JTextSourceToken token, String message) {
        add(JSourceMessage.error(id, group, token, message));
    }

    @Override
    public void warn(String id, String group, JTextSourceToken token, String message) {
        add(JSourceMessage.warning(id, group, token, message));
    }

    @Override
    public void cinfo(String id, String group, JTextSourceToken token, String message,Object ...params) {
        add(JSourceMessage.cinfo(id, group, token, message,params));
    }

    @Override
    public void cerror(String id, String group, JTextSourceToken token, String message,Object ...params) {
        add(JSourceMessage.cerror(id, group, token, message,params));
    }

    @Override
    public void cwarn(String id, String group, JTextSourceToken token, String message,Object ...params) {
        add(JSourceMessage.cwarning(id, group, token, message,params));
    }

    @Override
    public void jinfo(String id, String group, JTextSourceToken token, String message,Object ...params) {
        add(JSourceMessage.jinfo(id, group, token, message,params));
    }

    @Override
    public void jerror(String id, String group, JTextSourceToken token, String message,Object ...params) {
        add(JSourceMessage.jerror(id, group, token, message,params));
    }

    @Override
    public void jwarn(String id, String group, JTextSourceToken token, String message,Object ...params) {
        add(JSourceMessage.jwarning(id, group, token, message,params));
    }

    @Override
    public void add(JSourceMessage message) {
        this.messages.add(message);
    }

    @Override
    public void addAll(Iterable<JSourceMessage> messages) {
        if (messages != null) {
            for (JSourceMessage message : messages) {
                add(message);
            }
        }
    }

    @Override
    public JMessageList clear() {
        this.messages.clear();
        return this;
    }

    @Override
    public Iterator<JSourceMessage> iterator() {
        return messages.iterator();
    }
}
