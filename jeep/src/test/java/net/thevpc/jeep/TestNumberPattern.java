package net.thevpc.jeep;

import net.thevpc.jeep.impl.tokens.JavaNumberTokenEvaluator;
import net.thevpc.jeep.core.tokens.NumberPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNumberPattern {
    @Test
    public void test1(){
        NumberPattern np=new NumberPattern(null, true, true, true, new char[0], new JavaNumberTokenEvaluator());
        JTokenMatcher matcher = np.matcher();
        check(matcher,"1",true,true,new Integer(1));
        check(matcher,"1.0",true,true,new Double(1));
        check(matcher,"1E01",true,true,new Double(10));
    }

    private void check(JTokenMatcher matcher, String str, boolean accept, boolean valid, Object value) {
        boolean _accept = matcher.reset().matches(str);
        boolean _valid = matcher.valid();
        Object _value = matcher.value();
        Assertions.assertEquals(accept,_accept);
        Assertions.assertEquals(valid,_valid);
        Assertions.assertEquals(value,_value);

    }

}
