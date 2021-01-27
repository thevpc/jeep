package net.thevpc.jeep;

import java.util.ArrayList;
import java.util.List;

public class JTokenBoundsBuilder implements JTokenBounds{
    private JToken startToken;
    private JToken endToken;
    private List<JToken> separators = new ArrayList<>();

    public JTokenBoundsBuilder(JToken startToken, JToken endToken) {
        this.setStartToken(startToken);
        this.setEndToken(endToken);
    }


    public JTokenBoundsBuilder(JTokenBoundsBuilder o) {
        this.setStartToken(o.getStartToken());
        this.setEndToken(o.getEndToken());
    }

    public JTokenBoundsBuilder() {
    }

    public JToken visitSeparator(JToken t) {
        separators.add(t);
        return visit(t);
    }

    public <T extends JNode> T visit(T t) {
        if (t != null) {
            if (getStartToken() == null) {
                setStartToken(t.getStartToken());
                setEndToken(t.getStartToken());
            } else {
                setEndToken(t.getEndToken());
            }
        }
        return t;
    }

    public JToken visit(JToken t) {
        if (t != null) {
            if (getStartToken() == null) {
                setStartToken(t);
                setEndToken(t);
            } else {
                setEndToken(t);
            }
        }
        return t;
    }

    public JToken getStartToken() {
        return startToken;
    }

    public JToken getEndToken() {
        return endToken;
    }

    public JToken[] getSeparators() {
        return separators.toArray(new JToken[0]);
    }

    public JTokenBoundsBuilder setStartToken(JToken startToken) {
        this.startToken = startToken;
        return this;
    }

    public JTokenBoundsBuilder setEndToken(JToken endToken) {
        this.endToken = endToken;
        return this;
    }

    public JTokenBoundsBuilder setSeparators(List<JToken> separators) {
        this.separators = separators;
        return this;
    }
}
