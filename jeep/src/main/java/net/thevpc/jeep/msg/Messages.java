/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.msg;

import java.util.logging.Level;

/**
 *
 * @author vpc
 */
public class Messages {

    private Messages() {
    }

    public static Message text(Level level, String s) {
        return new StringMessage(level, s);
    }

    public static Message cmessage(Level level, String s, Object... params) {
        return new CFormattedMessage(level, s, params);
    }

    public static Message jmessage(Level level, String s, Object... params) {
        return new JFormattedMessage(level, s, params);
    }

}
