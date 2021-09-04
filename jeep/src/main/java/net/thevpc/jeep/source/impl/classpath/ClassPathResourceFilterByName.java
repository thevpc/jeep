package net.thevpc.jeep.source.impl.classpath;

import net.thevpc.common.classpath.ClassPathResourceFilter;

import java.net.URL;

public abstract class ClassPathResourceFilterByName implements ClassPathResourceFilter {
    public static ClassPathResourceFilter of(String path, String fileNameFilter) {
        if (fileNameFilter == null) {
            return null;
        } else if (fileNameFilter.startsWith("*") && fileNameFilter.substring(1).indexOf('*') < 0) {
            return new ClassPathResourceFilterBySuffix(path, fileNameFilter.substring(1));
        } else {
            throw new IllegalArgumentException("FixMeLaterException");
        }
    }

    private final String path;

    public ClassPathResourceFilterByName(String path) {
        if (path != null) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
        }
        this.path = path;

    }

    @Override
    public boolean acceptLibrary(URL url) {
        return true;
    }

    @Override
    public boolean acceptResource(String url) {
        boolean b = (path == null
                || url.startsWith(path + "/")
                || url.equals(path)) && acceptName(nameOf(url));
        return b;
    }

    private String nameOf(String url) {
        int i = url.lastIndexOf('/');
        if (i >= 0) {
            return url.substring(i + 1);
        }
        return url;
    }

    public abstract boolean acceptName(String name);
}
