package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JAnnotationInstance;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class DefaultJAnnotationInstanceList implements JAnnotationInstanceList {
    private List<JAnnotationInstance> list=new ArrayList<>(); //with duplicates
    private Set<JAnnotationInstance> set=new HashSet<>(); //without duplicates

    @Override
    public Stream<JAnnotationInstance> stream() {
        return list.stream();
    }

    public void addAll(JAnnotationInstance... annotations){
        if(annotations!=null) {
            for (JAnnotationInstance a : annotations) {
                add(a);
            }
        }
    }

    public void add(JAnnotationInstance a){
        list.add(a);
    }

    public void remove(JAnnotationInstance a){
        list.remove(a);
        set.clear();
        set.addAll(list);
    }

    @Override
    public JAnnotationInstance[] toArray() {
        return list.toArray(new JAnnotationInstance[0]);
    }

    @Override
    public <T> T[] toArray(Class<T> cls) {
        return list.toArray((T[]) Array.newInstance(cls,0));
    }

    @Override
    public List<JAnnotationInstance> toList() {
        return list;
    }

    @Override
    public boolean contains(JAnnotationInstance a) {
        return set.contains(a);
    }
}
