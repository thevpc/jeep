package net.thevpc.jeep.source.impl.impl;

import java.net.URL;
import java.util.function.Supplier;

public class URLCharSupplier implements Supplier<char[]> {
    private final URL url;

    public URLCharSupplier(URL url) {
        this.url = url;
    }

    @Override
    public char[] get() {
        return JSourceUtils.urlToCharArray(url);
    }

    @Override
    public String toString() {
        return url.toString();
    }
}
