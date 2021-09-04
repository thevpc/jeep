/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.source;

import net.thevpc.jeep.source.impl.JTextSourceFile;
import net.thevpc.jeep.source.impl.JTextSourceFileURL;
import net.thevpc.jeep.source.impl.JTextSourceResourcesFolder;
import net.thevpc.jeep.source.impl.JTextSourceString;
import net.thevpc.jeep.source.impl.classpath.JTextSourceFolderURL;
import net.thevpc.jeep.source.impl.classpath.JTextSourceResourceFile;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author thevpc
 */
public class JTextSourceFactory {

    public static JTextSource fromString(String text, String sourceName) {
        for (JTextSource s : rootString(text, sourceName)) {
            return s;
        }
        return null;
    }

    public static JTextSource fromURI(URI url) {
        try {
            for (JTextSource s : rootURL(url.toURL())) {
                return s;
            }
            return null;
        } catch (MalformedURLException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static JTextSource fromURL(URL url) {
        for (JTextSource s : rootURL(url)) {
            return s;
        }
        return null;
    }

    public static JTextSource fromFile(Path file) {
        for (JTextSource s : rootFile(file.toFile())) {
            return s;
        }
        return null;
    }

    public static JTextSource fromFile(File file) {
        for (JTextSource s : rootFile(file)) {
            return s;
        }
        return null;
    }

    public static JTextSourceRoot rootFile(File file) {
        return new JTextSourceFile(file);
    }

    public static JTextSourceRoot rootString(String text, String sourceName) {
        return new JTextSourceString(text, sourceName);
    }

    public static JTextSourceRoot rootURL(URL url) {
        return new JTextSourceFileURL(url);
    }

    public static JTextSourceRoot rootURLFolder(URL url, String fileNameFilter) {
        return new JTextSourceFolderURL(url, fileNameFilter);
    }

    public static JTextSourceRoot rootResourceFolder(String url, String fileNameFilter) {
        return new JTextSourceResourcesFolder(url, fileNameFilter);
    }

    public static JTextSourceRoot rootResourceFile(String url) {
        return new JTextSourceResourceFile(url);
    }

    public static JTextSource fromURI(String file) {
        JTextSource last = null;
        for (JTextSource s : rootURI(file)) {
            if (last == null) {
                last = s;
            } else {
                throw new IllegalArgumentException("Too Many files");
            }
        }
        return last;
    }

    public static JTextSourceRoot rootURI(String uri) {
        if (uri.startsWith("file:")) {
            URI uri2 = null;
            try {
                uri2 = new URL(uri).toURI();
                if (uri2.getAuthority() != null && uri2.getAuthority().length() > 0) {
                    // Hack for UNC Path
                    uri2 = (new URL("file://" + uri.substring("file:".length()))).toURI();
                }
            } catch (URISyntaxException | MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
            return rootFile(Paths.get(uri2).toFile());
        } else if (uri.startsWith("string:")) {
            return rootString(uri.substring("string:".length()), "<Text>");
        } else if (uri.startsWith("http:") || uri.startsWith("https:")
                || uri.startsWith("jar:")
                || uri.startsWith("zip:")) {
            if (uri.endsWith("/")) {
                try {
                    return rootURLFolder(new URL(uri), null);
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                try {
                    return rootURL(new URL(uri));
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        } else if (uri.startsWith("resource:")) {
            if (uri.endsWith("/")) {
                return rootResourceFolder(uri.substring("resource:".length()), null);
            } else {
                return rootResourceFile(uri.substring("resource:".length()));
            }
        } else if (uri.matches("[a-z]{2,}:.*")) {
            //this is an url
            try {
                return rootURL(new URL(uri));
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            //this is a file
            return rootFile(Paths.get(uri).toFile());
        }
    }
}
