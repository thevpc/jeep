//package net.thevpc.jeep.impl.tokens;
//
//import net.thevpc.jeep.JToken;
//import net.thevpc.jeep.JTokenMatcher;
//
//public class FalseJTokenMatcher implements JTokenMatcher {
//    public static final JTokenMatcher INSTANCE= new FalseJTokenMatcher();
//    @Override
//    public JTokenMatcher reset() {
//        return this;
//    }
//
//    @Override
//    public boolean push(char c) {
//        return false;
//    }
//
//    @Override
//    public boolean push(String c) {
//        return false;
//    }
//
//    @Override
//    public String image() {
//        return "";
//    }
//
//    @Override
//    public Object value() {
//        return "";
//    }
//
//    @Override
//    public boolean valid() {
//        return false;
//    }
//
//    @Override
//    public boolean push(net.thevpc.jeep.JTokenizerReader reader, JToken token) {
//        return false;
//    }
//
//    @Override
//    public JTokenPatternOrder order() {
//        return Integer.MAX_VALUE;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return 1389;
//    }
//}
