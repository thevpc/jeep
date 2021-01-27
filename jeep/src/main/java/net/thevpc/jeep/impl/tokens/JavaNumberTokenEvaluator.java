package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.JTokenEvaluator;

public class JavaNumberTokenEvaluator implements JTokenEvaluator {
    public static final JTokenEvaluator JAVA_NUMBER=new JavaNumberTokenEvaluator();
    @Override
    public Object eval(int id, String image, String cleanImage, String typeName) {
        if (image.indexOf('.') >= 0 || image.indexOf('e') >= 0 || image.indexOf('E') >= 0) {
            switch (cleanImage.charAt(cleanImage.length() - 1)) {
                case 'f':
                case 'F': {
                    return Float.parseFloat(cleanImage.substring(0, cleanImage.length() - 1));
                }
            }
            return Double.parseDouble(cleanImage);
        } else {
            int radix;
            if (cleanImage.startsWith("0b")) {
                radix = 2;
            } else if (cleanImage.startsWith("0x")) {
                radix = 16;
            } else if (cleanImage.startsWith("0")) {
                radix = 8;
            } else {
                radix = 10;
            }
            switch (cleanImage.charAt(cleanImage.length() - 1)) {
                case 'l':
                case 'L': {
                    return Long.parseLong(cleanImage.substring(0, cleanImage.length() - 1), radix);
                }
            }
            return Integer.parseInt(cleanImage, radix);
        }
    }
}
