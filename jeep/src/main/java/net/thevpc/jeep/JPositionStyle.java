package net.thevpc.jeep;

public enum JPositionStyle {
    BEFORE(false),
    START(true),
    MIDDLE(true),
    END(true),
    AFTER(false);
    private boolean inside;

    JPositionStyle(boolean inside) {
        this.inside = inside;
    }

    public boolean isInside() {
        return inside;
    }
}
