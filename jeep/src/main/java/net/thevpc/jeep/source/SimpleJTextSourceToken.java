package net.thevpc.jeep.source;

public class SimpleJTextSourceToken implements JTextSourceToken,Cloneable {
    public String image;
    public long tokenNumber = 0;

    public int startLineNumber = 0;
    public int startColumnNumber = 0;
    public int startCharacterNumber = 0;

    public int endLineNumber = 0;
    public int endColumnNumber = 0;
    public int endCharacterNumber = 0;
    public JTextSource source;

    public String getImage() {
        return image;
    }

    @Override
    public long getTokenNumber() {
        return tokenNumber;
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
    public JTextSource getSource() {
        return source;
    }

    public SimpleJTextSourceToken setImage(String image) {
        this.image = image;
        return this;
    }

    public SimpleJTextSourceToken setTokenNumber(long tokenNumber) {
        this.tokenNumber = tokenNumber;
        return this;
    }

    public SimpleJTextSourceToken setStartLineNumber(int startLineNumber) {
        this.startLineNumber = startLineNumber;
        return this;
    }

    public SimpleJTextSourceToken setStartColumnNumber(int startColumnNumber) {
        this.startColumnNumber = startColumnNumber;
        return this;
    }

    public SimpleJTextSourceToken setStartCharacterNumber(int startCharacterNumber) {
        this.startCharacterNumber = startCharacterNumber;
        return this;
    }

    public SimpleJTextSourceToken setEndLineNumber(int endLineNumber) {
        this.endLineNumber = endLineNumber;
        return this;
    }

    public SimpleJTextSourceToken setEndColumnNumber(int endColumnNumber) {
        this.endColumnNumber = endColumnNumber;
        return this;
    }

    public SimpleJTextSourceToken setEndCharacterNumber(int endCharacterNumber) {
        this.endCharacterNumber = endCharacterNumber;
        return this;
    }

    public SimpleJTextSourceToken setSource(JTextSource source) {
        this.source = source;
        return this;
    }

    public SimpleJTextSourceToken setStartPosition(JTextSourcePosition position){
        this.startLineNumber = position.getCurrentRowNumber();
        this.startColumnNumber = position.getCurrentColumnNumber();
        this.startCharacterNumber = position.getCurrentCharNumber();
        this.tokenNumber = position.getCurrentTokenNumber();
        return this;
    }

    public SimpleJTextSourceToken setEndPosition(JTextSourcePosition position){
        this.endLineNumber = position.getCurrentRowNumber();
        this.endColumnNumber = position.getCurrentColumnNumber();
        this.endCharacterNumber = position.getCurrentCharNumber();
        return this;
    }

    @Override
    public JTextSourceToken copy() {
        try {
            return (JTextSourceToken) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
}
