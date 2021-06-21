package net.thevpc.jeep.editorkits;

import javax.swing.*;
import javax.swing.text.EditorKit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AvailableEditorKits {
    private static Map<String, Supplier<EditorKit>> available = new HashMap<>();

    static {
        register(() -> new CppLangJSyntaxKit(true), "text/x-c++src","text/x-c++hdr");
        register(() -> new CppLangJSyntaxKit(false), "text/x-csrc","text/x-chdr");
        register(() -> new JavaJSyntaxKit(), "text/java","text/x-java");
        register(() -> new JavascriptJSyntaxKit(), "text/javascript");
        register(() -> new JsonJSyntaxKit(), "application/json");
        register(() -> new CssJSyntaxKit(), "text/css");
        register(() -> new MarkdownJSyntaxKit(), "text/markdown");
        register(() -> new NTFJSyntaxKit(), "text/x-nuts-text-format");
        register(() -> new ShellLangJSyntaxKit(true), "application/x-bash","application/x-shellscript");
        register(() -> new HadraJSyntaxKit(), "application/x-hadra");
        register(() -> new BibtexJSyntaxKit(), "application/x-bibtex");
        register(() -> new XmlJSyntaxKit(), "text/xml");
        register(() -> new XmlJSyntaxKit(), "text/html");
        register(() -> new TexJSyntaxKit(), "application/x-latex");
        register(() -> new TexJSyntaxKit(), "application/x-tex");
        register(() -> new SqlJSyntaxKit(), "application/sql");
    }

    public static Map<String, Supplier<EditorKit>> getAvailable() {
        return available;
    }

    public static void installEditorKits(JEditorPane editor) {
        for (Map.Entry<String, Supplier<EditorKit>> ee : AvailableEditorKits.getAvailable().entrySet()) {
            editor.setEditorKitForContentType(ee.getKey(), ee.getValue().get());
        }
    }
    public static EditorKit create(String c) {
        Supplier<EditorKit> q = available.get(c);
        if (q != null) {
            return q.get();
        }
        return null;
    }

    public static void register(Supplier<EditorKit> k, String ... cc) {
        for (String c : cc) {
            if (k == null) {
                available.remove(c);
            } else {
                available.put(c, k);
            }
        }
    }
}
