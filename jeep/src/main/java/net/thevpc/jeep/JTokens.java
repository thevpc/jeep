package net.thevpc.jeep;

import net.thevpc.jeep.core.tokens.JTokenDef;

import java.io.File;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;

public interface JTokens {
    JTokenFactory getFactory();

    JTokens setFactory(JTokenFactory factory);

    JTokenConfig config();

    JTokens setConfig(JTokenConfig definition);

    JTokenizer of(URL file);

    JTokenizer of(Path file);

    JTokenizer of(File file);

    JTokenizer of(String text);

    JTokenizer of(Reader reader);

    JTokenizer of(Reader reade, JTokenConfig config);
    
    JTokenizer of(JTokenizerReader reader, JTokenConfig config);
    
    JTokenizer of(JTokenizerReader reader);

    JTokenDef[] tokenDefinitions();

}
