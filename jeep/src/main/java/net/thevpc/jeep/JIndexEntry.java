package net.thevpc.jeep;

public final class JIndexEntry {
    private final String key;
    private final String value;
//    private final int hash;

    public JIndexEntry(String key, String value) {
        if(key==null|| value==null){
            throw new NullPointerException();
        }
        this.key = key;
        this.value = value;
//        this.hash = key.hashCode()*31+value.hashCode();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JIndexEntry JIndexEntry = (net.thevpc.jeep.JIndexEntry) o;
        return key.equals(JIndexEntry.key) &&
                value.equals(JIndexEntry.value);
    }

    @Override
    public int hashCode() {
//        return hash;//key.hashCode()*31+value.hashCode();
        return key.hashCode()*31+value.hashCode();
    }

    @Override
    public String toString() {
        return key+"='" + value + '\'';
    }
}
