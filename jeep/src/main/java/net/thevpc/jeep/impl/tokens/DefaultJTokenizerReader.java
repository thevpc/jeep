/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.JTokenizerReader;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/**
 *
 * @author thevpc
 */
public class DefaultJTokenizerReader implements JTokenizerReader {

    private PushbackReader reader;

    public DefaultJTokenizerReader(Reader reader) {
        this.reader = (reader instanceof PushbackReader) ? (PushbackReader) reader : new PushbackReader(reader, 50);
    }

    @Override
    public int read() {
        try {
            return reader.read();
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public void unread(char c) {
        try {
            reader.unread(c);
        } catch (IOException e) {
            //
        }
    }

    @Override
    public void unread(char[] c) {
        try {
            reader.unread(c);
        } catch (IOException e) {
            //
        }
    }

}
