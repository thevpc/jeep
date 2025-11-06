package net.thevpc.jeep.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ImplicitValue<V, Sv, Uv> {
    private List<Supplier<Sv>> suppliers = new ArrayList<>();
    private List<Sv> suppliedValues = new ArrayList<>();
    private boolean evaluated;
    protected Uv userDefinedValue;
    protected V value;
    protected Combiner<V, Sv, Uv> combiner;

    interface Combiner<V, V1, V2> {
        V combine(List<V1> v1, V2 v2);
    }

    public static <X> ImplicitValue<List<X>, List<X>, List<X>> ofList() {
        return new ListImplicitValue<>();
    }

    public static <K, X> ImplicitValue<List<X>, List<X>, List<X>> ofUniqueList(Function<X, K> keyProvider) {
        return new UniqueListImplicitValue<>(keyProvider);
    }
    public static <K, X> MapForListImplicitValue<K,X> ofMappedList(Function<X, K> keyProvider) {
        return new MapForListImplicitValue<>(keyProvider);
    }

    public static <K, X> MapImplicitValue<K, X> ofMap(Function<X, K> keyProvider) {
        return new MapImplicitValue<>(keyProvider);
    }

    public ImplicitValue() {
    }

    public ImplicitValue<V, Sv, Uv> addSupplier(Supplier<Sv> supplier) {
        this.suppliers.add(supplier);
        this.suppliedValues.add(null);
        this.evaluated = false;
        return this;
    }

    protected void invalidate() {
        evaluated = false;
        List<Sv> values = this.suppliedValues;
        for (int i = 0; i < values.size(); i++) {
            values.set(i, null);
        }
        value = null;
    }

    public V value() {
        build(true);
        return value;
    }

    protected void build(boolean ensureSupplied) {
        if (ensureSupplied) {
            if (!evaluated) {
                for (int i = 0; i < suppliedValues.size(); i++) {
                    suppliedValues.set(i, suppliers.get(i).get());
                }
                evaluated = true;
            }
        }
        value = combiner.combine(suppliedValues, userDefinedValue);
    }

    public static class ListImplicitValue<X> extends ImplicitValue<List<X>, List<X>, List<X>> {
        public ListImplicitValue() {
            this.combiner = new Combiner<List<X>, List<X>, List<X>>() {
                @Override
                public List<X> combine(List<List<X>> v1, List<X> v2) {
                    List<X> l = new ArrayList<>();
                    if (v1 != null) {
                        for (List<X> v11 : v1) {
                            if (v11 != null) {
                                l.addAll(v11);
                            }
                        }
                    }
                    if (v2 != null) {
                        l.addAll(v2);
                    }
                    return l;
                }
            };
        }
    }

    public static class UniqueListImplicitValue<K, X> extends ImplicitValue<List<X>, List<X>, List<X>> {
        public UniqueListImplicitValue(Function<X, K> keyProvider) {
            this.combiner = new Combiner<List<X>, List<X>, List<X>>() {
                @Override
                public List<X> combine(List<List<X>> v1, List<X> v2) {
                    Map<K, X> l = new LinkedHashMap<>();
                    if (v1 != null) {
                        for (List<X> v11 : v1) {
                            if(v11!=null){
                                for (X x : v11) {
                                    if (x != null) {
                                        K k = keyProvider.apply(x);
                                        if (!l.containsKey(k)) {
                                            l.put(k, x);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (v2 != null) {
                        for (X x : v2) {
                            if (x != null) {
                                K k = keyProvider.apply(x);
                                if (!l.containsKey(k)) {
                                    l.put(k, x);
                                }
                            }
                        }
                    }
                    return new ArrayList<>(l.values());
                }
            };
        }

        public void clear() {
            if (userDefinedValue != null && !userDefinedValue.isEmpty()) {
                userDefinedValue.clear();
                invalidate();
            }
        }

        public void addAll(List<X> list) {
            if (list != null && !list.isEmpty()) {
                if (userDefinedValue == null) {
                    userDefinedValue = new ArrayList<>();
                }
                userDefinedValue.addAll(list);
                invalidate();
            }
        }

        public void add(X v) {
            if (userDefinedValue == null) {
                userDefinedValue = new ArrayList<>();
            }
            userDefinedValue.add(v);
            invalidate();
        }
    }

    public static class MapForListImplicitValue<K, X> extends ImplicitValue<Map<K, X>, List<X>, List<X>> {
        Function<X, K> keyProvider;
        public MapForListImplicitValue(Function<X, K> keyProvider) {
            this.keyProvider=keyProvider;
            this.combiner=new Combiner<Map<K, X>, List<X>, List<X>>() {
                @Override
                public Map<K, X> combine(List<List<X>> v1, List<X> v2) {
                    Map<K, X> l = new LinkedHashMap<>();
                    if (v1 != null) {
                        for (List < X > v11 : v1) {
                            for (X x : v11) {
                                if (x != null) {
                                    K k = keyProvider.apply(x);
                                    if (!l.containsKey(k)) {
                                        l.put(k, x);
                                    }
                                }
                            }
                        }
                    }
                    if (v2 != null) {
                        for (X x : v2) {
                            if (x != null) {
                                K k = keyProvider.apply(x);
                                if (!l.containsKey(k)) {
                                    l.put(k, x);
                                }
                            }
                        }
                    }
                    return l;
                }
            };
        }

        public void addAll(List<X> list) {
            if (list != null && !list.isEmpty()) {
                if (userDefinedValue == null) {
                    userDefinedValue = new ArrayList<>();
                }
                userDefinedValue.addAll(list);
                invalidate();
            }
        }

        public void add(X x) {
            if (userDefinedValue == null) {
                userDefinedValue = new ArrayList<>();
            }
            userDefinedValue.add(x);
            invalidate();
        }

        public X get(K fieldName) {
            build(true);
            return value.get(fieldName);
        }

        public boolean isEmpty() {
            if (userDefinedValue != null) {
                if(userDefinedValue.isEmpty()){
                    return false;
                }
            }
            build(true);
            if (value != null) {
                if (!value.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public List<X> values() {
            build(true);
            if (value == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(value.values());
        }
    }

    public static class MapImplicitValue<K, X> extends ImplicitValue<Map<K, X>, Map<K, X>, Map<K, X>> {
        private Function<X, K> keyProvider;

        public MapImplicitValue(Function<X, K> keyProvider) {
            this.keyProvider = keyProvider;
            this.combiner = new Combiner<Map<K, X>, Map<K, X>, Map<K, X>>() {
                @Override
                public Map<K, X> combine(List<Map<K, X>> v1, Map<K, X> v2) {
                    Map<K, X> l = new LinkedHashMap<>();
                    if (v1 != null) {
                        for (Map<K, X> v11 : v1) {
                            if(v11!=null){
                                for (Map.Entry<K, X> x : v11.entrySet()) {
                                    if (x != null) {
                                        if (!l.containsKey(x.getKey())) {
                                            l.put(x.getKey(), x.getValue());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (v2 != null) {
                        for (Map.Entry<K, X> x : v2.entrySet()) {
                            if (x != null) {
                                if (!l.containsKey(x.getKey())) {
                                    l.put(x.getKey(), x.getValue());
                                }
                            }
                        }
                    }
                    return l;
                }
            };
        }

        public X get(K fieldName) {
            build(true);
            return value.get(fieldName);
        }

        public void add(X f) {
            put(keyProvider.apply(f), f);
        }

        public void put(K name, X f) {
            if (userDefinedValue == null) {
                userDefinedValue = new LinkedHashMap<>();
            }
            userDefinedValue.put(name, f);
            invalidate();
        }

        public List<X> values() {
            build(true);
            if (value == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(value.values());
        }

        public boolean isEmpty() {
            build(true);
            if (value != null) {
                if (!value.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

}
