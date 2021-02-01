package net.thevpc.jeep;

import net.thevpc.common.textsource.log.impl.DefaultJTextSourceLog;


public class DefaultJCompilerLog extends DefaultJTextSourceLog implements JCompilerLog {

    public DefaultJCompilerLog() {
        super("compilation");
    }

    @Override
    public String getErrorMssage() {
        return isSuccessful() ? null : getFooterMessage();
    }

}
