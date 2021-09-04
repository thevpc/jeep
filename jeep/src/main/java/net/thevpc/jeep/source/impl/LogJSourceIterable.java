package net.thevpc.jeep.source.impl;

import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceReport;

public abstract class LogJSourceIterable implements Iterable<JTextSource>{
    protected JTextSourceReport log;

    public LogJSourceIterable(JTextSourceReport log) {
        this.log = log;
    }
}
