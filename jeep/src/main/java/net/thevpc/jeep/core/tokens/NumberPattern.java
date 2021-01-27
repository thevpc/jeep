package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.AbstractTokenPattern;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.impl.tokens.JavaNumberTokenEvaluator;
import net.thevpc.jeep.*;

import java.util.ArrayList;
import java.util.List;

public class NumberPattern extends AbstractTokenPattern {
    public static final JTokenDef INT=new JTokenDef(
            JTokenId.NUMBER_INT,
            "NUMBER_INT",
            JTokenType.TT_NUMBER,
            "TT_NUMBER",
            "[0-9]+"
    );
    public static final JTokenDef FLOAT=new JTokenDef(
            JTokenId.NUMBER_FLOAT,
            "NUMBER_FLOAT",
            JTokenType.TT_NUMBER,
            "TT_NUMBER",
            "[0-9]+[.][0-9]+"
    );
    public static final JTokenDef INFINITY=new JTokenDef(
            JTokenId.NUMBER_INFINITY,
            "NUMBER_INFINITY",
            JTokenType.TT_NUMBER,
            "TT_NUMBER",
            "\u221e"
    );

    private boolean acceptIntNumber;
    private boolean acceptFloatNumber;
    private boolean acceptInfinity;
//    private boolean acceptComplexNumber;
    private char[] numberSuffixes;
    private JTokenEvaluator numberEvaluator;
    private JTokenDef intDef;
    private JTokenDef floatDef;
    private JTokenDef infinityDef;

    public NumberPattern(boolean acceptIntNumber, boolean acceptFloatNumber, boolean acceptInfinity,char[] numberSuffixes,JTokenEvaluator numberEvaluator) {
        this(null,acceptIntNumber,acceptFloatNumber,acceptInfinity,numberSuffixes,numberEvaluator);
    }

    public NumberPattern(String name, boolean acceptIntNumber, boolean acceptFloatNumber, boolean acceptInfinity,char[] numberSuffixes,JTokenEvaluator numberEvaluator) {
        this(null,null,null, name, acceptIntNumber, acceptFloatNumber, acceptInfinity,numberSuffixes,numberEvaluator);
    }

    public NumberPattern(JTokenDef intDef, JTokenDef floatDef, JTokenDef infinityDef, String name, boolean acceptIntNumber, boolean acceptFloatNumber, boolean acceptInfinity, char[] numberSuffixes, JTokenEvaluator numberEvaluator) {
        super(JTokenPatternOrder.ORDER_NUMBER, name == null ? "Numbers" : name);
        this.intDef = intDef ==null?INT : intDef;
        this.floatDef = floatDef ==null?FLOAT : floatDef;
        this.infinityDef =infinityDef==null?INFINITY : infinityDef;
        this.acceptIntNumber = acceptIntNumber;
        this.acceptFloatNumber = acceptFloatNumber;
        this.acceptInfinity = acceptInfinity;
        this.numberEvaluator = numberEvaluator==null? JavaNumberTokenEvaluator.JAVA_NUMBER:numberEvaluator;
        if(numberSuffixes==null){
            numberSuffixes=new char[0];
        }
        for (int i = 0; i < numberSuffixes.length; i++) {
            switch (numberSuffixes[i]){
                case 'e':
                case 'E':{
                    if(acceptFloatNumber){
                        throw new JParseException("Invalid suffix : "+numberSuffixes[i]);
                    }
                }
            }
        }
        this.numberSuffixes = numberSuffixes;
    }

    @Override
    public void bindToState(JTokenizerState state) {
        super.bindToState(state);
        intDef = intDef.bindToState(state);
        floatDef = floatDef.bindToState(state);
        infinityDef = infinityDef.bindToState(state);
    }


