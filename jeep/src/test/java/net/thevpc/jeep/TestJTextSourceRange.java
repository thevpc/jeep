package net.thevpc.jeep;

import net.thevpc.jeep.log.JSourceMessage;
import net.thevpc.jeep.source.JTextSourceFactory;
import org.junit.jupiter.api.Test;

public class TestJTextSourceRange {

    @Test
    public void test1() {
        JToken t = new JToken();
        String text = "\nexample\nmessage\n";
        t.source = JTextSourceFactory.fromString(text, null);
        t.startCharacterNumber = text.length();
        t.endCharacterNumber = 4;
        JSourceMessage m = JSourceMessage.error("0", null, t, "error");
        System.out.println(m);
    }
}
