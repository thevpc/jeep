package net.thevpc.jeep;

import net.thevpc.jeep.impl.JEnum;
import net.thevpc.jeep.impl.JEnumDefinition;
import net.thevpc.jeep.impl.JEnumTypeRegistry;
import net.thevpc.jeep.impl.JEnumTypes;

public final class JTokenType extends JEnum {

    /**
     * A constant indicating that the end of the stream has been read.
     */
    public static final int TT_EOF = -1;
    /**
     * A constant indicating that the end of the line has been read.
     */
    public static final int TT_EOL = '\n';
    /**
     * A constant indicating that a number token has been read.
     */
    public static final int TT_IDENTIFIER = -2;
    public static final int TT_STRING = -3;
    public static final int TT_NUMBER = -4;
    public static final int TT_DOLLAR_VAR = -8;
    public static final int TT_LINE_COMMENTS = -9;
    public static final int TT_BLOCK_COMMENTS = -10;
    public static final int TT_OPERATOR = -11;
    public static final int TT_KEYWORD = -12;
    public static final int TT_SEPARATOR = -13;
    public static final int TT_GROUP_SEPARATOR = -14;
    public static final int TT_TEMPORAL = -15;
    public static final int TT_REGEX = -16;
    public static final int TT_STRING_INTERP = -17;
    public static final int TT_WHITESPACE = -98;
    public static final int TT_ERROR = -99;
    public static final int TT_NOTHING = -100;
    public static final JEnumDefinition<JTokenType> JTOKEN_TYPES = JEnumTypes.of(JTokenType.class);

    public static class Enums {

        /**
         * A constant indicating that the end of the stream has been read.
         */
        public static final JEnum TT_EOF = JTOKEN_TYPES.valueOf("TT_EOF");
        /**
         * A constant indicating that the end of the line has been read.
         */
        public static final JEnum TT_EOL = JTOKEN_TYPES.valueOf("TT_EOL");
        /**
         * A constant indicating that a number token has been read.
         */
        public static final JTokenType TT_IDENTIFIER = JTOKEN_TYPES.valueOf("TT_IDENTIFIER");
        public static final JTokenType TT_STRING = JTOKEN_TYPES.valueOf("TT_STRING");
        public static final JTokenType TT_NUMBER = JTOKEN_TYPES.valueOf("TT_NUMBER");
        public static final JTokenType TT_DOLLAR_VAR = JTOKEN_TYPES.valueOf("TT_DOLLAR_VAR");
        public static final JTokenType TT_LINE_COMMENTS = JTOKEN_TYPES.valueOf("TT_LINE_COMMENTS");
        public static final JTokenType TT_BLOCK_COMMENTS = JTOKEN_TYPES.valueOf("TT_BLOCK_COMMENTS");
        public static final JTokenType TT_OPERATOR = JTOKEN_TYPES.valueOf("TT_OPERATOR");
        public static final JTokenType TT_KEYWORD = JTOKEN_TYPES.valueOf("TT_KEYWORD");
        public static final JTokenType TT_SEPARATOR = JTOKEN_TYPES.valueOf("TT_SEPARATOR");
        public static final JTokenType TT_GROUP_SEPARATOR = JTOKEN_TYPES.valueOf("TT_GROUP_SEPARATOR");
        public static final JTokenType TT_TEMPORAL = JTOKEN_TYPES.valueOf("TT_TEMPORAL");
        public static final JTokenType TT_REGEX = JTOKEN_TYPES.valueOf("TT_REGEX");
        public static final JTokenType TT_STRING_INTERP = JTOKEN_TYPES.valueOf("TT_STRING_INTERP");
        public static final JTokenType TT_WHITESPACE = JTOKEN_TYPES.valueOf("TT_WHITESPACE");
        public static final JTokenType TT_ERROR = JTOKEN_TYPES.valueOf("TT_ERROR");
        public static final JTokenType TT_NOTHING = JTOKEN_TYPES.valueOf("TT_NOTHING");

    }
//    private static final Logger LOG = Logger.getLogger(JTokenType.class.getName());

    private JTokenType(JEnumDefinition type, String name, int value) {
        super(type, name, value);
    }
}
