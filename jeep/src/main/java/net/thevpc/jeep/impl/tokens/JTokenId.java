package net.thevpc.jeep.impl.tokens;

import java.util.HashMap;
import java.util.Map;

public class JTokenId {

    public static final int EOF = -1;
    public static final int IDENTIFIER = 1;
    public static final int WHITESPACE = 2;
    public static final int LINE_COMMENTS = 3;
    public static final int BLOCK_COMMENTS = 4;

    public static final int DOLLAR_VAR_SHORT = 5;
    public static final int DOLLAR_VAR_LONG = 6;

    public static final int NUMBER_INT = 21;
    public static final int NUMBER_FLOAT = 22;
    public static final int NUMBER_INFINITY = 23;
    public static final int SIMPLE_QUOTES = 31;
    public static final int DOUBLE_QUOTES = 33;
    public static final int ANTI_QUOTES = 35;
    public static final int TEMPORAL = 37;
    public static final int REGEX = 39;
    public static final int OFFSET_OPERATORS = 100;
    public static final int OFFSET_KEYWORDS = 500;

    public static final int OFFSET_SEPARATORS = 2500;
    public static final int OFFSET_CUSTOM = 10000;

    private static final Map<String, String> DEFAULT_SPECIAL_NAMES = new HashMap<String, String>();
    public static final Map<String, String> SPECIAL_NAMES = new HashMap<String, String>();

    static {
        setDefaultSpecialName("", "EMPTY");
        setDefaultSpecialName("!in", "NOT_IN");
        setDefaultSpecialName("!is", "NOT_IS");
        setDefaultSpecialName("!=", "NOT_EQ");
        setDefaultSpecialName("!==", "NOT_EQ2");
        setDefaultSpecialName("!===", "NOT_EQ3");
        setDefaultSpecialName("!", "NOT");
//        setDefaultSpecialName("-", "MINUS");
//        setDefaultSpecialName("*", "STAR");
//        setDefaultSpecialName("/", "DIV");
//        setDefaultSpecialName("=", "EQ");
//        setDefaultSpecialName("==", "EQ2");
//        setDefaultSpecialName("===", "EQ3");
//        setDefaultSpecialName("!=", "NEQ");
//        setDefaultSpecialName("!==", "NEQ2");
//        setDefaultSpecialName("!===", "NEQ3");
//        setDefaultSpecialName("&&", "AMPERSAND2");
//        setDefaultSpecialName("&&&", "AMPERSAND3");
//        setDefaultSpecialName("&=", "AMPERSAND_EQ");
//        setDefaultSpecialName("++", "PLUS2");
//        setDefaultSpecialName("--", "MINUS2");
//        setDefaultSpecialName("**", "ASTERISK2");
//        setDefaultSpecialName("***", "ASTERISK3");
//        setDefaultSpecialName("<", "LT");
//        setDefaultSpecialName("<=", "LTE");
//        setDefaultSpecialName(">", "GT");
//        setDefaultSpecialName(">=", "GTE");
//        setDefaultSpecialName("<>", "DIFF");
//        setDefaultSpecialName("<-", "LT_ARROW");
//        setDefaultSpecialName("|", "VOR");
//        setDefaultSpecialName("||", "VOR2");
//        setDefaultSpecialName("|||", "VOR3");
//        setDefaultSpecialName("|=", "VOR_EQ");
//        setDefaultSpecialName("..", "DOT2");
//        setDefaultSpecialName("...", "DOT3");
//        setDefaultSpecialName("..<", "DOT2_LT");
//        setDefaultSpecialName("<..", "LT_DOT2");
//        setDefaultSpecialName("..>", "DOT2_GT");
//        setDefaultSpecialName(">..", "GT_DOT2");
//        setDefaultSpecialName("^", "CIRCUMFLEX");
//        setDefaultSpecialName("^^", "CIRCUMFLEX2");
//        setDefaultSpecialName("^=", "CIRCUMFLEX_EQ");
//        setDefaultSpecialName("<<", "LT2");
//        setDefaultSpecialName("<<<", "LT3");
//        setDefaultSpecialName(">>", "GT2");
//        setDefaultSpecialName(">>>","GT3");
//        setDefaultSpecialName("=>","EQ_GT");
//        setDefaultSpecialName("=<","EQ_LT");
//        setDefaultSpecialName("->","MINUS_GT");
//        setDefaultSpecialName("-<","MINUS_LT");
//        setDefaultSpecialName("-=","MINUS_EQ");
//        setDefaultSpecialName("+=","PLUS_EQ");
//        setDefaultSpecialName("+","PLUS");
//        setDefaultSpecialName("-","MINUS");
//        setDefaultSpecialName(":=","COLON_EQ");
//        setDefaultSpecialName(":-","COLON_MINUS");
//        setDefaultSpecialName(":--","COLON_MINUS2");
//        setDefaultSpecialName(":+","COLON_PLUS");
//        setDefaultSpecialName(":++","COLON_PLUS2");
//        setDefaultSpecialName("?","QUESTION");
//        setDefaultSpecialName("??","QUESTION2");
//        setDefaultSpecialName("???","QUESTION3");
//        setDefaultSpecialName(":**","COLON_ASTERISK2");
//        setDefaultSpecialName(":***","COLON_ASTERISK3");
//        setDefaultSpecialName("*=","ASTERISK_EQ");
//        setDefaultSpecialName("%","PERCENT");
//        setDefaultSpecialName("%%","PERCENT2");
//        setDefaultSpecialName("%=","PERCENT_EQ");
//        setDefaultSpecialName("!!","EXCLAMATION2");
//        setDefaultSpecialName("~=","TILDE_EQUALS");
//        setDefaultSpecialName(".?","DOT_QUESTION");
//        setDefaultSpecialName("!","EXCLAMATION");
//        setDefaultSpecialName("(", "OPEN_PAR");
//        setDefaultSpecialName(")", "CLOSE_PAR");
//        setDefaultSpecialName("[", "OPEN_BRACKET");
//        setDefaultSpecialName("]", "CLOSE_BRACKET");
//        setDefaultSpecialName("{", "OPEN_BRACE");
//        setDefaultSpecialName("}", "CLOSE_BRACE");
    }

