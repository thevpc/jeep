package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.core.tokens.StringPattern;
import net.thevpc.jeep.impl.tokens.JTokenId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStringPattern2 {
    @Test
    public void test1(){
        StringPattern p=new StringPattern(
                new JTokenDef(
                        JTokenId.DOUBLE_QUOTES
                        ,"DOUBLE_QUOTES_SPECIAL",
                        JTokenType.TT_STRING
                        ,"TT_STRING",
                        "\"\""
                        )
                ,"$\"","\"",'\\',true);
        JTokenMatcher matcher = p.matcher();
        check(matcher,"$\"hello\"",true,true);
        check(matcher,"$\"\"",true,true);
        check(matcher,"$\"H\"\"",false,true);
    }

    private void check(JTokenMatcher matcher, String value, boolean accept, boolean valid) {
        boolean _accept = matcher.reset().matches(value);
        boolean _valid = matcher.valid();
        Assertions.assertEquals(accept,_accept);
        Assertions.assertEquals(valid,_valid);

    }
}
