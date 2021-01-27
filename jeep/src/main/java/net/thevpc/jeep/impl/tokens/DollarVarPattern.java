package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.tokens.AbstractJTokenMatcher;
import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.core.tokens.JTokenPatternOrder;

public class DollarVarPattern extends AbstractTokenPattern {

    public static final JTokenDef SHORT = new JTokenDef(
            JTokenId.DOLLAR_VAR_SHORT,
            "DOLLAR_VAR",
            JTokenType.TT_DOLLAR_VAR,
            "TT_DOLLAR_VAR",
            "$<var>"
    );
    public static final JTokenDef LONG = new JTokenDef(
            JTokenId.DOLLAR_VAR_LONG,
            "DOLLAR_VAR_LONG",
            JTokenType.TT_DOLLAR_VAR,
            "TT_DOLLAR_VAR",
            "${<var>}"
    );

    private final boolean escape;
    private final boolean cstyleEscape;
    private JTokenDef shortInfo;
    private JTokenDef longInfo;

    public DollarVarPattern() {
        this(null);
    }

    public DollarVarPattern(String name) {
        this(null, null, true, true, name);
    }

    public DollarVarPattern(JTokenDef shortInfo, JTokenDef longInfo, boolean escape, boolean cstyleEscape, String name) {
        super(JTokenPatternOrder.ORDER_STRING, name == null ? "DollarVar" : name);
        this.shortInfo = shortInfo == null ? SHORT : shortInfo;
        this.longInfo = longInfo == null ? LONG : longInfo;
        this.escape = escape;
        this.cstyleEscape = cstyleEscape;
    }

    @Override
    public void bindToState(JTokenizerState state) {
        super.bindToState(state);
        shortInfo = shortInfo.bindToState(state);
        longInfo = longInfo.bindToState(state);
    }

    @Override
    public JTokenMatcher matcher() {
        return new StringJMatcherLong(order(), escape, cstyleEscape);
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return new JTokenDef[]{shortInfo, longInfo};
    }

    public class StringJMatcherLong extends AbstractJTokenMatcher {

        final int READ_DOLLAR = 0;
        final int READ_OPEN_BRACE = 1;
        final int READ_CONTENT_LONG = 2;
        final int READ_CONTENT_SHORT = 3;
        final int ESCAPED_SHORT = 4;
        final int ESCAPED_LONG = 5;
        final int READ_END = 6;
        final int COMPLETED_SHORT = 7;
        final int COMPLETED_LONG = 8;
        final int ERROR = -1;
        private final boolean escape;
        private final boolean cstyleEscape;
        int status = READ_DOLLAR;
        private StringBuilder value = new StringBuilder();

        public StringJMatcherLong(JTokenPatternOrder order, boolean escape, boolean cstyleEscape) {
            super(order);
            this.escape = escape;
            this.cstyleEscape = cstyleEscape;
        }

        @Override
        public JTokenPattern pattern() {
            return DollarVarPattern.this;
        }

        @Override
        public JTokenMatcher reset() {
            status = READ_DOLLAR;
            value.delete(0, value.length());
            return super.reset();
        }

        @Override
        protected void fillToken(JToken token, JTokenizerReader reader) {
            switch (status) {
                case COMPLETED_SHORT: {
                    fill(shortInfo, token);
                    break;
                }
                case COMPLETED_LONG: {
                    fill(longInfo, token);
                    break;
                }
                case READ_CONTENT_SHORT: {
                    fill(shortInfo, token);
                    token.setError(1, "EOF");
                    break;
                }
                case READ_CONTENT_LONG: {
                    fill(longInfo, token);
                    token.setError(1, "EOF");
                    break;
                }
                default: {
                    fill(longInfo, token);
                    token.setError(1, "EOF");
                    break;
                }
            }
            token.image = image();
            token.sval = value.toString();
        }

        @Override
        public boolean matches(char c) {
            switch (status) {
                case READ_DOLLAR: {
                    if (c == '$') {
                        image.append(c);
                        status = READ_OPEN_BRACE;
                        return true;
                    } else {
                        status = ERROR;
                        return false;
                    }
                }
                case READ_OPEN_BRACE: {
                    if (c == '{') {
                        image.append(c);
                        status = READ_CONTENT_LONG;
                        return true;
                    } else if (c == '}') {
                        image.append(c);
                        status = ERROR;
                        return true;
                    } else {
                        status = READ_CONTENT_SHORT;
                        image.append(c);
                        value.append(c);
                        return true;
                    }
                }
                case READ_CONTENT_LONG: {
                    if (c == '}') {
                        image.append(c);
                        status = COMPLETED_LONG;
                        return true;
                    } else if (c == '\\') {
                        image.append(c);
                        status = ESCAPED_LONG;
                        return true;
                    } else {
                        image.append(c);
                        value.append(c);
                        return true;
                    }
                }
                case READ_CONTENT_SHORT: {
                    if (c == '_' || Character.isLetterOrDigit(c)) {
                        image.append(c);
                        value.append(c);
                        return true;
                    } else if (c == '\\') {
                        image.append(c);
                        status = ESCAPED_SHORT;
                        return true;
                    } else {
                        status = COMPLETED_SHORT;
                        return false;
                    }
                }

                case ESCAPED_LONG: {
                    if (c == '}') {
                        image.append(c);
                        value.append(c);
                        status = ESCAPED_LONG;
                        return true;
                    } else {
                        boolean processed = false;
                        if (cstyleEscape) {
                            switch (c) {
                                case 'n': {
                                    image.append(c);
                                    value.append('\n');
                                    status = READ_CONTENT_LONG;
                                    processed = true;
                                    break;
                                }
                                case 't': {
                                    image.append(c);
                                    value.append('\t');
                                    status = READ_CONTENT_LONG;
                                    processed = true;
                                    break;
                                }
                                case 'f': {
                                    image.append(c);
                                    value.append('\f');
                                    status = READ_CONTENT_LONG;
                                    processed = true;
                                    break;
                                }
                            }
                        }
                        if (!processed) {
                            image.append(c);
                            value.append(c);
                            status = READ_CONTENT_LONG;
                            processed = true;
                        }
                        return true;
                    }
                }
                case ESCAPED_SHORT: {
                    boolean processed = false;
                    if (cstyleEscape) {
                        switch (c) {
                            case 'n': {
                                image.append(c);
                                value.append('\n');
                                status = READ_CONTENT_SHORT;
                                processed = true;
                                break;
                            }
                            case 't': {
                                image.append(c);
                                value.append('\t');
                                status = READ_CONTENT_SHORT;
                                processed = true;
                                break;
                            }
                            case 'f': {
                                image.append(c);
                                value.append('\f');
                                status = READ_CONTENT_SHORT;
                                processed = true;
                                break;
                            }
                        }
                    }
                    if (!processed) {
                        image.append(c);
                        value.append(c);
                        status = READ_CONTENT_SHORT;
                        processed = true;
                    }
                    return true;
                }
                case COMPLETED_LONG:
                case COMPLETED_SHORT: {
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
            return (status == COMPLETED_LONG
                    || status == COMPLETED_SHORT
                    || status == READ_CONTENT_SHORT);
        }
    }

}
