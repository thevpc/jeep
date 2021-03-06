package net.thevpc.jeep;

import java.io.File;
import java.net.URL;

public interface JIndexer {
    int indexSDK(String sdkHome, boolean force, JCompilerLog clog);

    /**
     *
     * @param compilationUnit compilationUnit
     * @return number of indexed types
     */
    int indexSource(JCompilationUnit compilationUnit, JCompilerLog clog);

    /**
     *
     * @param file  file
     * @param force force
     * @return number of indexed types
     */
    int indexLibrary(File file, boolean force, JCompilerLog clog);

    /**
     *
     * @param file  file
     * @param force force
     * @return number of indexed types
     */
    int indexLibrary(URL file, boolean force, JCompilerLog clog);
}
