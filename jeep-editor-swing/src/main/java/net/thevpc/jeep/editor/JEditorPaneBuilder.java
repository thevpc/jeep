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
    private JFooter footer;
    private JComponent component;

    public JEditorPaneBuilder() {
    }

    public JEditorPane editor(){
        if(editor==null){
            editor=new JEditorPane();
        }
        return editor;
    }

    public JEditorPaneBuilder addLineNumbers(){
        LineNumbersRuler.installEditor(editor());
        return this;
    }

    public JEditorPaneBuilder addAutoComplete(JCompletion completion){
        JAutoCompleteWindow.installEditor(editor(),completion);
        return this;
    }

    public JEditorPaneBuilder setEditorKit(String type, EditorKit k) {
        editor().setEditorKitForContentType(type,k);
        editor().setContentType(type);
        return this;
    }

    public JComponent component(){
        JEditorPane jEditorPane = editor();
        JScrollPane pane=new JScrollPane(jEditorPane);
        JPanel p=new JPanel(new BorderLayout());
        p.add(pane, BorderLayout.CENTER);
        if(footer!=null){
            p.add(footer.footer, BorderLayout.SOUTH);
        }
        return component=p;
    }

    public JFooter footer(){
        if(footer ==null){
            footer =new JFooter();
        }
        return footer;
    }

    public class JFooter {
        private Box footer;

        public JFooter() {
            this.footer = Box.createHorizontalBox();
        }

        public JFooter addGlue(){
            footer.add(Box.createHorizontalGlue());
            return this;
        }

        public JEditorPaneBuilder end(){
            return JEditorPaneBuilder.this;
        }

        public JFooter addCaret(){
            footer.add(new JCaretPosLabel(editor()));
            return this;
        }

        public JFooter add(JComponent component) {
            if(component!=null) {
                footer.add(component);
            }
            return this;
        }
    }
}
