package net.thevpc.jeep.source.impl.classpath;

public class ClassPathResourceFilterBySuffix extends ClassPathResourceFilterByName {

    private final String suffix;

    public ClassPathResourceFilterBySuffix(String path, String suffix) {
        super(path);
        this.suffix = suffix;
    }

    public boolean acceptName(String name) {
        return name.endsWith(suffix);
    }
}
