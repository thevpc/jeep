package net.thevpc.jeep.source;

public class JTextSourcePositionValue implements JTextSourcePosition {
    private final int currentRowNumber ;
    private final int currentColumnNumber;
    private final int currentCharNumber ;
    private final int currentTokenNumber;

    public JTextSourcePositionValue(JTextSourcePosition o) {
        if(o==null){
            this.currentRowNumber = 0;
            this.currentColumnNumber = 0;
            this.currentCharNumber = 0;
            this.currentTokenNumber = 0;
        }else{
            this.currentRowNumber = o.getCurrentRowNumber();
            this.currentColumnNumber = o.getCurrentColumnNumber();
            this.currentCharNumber = o.getCurrentCharNumber();
            this.currentTokenNumber = o.getCurrentTokenNumber();
        }
    }

    public JTextSourcePositionValue(int currentCharNumber, int currentRowNumber, int currentColumnNumber, int currentTokenNumber) {
        this.currentRowNumber = currentRowNumber;
        this.currentColumnNumber = currentColumnNumber;
        this.currentCharNumber = currentCharNumber;
        this.currentTokenNumber = currentTokenNumber;
    }

    @Override
    public int getCurrentRowNumber() {
        return currentRowNumber;
    }

    @Override
    public int getCurrentColumnNumber() {
        return currentColumnNumber;
    }

    @Override
    public int getCurrentCharNumber() {
        return currentCharNumber;
    }

    @Override
    public int getCurrentTokenNumber() {
        return currentTokenNumber;
    }

    @Override
    public JTextSourcePosition readOnly() {
        return this;
    }

    @Override
    public String toString() {
        return "Position{" + "currentRowNumber=" + currentRowNumber
                + ", currentColumnNumber=" + currentColumnNumber
                + ", currentCharNumber=" + currentCharNumber
                + ", currentTokensNumber=" + currentTokenNumber
                + "}";
    }

    public JTextSourcePositionValue copy(){
        try {
            return (JTextSourcePositionValue) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
}
