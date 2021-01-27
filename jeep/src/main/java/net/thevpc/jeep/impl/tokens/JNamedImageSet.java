/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.tokens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author thevpc
 */
public class JNamedImageSet {

    private final Set<JNamedImage> ops = new HashSet<>();
    private final Map<String, JNamedImage> opsByImage = new HashMap<>();
    private final Map<String, JNamedImage> opsByName = new HashMap<>();
    private final Map<Integer, JNamedImage> opsById = new HashMap<>();

    public void remove(JNamedImage a) {
        if (!ops.contains(a)) {
            return;
        }
        boolean o = ops.remove(a);
        Set<JNamedImage> other = new HashSet<>();
        other.add(opsByImage.remove(a.image()));
        other.add(opsByName.remove(a.getIdName()));
        if (a.hasPreferredId()) {
            other.add(opsById.remove(a.getPreferredId()));
        }
        other.remove(null);
        other.remove(a);
        for (JNamedImage oo : other) {
            remove(oo);
        }
    }

    public boolean contains(JNamedImage a) {
        if (ops.contains(a)) {
            return true;
        }
        return ops.contains(a)
                || opsByImage.containsKey(a.getIdName())
                || opsByImage.containsKey(a.image())
                || (a.hasPreferredId() && opsById.containsKey(a.getPreferredId()));
    }

    public void addAll(JNamedImageSet coll) {
        if (coll != null) {
            for (JNamedImage a : coll.ops) {
                add(a);
            }
        }
    }

    public void addAll(Collection<JNamedImage> coll) {
        if (coll != null) {
            for (JNamedImage a : coll) {
                add(a);
            }
        }
    }

    public void addAll(JNamedImage... arr) {
        if (arr != null) {
            for (JNamedImage a : arr) {
                add(a);
            }
        }
    }

    public void add(JNamedImage a) {
        if (a != null) {
            if (ops.contains(a)) {
                return;
            }
            if (contains(a)) {
                throw new IllegalArgumentException("Already bound");
            }
            ops.add(a);
            opsByImage.put(a.image(), a);
            opsByName.put(a.getIdName(), a);
            if (a.hasPreferredId()) {
                opsById.put(a.getPreferredId(), a);
            }
        }
    }

    public void clear() {
        ops.clear();
        opsById.clear();
        opsByImage.clear();
        opsByName.clear();
    }

    public int size() {
        return ops.size();
    }

    public JNamedImage[] toArray() {
        return ops.toArray(new JNamedImage[0]);
    }

    public Set<JNamedImage> toSet() {
        return new LinkedHashSet<>(ops);
    }

    public List<JNamedImage> toList() {
        return new ArrayList<>(ops);
    }
}
