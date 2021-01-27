package net.thevpc.jeep.impl;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.SimpleJParser;
import net.thevpc.common.textsource.JTextSourceFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;

public class DefaultJParsers implements JParsers {

    private JContext context;
    private JParserFactory factory;
    private JParsers parent;

    public DefaultJParsers(JContext context, JParsers parent) {
        this.context = context;
        this.parent = parent;
    }

    @Override
    public JParserFactory getFactory() {
        return factory;
    }

    @Override
    public JParsers setFactory(JParserFactory factory) {
        this.factory = factory;
        return this;
    }

    @Override
    public JParser of(URL url, JCompilationUnit compilationUnit) {
        if (url == null) {
            throw new UncheckedIOException(new IOException("Null URL to compile"));
        }
        try {
            InputStream in = url.openStream();
            if (in == null) {
                throw new IOException("Invalid URL " + url);
            }
            return of(new InputStreamReader(in), compilationUnit);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public JParser of(URL url) {
        return of(url, new DefaultJCompilationUnit(JTextSourceFactory.fromURL(url)));
    }

    @Override
    public JParser of(Path file, JCompilationUnit compilationUnit) {
        try {
            return of(new FileReader(file.toFile()), compilationUnit);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public JParser of(File file) {
        return of(file, new DefaultJCompilationUnit(JTextSourceFactory.fromFile(file)));
    }

    @Override
    public JParser of(File file, JCompilationUnit compilationUnit) {
        if (file == null) {
            throw new UncheckedIOException(new IOException("Null File to compile"));
        }
        try {
            return of(new FileReader(file), compilationUnit);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public JParser of(String text) {
        return of(text, new DefaultJCompilationUnit(JTextSourceFactory.fromString(text,null)));
    }

    @Override
    public JParser of(String text, JCompilationUnit compilationUnit) {
        return of(new StringReader(text == null ? "" : text), compilationUnit);
    }

    @Override
    public JParser of(Reader reader, JCompilationUnit compilationUnit) {
        return of(reader, compilationUnit, null);
    }

    @Override
    public JParser of(Reader reader, JCompilationUnit compilationUnit, JContext context) {
        if (context == null) {
            context = this.context;
        }
        if (reader == null) {
            throw new UncheckedIOException(new IOException("Null Reader to compile"));
        }
        JParserFactory f = getFactory();
        if (f != null) {
            JParser p = f.create(context.tokens().of(reader,true,true), compilationUnit, context);
            if (p != null) {
                return p;
            }
        }
        if (parent != null) {
            JParser s = parent.of(reader, compilationUnit, context);
            if (s != null) {
                return s;
            }
        }
        return new SimpleJParser(context.tokens().of(reader,true,true),compilationUnit,context);
//        return new DefaultJParser(context.tokens().of(reader), compilationUnit, context);
    }

}
