package net.thevpc.jeep.impl.compiler;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class IteratorUtils {

    public static <T,V> Iterator<V> mapOptional(Iterator<T> base, Function<T, Optional<V>> f){
        return new Iterator<V>() {
            private V next;
            @Override
            public boolean hasNext() {
                while(true){
                    if(base.hasNext()){
                        T t = base.next();
                        Optional<V> o = f.apply(t);
                        if(o.isPresent()){
                            next=o.get();
                            return true;
                        }
                    }else{
                        next=null;
                        return false;
                    }
                }
            }
            @Override
            public V next() {
               return next;
            }
        };
    }

    public static <T,V> Iterator<V> map(Iterator<T> base, Function<T,V> f){
        return new Iterator<V>() {
            @Override
            public boolean hasNext() {
                return base.hasNext();
            }
            @Override
            public V next() {
                T n = base.next();
                return f.apply(n);
            }
        };
    }

    public static <T> Iterator<T> filter(Iterator<T> base,Predicate<T> p){
        return new Iterator<T>() {
            private T next;
            @Override
            public boolean hasNext() {
                while(true){
                    if(base.hasNext()){
                        next=base.next();
                        if(p.test(next)){
                            return true;
                        }
                    }else{
                        next=null;
                        return false;
                    }
                }
            }
            @Override
            public T next() {
                return next;
            }
        };
    }
}
