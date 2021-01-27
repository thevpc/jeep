package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.AbstractTokenPattern;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.*;

public class StringPattern extends AbstractTokenPattern {
    public static final JTokenDef INFO_DOUBLE_QUOTES = new JTokenDef(
            JTokenId.DOUBLE_QUOTES,
            "DOUBLE_QUOTES",
            JTokenType.TT_STRING,
            "TT_STRING",
            "\"..\""
    );
    public static final JTokenDef INFO_SIMPLE_QUOTES = new JTokenDef(
            JTokenId.SIMPLE_QUOTES,
            "SIMPLE_QUOTES",
            JTokenType.TT_STRING,
            "TT_STRING",
            "'..'"
    );
    public static final JTokenDef INFO_ANTI_QUOTES = new JTokenDef(
            JTokenId.ANTI_QUOTES,
            "ANTI_QUOTES",
            JTokenType.TT_STRING,
            "TT_STRING",
            "`..`"
    );
    private final String start;
    private final String end;
    private final char escape;
    private final boolean cstyleEscape;
    private final boolean shortType;
    private JTokenDef info;
    public StringPattern(JTokenDef info, String start, String end) {
        this(info, start, end, '\\', true);
    }
    public StringPattern(JTokenDef info, String start, String end, char escape, boolean cstyleEscape) {
        super(JTokenPatternOrder.ORDER_STRING, info.idName);
        this.info = info;
        this.start = start;
        this.end = end;
        this.escape = escape;
        this.cstyleEscape = cstyleEscape;
        this.shortType = start.length() == 1 && end.equals(start);
        if (start.length() == 0 || end.length() == 0) {
            throw new JParseException("Illegal empty Start/End pattern.");
        }
    }
    public StringPattern(JTokenDef info, char quote, char escape, boolean cstyleEscape) {
        super(JTokenPatternOrder.ORDER_STRING, info.idName);
        this.info = info;
        this.start = String.valueOf(quote);
        this.end = start;
        this.escape = escape;
        this.cstyleEscape = cstyleEscape;
        this.shortType = true;
    }

    public static StringPattern DOUBLE_QUOTES() {
        return new StringPattern(
                INFO_DOUBLE_QUOTES, '\"', '\\', true
        );
    }

    public static StringPattern SIMPLE_QUOTES() {
        return new StringPattern(
                INFO_SIMPLE_QUOTES, '\'', '\\', true
        );
    }

    public static StringPattern ANTI_QUOTES() {
        return new StringPattern(
                INFO_SIMPLE_QUOTES, '`', '\\', true
        );
    }

    @Override
    public void bindToState(JTokenizerState state) {
        super.bindToState(state);
        info = info.bindToState(state);
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return new JTokenDef[]{info};
    }

    @Override
    public JTokenMatcher matcher() {
        if (shortType) {
            return new StringJMatcherShort(order(), start.charAt(0), escape, cstyleEscape);
        } else {
            return new StringJMatcherLong(order(), start, end, escape, cstyleEscape);
        }
    }

    public class StringJMatcherShort extends AbstractJTokenMatcher {

        final int INIT = 0;
        final int CONTENT = 1;
        final int ESCAPED = 2;
        final int END = 3;
        private final char quote;
        private final char escape;
        private final boolean cstyleEscape;
        int status = INIT;
        private StringBuilder value = new StringBuilder();

        public StringJMatcherShort(JTokenPatternOrder order, char quote, char escape, boolean cstyleEscape) {
            super(order);
            this.quote = quote;
            this.escape = escape;
            this.cstyleEscape = cstyleEscape;
        }

        @Override
        public JTokenPattern pattern() {
            return StringPattern.this;
        }

        @Override
        public JTokenMatcher reset() {
            status = INIT;
            value.delete(0, value.length());
            return super.reset();
        }

        @Override
        protected void fillToken(JToken token, JTokenizerReader reader) {
            fill(info, token);
            token.image = image();
            token.sval = value.toString();
            if (status != END) {
                token.setError(1,"EOF");
            }
        }

