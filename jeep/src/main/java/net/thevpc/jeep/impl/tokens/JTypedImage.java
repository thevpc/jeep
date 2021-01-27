package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.JToken;

public class JTypedImage {

    public static final int NO_ID = Integer.MAX_VALUE;
    private int type;
    private String typeName;
    private String idName;
    private String image;
    private int preferredId;

    public JTypedImage(String image, int type, String typeName) {
        this(image, type, typeName, image, NO_ID);
    }

    public JTypedImage(String image, int type, String typeName, int preferredId) {
        this(image, type, typeName, image, preferredId);
    }

    public JTypedImage(String image, int type, String typeName, String idName) {
        this(image, type, typeName, idName, NO_ID);
    }

    public JTypedImage(String image, int type, String typeName, String idName, int preferredId) {
        this.image = image;
        this.type = type;
        this.idName = idName;
        this.typeName = typeName;
        this.preferredId = preferredId;
    }

    public boolean hasPreferredId() {
        return preferredId != NO_ID;
    }

    public int getPreferredId() {
        return preferredId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String image() {
        return image;
    }

    public String getIdName() {
        return idName;
    }

    public int ttype() {
        return type;
    }

    @Override
    public String toString() {
        return "JTypedImage{"
                + "id='" + idName + '\''
                + ", type=" + JToken.ttypeName(type)
                + '}';
    }
}
