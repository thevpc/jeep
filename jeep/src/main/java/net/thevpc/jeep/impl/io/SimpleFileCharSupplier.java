package net.thevpc.jeep.impl.io;

import net.thevpc.jeep.util.JeepUtils;

import java.io.File;
import java.util.function.Supplier;

public class SimpleFileCharSupplier implements Supplier<char[]> {
    private final File file;

    public SimpleFileCharSupplier(File file) {
        this.file = file;
    }

    @Override
    public char[] get() {
        return JeepUtils.fileToCharArray(file);
    }

    @Override
    public String toString() {
        return file.getPath();
    }
}
