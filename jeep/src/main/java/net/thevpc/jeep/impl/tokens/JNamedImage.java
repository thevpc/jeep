package net.thevpc.jeep.impl.tokens;

public class JNamedImage {

    private final String image;
    private final String idName;
    private final int preferredId;

    public JNamedImage(String image, String idName, int preferredId) {
        this.preferredId = preferredId;
        this.image = image;
        this.idName = idName;
    }
    public JNamedImage(String image, String idName) {
        this.preferredId = Integer.MIN_VALUE;
        this.image = image;
        this.idName = idName;
    }

    public int getPreferredId() {
        return preferredId;
    }

    public boolean hasPreferredId() {
        return preferredId != Integer.MIN_VALUE;
    }

    public String getIdName() {
        return idName;
    }

    public String image() {
        return image;
    }

    @Override
    public String toString() {
        return "JNamedImage{"
                + "image='" + image + '\''
                + ", idName=" + idName
                + '}';
    }

}
