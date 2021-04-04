package net.thevpc.jeep.editor;

import net.thevpc.jeep.JCompletion;
import net.thevpc.jeep.editor.comp.JAutoCompleteWindow;
import net.thevpc.jeep.editor.comp.JCaretPosLabel;
import net.thevpc.jeep.editor.comp.LineNumbersRuler;

import javax.swing.*;
import javax.swing.text.EditorKit;
import java.awt.*;

public class JEditorPaneBuilder {

    private JEditorPane editor;
    private JAutoCompleteWindow extAutoComplete;
    private LineNumbersRuler extLineNumbersRuler;
    private JHeader header;
    private JFooter footer;
    private JComponent component;
    private boolean noScroll;

    public JEditorPaneBuilder() {
    }

    public JEditorPaneBuilder setEditor(JEditorPane editor) {
        this.editor=editor;
        return this;
    }
    public JEditorPane editor() {
        if (editor == null) {
            editor = new JEditorPane();
        }
        return editor;
    }

    public boolean isNoScroll() {
        return noScroll;
    }

    public JEditorPaneBuilder setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
        return this;
    }
    

    public JEditorPaneBuilder addLineNumbers() {
        LineNumbersRuler.installEditor(editor());
        return this;
    }

    public JEditorPaneBuilder addAutoComplete(JCompletion completion) {
        JAutoCompleteWindow.installEditor(editor(), completion);
        return this;
    }

    public JEditorPaneBuilder setEditorKit(String type, EditorKit k) {
        editor().setEditorKitForContentType(type, k);
        editor().setContentType(type);
        return this;
    }

    public JComponent component() {
        if (component == null) {
            JEditorPane jEditorPane = editor();
            JComponent base=noScroll?jEditorPane:new JScrollPane(jEditorPane);
            if (footer != null || header != null) {
                JPanel p = new JPanel(new BorderLayout());
                p.add(base, BorderLayout.CENTER);
                if (footer != null) {
                    p.add(footer.content, BorderLayout.SOUTH);
                }
                if (header != null) {
                    p.add(header.content, BorderLayout.NORTH);
                }
                p.setBorder(null);
                component = p;
            } else {
                component = base;
            }
        }
        return component;
    }

    public JFooter footer() {
        if (footer == null) {
            footer = new JFooter();
        }
        return footer;
    }

    public JHeader header() {
        if (header == null) {
            header = new JHeader();
        }
        return header;
    }

    public class JHeader {

        private Box content;

        public JHeader() {
            this.content = Box.createHorizontalBox();
        }

        public JHeader addGlue() {
            content.add(Box.createHorizontalGlue());
            return this;
        }

        public JEditorPaneBuilder end() {
            return JEditorPaneBuilder.this;
        }

        public JHeader addCaret() {
            content.add(new JCaretPosLabel(editor()));
            return this;
        }

        public JHeader add(JComponent component) {
            if (component != null) {
                content.add(component);
            }
            return this;
        }
    }

    public class JFooter {

        private Box content;

        public JFooter() {
            this.content = Box.createHorizontalBox();
        }

        public JFooter addGlue() {
            content.add(Box.createHorizontalGlue());
            return this;
        }

        public JEditorPaneBuilder end() {
            return JEditorPaneBuilder.this;
        }

        public JFooter addCaret() {
            content.add(new JCaretPosLabel(editor()));
            return this;
        }

        public JFooter add(JComponent component) {
            if (component != null) {
                content.add(component);
            }
            return this;
        }
    }
}
