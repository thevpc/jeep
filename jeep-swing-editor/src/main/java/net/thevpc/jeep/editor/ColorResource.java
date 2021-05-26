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
 * @author vpc
 */
public interface ColorResource {

    static ColorResource of(String c) {
        return ColorResource.of(c, Color.black);
    }

    static ColorResource of(String c, Color defaultColor) {
        return new DefaultColorResource(c, defaultColor);
    }

    static ColorResource of(Color c) {
        return new ColorResource() {
            public Color get() {
                return c;
            }
        };
    }

    Color get();

}
