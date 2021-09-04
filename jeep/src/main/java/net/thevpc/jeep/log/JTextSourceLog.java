package net.thevpc.jeep.log;

import java.util.List;

public interface JTextSourceLog extends JMessageList {
    int getErrorCountAtLine(int line);

    int getErrorCount();

    int getWarningCount();

    JTextSourceLog clear();

    JTextSourceLog copy();

    boolean isSuccessful();

    JSourceMessage[] toArray();

    List<JSourceMessage> toList();

    int size();

    void print();

    void printFooter();
}
