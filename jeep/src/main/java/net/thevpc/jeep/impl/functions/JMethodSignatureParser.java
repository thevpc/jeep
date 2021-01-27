package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypeName;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.core.types.JTypeNameParser;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JMethodSignatureParser {

    public static JSignature parseSignature(String reader, JTypes types) {
        return parseSignature(new StringReader(reader),types);
    }
    public static JNameSignature parseNameSignature(String reader) {
        return parseNameSignature(new StringReader(reader));
    }

    public static JSignature parseSignature(Reader reader,JTypes types) {
        JNameSignature s = parseNameSignature(reader);
        JTypeName[] nargs = s.argTypes();
        JType[] jTypes = new JType[nargs.length];
        for (int i = 0; i < nargs.length; i++) {
            JTypeName jTypeName = nargs[i];
            JType e = types.forName(jTypeName.fullName());
            if(e==null){
                throw new IllegalArgumentException("type not found "+jTypeName);
            }
            jTypes[i]=e;
        }
        return new JSignature(s.name(), jTypes, s.isVarArgs());
    }

    public static JNameSignature parseNameSignature(Reader reader) {
        JTypeNameParser r = new JTypeNameParser(reader);
        String name = r.readName();
        r.skipWhites();
        int c = r.peekChar();
        if (c != '(') {
            throw new IllegalArgumentException("Expected '('");
        }
        r.readChar();
        c = r.peekChar();
        List<JTypeName> nargs=new ArrayList<>();
        if (c == ')') {
            return new JNameSignature(name, nargs.toArray(new JTypeName[0]), false);
        }

        while(true){
            JTypeName jTypeName = r.readTypeName();
            nargs.add(jTypeName);
            c = r.peekChar();
            if (c == ',') {
                r.readChar();
                r.skipWhites();
                //continue
            }else if (c == ')') {
                r.readChar();
                break;
            }else if (c != '(') {
                throw new IllegalArgumentException("Expected ')'");
            }
        }
        boolean varArg=false;
        for (int i = 0; i < nargs.size(); i++) {
            JTypeName jTypeName = nargs.get(i);
            if(i==nargs.size()-1 && jTypeName.isVarArg()){
                varArg=true;
                jTypeName=jTypeName.replaceVarArg();
                nargs.set(i,jTypeName);
            }
        }
        return new JNameSignature(name, nargs.toArray(new JTypeName[0]), varArg);
    }
}