        @Override
        public boolean matches(char c) {
            switch (status) {
                case INIT: {
                    if (c == quote) {
                        image.append(c);
                        status = CONTENT;
                        return true;
                    }
                    return false;
                }
                case CONTENT: {
                    if (c == escape) {
                        status = ESCAPED;
                        image.append(c);
                    } else if (c == quote) {
                        image.append(c);
                        status = END;
                        return true;
                    } else {
                        image.append(c);
                        value.append(c);
                    }
                    return true;
                }
                case ESCAPED: {
                    if (c == quote) {
                        image.append(c);
                        value.append(c);
                        status = CONTENT;
                        return true;
                    } else {
                        boolean processed = false;
                        if (cstyleEscape) {
                            switch (c) {
                                case 'n': {
                                    image.append(c);
                                    value.append('\n');
                                    status = CONTENT;
                                    processed = true;
                                    break;
                                }
                                case 't': {
                                    image.append(c);
                                    value.append('\t');
                                    status = CONTENT;
                                    processed = true;
                                    break;
                                }
                                case 'f': {
                                    image.append(c);
                                    value.append('\f');
                                    status = CONTENT;
                                    processed = true;
                                    break;
                                }
                            }
                        }
                        if (!processed) {
                            image.append(c);
                            value.append(c);
                            status = CONTENT;
                            processed = true;
                        }
                        return true;
                    }
                }
                case END: {
                    return false;
                }
            }
            throw new JParseException("Unsupported");
        }

        @Override
        public Object value() {
            return value.toString();
        }

        @Override
        public boolean valid() {
            return (status >= CONTENT);
        }
    }

    public class StringJMatcherLong extends AbstractJTokenMatcher {

        final int READ_START = 0;
        final int CONTENT = 2;
        final int ESCAPED = 3;
        final int READ_END = 4;
        final int COMPLETED = 5;
        final int ERROR = -1;
        private final String start;
        private final String end;
        private final char escape;
        private final boolean cstyleEscape;
        int status = READ_START;
        int counter = 0;
        private StringBuilder value = new StringBuilder();

        public StringJMatcherLong(JTokenPatternOrder order, String start, String end, char escape, boolean cstyleEscape) {
            super(order);
            this.start = start;
            this.end = end;
            this.escape = escape;
            this.cstyleEscape = cstyleEscape;
        }

        @Override
        public JTokenPattern pattern() {
            return StringPattern.this;
        }

        @Override
        public JTokenMatcher reset() {
            status = READ_START;
            counter = 0;
            value.delete(0, value.length());
            return super.reset();
        }

        @Override
        protected void fillToken(JToken token, JTokenizerReader reader) {
            fill(info, token);
            token.image = image();
            token.sval = value.toString();
            if (status != COMPLETED) {
                token.setError(1,"EOF");
            }
        }

        @Override
        public boolean matches(char c) {
            switch (status) {
                case READ_START: {
                    if (c == start.charAt(counter)) {
                        counter++;
                        image.append(c);
                        if (counter >= start.length()) {
                            counter = 0;
                            status = CONTENT;
                        }
                        return true;
                    } else {
                        status = ERROR;
                        return false;
                    }
                }
                case CONTENT: {
                    if (c == escape) {
                        status = ESCAPED;
                        image.append(c);
                    } else if (c == end.charAt(0)) {
                        image.append(c);
                        if (end.length() == 1) {
                            status = COMPLETED;
                        } else {
                            counter++;
                            status = READ_END;
                        }
                        return true;
                    } else {
                        image.append(c);
                        value.append(c);
                    }
                    return true;
                }
                case ESCAPED: {
                    if (c == end.charAt(0)) {
                        image.append(c);
                        value.append(c);
                        status = CONTENT;
                        return true;
                    } else {
                        boolean processed = false;
                        if (cstyleEscape) {
                            switch (c) {
                                case 'n': {
                                    image.append(c);
                                    value.append('\n');
                                    status = CONTENT;
                                    processed = true;
                                    break;
                                }
                                case 't': {
                                    image.append(c);
                                    value.append('\t');
                                    status = CONTENT;
                                    processed = true;
                                    break;
                                }
                                case 'f': {
                                    image.append(c);
                                    value.append('\f');
                                    status = CONTENT;
                                    processed = true;
                                    break;
                                }
                            }
                        }
                        if (!processed) {
                            image.append(c);
                            value.append(c);
                            status = CONTENT;
                            processed = true;
                        }
                        return true;
                    }
                }
                case READ_END: {
                    if (c == end.charAt(counter)) {
                        counter++;
                        image.append(c);
                        if (counter >= end.length()) {
                            counter = 0;
                            status = COMPLETED;
                        }
                        return true;
                    } else {
                        value.append(end, 0, counter);
                        counter = 0;
                        status = CONTENT;
                        return false;
                    }
                }
                case COMPLETED: {
                    return false;
                }
            }
            throw new JParseException("Unsupported");
        }

        @Override
        public Object value() {
            return value.toString();
        }

        @Override
        public boolean valid() {
            return (status >= CONTENT);
        }
    }

}
