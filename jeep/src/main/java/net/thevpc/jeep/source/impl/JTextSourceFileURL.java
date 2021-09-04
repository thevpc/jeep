/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source.impl;

import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceFactory;
import net.thevpc.jeep.source.JTextSourceReport;
import net.thevpc.jeep.source.JTextSourceRoot;
import net.thevpc.jeep.source.impl.impl.URLCharSupplier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author thevpc
 */
public class JTextSourceFileURL implements JTextSourceRoot {
    
    private URL url;

    public JTextSourceFileURL(URL url) {
        this.url = url;
        if (url == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String getId() {
        return url.toString();
    }

    @Override
    public Iterable<JTextSource> iterate(JTextSourceReport log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JTextSource> iterator() {
                if(url.getProtocol().equals("file")){
                    File f;
                    try {
                        f = Paths.get(url.toURI()).toFile();
                        return JTextSourceFactory.rootFile(f).iterator();
                    } catch (URISyntaxException ex) {
                        //ignore...
                    }
                }
                URL resource = url;
                if (resource != null) {
                    try {
                        InputStream in = resource.openStream();
                        if (in != null) {
                            return Collections.singleton((JTextSource) new DefaultJTextSource(
                                    url.toString(),
                                    new InputStreamReader(in), new URLCharSupplier(resource))).iterator();
                        }
                    } catch (IOException e) {
                        if(log!=null){
                            log.reportError("Q000", null, e.getMessage() + ". url  not found : " + url);
                        }
                    }
                }
                return Collections.emptyIterator();
            }
        };
    }
    
}
