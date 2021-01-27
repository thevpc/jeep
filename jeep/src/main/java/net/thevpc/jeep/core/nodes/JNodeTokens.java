package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JToken;

import java.util.ArrayList;
import java.util.List;

public class JNodeTokens {
    private JToken start;
    private JToken end;
    private List<JToken> separators=new ArrayList<>();

    public JToken getStart() {
        return start;
    }

    public JNodeTokens setStart(JToken start) {
        this.start = start;
        return this;
    }

    public JToken getEnd() {
        return end;
    }

    public JNodeTokens setEnd(JToken end) {
        this.end = end;
        return this;
    }

    public JToken[] getSeparatorsArray() {
        return separators.toArray(new JToken[0]);
    }
    public List<JToken> getSeparators() {
        return separators;
    }

    public JNodeTokens addSeparators(List<JToken> separators) {
        this.separators.addAll(separators);
        return this;
    }
    public JNodeTokens setSeparators(List<JToken> separators) {
        this.separators = separators;
        return this;
    }

    public JNodeTokens addSeparator(JToken token) {
        this.separators.add(token);
        return this;
    }
}
