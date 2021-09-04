package net.thevpc.jeep;


import net.thevpc.jeep.log.JTextSourceLog;

public interface JCompilerLog extends JTextSourceLog {
    String getErrorMessage();
}
