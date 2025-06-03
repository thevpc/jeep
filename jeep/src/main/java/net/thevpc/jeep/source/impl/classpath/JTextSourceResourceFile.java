/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source.impl.classpath;

import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceReport;
import net.thevpc.jeep.source.JTextSourceRoot;
import net.thevpc.jeep.source.impl.DefaultJTextSource;
import net.thevpc.jeep.source.impl.LogJSourceIterable;
import net.thevpc.jeep.source.impl.impl.URLCharSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author thevpc
 */
public class JTextSourceResourceFile implements JTextSourceRoot {

    String path;

    public JTextSourceResourceFile(String path) {
        this.path = path;
        if (path == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String toString() {
        return "JTextSourceResourceFile{" +
                "path='" + path + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return path;
    }

    @Override
    public Iterable<JTextSource> iterate(JTextSourceReport log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JTextSource> iterator() {
                String path2 = path;
                if (path2.startsWith("/")) {
                    path2 = path2.substring(1);
                }
                URL resource = ClassLoader.getSystemClassLoader().getResource(path2);
                if (resource != null) {
                    try {
                        InputStream in = resource.openStream();
                        if (in != null) {
                            return Collections.singleton((JTextSource) new DefaultJTextSource("resource://" + path2, new InputStreamReader(in), new URLCharSupplier(resource))).iterator();
                        }else{
                            if (log != null) {
                                log.reportError("Q000", null, "resource not found : " + path);
                            }
                        }
                    } catch (IOException ioException) {
                        if (log != null) {
                            log.reportError("Q000", null, ioException.getMessage() + ". resource not found : " + path);
                        }
                    }
                } else {
                    if (log != null) {
                        log.reportError("Q000", null, "resource not found : " + path);
                    }
                }
                return Collections.emptyIterator();
            }
        };
    }

}
