package net.thevpc.jeep.core.tokens;

public class JavaIdPattern extends SimpleTokenPattern {

    public JavaIdPattern() {
        super();
    }

    @Override
    public boolean accept(CharSequence prefix, char c) {
        if(prefix.length()==0 ){
            return Character.isJavaIdentifierStart(c);
        }else{
            return Character.isJavaIdentifierPart(c);
        }
    }

    @Override
    public boolean valid(CharSequence image) {
        return !image.toString().equals("_");
    }
    
}
