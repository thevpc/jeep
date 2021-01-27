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
        clog.error(id,group,message,null);
    }

    @Override
    public void reportWarning(String id, String group, String message) {
        clog.warn(id,group,message,null);
    }
}
