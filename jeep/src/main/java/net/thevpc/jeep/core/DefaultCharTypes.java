package net.thevpc.jeep.core;

public class DefaultCharTypes {
    public static final int DEFAULT_TYPE_OTHER=0;
    public static final int DEFAULT_TYPE_GROUP=1;
    public static final int DEFAULT_TYPE_SEPARATOR=2;
    public static final int DEFAULT_TYPE_OPERATOR=3;
    public static final int DEFAULT_TYPE_QUOTE=4;
    public static final int DEFAULT_TYPE_LETTER=5;
    public static final int DEFAULT_TYPE_DIGIT=6;
    public static final int DEFAULT_TYPE_SPACE=7;

    public static int defaultCharTypeOf(char c){
        switch (c){
            case ',':
            case ';':
            case ':':{
                return DEFAULT_TYPE_SEPARATOR;
            }
            case '.':
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
            case '~':
            case '!':
            case '&':
            case '=':
            case '<':
            case '>':
            {
                return DEFAULT_TYPE_OPERATOR;
            }
            case '(':
            case ')':
            case '[':
            case ']':
            case '{':
            case '}':
            {
                return DEFAULT_TYPE_GROUP;
            }
            case '\"':
            case '\'':
            case '`':
            {
                return DEFAULT_TYPE_QUOTE;
            }
            case '_':
            {
                return DEFAULT_TYPE_LETTER;
            }
        }
        if(Character.isWhitespace(c)){
            return DEFAULT_TYPE_SPACE;
        }
        if(Character.isDigit(c)){
            return DEFAULT_TYPE_DIGIT;
        }
        if(Character.isLetter(c)){
            return DEFAULT_TYPE_LETTER;
        }
        return DEFAULT_TYPE_OTHER;
    }
}
