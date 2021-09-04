package net.thevpc.jeep.source.impl;

import net.thevpc.jeep.source.impl.impl.JSourceUtils;

import java.io.File;
import java.util.function.Supplier;

public class FileCharSupplier implements Supplier<char[]> {
    private final File file;

    public FileCharSupplier(File file) {
        this.file = file;
    }

    @Override
    public char[] get() {
        return JSourceUtils.fileToCharArray(file);
    }

    @Override
    public String toString() {
        return file.getPath();
    }
}
