package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.impl.JEnum;
import net.thevpc.common.textsource.JTextSourceToken;
import net.thevpc.common.textsource.JTextSource;

public class JToken implements Cloneable, Comparable<JToken>, JTextSourceToken {

    private static final char[] HEXARR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public long tokenNumber = 0;

    public int startLineNumber = 0;
    public int startColumnNumber = 0;
    public int startCharacterNumber = 0;

    public int endLineNumber = 0;
    public int endColumnNumber = 0;
    public int endCharacterNumber = 0;

    public int catId;
    public int errorId;
    public String errorName;
    public int pushState;
    public JTokenDef def;
    public String image;
    public String sval;
    public Object oval;
    public JTextSource source;

    public JToken() {
        sval = null;
        image = null;
    }

    public static JPositionStyle getPosition(JToken nameToken, int caretOffset) {
        if (nameToken == null) {
            return null;
        }
        return nameToken.getPosition(caretOffset);
    }

    public static JPositionStyle getPositionStrict(JToken nameToken, int caretOffset) {
        if (nameToken == null) {
            return null;
        }
        return nameToken.getPositionStrict(caretOffset);
    }

    public static String ttypeName(int ttype) {
        JEnum i = JTokenType.JTOKEN_TYPES.find(ttype);
        if (i != null) {
            return i.getName();
        }
        if (ttype > 32) {
            return "CHAR('" + (char) ttype + "')";
        }
        if (ttype >= 0) {
            return "CHAR(" + ttype + ")";
        }
        return "TTYPE<" + ttype + ">";
    }

    public static String quoteString2(String s) {
        String s2 = quoteString(s);
        if (s2.contains("\\u")) {
            //contains bizarre char
            return "\"" + escapeString2(s) + "\"" + " as " + s2;
        }
        return s2;
    }

    public static String quoteString(String s) {
        if (s == null) {
            return "null";
        }
        return "\"" + escapeString(s) + "\"";
    }

    public static String escapeString(String s, boolean newLine, boolean tab, boolean special,
                                      boolean simpleQuote,
                                      boolean doubleQuote,
                                      boolean antiQuote,
                                      boolean unicode,
                                      boolean backSlash
    ) {
        StringBuilder outBuffer = new StringBuilder();
        for (char aChar : s.toCharArray()) {
            switch (aChar) {
                case '\\': {
                    if (backSlash) {
                        outBuffer.append("\\").append(aChar);
                    } else {
                        outBuffer.append(aChar);
                    }
                    break;
                }
                case '\'': {
                    if (simpleQuote) {
                        outBuffer.append("\\").append(aChar);
                    } else {
                        outBuffer.append(aChar);
                    }
                    break;
                }
                case '\"': {
                    if (doubleQuote) {
                        outBuffer.append("\\").append(aChar);
                    } else {
                        outBuffer.append(aChar);
                    }
                    break;
                }
                case '`': {
                    if (antiQuote) {
                        outBuffer.append("\\").append(aChar);
                    } else {
                        outBuffer.append(aChar);
                    }
                    break;
                }
                case '\t': {
                    if (tab) {
                        outBuffer.append("\\t");
                    } else {
                        outBuffer.append(aChar);
                    }
                    break;
                }
                case '\n': {
                    if (newLine) {
                        outBuffer.append("\\n");
                    } else {
                        outBuffer.append(aChar);
                    }
                    break;
                }
                case '\r': {
                    if (newLine) {
                        outBuffer.append("\\r");
                    } else {
                        outBuffer.append(aChar);
                    }
                    break;
                }
                case '\f': {
                    if (special) {
                        outBuffer.append("\\f");
                    } else {
                        outBuffer.append(aChar);
                    }
                    break;
                }
                default: {
                    if (unicode) {
                        if (((aChar < 0x0020) || (aChar > 0x007e))) {
                            outBuffer.append('\\');
                            outBuffer.append('u');
                            outBuffer.append(toHex((aChar >> 12) & 0xF));
                            outBuffer.append(toHex((aChar >> 8) & 0xF));
                            outBuffer.append(toHex((aChar >> 4) & 0xF));
                            outBuffer.append(toHex(aChar & 0xF));
                        } else {
                            outBuffer.append(aChar);
                        }
                    } else {
                        outBuffer.append(aChar);
                    }
                }
            }
        }
        return outBuffer.toString();
    }

    public static String escapeString(String s) {
        StringBuilder outBuffer = new StringBuilder();

        for (char aChar : s.toCharArray()) {
            if (aChar == '\\') {
                outBuffer.append("\\\\");
            } else if (aChar == '"') {
                outBuffer.append("\\\"");
            } else if ((aChar > 61) && (aChar < 127)) {
                outBuffer.append(aChar);
            } else {
                switch (aChar) {
                    case '\t':
                        outBuffer.append("\\t");
                        break;
                    case '\n':
                        outBuffer.append("\\n");
                        break;
                    case '\r':
                        outBuffer.append("\\r");
                        break;
                    case '\f':
                        outBuffer.append("\\f");
                        break;
                    default:
                        if (((aChar < 0x0020) || (aChar > 0x007e))) {
                            outBuffer.append('\\');
                            outBuffer.append('u');
                            outBuffer.append(toHex((aChar >> 12) & 0xF));
                            outBuffer.append(toHex((aChar >> 8) & 0xF));
                            outBuffer.append(toHex((aChar >> 4) & 0xF));
                            outBuffer.append(toHex(aChar & 0xF));
                        } else {
                            outBuffer.append(aChar);
                        }
                }
            }
        }
        return outBuffer.toString();
    }

