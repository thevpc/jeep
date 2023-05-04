/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source.impl;

import net.thevpc.jeep.source.JTextSource;
import net.thevpc.jeep.source.JTextSourceReport;
import net.thevpc.jeep.source.JTextSourceRoot;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author thevpc
 */
public class JTextSourceFile implements JTextSourceRoot {

    private File file;
    private String id;

    public JTextSourceFile(File file) {
        this.file = file;
        if (file == null) {
            throw new NullPointerException();
        }
        id = file.getAbsolutePath();
        try {
            id = file.getCanonicalPath();
        } catch (Exception ex) {
            //ignore.
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public Iterable<JTextSource> iterate(JTextSourceReport log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JTextSource> iterator() {
                if (!file.isFile() && !file.isDirectory()) {
                    if (log != null) {
                        log.reportError("Q000", null, "file not found : " + file.getPath());
                    }
                }
                if (file.isFile()) {
                    try {
                        return Collections.singleton((JTextSource) new DefaultJTextSource(file.getPath(), new FileReader(file), new FileCharSupplier(file))).iterator();
                    } catch (IOException e) {
                        if (log != null) {
                            log.reportError("Q000", null, e.getMessage() + " : " + file.getPath());
                        }
                    }
                }
                try {
                    return Files.walk(file.toPath()).filter((x) -> Files.isRegularFile(x)).map((x) -> x.toFile()).filter((x) -> x.getName().endsWith(".hl")).map((x) -> {
                        try {
                            return (JTextSource) new DefaultJTextSource(x.toString(), new FileReader(x), new FileCharSupplier(x));
                        } catch (Exception e) {
                            if (log != null) {
                                log.reportError("Q000", null, e.getMessage() + " : " + file.getPath());
                            }
                            return null;
                        }
                    }).filter(Objects::nonNull).iterator();
                } catch (NoSuchFileException e) {
                    if (log != null) {
                        log.reportError("Q000", null, "file not found : " + e.getMessage());
                    }
                    return Collections.emptyIterator();
                } catch (IOException e) {
                    if (log != null) {
                        log.reportError("Q000", null, e.getMessage() + " : " + file.getPath());
                    }
                    return Collections.emptyIterator();
                }
            }
        };
    }

    @Override
    public String toString() {
        if(file==null){
            return "null";
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }
}
