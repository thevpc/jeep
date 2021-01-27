package net.thevpc.jeep;

import net.thevpc.jeep.core.JIndexQuery;

import java.util.*;

public class JIndexStoreMemory implements JIndexStore{
    private Map<String, IndexedSource> sources = new HashMap<>();

    private IndexedElementType resolveIndexedElementType(String source, String elementType) {
        IndexedSource s = sources.get(source);
        if (s != null) {
            s = new IndexedSource(source);
            sources.put(source, s);
        }
        return s.elementTypes.get(elementType);
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
    public void index(String source, String elementType, JIndexedElement element,boolean force) {
        getSource(source, true).getElementType(elementType, true).index(element,force);
    }
    @Override
    public void index(String source, String elementType, JIndexDocument element,boolean force) {
        getSource(source, true).getElementType(elementType, true).index(element,force);
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
    public <T extends JIndexedElement>  Set<T> searchElements(String source, String elementType, JIndexQueryLookup... keyPairs) {
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
    public <T extends JIndexedElement>  Set<T> searchElements(String source, String elementType, JIndexQuery query) {
        return searchElements(source,elementType,query.getWheres());
    }

    @Override
    public Set<JIndexDocument> searchDocuments(String source, String elementType, JIndexQuery query) {
        return searchDocuments(source,elementType,query.getWheres());
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

    public static class IndexedSource {
        private String sourceUuid;
        private Map<String, IndexedElementType> elementTypes = new HashMap<>();

        public IndexedSource(String sourceUuid) {
            this.sourceUuid = sourceUuid;
        }

        public void removeIndex(String elementType){
            elementTypes.remove(elementType);
        }

        private IndexedElementType getElementType(String elementType, boolean create) {
            IndexedElementType s = elementTypes.get(elementType);
            if (s == null) {
                if (create) {
                    s = new IndexedElementType(elementType);
                    elementTypes.put(elementType, s);
                }
            }
            return s;
        }

        public <T extends JIndexedElement>  Set<T> search(String elementType, JIndexQueryLookup... keyPairs) {
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

    public static class IndexedItem {
        JIndexedElement element;
        JIndexDocument document;

        public IndexedItem(JIndexDocument document) {
            this.document = document;
        }

        public IndexedItem(JIndexedElement element) {
            this.element = element;
            this.document = element.toDocument();
        }
        //        Set<JIndexEntry> entries=new HashSet<>();
    }

    public static class IndexedElementType {
        private String elementTypeName;
        private Map<JIndexEntry, Set<String>> entries = new HashMap<>();
        private Map<String, IndexedItem> items = new HashMap<>();
//        private Map<String, JIndexedElement> idToElement = new HashMap<>();
//        private Map<String, Set<JIndexEntry>> reverseElements = new HashMap<>();
//        private Map<String, JIndexDocument> idToDocument = new HashMap<>();

        public IndexedElementType(String elementTypeName) {
            this.elementTypeName = elementTypeName;
        }

        public <T extends JIndexedElement>  Set<T> search(JIndexQueryLookup... keyPairs) {
            Set<String> all = searchItems(keyPairs);
            if(all.isEmpty()){
                return Collections.emptySet();
            }
            int size = all.size();
            HashSet s = new HashSet(size);
            for (String id : all) {
                s.add(getElementById(id));
            }
            return s;
        }

        public Set<JIndexDocument> searchDocuments(JIndexQueryLookup... keyPairs) {
            Set<String> all = searchItems(keyPairs);
            if(all.isEmpty()){
                return Collections.emptySet();
            }
            int size = all.size();
            HashSet<JIndexDocument> s = new HashSet<JIndexDocument>(size);
            for (String id : all) {
                s.add(getDocumentById(id));
            }
            return s;
        }

        private Set<String> searchItems(JIndexQueryLookup... keyPairs) {
            if (keyPairs.length == 0) {
                return items.keySet();
            }
            Set<String> ok = null;
            for (int i = 0; i < keyPairs.length; i++) {
                JIndexQueryLookup lookup = keyPairs[i];
                Set<String> found = entries.get(new JIndexEntry(lookup.getKey(),lookup.getValue()));
                if (found != null) {
                    if (ok == null) {
                        ok = new HashSet<>(found);
                    } else {
                        ok.retainAll(found);
                    }
                    if (ok.isEmpty()) {
                        return Collections.emptySet();
                    }
                } else {
                    return Collections.emptySet();
                }
            }
            if (ok == null) {
                return Collections.emptySet();
            }
            return ok;
        }

        protected JIndexedElement getElementById(String id) {
            return items.get(id).element;
        }

        protected JIndexDocument getDocumentById(String id) {
            return items.get(id).document;
        }

        public void index(JIndexedElement element,boolean force) {
            String id = element.getId();
            if(!force && items.containsKey(id)){
                return;
            }
            index0(id,new IndexedItem(element));
        }

        public void index(JIndexDocument document,boolean force) {
            String id = document.getId();
            if(!force && items.containsKey(id)){
                return;
            }
            index0(id,new IndexedItem(document));
        }

        public void index0(String id, IndexedItem item) {
            IndexedItem old = items.put(id,item);
            if(old!=null){
                for (JIndexEntry se : old.document.getSearchableEntries()) {
                    Set<String> ids = entries.get(se);
                    ids.remove(id);
                }
            }
            for (JIndexEntry ip : item.document.getSearchableEntries()) {
                Set<String> all = entries.get(ip);
                if (all == null) {
                    all = new HashSet<>();
                    entries.put(ip, all);
                }
                all.add(id);
            }
        }

        public void removeIndex(String id) {
            IndexedItem old = items.get(id);
            if(old!=null){
                for (JIndexEntry se : old.document.getSearchableEntries()) {
                    Set<String> ids = entries.get(se);
                    ids.remove(id);
                }
                items.remove(id);
            }
        }
    }
}
