package net.thevpc.jeep.source;

public class JTextSourcePositionTracker implements JTextSourcePosition,Cloneable{
    private int currentRowNumber = 0;
    private int currentColumnNumber = 0;
    private int currentCharNumber = 0;
    private int currentTokenNumber = 0;

    public int getCurrentRowNumber() {
        return currentRowNumber;
    }

    public int getCurrentColumnNumber() {
        return currentColumnNumber;
    }

    public int getCurrentCharNumber() {
        return currentCharNumber;
    }

    public int getCurrentTokenNumber() {
        return currentTokenNumber;
    }

    @Override
    public String toString() {
        return "Position{" + "currentRowNumber=" + currentRowNumber
                + ", currentColumnNumber=" + currentColumnNumber
                + ", currentCharNumber=" + currentCharNumber
                + ", currentTokensNumber=" + currentTokenNumber
                + "}";
    }

    @Override
    public JTextSourcePosition readOnly() {
        return new JTextSourcePositionValue(this);
    }

    public JTextSourcePositionTracker copy(){
        try {
            return (JTextSourcePositionTracker) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

    public SimpleJTextSourceToken onReadToken(SimpleJTextSourceToken token) {
        token.startLineNumber = this.getCurrentRowNumber();
        token.startColumnNumber = this.getCurrentColumnNumber();
        token.startCharacterNumber = this.getCurrentCharNumber();
        token.tokenNumber = this.getCurrentTokenNumber();
        String image = token.image;
        if(image!=null) {
            char[] chars = image.toCharArray();
            this.onReadChars(chars);
        }
        token.endLineNumber = this.getCurrentRowNumber();
        token.endColumnNumber = this.getCurrentColumnNumber();
        token.endCharacterNumber = this.getCurrentCharNumber();
        return token;
    }

    public void onReadString(String chars) {
        onReadChars(chars.toCharArray());
    }

    public void onReadChar(char c) {
        currentCharNumber += 1;
        switch (c) {
            case '\r':
            case '\n':{
                currentRowNumber++;
                currentColumnNumber = 0;
                break;
            }
            default: {
                currentColumnNumber++;
            }
        }
    }

    public void onReadChars(char[] chars) {
        currentTokenNumber++;
        currentCharNumber += chars.length;
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '\r': {
                    if (i + 1 < chars.length && chars[i + 1] == '\n') {
                        i++;
                        currentRowNumber++;
                        currentColumnNumber = 0;
                    } else {
                        currentRowNumber++;
                        currentColumnNumber = 0;
                    }
                    break;
                }
                case '\n': {
                    currentRowNumber++;
                    currentColumnNumber = 0;
                    break;
                }
                default: {
                    currentColumnNumber++;
                }
            }
        }
    }

}
