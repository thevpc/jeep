package net.thevpc.jeep;

import java.util.Set;

public interface JIndexDocument {
    JIndexDocument add(String key, String value);

    JIndexDocument add(String key, String value, boolean searchable);

    JIndexDocument add(String key, String value, boolean searchable, boolean stored);

    Set<JIndexEntry> getSearchableEntries();

    Set<JIndexEntry> getAllEntries();

    String getValue(String key);
    String[] getSearchableEntries(String key);
    String getId();
}
