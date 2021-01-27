package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenMatcher;
import net.thevpc.jeep.JTokenizerReader;

public abstract class AbstractJTokenMatcher implements JTokenMatcher {

    protected StringBuilder image = new StringBuilder();
    protected JTokenPatternOrder order;

    public AbstractJTokenMatcher(JTokenPatternOrder order) {
        this.order = order;
    }

    @Override
    public JTokenPatternOrder order() {
        return order;
    }

    @Override
    public JTokenMatcher reset() {
        image.delete(0, image.length());
        return this;
    }

//    public abstract boolean peek(char c);
    @Override
    public boolean matches(String c) {
        for (char c1 : c.toCharArray()) {
            if (!matches(c1)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String image() {
        return image.toString();
    }

    public boolean matches(JTokenizerReader reader, JToken outputToken) {
        StringBuilder unread = new StringBuilder();
        int length = image().length();
        while (true) {
            int cc = reader.read();
            if (cc == -1) {
                boolean valid = valid();
                if (valid) {
                    fillToken(outputToken, reader);
                    return length > 0;
                } else {
                    if (unread.length() > 0) {
                        reader.unread(unread.toString().toCharArray());
                    }
                }
                return false;
            } else if (matches((char) cc)) {
                unread.append((char) cc);
                length++;
                //
            } else {
                if (length > 0) {
                    boolean valid = valid();
                    if (valid) {
                        reader.unread((char) cc);
                        fillToken(outputToken, reader);
                        return true;
                    } else {
                        unread.append((char) cc);
                        reader.unread(unread.toString().toCharArray());
                        return false;
                    }
                } else {
                    reader.unread((char) cc);
                    return false;
                }
            }
        }

    }

    protected abstract void fillToken(JToken token, JTokenizerReader reader);

    @Override
    public String toString() {
        return "Matcher for "+pattern().toString();
    }
}
