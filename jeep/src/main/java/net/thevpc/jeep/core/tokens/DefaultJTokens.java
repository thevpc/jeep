package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.DefaultJTokenizerReader;
import net.thevpc.jeep.impl.tokens.JTokenizerImpl;
import net.thevpc.jeep.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;

public class DefaultJTokens implements JTokens {

    private JContext context;
    private JTokens parent;
    private JTokenFactory factory;
    private JTokenConfig config;

    public DefaultJTokens(JContext context, JTokens parent, JTokenConfig config) {
        this.context = context;
        this.parent = parent;
        this.config = config==null?null:config.readOnly();
    }

    @Override
    public JTokenConfig config() {
        if (config != null) {
            return config;
        }
        if (parent != null) {
            JTokenConfig c = parent.config();
            if (c != null) {
                //parent's config is always set to read only
                return c.readOnly();
            }
        }
        return null;
    }

    @Override
    public JTokens setConfig(JTokenConfig definition) {
        config = definition == null ? null : definition.readOnly();
        return this;
    }

    @Override
    public JTokenFactory getFactory() {
        return factory;
    }

    @Override
    public JTokens setFactory(JTokenFactory factory) {
        this.factory = factory;
        return this;
    }

    @Override
    public JTokenizer of(URL url) {
        if (url == null) {
            throw new UncheckedIOException(new IOException("null URL to tokenize"));
        }
        try {
            InputStream in = url.openStream();
            if (in == null) {
                throw new IOException("Invalid URL " + url);
            }
            return of(new InputStreamReader(in));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public JTokenizer of(Path file) {
        try {
            return of(new FileReader(file.toFile()));
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public JTokenizer of(File file) {
        if (file == null) {
            throw new UncheckedIOException(new IOException("null File to tokenize"));
        }
        try {
            return of(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public JTokenizer of(String text) {
        return of(new StringReader(text == null ? "" : text));
    }

    @Override
    public JTokenizer of(Reader reader) {
        return of(reader, null);
    }
    
    @Override
    public JTokenizer of(JTokenizerReader reader) {
        return of(reader, null);
    }

    @Override
    public JTokenizer of(Reader reader, JTokenConfig config) {
        return of(new DefaultJTokenizerReader(reader), config);
    }

    @Override
    public JTokenizer of(JTokenizerReader reader, JTokenConfig config) {
        if (reader == null) {
            throw new UncheckedIOException(new IOException("Null Reader to tokenize"));
        }
        if (config == null) {
            config = this.config();
        }
        JTokenFactory f = getFactory();
        if (f != null) {
            JTokenizer t = f.create(reader, config, context);
            if (t != null) {
                return t;
            }
        }
        if (parent != null) {
            JTokenizer t = parent.of(reader, config);
            if (t != null) {
                return t;
            }
        }
        return new JTokenizerImpl(reader, config);
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return of("").getTokenDefinitions();
    }
}
