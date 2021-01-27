package net.thevpc.jeep.impl.compiler;

import net.thevpc.jeep.JImportInfo;
import net.thevpc.jeep.JToken;

import java.util.Objects;

public class DefaultJImportInfo implements JImportInfo {
    private String importValue;
    private JToken token;

    public DefaultJImportInfo(String importValue, JToken token) {
        this.importValue = importValue;
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultJImportInfo that = (DefaultJImportInfo) o;
        return Objects.equals(importValue, that.importValue) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(importValue, token);
    }

    @Override
    public JToken token() {
        return token;
    }

    public String importValue() {
        return importValue;
    }

    @Override
    public String toString() {
        return String.valueOf(importValue);
    }
}
