package net.thevpc.jeep.util;

import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenType;
import net.thevpc.jeep.core.tokens.JTokenDef;

public class JTokenUtils {
    public static JToken createTokenIdPointer(JToken fromToken, String image){
        JToken t = fromToken.copy();
        t.image=image;
        t.sval=image;
        return t;
    }

    public static JToken createSeparatorToken(String image){
        return createUnknownToken(JTokenType.TT_SEPARATOR,image);
    }

    public static JToken createOpToken(String image){
        return createUnknownToken(JTokenType.TT_OPERATOR,image);
    }

    public static JToken createWordToken(String image){
        return createUnknownToken(JTokenType.TT_IDENTIFIER,image);
    }

    public static JToken createKeywordToken(String image){
        return createUnknownToken(JTokenType.TT_KEYWORD,image);
    }
    public static JToken createUnknownToken(int ttype,String image){
        JToken t = new JToken();
        t.image=image;
        t.sval=image;
        t.tokenNumber=-2;
        t.startCharacterNumber=-2;
        t.endCharacterNumber=-2;
        t.startColumnNumber=-2;
        t.endColumnNumber=-2;
        t.startLineNumber=-2;
        t.endLineNumber=-2;
        return t;
    }

    public static void fillToken(JTokenDef def, JToken token){
        token.def=def;
    }
}
