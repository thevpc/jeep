package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenFormat;
import net.thevpc.jeep.JTokenType;

public class JTokenColumnFormat implements JTokenFormat {
    int ttypeWidth = 5;
    int imageLayoutWidth = 20;
    int idWidth = 5;
    int idNameWidth = 30;
    int ttypeNameWidth = 20;
    int stateNameWidth = 20;
    int tokenNumberWidth = 2;
    int lineNumberWidth = 2;
    int columnNumberWidth = 2;

    @Override
    public String format(JToken token) {
        StringBuilder ret = new StringBuilder("Token ");
        if (token.tokenNumber >= 0) {
            ret.append("#").append(
                    String.format("%-" + this.tokenNumberWidth + "d", token.tokenNumber + 1)
            ).append(" ");
        }
        if (token.def.stateName != null) {
            ret.append("[").append(token.def.stateId).append("]");
            ret.append(String.format("%-" + this.stateNameWidth + "s", token.def.stateName));
            ret.append(" ");
        }
        String ttypeName = token.def.ttypeName;
        if (ttypeName == null) {
            ttypeName = "$" + token.name();
        }
        String idName = token.def.idName;
        if (idName == null) {
            idName = "?";
        }
        ret.append("[").append(String.format("%-" + this.ttypeWidth + "d", token.def.ttype)).append("]");
        ret.append(String.format("%-" + this.ttypeNameWidth + "s", ttypeName));
        ret.append(" ");
        ret.append("[").append(String.format("%-" + this.idWidth + "d", token.def.id)).append("]");
        ret.append(String.format("%-" + this.idNameWidth + "s", idName));
        switch (token.def.ttype) {
            case JTokenType.TT_EOF:
            case JTokenType.TT_EOL: {
                break;
            }
            case JTokenType.TT_IDENTIFIER:
                ret.append("(").append(token.sval == null ? "" : JToken.escapeString(token.sval)).append(")");
                break;
            case JTokenType.TT_STRING:
            case JTokenType.TT_STRING_INTERP: {
                ret.append("(").append(JToken.escapeString(
                        token.image,
                        true, true, true, false, false, false, false, false
                )).append(")");
                break;
            }
            case JTokenType.TT_NUMBER:
                ret.append("(").append(token.image).append(")");
                break;
            case JTokenType.TT_KEYWORD:
            case JTokenType.TT_ERROR:
            case JTokenType.TT_NOTHING: {
                String s = JToken.quoteString(token.image);
                ret.append("(").append(s).append(")");
                break;
            }
            case JTokenType.TT_WHITESPACE: {
                String s = JToken.quoteString(token.image);
                ret.append("(").append(s).append(")");
                break;
            }
            case JTokenType.TT_OPERATOR: {
                String s = JToken.escapeString(token.image);
                if (!s.equals(token.image)) {
                    ret.append("(").append(token.image).append(")=").append(s);
                } else {
                    ret.append("(").append(s).append(")");
                }
                break;
            }
            case JTokenType.TT_BLOCK_COMMENTS:
            case JTokenType.TT_LINE_COMMENTS: {
                break;
            }
            default: {
                if (token.def.ttype < 0) {
                    ret.append("(").append(token.image).append(")");
                } else {
                    ret.append("['").append((char) token.def.ttype).append("']");
                }
                break;
            }
        }
        if (token.tokenNumber >= 0) {
            ret.append(", at line ")
                    .append(String.format("%-" + this.lineNumberWidth + "d", token.startLineNumber + 1))
                    .append(", column ")
                    .append(String.format("%-" + this.columnNumberWidth + "d", token.startColumnNumber + 1));
            ;
            if (token.def.ttype != JTokenType.TT_EOF) {
                ret.append(", range [").append(token.startCharacterNumber + 1)
                        .append("..").append(token.endCharacterNumber).append("]");
            }
        }
        return ret.toString();
    }

    @Override
    public String format(JTokenDef token) {
        StringBuilder ret = new StringBuilder("Token ");
        if (token.stateName != null) {
            ret.append("[").append(token.stateId).append("]");
            ret.append(String.format("%-" + this.stateNameWidth + "s", token.stateName));
            ret.append(" ");
        }
        String ttypeName = token.ttypeName;
        if (ttypeName == null) {
            ttypeName = "$" + JToken.ttypeName(token.ttype);
        }
        String idName = token.idName;
        if (idName == null) {
            idName = "?";
        }
        ret.append("[").append(String.format("%-" + this.ttypeWidth + "d", token.ttype)).append("]");
        ret.append(String.format("%-" + this.ttypeNameWidth + "s", ttypeName));
        ret.append(" ");
        ret.append("[").append(String.format("%-" + this.idWidth + "d", token.id)).append("]");
        ret.append(String.format("%-" + this.idNameWidth + "s", idName));
        ret.append(" ").append(String.format("%-" + this.imageLayoutWidth + "s", token.imageLayout));
        return ret.toString();
    }
}
