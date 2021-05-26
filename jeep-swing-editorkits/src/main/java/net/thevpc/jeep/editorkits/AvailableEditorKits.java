package net.thevpc.jeep.editorkits;

import javax.swing.text.EditorKit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AvailableEditorKits {
    private static Map<String, Supplier<EditorKit>> available = new HashMap<>();

    static {
        register("text/x-cppsrc",()->new CppLangJSyntaxKit(true));
        register("text/x-c++hdr",()->new CppLangJSyntaxKit(true));
        register("text/x-csrc",()->new CppLangJSyntaxKit(false));
        register("text/java",()->new JavaJSyntaxKit());
        register("text/x-java",()->new JavaJSyntaxKit());
        register("text/javascript",()->new JavaJSyntaxKit());
        register("text/markdown",()->new MarkdownJSyntaxKit());
        register("text/x-nuts-text-format",()->new NTFJSyntaxKit());
        register("application/x-bash",()->new ShellLangJSyntaxKit(true));
        register("application/x-shellscript",()->new ShellLangJSyntaxKit(true));
        register("application/x-hadra",()->new ShellLangJSyntaxKit(true));
    }

    public static Map<String, Supplier<EditorKit>> getAvailable() {
        return available;
    }

    public static EditorKit create(String c) {
        Supplier<EditorKit> q = available.get(c);
        if(q!=null){
            return q.get();
        }
        return null;
    }

    public static void register(String c, Supplier<EditorKit> k) {
        if (k == null) {
            available.remove(c);
        } else {
            available.put(c, k);
        }
    }
}
