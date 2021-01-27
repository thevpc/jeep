package net.thevpc.jeep.core.types;

import net.thevpc.jeep.JTypeNameOrVariable;
import net.thevpc.jeep.JTypeName;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JTypeNameParser {
    public static final String NON_ID_CHARS = "\\[]{}()<>*+/~&=£%'\"`;,:#@!?-";
    private PushbackReader reader;

    public JTypeNameParser(Reader reader) {
        this.reader = (reader instanceof PushbackReader)?(PushbackReader) reader:new PushbackReader(reader,10);
    }

    public static JTypeName parseType(String typeName) {
        if(typeName==null){
            throw new NullPointerException();
        }
        return parseType(new StringReader(typeName));
    }

    public static JTypeName parseType(Reader reader) {
        //good looking ?
        //PushbackReader pr=reader?new PushbackReader(reader);
        PushbackReader pr=(reader instanceof PushbackReader)?(PushbackReader) reader:new PushbackReader(reader);
        return new JTypeNameParser(pr).readTypeName();
    }

    public JTypeName readTypeName() {
        skipWhites();
        int c = peekChar();
        String name = null;
        JTypeName y = null;
        if (c == '?') {
            readChar();
            name = "?";
        } else {
            name = readName();
            if (name.isEmpty()) {
                name = readName();
                throw new IllegalArgumentException("Expected Name");
            }
        }
        skipWhites();
        JTypeNameOrVariable[] params = new JTypeName[0];
        int array = 0;
        boolean varArg = false;
        c = peekChar();
        if (c == '<') {
            params = readArguments();
        }
        c = peekChar();
        if (c == '[') {
            while (readArr()) {
                array++;
            }
        }
        if (c == '.') {
            if (readVarArg()) {
                varArg = true;
            }
        }
        return new DefaultTypeName(name, params, array, varArg);
    }

    private JTypeNameOrVariable readBoundArg() {
        int c = peekChar();
        JTypeName y = null;
        if (c == '?') {
            readChar();
        } else {
            y = readTypeName();
        }
        JTypeNameOrVariable ta = y;
        skipWhites();
        c = peekChar();
        if (c == 'e' || c == 's') {
            String extendsOrSuper = readName();
            JTypeName z = readTypeName();
            List<JTypeName> other=new ArrayList<>();
            other.add(z);
            JTypeName[] e = null;
            JTypeName[] s = null;
            while(true){
                skipWhites();
                c=peekChar();
                if(c=='&'){
                    readChar();
                    z = readTypeName();
                    other.add(z);
                }else{
                    break;
                }
            }
            JTypeName[] tnz=other.toArray(new JTypeName[0]);
            if ("extends".equals(extendsOrSuper)) {
                e=tnz;
            } else if ("super".equals(extendsOrSuper)) {
                s=tnz;
            } else {
                throw new IllegalArgumentException("Unexpected token " + extendsOrSuper);
            }
            return new JTypeNameBounded(y == null ? "?" : y.name(), e, s);
        } else {
            //readChar();
            return y;
        }
    }

    private JTypeNameOrVariable[] readArguments() {
        skipWhites();
        int c = readChar();
        if (c == -1) {
            return null;
        }
        if (c != '<') {
            throw new IllegalArgumentException("Expected '<'");
        }
        List<JTypeNameOrVariable> params = new ArrayList<>();
        boolean readMore=true;
        while (readMore) {
            c = peekChar();
            switch (c){
                case -1:{
                    readMore=false;
                    break;
                }
                case ',':{
                    readChar();
                    break;
                }
                case '>':{
                    readChar();
                    readMore=false;
                    break;
                }
                default:{
                    params.add(readBoundArg());
                }
            }
        }
        return params.toArray(new JTypeNameOrVariable[0]);
    }

    public void unreadChar(int c) {
        try {
            reader.unread(c);
        } catch (IOException e) {
            //
        }
    }

    public int peekChar() {
        int c = readChar();
        if (c != -1) {
            unreadChar(c);
        }
        return c;
    }

    private boolean readVarArg() {
        int c = readChar();
        if (c == -1) {
            return false;
        }
        if (c != '.') {
            unreadChar('.');
            return false;
        }
        c = readChar();
        if (c == -1) {
            unreadChar('.');
            return false;
        }
        if (c != '.') {
            unreadChar('.');
            unreadChar('.');
            return false;
        }
        c = readChar();
        if (c == -1) {
            unreadChar('.');
            unreadChar('.');
            return false;
        }
        if (c != '.') {
            unreadChar(c);
            unreadChar('.');
            unreadChar('.');
            return false;
        }
        return true;
    }

    private boolean readArr() {
        int c = readChar();
        if (c == -1) {
            return false;
        }
        if (c == '[') {
            int c2 = readChar();
            if (c2 == ']') {
                return true;
            }
            unreadChar(c2);
            unreadChar(c);
            return false;
        }
        unreadChar(c);
        return false;
    }

    public int readChar() {
        int c = -1;
        try {
            c = reader.read();
        } catch (IOException e) {
            //
        }

        return c;
    }

    public void skipWhites() {
        while (true) {
            int c = readChar();
            if (c < 0) {
                break;
            }
            if (Character.isWhitespace(c)) {
                //proceed;
            } else {
                unreadChar(c);
                break;
            }
        }
    }

    public String readName() {
        skipWhites();
        StringBuilder sb = new StringBuilder();
        while (true) {
            int c = readChar();
            if (c < 0) {
                break;
            }
            if (Character.isWhitespace(c) || NON_ID_CHARS.indexOf(c)>=0) {
                unreadChar(c);
                break;
            } else if (sb.length() > 0 && c == '.') {
                if (sb.charAt(sb.length() - 1) == '.') {
                    unreadChar('.');
                    unreadChar('.');
                    break;
                }
                sb.append((char) c);
            } else {
                sb.append((char) c);
            }
        }
        return sb.toString();
    }
}
