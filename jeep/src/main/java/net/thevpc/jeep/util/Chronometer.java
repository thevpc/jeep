/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.jeep.util;

import java.io.Serializable;

/**
 * @author taha.bensalah@gmail.com
 */
public class Chronometer implements Serializable {

    private final static long serialVersionUID = 1L;
    private long accumulated;
    private long startDate;
    private long endDate;
    private String name;
    private long lastTime;
    private boolean running;

    public static Chronometer start() {
        Chronometer c = new Chronometer();
        c.startNow();
        return c;
    }

    public static Chronometer start(String name) {
        Chronometer c = new Chronometer(name);
        c.startNow();
        return c;
    }

    private Chronometer() {
    }

    public Chronometer copy() {
        Chronometer c = new Chronometer();
        c.name = name;
        c.endDate = endDate;
        c.startDate = startDate;
        c.accumulated = accumulated;
        c.lastTime = lastTime;
        c.running = running;
        return c;
    }

    /**
     * restart chronometer and returns a stopped snapshot/copy of the current
     *
     * @return
     */
    public Chronometer restart() {
        stop();
        Chronometer c = copy();
        startNow();
        return c;
    }

    /**
     * restart chronometer with new name and returns a stopped snapshot/copy of
     * the current (with old name)
     *
     * @param newName
     * @return
     */
    public Chronometer restart(String newName) {
        stop();
        Chronometer c = copy();
        setName(newName);
        startNow();
        return c;
    }

    public Chronometer(String name) {
        this.name = name;
        startNow();
    }

    public Chronometer setName(String desc) {
        this.name = desc;
        return this;
    }

    public Chronometer updateDescription(String desc) {
        setName(desc);
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isStarted() {
        return startDate != 0;
    }

    public boolean isStopped() {
        return endDate == 0;
    }

    public Chronometer startNow() {
        endDate = 0;
        startDate = System.nanoTime();
        lastTime = startDate;
        accumulated = 0;
        running = true;
        return this;
    }

    public Chronometer accumulate() {
        if (running) {
            long n = System.nanoTime();
            accumulated += n - lastTime;
            lastTime = n;
        }
        return this;
    }

    public TimeDuration lap() {
        if (running) {
            long n = System.nanoTime();
            long lapValue = n - lastTime;
            this.accumulated += lapValue;
            lastTime = n;
            return new TimeDuration(lapValue);
        }
        return new TimeDuration(0);
    }

    public boolean isSuspended() {
        return !running;
    }

    public Chronometer suspend() {
        if (running) {
            long n = System.nanoTime();
            accumulated += n - lastTime;
            lastTime = -1;
            running = false;
        }
        return this;
    }

    public Chronometer resume() {
        if (!running) {
            lastTime = System.nanoTime();
            running = true;
        }
        return this;
    }

    public Chronometer stop() {
        if (running) {
            endDate = System.nanoTime();
            accumulated += endDate - lastTime;
            lastTime = -1;
            running = false;
        }
        return this;
    }

    public long getStartTime() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public TimeDuration getDuration() {
        if (startDate == 0) {
            return new TimeDuration(0);
        }
        if (running) {
            long curr = System.nanoTime() - lastTime;
            return new TimeDuration(curr + accumulated);
        }
        return new TimeDuration(accumulated);

    }

    public long getTime() {
        if (startDate == 0) {
            return 0;
        }
        if (running) {
            long curr = System.nanoTime() - lastTime;
            return (curr + accumulated);
        }
        return accumulated;
    }

    public String toString() {
        String s = name == null ? "" : name + "=";
        return s + getDuration().toString();
    }


    public static class TimeDuration{
        private long duration;

        public TimeDuration(long duration) {
            this.duration = duration;
        }

        public long getTime() {
            return duration;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            boolean started = false;
            int h = getHours();
            int mn = getMinutes();
            int s = getSeconds();
            int ms = getMilliSeconds();
            int us = getMicroSeconds();
            int ns = getNanoSeconds();
            if (h > 0) {
                if (started) {
                    sb.append(" ");
                }
                sb.append(formatRight(h, 2)).append("h");
                started = true;
            }
            if (mn > 0 || started) {
                if (started) {
                    sb.append(" ");
                }
                sb.append(formatRight(mn, 2)).append("mn");
                started = true;
            }
            if (s > 0 || started) {
                if (started) {
                    sb.append(" ");
                }
                sb.append(formatRight(s, 2)).append("s");
                started = true;
            }
            if (s > 0 || started) {
                if (started) {
                    sb.append(" ");
                }
                sb.append(formatRight(ms, 3)).append("ms");
                started = true;
            }
            if (s > 0 || started) {
                if (started) {
                    sb.append(" ");
                }
                sb.append(formatRight(us, 3)).append("us");
                started = true;
            }
            if (started) {
                sb.append(" ");
            }
            sb.append(formatRight(ns, 3)).append("ns");
            return sb.toString();
        }

        public int getHours() {
            return (int) (toHours() % 24);
        }

        public int getMinutes() {
            return (int) (toMinutes() % 60L);
        }

        public int getSeconds() {
            return (int) (toSeconds() % 60L);
        }

        public int getMilliSeconds() {
            return (int) (toMilliSeconds() % 1000L);
        }

        public int getMicroSeconds() {
            return (int) (toMicroSeconds() % 1000);
        }

        public int getNanoSeconds() {
            return (int) (getTime() % 1000);
        }

        public long toHours() {
            return getTime() / (60000000000L * 60);
        }

        public long toMinutes() {
            return getTime() / 60000000000L;
        }

        public long toSeconds() {
            return getTime() / 1000000000;
        }

        public long toMilliSeconds() {
            return getTime() / 1000000;
        }

        public long toMicroSeconds() {
            return getTime() / 1000;
        }

        private static String formatRight(Object number, int size) {
            String s = String.valueOf(number);
            int len = s.length();
            int bufferSize = Math.max(size, len);
            StringBuilder sb = new StringBuilder(bufferSize);
            for (int i = bufferSize-len; i >0 ; i--) {
                sb.append(' ');
            }
            sb.append(s);
            return sb.toString();
        }

    }
}
