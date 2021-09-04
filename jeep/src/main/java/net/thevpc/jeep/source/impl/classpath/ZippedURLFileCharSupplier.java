package net.thevpc.jeep.source.impl.classpath;

import net.thevpc.common.classpath.ClassPathResource;
import net.thevpc.common.classpath.ClassPathResourceFilter;
import net.thevpc.common.classpath.ClassPathUtils;
import net.thevpc.jeep.source.impl.impl.JSourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.function.Supplier;

public class ZippedURLFileCharSupplier implements Supplier<char[]> {
    private final URL url;
    private final String path;
    private final ClassPathResourceFilter filter;

    public ZippedURLFileCharSupplier(URL url, String path,ClassPathResourceFilter filter) {
        this.url = url;
        this.path = path;
        this.filter = filter;
    }

    public String getPath() {
        return path;
    }

    @Override
    public char[] get() {
        for (ClassPathResource r : ClassPathUtils.resolveResources(new URL[]{url}, filter)) {
            if (r.getPath().equals(path)) {
                try (InputStream in = r.open()) {
                    return JSourceUtils.inputStreamToCharArray(in);
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
        }
        throw new IllegalArgumentException("source path not found " + url + "#" + path);
    }

    @Override
    public String toString() {
        return String.valueOf(url);
    }
}
