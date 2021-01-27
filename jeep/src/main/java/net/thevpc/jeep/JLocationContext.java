package net.thevpc.jeep;

public class JLocationContext {
    private JNodePath nodePath;
    private JToken token;
    private JPositionStyle pos;
    private Object other;

    public JLocationContext(JNodePath nodePath, JToken token, JPositionStyle pos, Object other) {
        this.setNodePath(nodePath);
        this.setToken(token);
        this.setPos(pos);
        this.setOther(other);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Location{nodePath=").append(getNodePath());
        if (token != null) {
            sb.append(", token=").append(getToken());
        }
        sb.append(", pos=").append(getPos());
        if (other != null) {
            sb.append(", other=").append(getOther());
        }
        sb.append('}');
        return sb.toString();
    }

    public JNodePath getNodePath() {
        return nodePath;
    }

    public JLocationContext setNodePath(JNodePath nodePath) {
        this.nodePath = nodePath;
        return this;
    }

    public JToken getToken() {
        return token;
    }

    public JLocationContext setToken(JToken token) {
        this.token = token;
        return this;
    }

    public JPositionStyle getPos() {
        return pos;
    }

    public JLocationContext setPos(JPositionStyle pos) {
        this.pos = pos;
        return this;
    }

    public Object getOther() {
        return other;
    }

    public JLocationContext setOther(Object other) {
        this.other = other;
        return this;
    }
}
