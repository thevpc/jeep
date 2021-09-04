/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source;

import java.io.Reader;

/**
 *
 * @author thevpc
 */
public interface JTextSource {
    String name();

    Reader reader();

    String text();

    char[] charArray();

    JTextSourceRange range(int from, int to);
}
