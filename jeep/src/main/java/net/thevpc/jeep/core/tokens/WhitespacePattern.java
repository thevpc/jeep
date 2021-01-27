package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.AbstractTokenPattern;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.*;

public class WhitespacePattern extends AbstractTokenPattern {
    public static final JTokenDef DEFAULT=new JTokenDef(
            JTokenId.WHITESPACE,
            "WHITESPACE",
            JTokenType.TT_WHITESPACE,
            "TT_WHITESPACE",
            "[ \\n\\t]"
    );
    private JTokenDef info;
    public WhitespacePattern() {
        this(null);
    }
    public WhitespacePattern(JTokenDef info) {
        super(JTokenPatternOrder.ORDER_WHITESPACE, info==null?"Whitespaces":info.idName);
        this.info=info==null?DEFAULT:info;
    }

    @Override
    public void bindToState(JTokenizerState state) {
        super.bindToState(state);
        info=info.bindToState(state);
    }

    @Override
    public JTokenMatcher matcher() {
        return new AbstractJTokenMatcher(order()) {
            @Override
            protected void fillToken(JToken token, JTokenizerReader reader) {
                token.image = image();
                token.sval = image();
                fill(info,token);
            }

            @Override
            public boolean matches(char c) {
                if (Character.isWhitespace(c)) {
                    image.append(c);
                    return true;
                }
                return false;
            }

            @Override
            public Object value() {
                return image();
            }

            @Override
            public boolean valid() {
                return true;
            }

            @Override
            public JTokenPattern pattern() {
                return WhitespacePattern.this;
            }
        };
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return new JTokenDef[]{info};
    }
}
