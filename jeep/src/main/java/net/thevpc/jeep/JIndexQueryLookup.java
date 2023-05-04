package net.thevpc.jeep;

public class JIndexQueryLookup {
    private final String key;
    private final String value;
    private final JIndexQueryLookupOp op;

    public JIndexQueryLookup(String key, String value,JIndexQueryLookupOp op) {
        this.key = key;
        this.value = value;
        this.op = op;
    }

    public JIndexQueryLookupOp getOp() {
        return op;
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
        JIndexQueryLookup o2 = (JIndexQueryLookup) o;
        return key.equals(o2.key) &&
                value.equals(o2.value);
    }

    @Override
    public int hashCode() {
//        return hash;//key.hashCode()*31+value.hashCode();
        return key.hashCode()*31+value.hashCode();
    }

    @Override
    public String toString() {
        switch (op){
            case EQ:
                return key+"='" + value + '\'';
            case NE:
                return key+"!='" + value + '\'';
            case DOT_START:
                return key+"~ '" + value + "\'.*";
            case EXISTS:
                return "exists("+key+")";
        }
        return key+"='" + value + '\'';
    }
}