    private static void setDefaultSpecialName(String image, String idName) {
        DEFAULT_SPECIAL_NAMES.put(image, idName);
    }

    public static void setSpecialName(String image, String idName) {
        if (idName == null) {
            SPECIAL_NAMES.remove(image, idName);
        } else {
            SPECIAL_NAMES.put(image, idName);
        }
    }

    public static String getName(String image) {
        String s = SPECIAL_NAMES.get(image);
        if (s != null) {
            return s;
        }
        s = DEFAULT_SPECIAL_NAMES.get(image);
        if (s != null) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        char[] arr = image.toCharArray();
        boolean wasSimple = false;
        boolean someNoSimple = false;
        boolean somMul=false;
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                if (sb.length() > 0) {
                    if (!wasSimple) {
                        sb.append('_');
                    }
                }
                sb.append(c);
                wasSimple=true;
            } else {
                someNoSimple=true;
                if (sb.length() > 0) {
                    sb.append('_');
                }
                wasSimple = false;
                StringBuilder sb2=new StringBuilder();
                for (char d : getCharName0(c).toCharArray()) {
                    if(d==' ' || d=='-'){
                        sb2.append('_');
                    }else{
                        sb2.append(d);
                    }
                }
                sb.append(sb2.toString());
                int cc=1;
                while(i+1<arr.length && arr[i+1]==c){
                    cc++;
                    i++;
                }
                if(cc>1){
                    somMul=true;
                    sb.append(cc);
                }
            }
        }
        if(arr.length>1 && !somMul){
            sb.insert(0,"SEQ_");
        }
        if(!someNoSimple){
            return sb.toString().toUpperCase();//always uppercase words...
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        System.out.println(getName("is"));
//    }

    private static String getCharName0(char c) {
        switch(c){
            case '=':return "EQ";
            case '!':return "EXCLAMATION";
            case '?':return "QUESTION";
            case '+':return "PLUS";
            case '-':return "MINUS";
            case '*':return "ASTERISK";
            case '^':return "CIRCUMFLEX";
            case '<':return "LT";
            case '>':return "GT";
            case 'a':return "AMPERSAND";
            case '%':return "PERCENT";
            case '|':return "PIPE";
            case '.':return "DOT";
        }
        return Character.getName(c);
    }
}
