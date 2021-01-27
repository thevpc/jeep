/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core;

import net.thevpc.jeep.impl.JEnum;
import net.thevpc.jeep.impl.JEnumDefinition;

/**
 *
 * @author thevpc
 */
public abstract class JTokenState extends JEnum {

    protected JTokenState(JEnumDefinition type, String name, int value) {
        super(type, name, value);
    }
    
}
