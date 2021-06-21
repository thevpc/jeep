package net.thevpc.jeep.editor;

import net.thevpc.jeep.JToken;

import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSyntaxView extends PlainView {

    public static final String PROPERTY_TEXTAA = "TextAA";

    private static final Logger LOG = Logger.getLogger(JSyntaxView.class.getName());
    private JSyntaxStyle DEFAULT_STYLE;
    private final boolean singleColorSelect;
    private final int rightMarginColumn;
    private final Object textAAHint;
    private final JSyntaxStyleManager styles;
    /**
     * The values for the string key for Text Anti-Aliasing
     */
    private static RenderingHints sysHints;

    static {
        sysHints = null;
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            @SuppressWarnings("unchecked")
            Map<RenderingHints.Key,?> map = (Map<RenderingHints.Key,?>)
                    toolkit.getDesktopProperty("awt.font.desktophints");
            sysHints = new RenderingHints(map);
        } catch (Throwable ex) {
            LOG.log(Level.FINE,ex,()->"error storing 'awt.font.desktophints'");
            //ignore...
        }
    }
    /**
     * Construct a new view using the given configuration and prefix given
     *
     * @param element
//     * @param config
//     * @param prefix
     */
    public JSyntaxView(Element element, JSyntaxStyleManager styles /*,Configuration config, String prefix*/) {
        super(element);
        DEFAULT_STYLE = styles.getTokenIdStyle(0);
        singleColorSelect =false;
        rightMarginColumn =120;
        String textaa = "ON";//"DEFAULT";
        textAAHint = TEXT_AA_HINT_NAMES.get(textaa);
        this.styles=styles;
    }

    @Override
    protected int drawUnselectedText(Graphics graphics, int x, int y, int p0,
                                     int p1) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                textAAHint);
        setRenderingHits((Graphics2D) graphics);
        Font saveFont = graphics.getFont();
        Color saveColor = graphics.getColor();
        JSyntaxDocument doc = (JSyntaxDocument) getDocument();
        Segment segment = getLineBuffer();
        drawRightMarginColumn(graphics, y);
        try {
            // Colour the parts
            Iterator<JToken> i = doc.getTokens(p0, p1);
            int start = p0;
            while (i.hasNext()) {
                JToken t = i.next();
                // t and s are the actual start and length of what we should
                // put on the screen.  assume these are the whole token....
                int l = t.length();
                int s = t.startCharacterNumber;
                // ... unless the token starts before p0:
                if (s < p0) {
                    // token is before what is requested. adjust the length and s
                    l -= (p0 - s);
                    s = p0;
                }
                // if token end (s + l is still the token end pos) is greater
                // than p1, then just put up to p1
                if (s + l > p1) {
                    l = p1 - s;
                }
                doc.getText(s, l, segment);
                x = styles.drawText(segment, x, y, graphics, this, t);
                start = t.endCharacterNumber;
            }
            // now for any remaining text not tokenized:
            if (start < p1) {
                doc.getText(start, p1 - start, segment);
                x = DEFAULT_STYLE.drawText(segment, x, y, graphics, this, start);
            }
        } catch (BadLocationException ex) {
            LOG.log(Level.SEVERE, "Bad Location "+ex.offsetRequested(), ex);
        } finally {
            graphics.setFont(saveFont);
            graphics.setColor(saveColor);
        }
        return x;
    }

    @Override
    protected int drawSelectedText(Graphics graphics, int x, int y, int p0, int p1)
            throws BadLocationException {
        if (singleColorSelect) {
            drawRightMarginColumn(graphics, y);
            return super.drawUnselectedText(graphics, x, y, p0, p1);
        } else {
            return drawUnselectedText(graphics, x, y, p0, p1);
        }
    }

    private void drawRightMarginColumn(Graphics graphics, int y) {
        if (rightMarginColumn > 0) {
            int m_x = rightMarginColumn * graphics.getFontMetrics().charWidth('m');
            int h = graphics.getFontMetrics().getHeight();
            Color c = styles.getRightMarginColor();
            if (c == null) {
                c = Color.BLACK;
            }
            graphics.setColor(c);
            graphics.drawLine(m_x, y, m_x, y - h);
        }
    }

    @Override
    protected void updateDamage(javax.swing.event.DocumentEvent changes,
                                Shape a,
                                ViewFactory f) {
        super.updateDamage(changes, a, f);
        java.awt.Component host = getContainer();
        host.repaint();
    }

    /**
     * Sets the Rendering Hints o nthe Graphics.  This is used so that
     * any painters can set the Rendering Hits to match the view.
     * @param g2d
     */
    public static void setRenderingHits(Graphics2D g2d) {
        if(sysHints!=null){
            g2d.addRenderingHints(sysHints);
        }
    }

    /**
     * The values for the string key for Text Anti-Aliasing
     */
    private static Map<String, Object> TEXT_AA_HINT_NAMES =
            new HashMap<String, Object>();

    static {
        TEXT_AA_HINT_NAMES.put("DEFAULT", RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        TEXT_AA_HINT_NAMES.put("GASP", RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        TEXT_AA_HINT_NAMES.put("HBGR", RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);
        TEXT_AA_HINT_NAMES.put("HRGB", RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        TEXT_AA_HINT_NAMES.put("VBGR", RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR);
        TEXT_AA_HINT_NAMES.put("VRGB", RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR);
        TEXT_AA_HINT_NAMES.put("OFF", RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        TEXT_AA_HINT_NAMES.put("ON", RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}

