/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.JTokenDef;

/**
 *
 * @author thevpc
 */
public interface JTokenizerState {

    public int getId();

    public String getName();

    public JTokenPattern[] patterns();

    public JTokenDef[] tokenDefinitions();
    
}
