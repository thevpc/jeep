package net.thevpc.jeep.source;

public class JTextSourceRange {
    private int start;
    private char[] value;

    public JTextSourceRange(int start, char[] value) {
        this.start = start;
        this.value = value;
    }

    public int start() {
        return start;
    }
    public int end() {
        return start+value.length;
    }

    public char[] value() {
        return value;
    }


    public JRangePointer trim(long cn,int window){
        char[] content = value();
        int rcn = (int) cn - start();
        if (rcn >= content.length) {
            //this will happen when pointing to the end of the file.
            rcn = content.length - 1;
        }
        int min = rcn;
        int max = rcn;
        while (min - 1 >= 0 && (rcn - min + 1) < window && content[min - 1] != '\n' && content[min - 1] != '\r') {
            min--;
        }
        while (min < rcn && Character.isWhitespace(content[min])) {
            min++;
        }
        while (max < content.length && (max - cn) < window && content[max] != '\n' && content[max] != '\r') {
            max++;
        }
        return new JRangePointer(
                new String(content, min, max - min),
                rcn - min
        );
    }
    public static class JRangePointer{
        private String text;
        private int offset;

        public JRangePointer(String text, int offset) {
            this.text = text;
            this.offset = offset;
        }

        public String getText() {
            return text;
        }

        public int getOffset() {
            return offset;
        }

    }
}
