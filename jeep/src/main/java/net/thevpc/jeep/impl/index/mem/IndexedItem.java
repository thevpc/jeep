package net.thevpc.jeep.impl.index.mem;

import net.thevpc.jeep.JIndexDocument;
import net.thevpc.jeep.JIndexedElement;

public class IndexedItem {
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
