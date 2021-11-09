/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.editor;

import java.awt.Color;
import javax.swing.UIManager;

/**
 *
 * @author thevpc
 */
public class DefaultColorResource implements ColorResource {
    
    private final String c;
    private final Color defaultColor;

    public DefaultColorResource(String c, Color defaultColor) {
        this.c = c;
        this.defaultColor = defaultColor;
    }

    public Color get() {
        for (String s : c.split("[ ;,]")) {
            if (s.length() > 0) {
                if (s.startsWith("#")) {
                    switch (s) {
                        case "#red":
                            return Color.RED;
                        case "#darkred":
                            return Color.RED.darker();
                        case "#blue":
                            return Color.BLUE;
                        case "#yellow":
                            return Color.YELLOW;
                        case "#darkyellow":
                            return Color.YELLOW;
                        case "#black":
                            return Color.BLACK;
                        case "#white":
                            return Color.WHITE;
                        case "#orange":
                            return Color.ORANGE;
                        case "#darkorange":
                            return Color.ORANGE.darker();
                        case "#pink":
                            return Color.PINK;
                        case "#darkpink":
                            return Color.PINK.darker();
                        case "#cyan":
                            return Color.CYAN;
                        case "#darkcyan":
                            return Color.CYAN.darker();
                        case "#darkgray":
                            return Color.DARK_GRAY;
                        case "#green":
                            return Color.GREEN;
                        case "#darkgreen":
                            return Color.GREEN.darker();
                        case "#magenta":
                            return Color.MAGENTA;
                        case "#darkmagenta":
                            return Color.MAGENTA.darker();
                        case "#lightgray":
                            return Color.LIGHT_GRAY;
                        case "#purple":
                            return Color.decode("#633194");
                        case "#darkpurple":
                            return Color.decode("#421d49");
                    }
                    return Color.decode(s);
                }
                Color col = UIManager.getColor(s);
                if (col != null) {
                    return col;
                }
            }
        }
        return defaultColor;
    }
    
}
