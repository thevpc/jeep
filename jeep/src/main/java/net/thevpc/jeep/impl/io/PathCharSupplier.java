package net.thevpc.jeep.impl.io;

import net.thevpc.jeep.util.JeepUtils;

import java.nio.file.Path;
import java.util.function.Supplier;

public class PathCharSupplier implements Supplier<char[]> {
    private final Path file;

    public PathCharSupplier(Path file) {
        this.file = file;
    }

    @Override
    public char[] get() {
        return JeepUtils.fileToCharArray(file.toFile());
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
