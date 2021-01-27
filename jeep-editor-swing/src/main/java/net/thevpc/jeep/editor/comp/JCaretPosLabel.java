package net.thevpc.jeep.editor.comp;

import net.thevpc.jeep.editor.JSyntaxUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

public class JCaretPosLabel extends JLabel {
    private JTextComponent component;
    public JCaretPosLabel(JTextComponent component) {
        this.component=component;
        setFont(component.getFont()/*.deriveFont(8f)*/);
        component.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateText();
            }
        });
        updateText();
    }
    private void updateText(){
        int c = component.getCaretPosition();
        int line = JSyntaxUtils.getLineNumber(component,c);
        int col = JSyntaxUtils.getColumnNumber(component,c);
        if(c<0 || line<0 || col<0){
            setText("");
        }
        setText((line+1)+":"+(col+1)+":"+(c+1));
    }
}
