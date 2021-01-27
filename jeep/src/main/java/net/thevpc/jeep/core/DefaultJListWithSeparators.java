package net.thevpc.jeep.core;

import net.thevpc.jeep.JListWithSeparators;
import net.thevpc.jeep.JToken;

import java.util.List;

public class DefaultJListWithSeparators<T> implements JListWithSeparators<T> {
    private List<T> items;
    private JToken startToken;
    private JToken endToken;
    private List<JToken> separatorTokens;

    public DefaultJListWithSeparators(List<T> items, JToken startToken, List<JToken> separatorTokens,JToken endToken) {
        this.items = items;
        this.startToken = startToken==null?null:startToken.copy();
        this.endToken = endToken==null?null:endToken.copy();
        this.separatorTokens =separatorTokens;
    }

    @Override
    public List<T> getItems() {
        return items;
    }

    @Override
    public JToken getStartToken() {
        return startToken;
    }

    @Override
    public JToken getEndToken() {
        return endToken;
    }

    @Override
    public List<JToken> getSeparatorTokens() {
        return separatorTokens;
    }

    @Override
    public JToken[] getSeparatorTokensArray() {
        return separatorTokens==null?null:separatorTokens.toArray(new JToken[0]);
    }
}