    @Override
    public JTokenMatcher matcher() {
        return new AbstractJTokenMatcher(order()) {
            private static final int INIT = 0;
            private static final int INT_PART = 1;
            private static final int DBL_PART = 2;
            private static final int DBL_E_SIGN = 4;
            private static final int DBL_E_NBR = 5;
            private static final int DBL_END = 6;
            private static final int INT_END = 7;
            private static final int ERROR = 8;
            private StringBuilder n = new StringBuilder();

            private int state = INIT;

            @Override
            public JTokenMatcher reset() {
                super.reset();
                state = INIT;
                n.delete(0, n.length());
                return this;
            }

            @Override
            protected void fillToken(JToken token, JTokenizerReader reader) {
                boolean removeAny = false;
                //should rollback extra chars
                while (image.length() > 0) {
                    boolean remove = false;
                    char c = image.charAt(image.length() - 1);
                    switch (c) {
                        case '_':
                        case '.':
                        case '+':
                        case 'e':
                        case 'E': {
                            //this is partial number
                            image.setLength(image.length() - 1);
                            reader.unread(c);
                            remove = true;
                            removeAny = true;
                            break;
                        }
                    }
                    if (!remove) {
                        break;
                    }
                }
                //do the same with n
                while (n.length() > 0) {
                    boolean remove = false;
                    char c = n.charAt(n.length() - 1);
                    switch (c) {
                        case '.':
                        case '+':
                        case 'e':
                        case 'E': {
                            //this is partial number
                            n.setLength(n.length() - 1);
                            remove = true;
                            removeAny = true;
                            break;
                        }
                    }
                    if (!remove) {
                        break;
                    }
                }
//                if (unreadLastDot) {
//                    reader.unread('.');
//                }
                if (removeAny) {
                    String image = image();
                    state = INT_PART;
                    for (char c : image.toCharArray()) {
                        if(c=='.'||c=='e'||c=='E'){
                            state = DBL_END;
                            break;
                        }
                    }
                }
                switch (state) {
                    case DBL_E_SIGN:
                    case DBL_PART:
                    case DBL_E_NBR:
                    case INT_PART: {
                        fill(intDef,token);
                        token.image = image();
                        token.sval = n.toString();
                        break;
                    }
                    case DBL_END: {
                        fill(floatDef,token);
                        token.image = image();
                        token.sval = n.toString();
                        break;
                    }
                    default: {
                        if(acceptFloatNumber){
                            fill(floatDef,token);
                        }else{
                            fill(intDef,token);
                        }
                        token.image = image();
                        token.sval = n.toString();
                        token.setError(1,"Invalid");
                    }
                }
            }

            @Override
            public boolean matches(char c) {
                switch (state) {
                    case INIT: {
                        if (c >= '0' && c <= '9') {
                            image.append(c);
                            n.append(c);
                            state = INT_PART;
//                        } else if (c == '.') {
////                            return false;
//                            if (acceptFloatNumber || acceptComplexNumber) {
//                                image.append(c);
//                                n.append(c);
//                                state = DBL_PART;
//                            } else {
//                                return false;
//                            }
//                        } else if (isComplexUnit(c, false)) {
//                            n.append("1.0");
//                            state = COMPLEX_END;
                        } else if (acceptInfinity && c == '\u221e') {
                            image.append(c);
                            n.append(Double.POSITIVE_INFINITY);
                            state = DBL_END;
                        } else {
                            return false;
                        }
                        break;
                    }
                    case INT_PART: {
                        if (c >= '0' && c <= '9') {
                            image.append(c);
                            n.append(c);
                        } else if (c == '_') {
                            image.append(c);
                        } else if (c == '.') {
                            if (acceptFloatNumber) {
                                image.append(c);
                                n.append(c);
                                state = DBL_PART;
                            } else {
                                return false;
                            }
                        } else if (c == 'E' || c == 'e') {
                            if (acceptFloatNumber) {
                                image.append(c);
                                n.append(c);
                                state = DBL_E_SIGN;
                            } else {
                                return false;
                            }
                        } else if (isUnit(c)) {
                            image.append(c);
                            n.append(c);
                            state = INT_END;
                        } else {
                            return false;
                        }
                        break;
                    }
                    case DBL_PART: {
                        if (c >= '0' && c <= '9') {
                            image.append(c);
                            n.append(c);
                        } else if (c == '_') {
                            image.append(c);
                        } else if (c == 'E' || c == 'e') {
                            if (image.length() == 1 && image.charAt(0) == '.') {
                                state = ERROR;
                                return false;
                            }
                            image.append(c);
                            n.append(c);
                            state = DBL_E_SIGN;
                        } else if (isUnit(c)) {
                            image.append(c);
                            n.append(c);
                            state = DBL_END;
                        } else {
//                            if (image.length() == 1 && image.charAt(0) == '.') {
//                                state = ERROR;
//                            }
//                            if (image.charAt(image.length() - 1) == '.') {
//                                image.setLength(image.length() - 1);
//                                if (image.length() > 0) {
//                                    unreadLastDot = true;
//                                    //reset to int type
//                                    state = INT_PART;
//                                } else {
//                                    state = ERROR;
//                                }
//                            }
                            return false;
                        }
                        break;
                    }
                    case DBL_E_SIGN: {
                        if (c >= '0' && c <= '9') {
                            image.append(c);
                            n.append(c);
                            state = DBL_E_NBR;
                        } else if (c == '_') {
                            image.append(c);
                        } else if (c == '+' || c == '-') {
                            image.append(c);
                            n.append(c);
                            state = DBL_E_NBR;
                        } else {
                            return false;
                        }
                        break;
                    }
                    case DBL_E_NBR: {
                        if (c >= '0' && c <= '9') {
                            image.append(c);
                            n.append(c);
                        } else if (c == '_') {
                            image.append(c);
                        } else if (isUnit(c)) {
                            image.append(c);
                            n.append(c);
                        } else {
                            return false;
                        }
                        break;
                    }
                    case DBL_END: {
                        return false;
                    }
                    case INT_END: {
                        return false;
                    }
                    default: {
                        throw new RuntimeException("Unsupported");
                    }
                }
                return true;
            }

            @Override
            public Object value() {
                switch (state) {
                    case DBL_E_SIGN:
                    case DBL_PART:
                    case DBL_E_NBR:
                    case DBL_END: {
                        return numberEvaluator.eval(
                                floatDef.id,image(),n.toString(),"double");
                    }
                    case INT_PART:
                    case INT_END:{
                            return numberEvaluator.eval(intDef.id,image(),n.toString(),"int");
                    }
                }
                return numberEvaluator.eval(floatDef.id,image(),n.toString(),"double");
            }

            @Override
            public boolean valid() {
                return n.length() > 0;
//                switch (state) {
//                    case DBL_PART:
//                    case COMPLEX_END:
//                    case DBL_END:
//                    case INT_PART:
//                    case DBL_E_NBR:
//                        return true;
//                }
//                return false;
            }

            @Override
            public JTokenPattern pattern() {
                return NumberPattern.this;
            }
        };
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        List<JTokenDef> tokens = new ArrayList<>();
        if (acceptIntNumber) {
            tokens.add(intDef);
        }
        if (acceptFloatNumber) {
            tokens.add(floatDef);
        }
        if (acceptInfinity) {
            tokens.add(infinityDef);
        }

        return tokens.toArray(new JTokenDef[0]);
    }

    private boolean isUnit(int c) {
        for (int i = 0; i < numberSuffixes.length; i++) {
            if(numberSuffixes[i]==c){
                return true;
            }
        }
        return false;
    }

}
