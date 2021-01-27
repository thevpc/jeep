package net.thevpc.jeep.core.index;

import net.thevpc.jeep.JIndexDocument;

import java.util.*;

import net.thevpc.jeep.JIndexEntry;

public class DefaultJIndexDocument implements JIndexDocument {
    private String id;
    private Set<JIndexEntry> searchableEntries =new LinkedHashSet<>();
    private Set<JIndexEntry> nonSearchableEntries =new LinkedHashSet<>();

    public DefaultJIndexDocument(String id) {
        this.id=id;
    }

    @Override
    public JIndexDocument add(String key, String value) {
        return add(key,value,true,true);
    }

    @Override
    public JIndexDocument add(String key, String value, boolean searchable) {
        return add(key,value,searchable,true);
    }

    @Override
    public JIndexDocument add(String key, String value, boolean searchable, boolean stored) {
        if(searchable) {
            searchableEntries.add(new JIndexEntry(key, value));
        }else{
            nonSearchableEntries.add(new JIndexEntry(key, value));
        }
        return this;
    }

    public void remove(String key, String value) {
        JIndexEntry entry = new JIndexEntry(key, value);
        searchableEntries.remove(entry);
        nonSearchableEntries.remove(entry);
    }

    @Override
    public Set<JIndexEntry> getSearchableEntries() {
        return Collections.unmodifiableSet(searchableEntries);
    }

    @Override
    public String getValue(String key) {
        for (JIndexEntry value : searchableEntries) {
            if(value.getKey().equals(key)){
                return value.getValue();
            }
        }
        return null;
    }

    public String[] getSearchableEntries(String key) {
        Set<String> s = new HashSet<>();
        for (JIndexEntry value : searchableEntries) {
            if(value.getKey().equals(key)){
                s.add(value.getValue());
            }
        }
        return s.toArray(new String[0]);
    }


    public Set<JIndexEntry> getAllEntries() {
        Set<JIndexEntry> s = new HashSet<>();
        s.addAll(searchableEntries);
        s.addAll(nonSearchableEntries);
        return s;
    }



    @Override
    public String getId() {
        return id;
    }

//    @Override
    public Set<String> keySet() {
        Set<String> s = new HashSet<>();
        for (JIndexEntry value : searchableEntries) {
            s.add(value.getKey());
        }
        for (JIndexEntry value : nonSearchableEntries) {
            s.add(value.getKey());
        }
        return s;
    }

    @Override
    public boolean equals(Object o) {
        return equalsById(o);
    }

    @Override
    public int hashCode() {
        return hashCodeById();
    }
    public boolean equalsById(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultJIndexDocument that = (DefaultJIndexDocument) o;
        return id.equals(that.id);
    }

    public int hashCodeById() {
        return Objects.hash(id);
    }

    public boolean equalsFull(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultJIndexDocument that = (DefaultJIndexDocument) o;
        return id.equals(that.id)
                && searchableEntries.equals(that.searchableEntries)
                && nonSearchableEntries.equals(that.nonSearchableEntries)
                ;
    }

    public int hashCodeFull() {
        return Objects.hash(id, searchableEntries,nonSearchableEntries);
    }

    @Override
    public String toString() {
        return "IndexDocument{" +
                "id='" + id + '\'' +
                ", searchableEntries=" + searchableEntries +
                ", nonSearchableEntries=" + nonSearchableEntries +
                '}';
    }
}
