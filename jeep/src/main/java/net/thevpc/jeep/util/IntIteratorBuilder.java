package net.thevpc.jeep.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class IntIteratorBuilder {
    public static Comparator<int[]> SUM_COMPARATOR = new IntTuple2Comparator();
    private int[] from;
    private int[] to;
    private long max = -1;

    public static IntIteratorBuilder iter() {
        return new IntIteratorBuilder();
    }

    private static long sum(int[] a) {
        long x = a[0];
        for (int i = 1; i < a.length; i++) {
            x += a[i];
        }
        return x;
    }

    public IntIteratorBuilder max(int max) {
        this.max = max;
        return this;
    }

    public IntIteratorBuilder from(int... from) {
        if (to != null && from != null && to.length != from.length) {
            throw new IllegalArgumentException("Dimension mismatch : expected dimension of " + to.length);
        }
        this.from = from;
        return this;
    }

    public IntIteratorBuilder to(int... to) {
        if (to != null && from != null && to.length != from.length) {
            throw new IllegalArgumentException("Dimension mismatch : expected dimension of " + from.length);
        }
        this.to = to;
        return this;
    }

    public IntIterator depthFirst() {
        if (to == null) {
            throw new IllegalArgumentException("Missing To");
        }
        return new IntIteratorByMax((from == null) ? new int[to.length] : from, to, max);
    }

    public IntIterator breadthFirst() {
        if (from == null && to == null) {
            throw new IllegalArgumentException("Missing From");
        }
        if (from == null) {
            from = new int[to.length];
        }
        return new IntIteratorBySum(from, to, max);
    }


    private static class IntTuple2Comparator implements Comparator<int[]> {
        @Override
        public int compare(int[] o1, int[] o2) {
            long s1 = sum(o1);
            long s2 = sum(o2);
            int c = Long.compare(s1, s2);
            if (c != 0) {
                return c;
            }
            for (int i = 0; i < o1.length; i++) {
                c = Integer.compare(o1[i], o2[i]);
                if (c != 0) {
                    return c;
                }
            }
            return 0;
        }
    }

    public static class IntIteratorBySum implements IntIterator {
        private int[] from;
        private int[] current;
        private long maxElements;
        private long visitedElements;
        private boolean finished;
        private int[] to;

        private IntIteratorBySum(int[] from, int[] to, long maxElements) {
            if (from == null || from.length == 0) {
                throw new IllegalArgumentException("Invalid init tuple");
            }
            this.from = from;
            this.to = to;
            for (int value : from) {
                if (value < 0) {
                    throw new IllegalArgumentException("Invalid from. Expected positive (or null) integers");
                }
            }
            this.maxElements = maxElements;
        }
        @Override
        public void remove() {
            throw new IllegalArgumentException("Unsupported");
        }

        private int getMax(int i) {
            int x = -1;
            if (to != null && i < to.length) {
                x = to[i];
            }
            return x;
        }
//        public static net.thevpc.common.util.IntIteratorBySum ofDim(int a, long maxElements) {
//            return new net.thevpc.common.util.IntIteratorBySum(new int[a], maxElements);
//        }
//
//        public static net.thevpc.common.util.IntIteratorBySum startWith(int[] a, long maxElements) {
//            return new IntIteratorBySum(a, maxElements);
//        }


        @Override
        public boolean isInfinite() {
            return true;
        }

        @Override
        public void next(int[] recipient) {
            if (current == null) {
                current = from;
            } else {
                long sum = sum(current);
                inc(current, current.length - 1, (int) sum);
            }
            System.arraycopy(current, 0, recipient, 0, current.length);
        }

        public List<int[]> next(int count) {
            List<int[]> a = new ArrayList<>(count);
            while (count > 0 && hasNext()) {
                int[] next = next();
//            System.out.println(Arrays.toString(next));
                a.add(next);
                count--;
            }
            return a;
        }

        @Override
        public boolean hasNext() {
            if (!finished && (maxElements < 0 || visitedElements < maxElements)) {
                if (current == null) {
                    current = from;
                    return true;
                } else {
                    while (true) {
                        long sum = sum(current);
                        inc(current, current.length - 1, (int) sum);
                        int acceptThis = current.length;
                        if (to != null) {
                            for (int i = 0; i < current.length; i++) {
                                int m = to[i];
                                if (m>=0 && current[i] >= m) {
                                    acceptThis--;
                                }
                            }
                        }
                        if (acceptThis == current.length) {
                            return true;
                        }
                        if (acceptThis == 0) {
                            finished = true;
                            return false;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public int[] next() {
            visitedElements++;

            return Arrays.copyOf(current, current.length);
        }

        private void inc(int[] current, int index, int max) {
            if (current.length == 1) {
                current[0]++;
                return;
            }
            if (max == 0) {
                current[current.length - 1]++;
                return;
            }
            if (index == current.length - 1) {
                if (current[index] > 0) {
                    current[index]--;
                    current[index - 1]++;
                } else {
                    inc(current, index - 1, max);
                }
            } else {
                if (current[index] == 0) {
                    inc(current, index - 1, max);
                } else if (current[index] == 1) {
                    if (index == 0) {
                        current[index] = 0;
                        current[current.length - 1] = max + 1;
                    } else {
                        current[index - 1]++;
                        current[index]--;
                    }
                } else {
                    if (index == 0) {
                        if (current[index] == max) {
                            for (int i = 0; i < current.length - 1; i++) {
                                current[index] = 0;
                            }
                            current[current.length - 1] = max + 1;
                        } else {
                            current[index]++;
                        }
                    } else {
                        int a = current[index];
                        current[current.length - 1] += a - 1;
                        current[index] = 0;
                        current[index - 1] += 1;
                    }
                }
            }
        }
    }

    public static class IntIteratorByMax implements IntIterator {
        private int[] start;
        private int[] current;
        private int[] max;
        private boolean started;
        private boolean finished;
        private long maxElements;
        private long visitedElements;

        private IntIteratorByMax(int[] start, int[] max, long maxElements) {
            if (max == null || max.length == 0) {
                throw new IllegalArgumentException("Invalid init tuple");
            }
            if (start == null) {
                start = new int[max.length];
            }
            if (start.length != max.length) {
                throw new IllegalArgumentException("Invalid tuple dimension " + start.length + " <> " + max.length);
            }
            this.max = max;
            this.start = start;
            this.maxElements = maxElements;
        }

        @Override
        public boolean isInfinite() {
            return false;
        }

        @Override
        public void remove() {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        public void next(int[] recipient) {
            if (current == null || finished) {
                throw new IllegalArgumentException("No more elements or you missed to call hasNext()");
            }
            System.arraycopy(current, 0, recipient, 0, current.length);
        }

        @Override
        public List<int[]> next(int count) {
            List<int[]> a = new ArrayList<>(count);
            while (count > 0 && hasNext()) {
                int[] next = next();
                a.add(next);
                count--;
            }
            return a;
        }

        @Override
        public boolean hasNext() {
            if (finished) {
                return false;
            }
            if (!started) {
                started = true;
                if (maxElements == 0) {
                    finished = true;
                    return false;
                }
                boolean ok = false;
                for (int i = 0; i < start.length; i++) {
                    if (start[i] < max[i]) {
                        ok = true;
                    }
                }
                if (ok) {
                    this.current = Arrays.copyOf(start, start.length);
                    visitedElements++;
                } else {
                    visitedElements++;
                    finished = true;
                    return false;
                }
            } else {
                int depth = 0;
                while (depth < current.length) {
                    current[depth]++;
                    if (current[depth] >= max[depth]) {
                        current[depth] = start[depth];
                        depth++;
                    } else {
                        break;
                    }
                }
                if (depth >= current.length || (maxElements > 0 && visitedElements > maxElements)) {
                    finished = true;
                    return false;
                }
                visitedElements++;
            }
            return true;
        }

        @Override
        public int[] next() {
            if (current == null || finished) {
                throw new IllegalArgumentException("No more elements or you missed to call hasNext()");
            }
            return Arrays.copyOf(current, current.length);
        }

    }

}
