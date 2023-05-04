package net.thevpc.jeep.impl.index.mem;

import net.thevpc.jeep.JIndexDocument;
import net.thevpc.jeep.JIndexQueryLookup;
import net.thevpc.jeep.JIndexedElement;

import java.util.*;

public class IndexedSource {
    private String sourceUuid;
    private Map<String, IndexedElementType> elementTypes = new HashMap<>();

    public IndexedSource(String sourceUuid) {
        this.sourceUuid = sourceUuid;
    }

    public void removeIndex(String elementType) {
        elementTypes.remove(elementType);
    }

    public IndexedElementType getElementType(String elementType, boolean create) {
        IndexedElementType s = elementTypes.get(elementType);
        if (s == null) {
            if (create) {
                s = new IndexedElementType(elementType,sourceUuid);
                elementTypes.put(elementType, s);
            }
        }
        return s;
    }

    public <T extends JIndexedElement> Set<T> search(String elementType, JIndexQueryLookup... keyPairs) {
        if (elementType != null) {
            IndexedElementType s = elementTypes.get(elementType);
            if (s != null) {
                return s.search(keyPairs);
            }
            return Collections.emptySet();
        } else {
            HashSet<JIndexedElement> all = new HashSet<>();
            for (IndexedElementType e : elementTypes.values()) {
                all.addAll(e.search(keyPairs));
            }
            return (Set<T>) all;
        }
    }

    public Set<JIndexDocument> searchDocuments(String elementType, JIndexQueryLookup... keyPairs) {
        if (elementType != null) {
            IndexedElementType s = elementTypes.get(elementType);
            if (s != null) {
                return s.searchDocuments(keyPairs);
            }
            return Collections.emptySet();
        } else {
            HashSet<JIndexDocument> all = new HashSet<>();
            for (IndexedElementType e : elementTypes.values()) {
                all.addAll(e.searchDocuments(keyPairs));
            }
            return all;
        }
    }
}
