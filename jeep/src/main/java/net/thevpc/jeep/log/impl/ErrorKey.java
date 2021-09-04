package net.thevpc.jeep.log.impl;

import net.thevpc.jeep.log.JSourceMessage;
import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceToken;

import java.util.Objects;

class ErrorKey {

    String id;
    long tokenId;
    String sourceName;

    public ErrorKey(JSourceMessage m) {
        String id = m.getId();
        if (id == null) {
            id = "::" + m.getMessage().getText();
        } else {
            id = id + "::" + m.getMessage().getText();
        }
        this.id = id;
        JTextSourceToken token = m.getToken();
        this.tokenId = token == null ? -1 : token.getTokenNumber();
        JTextSource s = token == null ? null : token.getSource();
        this.sourceName = s == null ? "" : s.name();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenId, sourceName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorKey errorKey = (ErrorKey) o;
        return tokenId == errorKey.tokenId
                && Objects.equals(id, errorKey.id)
                && Objects.equals(sourceName, errorKey.sourceName);
    }
}
