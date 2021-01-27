package net.thevpc.jeep;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JIndexerRepository<T extends JIndexer> {
    private Function<String,T> factory;
    private Map<String, T> indexers = new HashMap<>();

    public JIndexerRepository(Function<String, T> factory) {
        this.factory = factory;
    }

    public T getIndexer(String projectId) {
        T i = indexers.get(projectId);
        if (i == null) {
            i = factory.apply(projectId);
            indexers.put(projectId, i);
        }
        return i;
    }
}
