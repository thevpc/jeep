package net.thevpc.jeep.core;

import java.util.Objects;

public class DefaultJChildInfo implements JChildInfo{
    private String name;
    private String index;

    public DefaultJChildInfo(String name, String index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIndex() {
        return index;
    }
    @Override
    public boolean isIndexed(){
        return index!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(name);
        if(index!=null){
            sb.append("[");
            sb.append(index);
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultJChildInfo that = (DefaultJChildInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, index);
    }
}
