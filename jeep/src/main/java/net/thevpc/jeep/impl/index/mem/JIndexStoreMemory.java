package net.thevpc.jeep.impl.index.mem;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JIndexQuery;

import java.util.*;

public class JIndexStoreMemory implements JIndexStore {
    private Map<String, IndexedSource> sources = new HashMap<>();

    private IndexedElementType resolveIndexedElementType(String source, String elementType) {
        IndexedSource s = sources.get(source);
        if (s != null) {
            s = new IndexedSource(source);
            sources.put(source, s);
        }
        return s.getElementType(elementType,false);
    }

    protected IndexedSource getSource(String source, boolean create) {
        IndexedSource s = sources.get(source);
        if (s == null) {
            if (create) {
                s = new IndexedSource(source);
                sources.put(source, s);
            }
        }
        return s;
    }

    @Override
    public void index(String source, String elementType, JIndexedElement element, boolean force) {
        getSource(source, true).getElementType(elementType, true).index(element, force);
    }

    @Override
    public void index(String source, String elementType, JIndexDocument element, boolean force) {
        getSource(source, true).getElementType(elementType, true).index(element, force);
    }

    @Override
    public void removeIndex(String source, String elementType, String id) {
        getSource(source, true).getElementType(elementType, true).removeIndex(id);
    }

    @Override
    public void removeIndex(String source) {
        sources.remove(source);
    }

    @Override
    public <T extends JIndexedElement> Set<T> searchElements(String source, String elementType, JIndexQueryLookup... keyPairs) {
        if (source != null) {
            IndexedSource s = sources.get(source);
            if (s != null) {
                return s.search(elementType, keyPairs);
            }
            return Collections.emptySet();
        } else {
            HashSet<T> all = new HashSet<>();
            for (IndexedSource e : sources.values()) {
                all.addAll(e.search(elementType, keyPairs));
            }
            return all;
        }
    }

    @Override
    public <T extends JIndexedElement> Set<T> searchElements(String source, String elementType, JIndexQuery query) {
        return searchElements(source, elementType, query.getWheres());
    }

    @Override
    public Set<JIndexDocument> searchDocuments(String source, String elementType, JIndexQuery query) {
        return searchDocuments(source, elementType, query.getWheres());
    }

    @Override
    public Set<JIndexDocument> searchDocuments(String source, String elementType, JIndexQueryLookup... keyPairs) {
        if (source != null) {
            IndexedSource s = sources.get(source);
            if (s != null) {
                return s.searchDocuments(elementType, keyPairs);
            }
            return Collections.emptySet();
        } else {
            HashSet<JIndexDocument> all = new HashSet<>();
            for (IndexedSource e : sources.values()) {
                all.addAll(e.searchDocuments(elementType, keyPairs));
            }
            return all;
        }
    }

}
