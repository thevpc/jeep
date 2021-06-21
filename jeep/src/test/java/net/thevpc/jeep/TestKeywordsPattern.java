package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.KeywordsPattern;
import net.thevpc.jeep.core.tokens.OperatorsPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestKeywordsPattern {

    @Test
    public void test1() {
        KeywordsPattern p = new KeywordsPattern(true, "abc", "abd", "gthg");
        JTokenMatcher matcher = p.matcher();
        check(matcher, "a", true, false);
        check(matcher, "abc", true, true);
        check(matcher, "abcd", false, false);
        check(matcher, "abd", true, true);
        check(matcher, "gt", true, false);
        check(matcher, "", true, false);
//        System.out.println(matcher.reset().push("a"));
//        System.out.println(matcher.reset().push("ab"));
//        System.out.println(matcher.reset().push("abc"));
//        System.out.println(matcher.reset().push("abd"));
//        System.out.println(matcher.reset().push("abf"));
    }

    @Test
    public void test2() {
        OperatorsPattern p = new OperatorsPattern("*", "**", "***");
        JTokenMatcher matcher = p.matcher();
//        check(matcher,"*",true,true);
//        check(matcher,"**",true,true);
//        check(matcher,"***",true,true);
        check(matcher, "****", false, true);
    }

    private void check(JTokenMatcher matcher, String value, boolean accept, boolean valid) {
        boolean _accept = matcher.reset().matches(value);
        boolean _valid = matcher.valid();
        Assertions.assertEquals(accept, _accept);
        Assertions.assertEquals(valid, _valid);

    }
}
