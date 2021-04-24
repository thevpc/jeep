package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.JTokenizerState;

import java.util.Objects;

public class JTokenDef implements Comparable<JTokenDef> {

    public final int id;
    public final String idName;
    public final int ttype;
    public final String ttypeName;
    public final int stateId;
    public final String stateName;
    public final String imageLayout;

    public JTokenDef(int ttype, String ttypeName) {
        if (ttype >= 0) {
            throw new IllegalArgumentException("expected positive id. got " + ttype);
        }
        this.id = -ttype;
        this.idName = ttypeName;
        this.ttype = ttype;
        this.ttypeName = "TT_" + ttypeName;
        this.stateId = 0;
        this.stateName = null;
        this.imageLayout = ttypeName;
    }

    public JTokenDef(int id, String idName, int ttype, String ttypeName, String imageLayout) {
        this.id = id;
        this.idName = idName;
        this.ttype = ttype;
        this.ttypeName = ttypeName;
        this.stateId = 0;
        this.stateName = null;
        this.imageLayout = imageLayout;
    }

    public JTokenDef(int id, String idName, int ttype, String ttypeName, String imageLayout, int stateId, String stateName) {
        this.id = id;
        this.idName = idName;
        this.ttype = ttype;
        this.ttypeName = ttypeName;
        this.stateId = stateId;
        this.stateName = stateName;
        this.imageLayout = imageLayout;
    }

    public JTokenDef bindToState(JTokenizerState state) {
        return new JTokenDef(
                id, idName,
                ttype, ttypeName, imageLayout,
                state.getId(),
                state.getName()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JTokenDef that = (JTokenDef) o;
        return id == that.id
                && ttype == that.ttype
                && stateId == that.stateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ttype, stateId);
    }

    @Override
    public int compareTo(JTokenDef o) {
        int x;
        x = Integer.compare(this.stateId, o.stateId);
        if (x != 0) {
            return x;
        }
        x = Integer.compare(this.ttype, o.ttype);
        if (x != 0) {
            return x;
        }
        x = Integer.compare(this.id, o.id);
        if (x != 0) {
            return x;
        }
        return 0;
    }

    public static int compare(JTokenDef x, JTokenDef y) {
        if (x == y) {
            return 0;
        }
        if (x == null) {
            return -1;
        }
        if (y == null) {
            return -1;
        }
        return x.compareTo(y);
    }

    public boolean isNumber() {
        return ttype == JTokenType.TT_NUMBER;
    }

    public boolean isFloat() {
        return id == JTokenId.NUMBER_FLOAT;
    }

    public boolean isInt() {
        return id == JTokenId.NUMBER_INT;
    }

    public boolean isString() {
        return ttype == JTokenType.TT_STRING;
    }

    public boolean isWhiteSpace() {
        return ttype == JTokenType.TT_WHITESPACE;
    }

    public boolean isComments() {
        return ttype == JTokenType.TT_BLOCK_COMMENTS || ttype == JTokenType.TT_LINE_COMMENTS;
    }

    public boolean isBlockComments() {
        return ttype == JTokenType.TT_BLOCK_COMMENTS;
    }

    public boolean isEOF() {
        return ttype == JTokenType.TT_EOF;
    }

    public boolean isLineComments() {
        return ttype == JTokenType.TT_LINE_COMMENTS;
    }

    public boolean isKeyword() {
        return ttype == JTokenType.TT_KEYWORD;
    }

    @Override
    public String toString() {
        return "JTokenDef{"
                + "id=" + id
                + ", idName='" + idName + '\''
                + ", ttype=" + ttype
                + ", ttypeName='" + ttypeName + '\''
                + ", stateId=" + stateId
                + ", stateName='" + stateName + '\''
                + ", imageLayout='" + imageLayout + '\''
                + '}';
    }
}
