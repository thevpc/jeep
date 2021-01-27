package net.thevpc.jeep.editor.comp;

import net.thevpc.jeep.editor.JSyntaxDocument;
import net.thevpc.jeep.editor.JSyntaxView;
import net.thevpc.jeep.editor.JSyntaxUtils;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LineNumbersRuler extends JPanel
        implements CaretListener, DocumentListener, PropertyChangeListener/*, SyntaxComponent*/ {

    public static final int DEFAULT_R_MARGIN = 5;
    public static final int DEFAULT_L_MARGIN = 5;
    private String status;
    private final static int MAX_HEIGHT = Integer.MAX_VALUE - 1000000;
    //  Text component this TextTextLineNumber component is in sync with
    private JEditorPane editor;
    private int minimumDisplayDigits = 2;
    //  Keep history information to reduce the number of times the component
    //  needs to be repainted
    private int lastDigits;
    private int lastHeight;
    private int lastLine;
    private MouseListener mouseListener = null;
    // The formatting to use for displaying numbers.  Use in String.format(numbersFormat, line)
    private String numbersFormat = "%3d";

    private Color currentLineColor;

    /**
     * Returns the JScrollPane that contains this EditorPane, or null if no
     * JScrollPane is the parent of this editor
     */
    public JScrollPane getScrollPane(JTextComponent editorPane) {
        Container p = editorPane.getParent();
        while (p != null) {
            if (p instanceof JScrollPane) {
                return (JScrollPane) p;
            }
            p = p.getParent();
        }
        return null;
    }

//    @Override
//    public void config(Configuration config) {
//        int right = config.getInteger(PROPERTY_RIGHT_MARGIN, DEFAULT_R_MARGIN);
//        int left  = config.getInteger(PROPERTY_LEFT_MARGIN , DEFAULT_L_MARGIN);
//        Color foreground = config.getColor(PROPERTY_FOREGROUND, Color.BLACK);
//        setForeground(foreground);
//        Color back = config.getColor(PROPERTY_BACKGROUND, Color.WHITE);
//        setBackground(back);
//        setBorder(BorderFactory.createEmptyBorder(0, left, 0, right));
//        currentLineColor = config.getColor(PROPERTY_CURRENT_BACK, back);
//    }

//    @Override
    public static LineNumbersRuler installEditor(final JEditorPane editor) {
        LineNumbersRuler e=new LineNumbersRuler();
        e.install(editor);
        return e;
    }

    public void install(final JEditorPane editor) {
        this.editor = editor;

        int right = DEFAULT_R_MARGIN;//config.getInteger(PROPERTY_RIGHT_MARGIN, DEFAULT_R_MARGIN);
        int left  = DEFAULT_L_MARGIN;//config.getInteger(PROPERTY_LEFT_MARGIN , DEFAULT_L_MARGIN);
        Color foreground = Color.darkGray;//config.getColor(PROPERTY_FOREGROUND, Color.BLACK);
        setForeground(foreground);
        Color back = new Color(240, 240, 240);//config.getColor(PROPERTY_BACKGROUND, Color.WHITE);
        setBackground(back);
        setBorder(BorderFactory.createEmptyBorder(0, left, 0, right));
        currentLineColor = back;//config.getColor(PROPERTY_CURRENT_BACK, back);


        setFont(editor.getFont());

        // setMinimumDisplayDigits(3);
        Insets ein = editor.getInsets();
        if (ein.top != 0 || ein.bottom != 0) {
            Insets curr = getInsets();
            setBorder(BorderFactory.createEmptyBorder(ein.top, curr.left, ein.bottom, curr.right));
        }

        editor.getDocument().addDocumentListener(this);
        editor.addCaretListener(this);
        editor.addPropertyChangeListener(this);
        editor.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                JScrollPane sp = getScrollPane(editor);
                if (sp != null) sp.setRowHeaderView(LineNumbersRuler.this);
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {

            }

            @Override
            public void ancestorMoved(AncestorEvent event) {

            }
        });
        JScrollPane sp = getScrollPane(editor);
        if (sp != null) sp.setRowHeaderView(this);
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                GotoLineDialog.showForEditor(editor);
            }
        };
        addMouseListener(mouseListener);
        setPreferredWidth(false);    // required for toggle-lines to correctly repaint
        status = "INSTALLING";
    }

