package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.AbstractTokenPattern;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.*;

public class LineCommentsPattern extends AbstractTokenPattern {

    public static final JTokenDef DEFAULT = new JTokenDef(
            JTokenId.LINE_COMMENTS,
            "LINE_COMMENTS",
            JTokenType.TT_LINE_COMMENTS,
            "TT_LINE_COMMENTS",
            "//"
    );

    private static int START = 0;
    private static int INTO = 1;
    private JTokenDef tokenDef;
    private String start;

    public LineCommentsPattern(String start) {
        this(null, start);
    }

    public LineCommentsPattern(JTokenDef tokenDef, String start) {
        super(JTokenPatternOrder.ORDER_LINE_COMMENTS, tokenDef == null ? "LineComments" : tokenDef.idName);
        this.tokenDef = tokenDef == null ? DEFAULT : tokenDef;
        this.start = start;
    }

    @Override
    public void bindToState(JTokenizerState state) {
        super.bindToState(state);
        tokenDef = tokenDef.bindToState(state);
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return new JTokenDef[]{tokenDef};
    }

    @Override
    public JTokenMatcher matcher() {
        return new AbstractJTokenMatcher(order()) {
            int pos = 0;
            int len = start.length();
            boolean wasr = false;
            boolean end = false;
            StringBuilder comments = new StringBuilder();

            @Override
            public JTokenMatcher reset() {
                wasr = false;
                end = false;
                pos = 0;
                comments.delete(0, comments.length());
                return super.reset();
            }

            @Override
            public JTokenPattern pattern() {
                return LineCommentsPattern.this;
            }

            @Override
            protected void fillToken(JToken token, JTokenizerReader reader) {
                token.image = image();
                token.sval = comments.toString();
                fill(tokenDef, token);
            }

            @Override
            public boolean matches(char c) {
                if (end) {
                    return false;
                }
                if (pos < len) {
                    if (start.charAt(pos) == c) {
                        pos++;
                        image.append(c);
                        return true;
                    } else {
                        return false;
                    }
                }
                if (wasr) {
                    if (c == '\n') {
                        image.append(c);
                        end = true;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (c == '\n') {
                        image.append(c);
                        end = true;
                        return true;
                    } else if (c == '\r') {
                        image.append(c);
                        wasr = true;
                        return true;
                    } else {
                        image.append(c);
                        comments.append(c);
                        return true;
                    }
                }
            }

            @Override
            public Object value() {
                return comments.toString();
            }

            @Override
            public boolean valid() {
                return (pos >= len);
            }
        };
    }
}
