/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source.impl.classpath;

import net.thevpc.common.classpath.ClassPathResource;
import net.thevpc.common.classpath.ClassPathResourceFilter;
import net.thevpc.common.classpath.ClassPathUtils;
import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceReport;
import net.thevpc.jeep.source.JTextSourceRoot;
import net.thevpc.jeep.source.impl.DefaultJTextSource;
import net.thevpc.jeep.source.impl.LogJSourceIterable;
import net.thevpc.jeep.source.impl.impl.IteratorUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 * @author thevpc
 */
public class JTextSourceFolderURL implements JTextSourceRoot {
    
    private URL url;

    private ClassPathResourceFilter filter;
    private String id;
    public JTextSourceFolderURL(URL url, String fileNameFilter) {
        this.url = url;
        this.filter = ClassPathResourceFilterByName.of(null, fileNameFilter);
        id=url+(fileNameFilter==null?"":(":"+fileNameFilter));
        if (url == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Iterable<JTextSource> iterate(JTextSourceReport log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JTextSource> iterator() {
                Iterator<ClassPathResource> classPathResources = ClassPathUtils.resolveResources(new URL[]{url}, filter).iterator();
                return IteratorUtils.mapOptional(
                        classPathResources, new Function<ClassPathResource, Optional<JTextSource>>() {
                            @Override
                            public Optional<JTextSource> apply(ClassPathResource r) {
                                String currentPath = r.getPath();
                                try {
                                    return Optional.of(new DefaultJTextSource(
                                            url.toString()+"?"+
                                                    r.getPath(),new InputStreamReader(r.open()), new ZippedURLFileCharSupplier(url, currentPath, filter)));
                                } catch (IOException e) {
                                    if (log != null) {
                                        log.reportError("Q000", null, e.getMessage() + ". url not found : " + url);
                                    }
                                }
                                return Optional.empty();
                            }
                        }
                );
            }
        };
    }
    
}
