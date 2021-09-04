package net.thevpc.jeep;

import net.thevpc.jeep.log.impl.DefaultJTextSourceLog;

public class DefaultJCompilerLog extends DefaultJTextSourceLog implements JCompilerLog {

    public DefaultJCompilerLog() {
        super("compilation");
    }

    @Override
    public String getErrorMessage() {
        return isSuccessful() ? null : getFooterMessage();
    }

}
