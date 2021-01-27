package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenizer;
import net.thevpc.jeep.JTokenizerSnapshot;

import java.util.ArrayList;
import java.util.List;

public class JTokenizerSnapshotImpl implements JTokenizerSnapshot {
    private final List<JToken> tokens = new ArrayList<>();
    private final JTokenizerImpl tokenizerState;
    private final int startTokenNumber;
    private boolean errorState = false;

    public JTokenizerSnapshotImpl(JTokenizer tokenizer, int startTokenNumber) {
        this.tokenizerState = (JTokenizerImpl) tokenizer;
        this.startTokenNumber = startTokenNumber;
    }

    public void save(JToken t) {
        tokens.add(t.copy());
    }

    public int size() {
        return tokens.size();
    }

    @Override
    public void rollback() {
        checkState();
        tokenizerState.onRollBackSnapshot(this, tokens, startTokenNumber);
    }

    @Override
    public void dispose() {
        this.tokenizerState.onDisposeSnapshot(this);
    }

    @Override
    public int getStartTokenNumber() {
        return startTokenNumber;
    }

    @Override
    public void invalidate() {
        errorState = true;
    }

    @Override
    public void close() {
        dispose();
    }

    protected void checkState() {
        if (errorState) {
            throw new IllegalArgumentException("error state");
        }
    }
}
