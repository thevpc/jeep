package net.thevpc.jeep;

import java.io.File;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;

public interface JParsers {

    JParserFactory getFactory();

    JParsers setFactory(JParserFactory factory);

    JParser of(URL file, JCompilationUnit compilationUnit);

    JParser of(URL file);

    JParser of(Path file, JCompilationUnit compilationUnit);

    JParser of(File file);

    JParser of(File file, JCompilationUnit compilationUnit);

    JParser of(String text);
    JParser of(String text, JCompilationUnit compilationUnit);

    JParser of(Reader reader, JCompilationUnit compilationUnit);

    JParser of(Reader reader, JCompilationUnit compilationUnit, JContext context);
}
