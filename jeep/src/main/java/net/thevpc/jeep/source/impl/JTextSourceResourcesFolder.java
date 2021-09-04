/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source.impl;

import net.thevpc.common.classpath.ClassPathResource;
import net.thevpc.common.classpath.ClassPathResourceFilter;
import net.thevpc.common.classpath.ClassPathUtils;
import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceReport;
import net.thevpc.jeep.source.JTextSourceRoot;
import net.thevpc.jeep.source.impl.classpath.ClassPathResourceFilterByName;
import net.thevpc.jeep.source.impl.classpath.ContextClassLoaderCharSupplier;
import net.thevpc.jeep.source.impl.impl.IteratorUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 * @author thevpc
 */
public class JTextSourceResourcesFolder implements JTextSourceRoot {

    private final String path;
    private final ClassPathResourceFilter filter;
    private final String id;

    public JTextSourceResourcesFolder(String path, String fileNameFilter) {
        this.path = path;
        this.filter = ClassPathResourceFilterByName.of(path, fileNameFilter);
        id=path+(fileNameFilter==null?"":(":"+fileNameFilter));
        if (path == null) {
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
                String path2 = path;
                if (path2.startsWith("/")) {
                    path2 = path2.substring(1);
                }
                Iterator<ClassPathResource> classPathResources = ClassPathUtils.resolveContextResources(filter, false).iterator();
                return IteratorUtils.mapOptional(classPathResources, new Function<ClassPathResource, Optional<JTextSource>>() {
                    @Override
                    public Optional<JTextSource> apply(ClassPathResource r) {
                        try {
                            return Optional.of(
                                    new DefaultJTextSource(JTextSourceResourcesFolder.this.path+"/"+r.getPath(),new InputStreamReader(r.open()), new ContextClassLoaderCharSupplier(r.getPath(), filter))
                            );
                        } catch (IOException e) {
                            if (log != null) {
                                log.reportError("Q000", null, e.getMessage() + ". resource not found : " + path);
                            }
                            return Optional.empty();
                        }
                    }
                });
            }
        };
    }

}
