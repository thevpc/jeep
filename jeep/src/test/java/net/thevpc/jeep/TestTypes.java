package net.thevpc.jeep;

import net.thevpc.jeep.core.DefaultJeep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestTypes {

    @Test
    public void test1() {
        Jeep j = new DefaultJeep();
        JType a = j.types().forName("java.util.Map<String,Integer>");
        String name = a.getName();
        Assertions.assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", name);
        JType b = j.types().forName("java.util.Map<String,int>");
        String name2 = b.getName();
        Assertions.assertEquals("java.util.Map<java.lang.String,int>", name2);
    }

    @Test
    public void test2() {
        Jeep j = new DefaultJeep();

        JType a = j.types().forName("java.util.HashMap<String,Integer>");
        String name = a.getName();
        Assertions.assertEquals("java.util.HashMap<java.lang.String,java.lang.Integer>", name);
        Assertions.assertArrayEquals(
                new String[]{
                        "java.util.AbstractMap<java.lang.String,java.lang.Integer>",
                        "java.util.Map<java.lang.String,java.lang.Integer>",
                        "java.lang.Cloneable",
                        "java.io.Serializable"},
                Arrays.stream(a.getParents()).map(JType::getName).toArray(String[]::new)
        );


        a = j.types().forName("java.util.HashMap");
        name = a.getName();
        Assertions.assertEquals("java.util.HashMap", name);
        Assertions.assertArrayEquals(
                new String[]{
                        "java.util.AbstractMap",
                        "java.util.Map",
                        "java.lang.Cloneable",
                        "java.io.Serializable"},
                Arrays.stream(a.getParents()).map(JType::getName).toArray(String[]::new)
        );


//
//        JType b = j.types().forName("java.util.Map<String,int>");
//        String name2 = b.name();
//        Assertions.assertEquals("java.util.Map<java.lang.String,int>", name2);
    }

//    public static class Example<A> extends HashMap<A,Integer>{
//        public <B extends List> Map<A,? extends B>[] doit(){
//            return null;
//        }
//        public Set doit2(){
//            return null;
//        }
//        public String doit3(){
//            return null;
//        }
//    }
}
