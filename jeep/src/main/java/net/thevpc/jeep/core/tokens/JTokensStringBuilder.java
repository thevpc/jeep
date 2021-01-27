package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.JTokensString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JTokensStringBuilder implements JTokensString {
    private int charLength = 0;
    private int line = 0;
    private int column = 0;
    private List<JToken> tokens = new ArrayList<>();

    public JTokensStringBuilder addSpace() {
        return addToken(JTokenId.WHITESPACE, JTokenType.TT_WHITESPACE,0, " ");
    }

    public JTokensStringBuilder addNewLine() {
        return addToken(JTokenId.WHITESPACE, JTokenType.TT_WHITESPACE, 0,"\n");
    }

    public JTokensStringBuilder addTab() {
        return addToken(JTokenId.WHITESPACE, JTokenType.TT_WHITESPACE, 0,"\t");
    }

    public JTokensStringBuilder addToken(int id, int ttype,int catId, String image) {
        JToken t = new JToken();
        t.def = new JTokenDef(id,String.valueOf(id),ttype,
                JTokenType.JTOKEN_TYPES.find(ttype).getName(),image,
                0,"NoState");
        t.catId = catId;
        t.image = image;
        return addToken0(t);
    }

    public JTokensStringBuilder addToken(JToken token) {
        if (token != null && !token.isEOF()) {
            return addToken0(token.copy());
        }
        return this;
    }

    private JTokensStringBuilder addToken0(JToken token) {
        if (token != null && !token.isEOF()) {
            token.startCharacterNumber = charLength;
            token.endCharacterNumber = charLength + token.image.length();
            charLength = token.endCharacterNumber;
            token.startLineNumber = line;
            token.startColumnNumber = column;
            for (char c : token.image.toCharArray()) {
                if (c == '\n') {
                    line++;
                    column = 0;
                } else {
                    column++;
                }
            }
            token.endLineNumber = line;
            token.endColumnNumber = column;
            tokens.add(token);
        }
        return this;
    }

    @Override
    public Iterator<JToken> iterator() {
        return tokens.iterator();
    }
}
