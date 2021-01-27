package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.AbstractTokenPattern;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.*;

public abstract class SimpleTokenPattern extends AbstractTokenPattern {
    protected JTokenDef info;
    private final static JTokenDef DEFAULT=new JTokenDef(
            JTokenId.IDENTIFIER,
            "IDENTIFIER",
            JTokenType.TT_IDENTIFIER,
            "TT_IDENTIFIER",
            "[a-z]+"
    );
    public abstract boolean accept(CharSequence prefix, char c);

    public SimpleTokenPattern() {
        this(null);
    }
    public SimpleTokenPattern(JTokenDef info) {
        super(JTokenPatternOrder.ORDER_IDENTIFIER,(info==null?DEFAULT:info).idName);
        this.info=info==null?DEFAULT:info;
    }

    @Override
    public void bindToState(JTokenizerState state) {
        super.bindToState(state);
        info=info.bindToState(state);
    }

    public boolean valid(CharSequence image) {
        return image.length() > 0;
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return new JTokenDef[]{info};
    }

    @Override
    public JTokenMatcher matcher() {
        return new AbstractJTokenMatcher(order()) {

            @Override
            public boolean matches(char c) {
                if (peek(c)) {
                    image.append(c);
                    return true;
                }
                return false;
            }

            @Override
            public JTokenPattern pattern() {
                return SimpleTokenPattern.this;
            }

            @Override
            protected void fillToken(JToken token, JTokenizerReader reader) {
                fill(info,token);
                token.image = image();
                token.sval = token.image;
            }

            //            @Override
            public boolean peek(char c) {
                return accept(image.toString(), c);
            }

            @Override
            public Object value() {
                return image();
            }

            @Override
            public boolean valid() {
                return SimpleTokenPattern.this.valid(image);
            }
        };

    }
}
