package net.thevpc.jeep;


import net.thevpc.common.textsource.log.impl.DefaultJTextSourceLog;

import java.io.PrintStream;

public class DefaultJCompilerLog extends DefaultJTextSourceLog implements JCompilerLog {

    public DefaultJCompilerLog() {
        super("Compilation");
    }

    public DefaultJCompilerLog(PrintStream out) {
        super("Compilation", out);
    }

}