    public static String escapeString2(String s) {
        StringBuilder outBuffer = new StringBuilder();

        for (char aChar : s.toCharArray()) {
            if (aChar == '\\') {
                outBuffer.append("\\\\");
            } else if (aChar == '"') {
                outBuffer.append("\\\"");
            } else if ((aChar > 61) && (aChar < 127)) {
                outBuffer.append(aChar);
            } else {
                switch (aChar) {
                    case '\t':
                        outBuffer.append("\\t");
                        break;
                    case '\n':
                        outBuffer.append("\\n");
                        break;
                    case '\r':
                        outBuffer.append("\\r");
                        break;
                    case '\f':
                        outBuffer.append("\\f");
                        break;
                    default:
                        outBuffer.append(aChar);
                }
            }
        }
        return outBuffer.toString();
    }

    private static char toHex(int nibble) {
        return HEXARR[(nibble & 0xF)];
    }

    public JToken copy() {
        try {
            return (JToken) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Unsupported clone");
        }
    }

    public boolean isOperator(String name) {
        return def.ttype == JTokenType.TT_OPERATOR && sval.equals(name);
    }

    public boolean isIdentifier() {
        return def.ttype == JTokenType.TT_IDENTIFIER;
    }

    public boolean isKeyword() {
        return def.ttype == JTokenType.TT_KEYWORD;
    }

    public boolean isImage(String image) {
        return image.equals(this.image);
    }

    public boolean isType(int t) {
        return def.ttype == t;
    }

    public boolean isNumber() {
        return
                def.ttype == JTokenType.TT_NUMBER;
    }

    public boolean isFloat() {
        return def.isFloat();
    }

    public boolean isInt() {
        return def.isInt();
    }




    public boolean isString() {
        return
                def.ttype == JTokenType.TT_STRING
                ;
    }

    public boolean isWhiteSpace() {
        return def.ttype == JTokenType.TT_WHITESPACE;
    }

    public boolean isComments() {
        return def.ttype == JTokenType.TT_BLOCK_COMMENTS || def.ttype == JTokenType.TT_LINE_COMMENTS;
    }

    public boolean isBlockComments() {
        return def.ttype == JTokenType.TT_BLOCK_COMMENTS;
    }


    public boolean isEOF() {
        return def.ttype == JTokenType.TT_EOF;
    }

    public boolean isLineComments() {
        return def.ttype == JTokenType.TT_LINE_COMMENTS

                ;
    }

    public boolean isKeyword(String name) {
        return def.ttype == JTokenType.TT_KEYWORD && sval.equals(name);
    }



    @Override
    public String toString() {
        return JTokenFormat.DEFAULT.format(this);
    }

    public String name() {
        return ttypeName(def.ttype);
    }

    @Override
    public int compareTo(JToken o) {
        if (this == o) {
            return 0;
        }
        int x = Integer.compare(this.startCharacterNumber, o.startCharacterNumber);
        if (x != 0) {
            return x;
        }
        x = Integer.compare(this.endCharacterNumber, o.endCharacterNumber);
        if (x != 0) {
            return x;
        }
        x = JTokenDef.compare(this.def, o.def);
        if (x != 0) {
            return x;
        }
        return 0;
    }

    public int length() {
        return endCharacterNumber - startCharacterNumber;
    }

    public JPositionStyle getPositionStrict(int caretOffset) {
        if (this.startCharacterNumber < 0) {
            return null;
        }
        if (caretOffset < this.startCharacterNumber) {
            return null;
        } else if (caretOffset == this.startCharacterNumber) {
            return JPositionStyle.START;
        } else if (caretOffset < this.endCharacterNumber) {
            return JPositionStyle.MIDDLE;
        } else if (caretOffset == this.endCharacterNumber) {
            return JPositionStyle.END;
        } else {
            return null;
        }
    }

    public JPositionStyle getPosition(int caretOffset) {
        if (this.startCharacterNumber < 0) {
            return null;
        }
        if (caretOffset < this.startCharacterNumber) {
            return JPositionStyle.BEFORE;
        } else if (caretOffset == this.startCharacterNumber) {
            return JPositionStyle.START;
        } else if (caretOffset < this.endCharacterNumber) {
            return JPositionStyle.MIDDLE;
        } else if (caretOffset == this.endCharacterNumber) {
            return JPositionStyle.END;
        } else {
            return JPositionStyle.AFTER;
        }
    }

    public boolean containsCaret(int caretOffset) {
        return caretOffset >= startCharacterNumber
                && caretOffset <= endCharacterNumber;
    }
    public JToken setError(int errorId,String errorMessage){
        this.errorId=errorId;
        this.errorName=errorMessage;
        return this;
    }

    public boolean isError(){
        return errorId!=0;
    }

    @Override
    public JTextSource getSource(){
        if(source!=null){
            return source;
        }
        return null;
    }

    @Override
    public int getStartLineNumber() {
        return startLineNumber;
    }

    @Override
    public int getStartColumnNumber() {
        return startColumnNumber;
    }

    @Override
    public int getStartCharacterNumber() {
        return startCharacterNumber;
    }

    @Override
    public int getEndLineNumber() {
        return endLineNumber;
    }

    @Override
    public int getEndColumnNumber() {
        return endColumnNumber;
    }

    @Override
    public int getEndCharacterNumber() {
        return endCharacterNumber;
    }

    @Override
    public long getTokenNumber() {
        return tokenNumber;
    }

    public int id(){
        return def.id;
    }
}
