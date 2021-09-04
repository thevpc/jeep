package net.thevpc.jeep.source;

import java.util.Iterator;

public interface JTextSourceRoot extends Iterable<JTextSource>{
    String getId();
    Iterable<JTextSource> iterate(JTextSourceReport log);

    @Override
    default Iterator<JTextSource> iterator() {
        return iterate(null).iterator();
    }
}
