package net.thevpc.jeep.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListBuilder<T> {
    private List<T> values = new ArrayList<>();
    private boolean skipNulls = false;

    public boolean isSkipNulls() {
        return skipNulls;
    }

    public ListBuilder<T> setSkipNulls(boolean skipNulls) {
        this.skipNulls = skipNulls;
        return this;
    }

    public ListBuilder<T> add(T t) {
        if (!skipNulls || t != null) {
            values.add(t);
        }
        return this;
    }

    public ListBuilder<T> addAll(Collection<? extends T> t) {
        if (t != null) {
            for (T value : t) {
                add(value);
            }
        }
        return this;
    }

    public ListBuilder<T> addAll(T[] t) {
        if (t != null) {
            for (T value : t) {
                add(value);
            }
        }
        return this;
    }
    public ListBuilder<T> addEach(T... t) {
        if (t != null) {
            for (T value : t) {
                add(value);
            }
        }
        return this;
    }

    public <H extends T> List<H> toList() {
        return (List<H>) values;
    }

    public Object[] toObjectArray() {
        return values.toArray();
    }

    public <H extends T> H[] toArray(Class<H> t) {
        Object[] objects = values.toArray();
        H[] n = (H[]) Array.newInstance(t, objects.length);
        System.arraycopy(objects,0,n,0,objects.length);
        return n;
    }
}
