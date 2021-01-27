package net.thevpc.jeep;

import net.thevpc.jeep.impl.tokens.JTokenizerImpl;
import net.thevpc.jeep.impl.tokens.JTokenizerSnapshotImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import net.thevpc.jeep.impl.tokens.DefaultJTokenizerReader;

public class TestJTokenizer {
    @Test
    public void test1() {
        String str="1+2*3**6***7";
        JTokenizer tokenizer=new JTokenizerImpl(new DefaultJTokenizerReader(new StringReader(str)),false,false,new JTokenConfigBuilder()
                .addOperator("*")
                .addOperator("**")
        );
        int tokCount=0;
        for (JToken jToken : tokenizer) {
            System.out.println(jToken);
            tokCount++;
        }
        Assertions.assertEquals(11,tokCount);
    }

    @Test
    public void test2() {
        String str=" a const constructor with constraints";
        JTokenizer tokenizer=new JTokenizerImpl(new DefaultJTokenizerReader(new StringReader(str)),
                false,false,
                new JTokenConfigBuilder()
                .addKeywords("const","constraints")
        );
        int tokCount=0;
        for (JToken jToken : tokenizer) {
            System.out.println(jToken);
            tokCount++;
        }
//        Assert.assertEquals(11,tokCount);
    }

    @Test
    public void testSnapshots() {
        String str="1 2 3 4 5 6 7 8 9 10";
        JTokenConfig config = new JTokenConfigBuilder()
                .addKeywords("const", "constraints");
        JTokenizer tokenizer=new JTokenizerImpl(new DefaultJTokenizerReader(new StringReader(str)), false,false,config);
        int tokCount0=0;
        for (JToken jToken : tokenizer) {
            System.out.println(jToken);
            tokCount0++;
        }

        tokenizer=new JTokenizerImpl(new DefaultJTokenizerReader(new StringReader(str)), false,false, config);
        int tokCount1=0;
        printNext("skip 1",tokenizer);
        printNext("skip 2",tokenizer);
        printNext("skip 3",tokenizer);
        tokenizer.peek(8);
        JTokenizerSnapshot s1 = tokenizer.snapshot();
        JToken ta1=printNext("ta1",tokenizer);
        JToken ta2=printNext("ta2",tokenizer);
//        JTokenizerSnapshot s2 = tokenizer.snapshot();
        JTokenizerSnapshot s3 = tokenizer.snapshot();
        JToken ta3=printNext("ta3",tokenizer);
        tokenizer.pushBack(ta3);
        tokenizer.pushBack(ta3);
//        s2.rollback();
        tokenizer.pushBack(ta3);
        System.out.println(((JTokenizerSnapshotImpl)s1).size());
        s1.rollback();

        JToken tb1=printNext("tb1",tokenizer);
        JToken tb2=printNext("tb2",tokenizer);
        JToken tb3=printNext("tb3",tokenizer);

        Assertions.assertEquals(ta1.image,tb1.image);
        Assertions.assertEquals(ta2.image,tb2.image);
        Assertions.assertEquals(ta3.image,tb3.image);
    }
    private JToken printNext(String text,JTokenizer t){
        JToken n = t.next();
        System.out.println(text+" : "+n);
        return n;
    }
}