//    @Override
    public void deinstall(JEditorPane editor) {
        removeMouseListener(mouseListener);
        status = "DEINSTALLING";
        editor.getDocument().removeDocumentListener(this);
        editor.removeCaretListener(this);
        editor.removePropertyChangeListener(this);
        JScrollPane sp = getScrollPane(editor);
        if (sp != null) {
            sp.setRowHeaderView(null);
        }
    }

    /**
     *  Gets the minimum display digits
     *
     *  @return the minimum display digits
     */
    public int getMinimumDisplayDigits() {
        return minimumDisplayDigits;
    }

    /**
     *  Specify the minimum number of digits used to calculate the preferred
     *  width of the component. Default is 3.
     *
     *  @param minimumDisplayDigits  the number digits used in the preferred
     *                               width calculation
     */
    public void setMinimumDisplayDigits(int minimumDisplayDigits) {
        this.minimumDisplayDigits = minimumDisplayDigits;
        setPreferredWidth(false);
    }

    /**
     *  Calculate the width needed to display the maximum line number
     */
    private void setPreferredWidth(boolean force) {
        int lines  = JSyntaxUtils.getLineCount(editor);
        int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

        //  Update sizes when number of digits in the line number changes

        if (force || lastDigits != digits) {
            lastDigits = digits;
            numbersFormat = "%" + digits + "d";
            FontMetrics fontMetrics = getFontMetrics(getFont());
            int width = fontMetrics.charWidth('0') * digits;
            Insets insets = getInsets();
            int preferredWidth = insets.left + insets.right + width;

            Dimension d = getPreferredSize();
            d.setSize(preferredWidth, MAX_HEIGHT);
            setPreferredSize(d);
            setSize(d);
        }
    }

    /**
     *  Draw the line numbers
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fontMetrics = getFontMetrics(getFont());
        Insets insets = getInsets();
        int currentLine = -1;
        try {
            currentLine = JSyntaxUtils.getLineNumber(editor, editor.getCaretPosition());
        } catch (Exception ex) {
            // this won't happen, even if it does, we can ignore it and we will not have
            // a current line to worry about...
        }

        int lh = fontMetrics.getHeight();
        int maxLines = JSyntaxUtils.getLineCount(editor);
        JSyntaxView.setRenderingHits((Graphics2D) g);

        Rectangle clip = g.getClip().getBounds();
        int topLine    = (int) (clip.getY() / lh);
        int bottomLine = Math.min(maxLines, (int) (clip.getHeight() + lh - 1) / lh + topLine + 1);

        for (int line = topLine; line < bottomLine; line++) {
            String lineNumber = String.format(numbersFormat, line + 1);
            int y  = line * lh + insets.top;
            int yt = y + fontMetrics.getAscent();
            if (line == currentLine) {
                g.setColor(currentLineColor);
                g.fillRect(0, y /* - lh + fontMetrics.getDescent() - 1 */, getWidth(), lh);
                g.setColor(getForeground());
                g.drawString(lineNumber, insets.left, yt);
            } else {
                g.drawString(lineNumber, insets.left, yt);
            }
        }
    }

    //
//  Implement CaretListener interface
//
    @Override
    public void caretUpdate(CaretEvent e) {
        //  Get the line the caret is positioned on

        int caretPosition = editor.getCaretPosition();
        Element root = editor.getDocument().getDefaultRootElement();
        int currentLine = root.getElementIndex(caretPosition);

        //  Need to repaint so the correct line number can be highlighted

        if (lastLine != currentLine) {
            repaint();
            lastLine = currentLine;
        }
    }

    //
//  Implement DocumentListener interface
//
    @Override
    public void changedUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        documentChanged();
    }

    /*
     *  A document change may affect the number of displayed lines of text.
     *  Therefore the lines numbers will also change.
     */
    private void documentChanged() {
        //  Preferred size of the component has not been updated at the time
        //  the DocumentEvent is fired

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int preferredHeight = editor.getPreferredSize().height;

                //  Document change has caused a change in the number of lines.
                //  Repaint to reflect the new line numbers

                if (lastHeight != preferredHeight) {
                    setPreferredWidth(false);
                    repaint();
                    lastHeight = preferredHeight;
                }
            }
        });
    }

    /**
     * Implement PropertyChangeListener interface
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("document")) {
            if (evt.getOldValue() instanceof JSyntaxDocument) {
                JSyntaxDocument syntaxDocument = (JSyntaxDocument) evt.getOldValue();
                syntaxDocument.removeDocumentListener(this);
            }
            if (evt.getNewValue() instanceof JSyntaxDocument && "INSTALLING".equals(status)) {
                JSyntaxDocument syntaxDocument = (JSyntaxDocument) evt.getNewValue();
                syntaxDocument.addDocumentListener(this);
                setPreferredWidth(false);
                repaint();
            }
        } else if (prop.equals("font") && evt.getNewValue() instanceof Font) {
            setFont((Font) evt.getNewValue());
            setPreferredWidth(true);
        }
        // TODO - theoretically also track "insets"
    }

}
