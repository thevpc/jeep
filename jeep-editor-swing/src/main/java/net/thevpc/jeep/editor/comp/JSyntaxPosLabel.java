package net.thevpc.jeep.editor.comp;

import net.thevpc.jeep.JCompletion;
import net.thevpc.jeep.JLocationContext;
import net.thevpc.jeep.JNode;
import net.thevpc.jeep.JNodePath;
import net.thevpc.jeep.editor.JSyntaxUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class JSyntaxPosLabel extends JLabel {
    private JTextComponent component;
    private JCompletion completion;
    private MyDocumentListener listener;
    public JSyntaxPosLabel(JTextComponent component,JCompletion completion) {
        this.component=component;
        this.completion=completion;
        setFont(component.getFont()/*.deriveFont(8f)*/);
        component.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateText();
            }
        });
        listener = new MyDocumentListener(completion, component);
        component.getDocument().addDocumentListener(listener);
        component.addPropertyChangeListener("document", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Document o = (Document) evt.getOldValue();
                if(o!=null){
                    o.removeDocumentListener(listener);
                }
                Document n = (Document) evt.getNewValue();
                if(n!=null){
                    n.addDocumentListener(listener);
                }
            }
        });
        updateText();
    }
    private void updateText(){
        int c = component.getCaretPosition();
        JLocationContext loc = completion.findLocation(c);
        if(loc==null){
            setText("");
        }else{
            setText(getPathString(loc));
        }
    }

    public String getPathString(JLocationContext loc){
        JNodePath p = loc.getNodePath();
        StringBuilder sb=new StringBuilder("<html>");
        for (JNode jNode : p) {
            if(sb.length()>0){
                sb.append("<font color=red>/</font>");
            }
            String simpleName = jNode.getClass().getSimpleName();
            if(simpleName.startsWith("HN")){
                simpleName=simpleName.substring(2);
            }
            sb.append(simpleName);
            if(jNode.getChildInfo()!=null) {
                sb.append("<font color=blue>(</font>");
                sb.append(jNode.getChildInfo());
                sb.append("<font color=blue>)</font>");
            }
        }
        if(loc.getToken()!=null){
            sb.append(":");
            sb.append("<font color=green>");
            sb.append(loc.getToken().name());
            sb.append("</font>");
        }
        if(loc.getPos()!=null){
            sb.append(":");
            sb.append(loc.getPos());
        }
        sb.append("</html>");
        return sb.toString();
    }

    private class MyDocumentListener implements DocumentListener {
        private final JCompletion completion;
        private final JTextComponent component;

        public MyDocumentListener(JCompletion completion, JTextComponent component) {
            this.completion = completion;
            this.component = component;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            completion.setCompilationUnit(component.getText(),
                    JSyntaxUtils.getSourceName(component));
            updateText();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            completion.setCompilationUnit(component.getText(), JSyntaxUtils.getSourceName(component));
            updateText();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            completion.setCompilationUnit(component.getText(), JSyntaxUtils.getSourceName(component));
            updateText();
        }
    }
}
