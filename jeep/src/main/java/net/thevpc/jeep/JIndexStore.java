package net.thevpc.jeep;

import net.thevpc.jeep.core.JIndexQuery;

import java.util.Set;

public interface JIndexStore {
    void index(String source, String elementType, JIndexDocument document,boolean force);
    void index(String source, String elementType, JIndexedElement element,boolean force);

    void removeIndex(String source, String elementType, String id);

    void removeIndex(String source);

    <T extends JIndexedElement>  Set<T> searchElements(String source, String elementType, JIndexQuery query);
    <T extends JIndexedElement>  Set<T> searchElements(String source, String elementType, JIndexQueryLookup... keyPairs);

    Set<JIndexDocument> searchDocuments(String source, String elementType, JIndexQuery query);
    Set<JIndexDocument> searchDocuments(String source, String elementType, JIndexQueryLookup... keyPairs);
}
