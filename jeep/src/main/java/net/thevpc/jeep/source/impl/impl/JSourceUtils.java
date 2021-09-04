/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source.impl.impl;

import java.io.*;
import java.net.URL;

/**
 * @author thevpc
 */
public class JSourceUtils {
    public static final String NEWLINE = "\n";

    public static char[] urlToCharArray(URL r) {
        try (InputStream in = r.openStream()) {
            return inputStreamToCharArray(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static char[] fileToCharArray(File r) {
        try (InputStream in = new FileInputStream(r)) {
            return inputStreamToCharArray(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static char[] inputStreamToCharArray(InputStream r) {
        return readerToCharArray(new InputStreamReader(r));
    }

    public static char[] readerToCharArray(Reader r) {
        CharArrayWriter w = new CharArrayWriter();
        char[] cc = new char[1024];
        int len;
        while (true) {
            try {
                if (!((len = r.read(cc)) > 0)) break;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            w.write(cc, 0, len);
        }
        return w.toCharArray();
    }
}
