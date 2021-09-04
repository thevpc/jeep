package net.thevpc.jeep.source.impl.classpath;

import net.thevpc.common.classpath.ClassPathResource;
import net.thevpc.common.classpath.ClassPathResourceFilter;
import net.thevpc.common.classpath.ClassPathUtils;
import net.thevpc.jeep.source.impl.impl.JSourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public class ContextClassLoaderCharSupplier implements Supplier<char[]> {
    private final String path;
    private final ClassPathResourceFilter filter;

    public ContextClassLoaderCharSupplier(String path, ClassPathResourceFilter filter) {
        this.path = path;
        this.filter = filter;
    }

    @Override
    public char[] get() {
        for (ClassPathResource r : ClassPathUtils.resolveContextResources(filter, false)) {
            if (r.getPath().equals(path)) {
                try (InputStream in = r.open()) {
                    return JSourceUtils.inputStreamToCharArray(in);
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
        }
        throw new IllegalArgumentException("source resource not found " + path);
    }

    @Override
    public String toString() {
        return String.valueOf(path);
    }
}
