package net.thevpc.jeep;

import net.thevpc.jeep.impl.tokens.DollarVarPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDollarVarPattern {
    @Test
    public void test1(){
        DollarVarPattern p=new DollarVarPattern();
        JTokenMatcher matcher = p.matcher();
        check(matcher,"$abc",true,true);
        check(matcher,"${abc}",true,true);
        check(matcher,"${ab c}",true,true);
        check(matcher,"$ab\\ c",true,true);
    }

    private void check(JTokenMatcher matcher, String value, boolean accept, boolean valid) {
        boolean _accept = matcher.reset().matches(value);
        boolean _valid = matcher.valid();

        Assertions.assertEquals(accept,_accept);
        Assertions.assertEquals(valid,_valid);

    }
}
