package net.thevpc.jeep.util;

import java.util.Iterator;
import java.util.List;

public interface IntIterator extends Iterator<int[]> {
    boolean isInfinite();

    void next(int[] recipient);

    List<int[]> next(int count);
}
