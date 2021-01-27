package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.AbstractTokenPattern;
import net.thevpc.jeep.*;

public class PushStatePattern extends AbstractTokenPattern {

    private JTokenDef info;
    private String text;
    private int pushState;

    public PushStatePattern(int id, String idName, int ttype, String ttypeName, String imageLayout,JTokenPatternOrder order, String text, int pushState) {
        super(order,idName);
        this.text = text;
        this.info = new JTokenDef( id, idName, ttype, ttypeName,imageLayout);
        this.pushState = pushState;
    }

    @Override
    public void bindToState(JTokenizerState state) {
        super.bindToState(state);
        info=info.bindToState(state);
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return new JTokenDef[]{info};
    }

    @Override
    public JTokenMatcher matcher() {
        return new AbstractJTokenMatcher(order()) {
            int pos = 0;
            @Override
            public JTokenPattern pattern() {
                return PushStatePattern.this;
            }

            @Override
            public JTokenMatcher reset() {
                pos = 0;
                return super.reset();
            }

            @Override
            protected void fillToken(JToken token, JTokenizerReader reader) {
                token.image = image();
                token.sval = token.image;
                token.pushState = pushState;
                fill(info,token);
            }

            @Override
            public boolean valid() {
                return  text.length() == pos;
            }

            @Override
            public boolean matches(char c) {
                if(pos< text.length() && text.charAt(pos)==c){
                    image.append(c);
                    pos++;
                    return true;
                }
                return false;
            }

            @Override
            public Object value() {
                return text;
            }
        };
    }
}
