package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.AbstractTokenPattern;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.*;

public class BlocCommentsPattern extends AbstractTokenPattern {

    public static final JTokenDef DEFAULT=new JTokenDef(
            JTokenId.BLOCK_COMMENTS,
            "BLOCK_COMMENTS",
            JTokenType.TT_BLOCK_COMMENTS,
            "TT_BLOCK_COMMENTS",
            "/**/"
    );
    private String start;
    private String end;
    private JTokenDef tokenDef;

    public BlocCommentsPattern(String start, String end) {
        this(null,start,end);
    }
    public BlocCommentsPattern(JTokenDef tokenDef, String start, String end) {
        super(JTokenPatternOrder.ORDER_BLOCK_COMMENTS,tokenDef==null?"BlocComments":tokenDef.idName);
        this.tokenDef =tokenDef==null?DEFAULT : tokenDef;
        this.start = start;
        this.end = end;
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
            final int START = 1;
            final int COMMENTS = 2;
            final int VALID = 4;
            int pos = 0;
            int state = START;
            StringBuilder comments = new StringBuilder();

            @Override
            public JTokenPattern pattern() {
                return BlocCommentsPattern.this;
            }

            @Override
            public JTokenMatcher reset() {
                pos = 0;
                state = START;
                comments.delete(0, comments.length());
                return super.reset();
            }

            @Override
            protected void fillToken(JToken token, JTokenizerReader reader) {
                token.image = image();
                token.sval = comments.toString();
                fill(tokenDef,token);
                if(state!=VALID){
                    token.setError(1,"EOF");
                }
            }

            @Override
            public boolean matches(char c) {
                switch (state) {
                    case START: {
                        if (start.charAt(pos) == c) {
                            pos++;
                            image.append(c);
                            if (pos >= start.length()) {
                                pos = 0;
                                state = COMMENTS;
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                    case COMMENTS: {
                        if (end.charAt(pos) == c) {
                            pos++;
                            image.append(c);
                            comments.append(c);
                            if (pos >= end.length()) {
                                comments.setLength(comments.length() - end.length());
                                state = VALID;
                            }
                        } else {
                            image.append(c);
                            comments.append(c);
                            pos = 0;
                        }
                        return true;
                    }
                    case VALID: {
                        return false;
                    }
                }
                return false;
            }

            @Override
            public Object value() {
                return comments.toString();
            }

            @Override
            public boolean valid() {
                return state >= COMMENTS;
            }
        };
    }
}
