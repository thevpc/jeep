package net.thevpc.jeep.impl.tokens;

import java.io.IOException;
import java.io.Reader;

public class JReader {
    private Reader reader;
    private StringBuilder safe=new StringBuilder();
    private Mode mode=Mode.NORMAL;
    private enum Mode{
        MARKED,
        NORMAL,
    }
    public JReader(Reader reader) {
        this.reader = reader;
    }


    public void pushBack(char c){
        safe.insert(0,c);
    }

    public void reset(){
        safe.delete(0,safe.length());
        mode=Mode.NORMAL;
    }

    public void mark(){
        reset();
        mode=Mode.MARKED;
    }

    public int read(){
        switch (mode){
            case MARKED:{
                int f = -1;
                try {
                    f = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(f!=-1){
                    safe.append((char)f);
                }
                return f;
            }
            case NORMAL:{
                if(safe.length()>0){
                    char c = safe.charAt(0);
                    safe.delete(0,1);
                    return c;
                }
                int f = -1;
                try {
                    f = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return f;
            }
        }
        throw new RuntimeException("Unsupported");
    }

    @Override
    public String toString() {
        return "JReader{" +
                ", safe=" + safe +
                ", mode=" + mode +
                '}';
    }
}
