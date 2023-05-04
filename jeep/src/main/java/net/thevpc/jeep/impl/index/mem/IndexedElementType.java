package net.thevpc.jeep.impl.index.mem;

import net.thevpc.jeep.JIndexDocument;
import net.thevpc.jeep.JIndexEntry;
import net.thevpc.jeep.JIndexQueryLookup;
import net.thevpc.jeep.JIndexedElement;

import java.util.*;

public class IndexedElementType {
    private String parentPath;
    private String elementTypeName;
    private Map<JIndexEntry, Set<String>> entries = new HashMap<>();
    private Map<String, IndexedItem> items = new HashMap<>();
//        private Map<String, JIndexedElement> idToElement = new HashMap<>();
//        private Map<String, Set<JIndexEntry>> reverseElements = new HashMap<>();
//        private Map<String, JIndexDocument> idToDocument = new HashMap<>();

    public IndexedElementType(String elementTypeName, String parentPath) {
        this.elementTypeName = elementTypeName;
        this.parentPath = parentPath;
    }

    public <T extends JIndexedElement> Set<T> search(JIndexQueryLookup... keyPairs) {
        Set<String> all = searchItems(keyPairs);
        if (all.isEmpty()) {
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
        if (all.isEmpty()) {
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
            Set<String> found = null;
            switch (lookup.getOp()) {
                case EQ: {
                    found = entries.get(new JIndexEntry(lookup.getKey(), lookup.getValue()));
                    break;
                }
                case NE: {
                    JIndexEntry r = new JIndexEntry(lookup.getKey(), lookup.getValue());
                    found = new HashSet<>();
                    for (Map.Entry<JIndexEntry, Set<String>> es : entries.entrySet()) {
                        if (!es.getKey().equals(r)) {
                            if (es.getValue() != null) {
                                found.addAll(es.getValue());
                            }
                        }
                    }
                    break;
                }
                case DOT_START: {
                    found = new HashSet<>();
                    for (Map.Entry<JIndexEntry, Set<String>> es : entries.entrySet()) {
                        if (es.getKey().getKey().equals(lookup.getKey())) {
                            String vv = lookup.getValue();
                            String v2 = es.getKey().getValue();
                            if (v2.equals(vv) || v2.startsWith(vv + ".")) {
                                if (es.getValue() != null) {
                                    found.addAll(es.getValue());
                                }
                            }
                        }
                    }
                    break;
                }
                case EXISTS: {
                    found = new HashSet<>();
                    for (Map.Entry<JIndexEntry, Set<String>> es : entries.entrySet()) {
                        if (es.getKey().getKey().equals(lookup.getKey())) {
                            if (es.getValue() != null) {
                                found.addAll(es.getValue());
                            }
                        }
                    }
                    break;
                }
            }
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

    public void index(JIndexedElement element, boolean force) {
        String id = element.getId();
        if (!force && items.containsKey(id)) {
            return;
        }
        index0(id, new IndexedItem(element));
    }

    public void index(JIndexDocument document, boolean force) {
        String id = document.getId();
        if (!force && items.containsKey(id)) {
            return;
        }
        index0(id, new IndexedItem(document));
    }

    public void index0(String id, IndexedItem item) {
        IndexedItem old = items.put(id, item);
        if (old != null) {
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
            //fullName='com.oracle.net.Sdp.SdpSocket'
//            if(ip.getKey().equals("fullName") && ip.getValue().contains("Hadruwaves")) {
//                System.out.println("[" + parentPath + "][" + elementTypeName + "] " + ip + " " + id);
//            }
            all.add(id);
        }
    }

    public void removeIndex(String id) {
        IndexedItem old = items.get(id);
        if (old != null) {
            for (JIndexEntry se : old.document.getSearchableEntries()) {
                Set<String> ids = entries.get(se);
                ids.remove(id);
            }
            items.remove(id);
        }
    }
}
