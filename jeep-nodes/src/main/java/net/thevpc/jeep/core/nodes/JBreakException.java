package net.thevpc.jeep.core.nodes;

public class JBreakException extends RuntimeException{
    private String name;

    public JBreakException() {
        this(null);
    }

    public JBreakException(String name) {
        super("Break to "+(name==null?"<ANY>":"name"));
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
