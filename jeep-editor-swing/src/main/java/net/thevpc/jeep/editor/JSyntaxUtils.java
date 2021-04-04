package net.thevpc.jeep.editor;

import net.thevpc.common.textsource.JTextSource;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;

public class JSyntaxUtils {

    public static final String SOURCE_NAME = "JSyntax.SourceName";

    public static String getSourceName(JTextComponent component) {
        return (String) component.getClientProperty(SOURCE_NAME);
    }

    public static void setSourceName(JTextComponent component, String sourceName) {
        component.putClientProperty(SOURCE_NAME, sourceName);
    }

    public static void setText(JTextComponent component, JTextSource source) {
        component.putClientProperty(SOURCE_NAME, source.name());
        component.setText(source.text());
    }

    /**
     * A helper function that will return the SyntaxDocument attached to the
     * given text component. Return null if the document is not a
     * SyntaxDocument, or if the text component is null
     */
    public static JSyntaxDocument getSyntaxDocument(JTextComponent component) {
        if (component == null) {
            return null;
        }
        Document doc = component.getDocument();
        if (doc instanceof JSyntaxDocument) {
            return (JSyntaxDocument) doc;
        } else {
            return null;
        }
    }

    public static int getLineCount(JTextComponent pane) {
        JSyntaxDocument sdoc = getSyntaxDocument(pane);
        if (sdoc != null) {
            return sdoc.getLineCount();
        }
        int count = 0;
        try {
            Document doc = pane.getDocument();
            if (doc instanceof PlainDocument) {
                int lineNumber = 1;
                String ss = pane.getText();
                for (char c : ss.toCharArray()) {
                    if (c == '\n') {
                        lineNumber++;
                    }
                }
                return lineNumber;
            }
            int p = doc.getLength() - 1;
            if (p > 0) {
                count = getLineNumber(pane, p);
            }
        } catch (Exception ex) {
            Logger.getLogger(JSyntaxUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    /**
     * Gets the Line Number at the give position of the editor component. The
     * first line number is ZERO
     *
     * @return line number
     */
    public static int getLineNumber(JTextComponent editor, int pos) {
        if (getSyntaxDocument(editor) != null) {
            JSyntaxDocument sdoc = getSyntaxDocument(editor);
            return sdoc.getLineNumberAt(pos);
        } else {
            Document doc = editor.getDocument();
            if (doc instanceof PlainDocument) {
                class Counter implements DocElementVisitor {

                    Segment segment = new Segment();
                    int lineNumber = 1;

                    @Override
                    public DocElementVisitorRet visit(Element elem) {
                        int s = elem.getStartOffset();
                        int e = elem.getEndOffset();
                        if (s > pos) {
                            return DocElementVisitorRet.EXIT;
                        }
                        if (elem.isLeaf()) {
                            try {
                                String ss = doc.getText(s, e);
                                for (char c : ss.toCharArray()) {
                                    if (c == '\n') {
                                        lineNumber++;
                                    }
                                }
                            } catch (BadLocationException ex) {
                                //ignore
                            }
                        }
                        return DocElementVisitorRet.CONTINUE;
                    }
                }
                Counter counter = new Counter();
                PlainDocument pd = (PlainDocument) doc;
                visitDocElement(pd.getDefaultRootElement(), counter);
                return counter.lineNumber;
            }
            return doc.getDefaultRootElement().getElementIndex(pos);
        }
    }

    public enum DocElementVisitorRet {
        EXIT,
        CONTINUE,
        SKIP_CHILDREN
    }

    public interface DocElementVisitor {

        /**
         * return true to continue
         *
         * @param e e
         * @return return true to continue
         */
        DocElementVisitorRet visit(Element e);
    }

    private static boolean visitDocElement(Element e, DocElementVisitor v) {
        DocElementVisitorRet r = v.visit(e);
        if (r == DocElementVisitorRet.EXIT) {
            return false;
        }
        if (r == DocElementVisitorRet.CONTINUE) {
            int c = e.getElementCount();
            for (int i = 0; i < c; i++) {
                if (!visitDocElement(e.getElement(i), v)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the column number at given position of editor. The first column is
     * ZERO
     *
     * @return the 0 based column number
     */
    public static int getColumnNumber(JTextComponent editor, int pos) {
        // speedup if the pos is 0
        if (pos == 0) {
            return 0;
        }
        Rectangle r = null;
        try {
            r = editor.modelToView(pos);
        } catch (BadLocationException e) {
            return -1;
        }
        if(r==null){
            return -1;
        }
        int start = editor.viewToModel(new Point(0, r.y));
        int column = pos - start;
        return column;
    }

    public static Rectangle getScreenBoundsForPoint(int x, int y) {
        GraphicsEnvironment env = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        for (GraphicsDevice device : devices) {
            GraphicsConfiguration config = device.getDefaultConfiguration();
            Rectangle gcBounds = config.getBounds();
            if (gcBounds.contains(x, y)) {
                return gcBounds;
            }
        }
        // If point is outside all monitors, default to default monitor (?)
        return env.getMaximumWindowBounds();
    }
}
