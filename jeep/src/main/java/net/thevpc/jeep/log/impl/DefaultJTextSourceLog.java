package net.thevpc.jeep.log.impl;

import net.thevpc.jeep.log.JSourceMessage;
import net.thevpc.jeep.log.JTextSourceLog;
import net.thevpc.jeep.source.JTextSourceToken;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;

public class DefaultJTextSourceLog implements JTextSourceLog, Cloneable {

    public static final String DEFAULT_OPERATION_NAME = "Operation";
    private int maxMessages = 1024;
    private String operationName;
    private List<JSourceMessage> messages = new ArrayList<>();
    private Set<ErrorKey> visitedMessages = new LinkedHashSet<>();
    private int errorsCount = 0;
    private int warningsCount = 0;
    private int infosCount = 0;

    public DefaultJTextSourceLog(String operationName) {
        this.operationName = operationName == null ? DEFAULT_OPERATION_NAME : operationName;
    }

    public String getOperationName() {
        return operationName;
    }

    protected DefaultJTextSourceLog setOperationName(String operationName) {
        this.operationName = operationName == null ? DEFAULT_OPERATION_NAME : operationName;
        return this;
    }

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
    public void cinfo(String id, String group, JTextSourceToken token, String message, Object... params) {
        add(JSourceMessage.cinfo(id, group, token, message, params));
    }

    @Override
    public void cerror(String id, String group, JTextSourceToken token, String message, Object... params) {
        add(JSourceMessage.cerror(id, group, token, message, params));
    }

    @Override
    public void cwarn(String id, String group, JTextSourceToken token, String message, Object... params) {
        add(JSourceMessage.cwarning(id, group, token, message, params));
    }

    @Override
    public void jinfo(String id, String group, JTextSourceToken token, String message, Object... params) {
        add(JSourceMessage.jinfo(id, group, token, message, params));
    }

    @Override
    public void jerror(String id, String group, JTextSourceToken token, String message, Object... params) {
        add(JSourceMessage.jerror(id, group, token, message, params));
    }

    @Override
    public void jwarn(String id, String group, JTextSourceToken token, String message, Object... params) {
        add(JSourceMessage.jwarning(id, group, token, message, params));
    }

    @Override
    public void add(JSourceMessage message) {
        if (messages.size() < maxMessages) {
            ErrorKey k = new ErrorKey(message);
            if (!messages.isEmpty()) {
                if (visitedMessages.contains(k)) {
                    return;
                }
            }
            visitedMessages.add(k);
            this.messages.add(message);
            final Level lvl = message.getLevel();
            if (lvl.intValue() >= Level.SEVERE.intValue()) {
                errorsCount++;
            } else if (lvl.intValue() >= Level.WARNING.intValue()) {
                warningsCount++;
            } else if (lvl.intValue() >= Level.INFO.intValue()) {
                infosCount++;
            }
            printlnMessage(message);
        }
    }

    @Override
    public int getErrorCountAtLine(int line) {
        int errors = 0;
        for (JSourceMessage message : messages) {
            if (message.getLevel().intValue() >= Level.SEVERE.intValue()) {
                JTextSourceToken token = message.getToken();
                int s0 = token.getStartLineNumber();
                int s1 = token.getEndLineNumber();
                if (s0 <= line && s1 > line + 1) {
                    errors++;
                }
            }
        }
        return errors;
    }

    @Override
    public int getErrorCount() {
        return errorsCount;
    }

    @Override
    public int getWarningCount() {
        return warningsCount;
    }

    @Override
    public JTextSourceLog clear() {
        messages.clear();
        errorsCount = 0;
        warningsCount = 0;
        infosCount = 0;
        return this;
    }

    @Override
    public JTextSourceLog copy() {
        DefaultJTextSourceLog c = null;
        try {
            c = (DefaultJTextSourceLog) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException(e);
        }
        c.messages = new ArrayList<>();
        for (JSourceMessage message : messages) {
            c.add(message);
        }
        return c;
    }

    @Override
    public boolean isSuccessful() {
        return getErrorCount() == 0;
    }

    @Override
    public JSourceMessage[] toArray() {
        return messages.toArray(new JSourceMessage[0]);
    }

    @Override
    public List<JSourceMessage> toList() {
        return new ArrayList<>(messages);
    }

    @Override
    public int size() {
        return messages.size();
    }

    @Override
    public Iterator<JSourceMessage> iterator() {
        return messages.iterator();
    }

    public void printlnMessage(JSourceMessage jSourceMessage) {
        System.err.println(jSourceMessage);
    }

    @Override
    public void print() {
        List<JSourceMessage> messages2 = new ArrayList<JSourceMessage>(messages);
        messages2.sort(new Comparator<JSourceMessage>() {
            @Override
            public int compare(JSourceMessage o1, JSourceMessage o2) {
                int c = Integer.compare(o2.getLevel().intValue(), o1.getLevel().intValue());
                if (c != 0) {
                    return c;
                }
                return 0;
            }
        });
        for (JSourceMessage jSourceMessage : messages2) {
            printlnMessage(jSourceMessage);
        }
        printFooter();
    }

    public String getFooterMessage() {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(b);
        String op = operationName;
        if (op == null) {
            op = DEFAULT_OPERATION_NAME;
        }
        final int errors = getErrorCount();
        final int warnings = getWarningCount();
        if (errors == 0 && warnings == 0) {
            out.printf("%s successful", op);
        } else if (errors == 0) {
            out.printf("%s successful with %d warning%s.", op, warnings, warnings > 1 ? "s" : "");
        } else if (warnings == 0) {
            out.printf("%s failed with %d error%s.", op, errors, errors > 1 ? "s" : "");
        } else {
            out.printf("%s failed with %d error%s and %d warning%s.", op, errors, errors > 1 ? "s" : "", warnings, warnings > 1 ? "s" : "");
        }
        out.flush();
        return b.toString();
    }

    @Override
    public void printFooter() {
        System.err.println("-----------------------------------------------------------------------------------");
        System.err.println(getFooterMessage());
    }

    @Override
    public void addAll(Iterable<JSourceMessage> messages) {
        if (messages != null) {
            for (JSourceMessage message : messages) {
                add(message);
            }
        }
    }

}
