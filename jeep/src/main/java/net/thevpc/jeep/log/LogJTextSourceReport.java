package net.thevpc.jeep.log;

import net.thevpc.jeep.JCompilerLog;
import net.thevpc.common.textsource.JTextSourceReport;

public class LogJTextSourceReport implements JTextSourceReport {
    private JCompilerLog clog;

    public LogJTextSourceReport(JCompilerLog clog) {
        this.clog = clog;
    }

    @Override
    public void reportError(String id, String group, String message) {
        clog.error(id,group,null, message);
    }

    @Override
    public void reportWarning(String id, String group, String message) {
        clog.warn(id,group,null, message);
    }
}
