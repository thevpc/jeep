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

    JTokenizer of(URL file, boolean skipComments, boolean skipSpaces);

    JTokenizer of(Path file, boolean skipComments, boolean skipSpaces);

    JTokenizer of(File file, boolean skipComments, boolean skipSpaces);

    JTokenizer of(String text, boolean skipComments, boolean skipSpaces);

    JTokenizer of(Reader reader, boolean skipComments, boolean skipSpaces);

    JTokenizer of(Reader reader, boolean skipComments, boolean skipSpaces, JTokenConfig config);
    
    JTokenizer of(JTokenizerReader reader, boolean skipComments, boolean skipSpaces, JTokenConfig config);
    
    JTokenizer of(JTokenizerReader reader, boolean skipComments, boolean skipSpaces);

    JTokenDef[] tokenDefinitions();

}
