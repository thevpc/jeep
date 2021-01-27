package net.thevpc.jeep.impl.io;

import net.thevpc.jeep.JTokenizerReader;

import java.util.ArrayList;
import java.util.List;

public class JTokenizerReaderEscape implements JTokenizerReader {
    private char escape;
    private List<Long> escaped=new ArrayList<>();
    private long index=0;
    private JTokenizerReader base;

    public JTokenizerReaderEscape(JTokenizerReader base,char escape) {
        this.base = base;
        this.escape = escape;
    }

    public char getEscape() {
        return escape;
    }

    public JTokenizerReaderEscape setEscape(char escape) {
        this.escape = escape;
        return this;
    }

    @Override
    public int read() {
        int x = base.read();
        index++;
        if(x<0) {
            return x;
        }
        if(escape>0 && x==escape){
            escaped.add(index-1);
            x=base.read();
        }
        return x;
    }

    @Override
    public void unread(char c) {
        if(escaped.size()>0) {
            long last = escaped.get(escaped.size() - 1);
            if(last==index-1){
                escaped.remove(escaped.size() - 1);
                index-=2;
            }else{
                index--;
            }
        }
    }

    @Override
    public void unread(char[] c) {
        for (int i = c.length-1; i >=0 ; i--) {
            base.unread(c);
        }
    }
}
