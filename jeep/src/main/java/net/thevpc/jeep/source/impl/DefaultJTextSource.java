/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source.impl;

import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceRange;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 *
 * @author thevpc
 */
public class DefaultJTextSource implements JTextSource {
    
    String name;
    Reader reader;
    Supplier<char[]> supplier;
    private boolean consumed=false;
    public DefaultJTextSource(String name, Reader reader, Supplier<char[]> supplier) {
        this.reader = reader;
        this.name = name;
        this.supplier = supplier;
    }

    public String name() {
        return name;
    }

    @Override
    public Reader reader() {
        if(consumed){
            return new StringReader(text());
        }else {
            consumed=true;
            return reader;
        }
    }

    @Override
    public String text() {
        return new String(supplier.get());
    }

    @Override
    public char[] charArray() {
        return supplier.get();
    }

    @Override
    public JTextSourceRange range(int from, int to) {
        char[] chars = charArray();
        if(from<0){
            from=0;
        }
        if(to>chars.length){
            to=chars.length;
        }
        return new JTextSourceRange(
                from,
                Arrays.copyOfRange(chars,from,to)
        );
    }
}
