/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.tokens;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenMatcher;
import net.thevpc.jeep.JTokenPattern;
import net.thevpc.jeep.JTokenizerReader;
import net.thevpc.jeep.core.tokens.AbstractJTokenMatcher;
import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.core.tokens.JTokenPatternOrder;

/**
 *
 * @author thevpc
 */
public class RegexpBasedTokenPattern extends AbstractTokenPattern {

    private JTokenDef def;
    private Pattern pattern;

    public RegexpBasedTokenPattern(JTokenDef def, JTokenPatternOrder order, Pattern pattern) {
        super(order, def.idName);
        this.def = def;
        this.pattern = pattern;
    }

    public RegexpBasedTokenPattern(JTokenDef def, JTokenPatternOrder order, String name, Pattern pattern) {
        super(order, name);
        this.def = def;
        this.pattern = pattern;
    }

    @Override
    public JTokenMatcher matcher() {
        Matcher m = pattern.matcher("");
        return new AbstractJTokenMatcher(order()) {
            boolean lastValid = false;
            boolean hitEnd = false;
            int bestCompleted=0;

            @Override
            protected void fillToken(JToken token, JTokenizerReader reader) {
                fill(def, token);
                token.image = image();
                token.sval = token.image;
                if (hitEnd) {
                    token.setError(1, "EOF");
                }

            }

            @Override
            public boolean matches(char c) {
                image.append(c);
                m.reset(image.toString());
                if (m.matches()) {
                    hitEnd = false;
                    bestCompleted=image.length();
                    lastValid = true;
                    return true;
                } else if (m.hitEnd()) {
                    lastValid = false;
                    hitEnd = true;
                    return true;
                } else {
                    hitEnd = false;
                    lastValid = false;
                    image.delete(image.length() - 1, image.length());
                    return false;
                }
            }

            @Override
            public Object value() {
                return image.substring(0, bestCompleted);
            }

            @Override
            public boolean valid() {
                return pattern.matcher(image.toString()).matches();
            }

            @Override
            public JTokenPattern pattern() {
                return RegexpBasedTokenPattern.this;
            }
        };
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return new JTokenDef[]{def};
    }

    @Override
    public String toString() {
        return "RegexpBasedTokenPattern{" + "def=" + def + ", pattern=" + pattern + ","+order()+'}';
    }
    

}
