package net.thevpc.jeep.editor;

import net.thevpc.jeep.JToken;
import net.thevpc.jeep.impl.tokens.JTokensStringFormat;
import net.thevpc.jeep.impl.tokens.JTokensStringFormatHtml;

import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The STyles to use for each TokenType.  The defaults are created here, and
 * then the resource META-INF/services/syntaxstyles.properties is read and
 * merged.  You can also pass a properties instance and merge your preferred
 * styles into the default styles
 *
 * @author Ayman
 */
public class JSyntaxStyleManager {

//    /**
//     * You can call the mergeStyles method with a Properties file to customize
//     * the existing styles.  Any existing styles will be overwritten by the
//     * styles you provide.
//     * @param styles
//     */
//    public void mergeStyles(Properties styles) {
//        for (String token : styles.stringPropertyNames()) {
//            String stv = styles.getProperty(token);
//            try {
//                TokenType tt = TokenType.valueOf(token);
//                JSyntaxStyle tokenStyle = new JSyntaxStyle(stv);
//                put(tt, tokenStyle);
//            } catch (IllegalArgumentException ex) {
//                LOG.warning("illegal token type or style for: " + token);
//            }
//        }
//    }
    private Map<Integer, JSyntaxStyle> tokenIdStyles;
    private Color rightMarginColor=new Color(0xFF7777);
    private Color caretColor=Color.BLACK;
    private static final Logger LOG = Logger.getLogger(JSyntaxStyleManager.class.getName());

    private static JSyntaxStyle NO_STYLE = new JSyntaxStyle("NO-STYLE");
    private static JSyntaxStyle DEFAULT_STYLE = new JSyntaxStyle("DEFAULT",
            ColorResource.of("TextPane.foreground", Color.BLACK)
            , Font.PLAIN);
    private static Font defaultFont;
    private Font font;
    private boolean useDefaultFont=true;
    private JTokensStringFormat tokensFormat=new JTokensStringFormatHtml(){
        @Override
        public HtmlStyle getStyle(int id, int ttype) {
            JSyntaxStyleManager jssm = JSyntaxStyleManager.this;
            JSyntaxStyle s = jssm.getTokenIdStyle(id);

            Color c = s.getColor()==null?null:s.getColor().get();
            String cs=c==null?null:String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
            return new HtmlStyle(cs,s.getFontStyle());
        }
    };

    public JTokensStringFormat getTokensFormat() {
        return tokensFormat;
    }

    public JSyntaxStyleManager() {
        setTokenIdStyle(0,NO_STYLE);
    }

    public static Font getDefaultFont() {
        if(defaultFont==null){
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fonts = ge.getAvailableFontFamilyNames();
            Arrays.sort(fonts);
            for (String s : new String[]{"Monospaced", "Courier new", "Courier"}) {
                if (Arrays.binarySearch(fonts, s) >= 0) {
                    defaultFont = new Font(s, Font.PLAIN, 13);
                    break;
                }
            }
        }
        return defaultFont;
    }

    public boolean isUseDefaultFont() {
        return useDefaultFont;
    }

    public JSyntaxStyleManager setUseDefaultFont(boolean useDefaultFont) {
        this.useDefaultFont = useDefaultFont;
        return this;
    }

    public Font getFont() {
        if(font==null){
            return getDefaultFont();
        }
        return font;
    }

    public JSyntaxStyleManager setFont(Font font) {
        this.font = font;
        return this;
    }

    public Color getCaretColor() {
        return caretColor;
    }

    public JSyntaxStyleManager setCaretColor(Color caretColor) {
        this.caretColor = caretColor;
        return this;
    }

    /**
     * Create default styles
     * @return
     */
    private static JSyntaxStyleManager createInstance() {
        JSyntaxStyleManager syntaxstyles = new JSyntaxStyleManager();
//        syntaxstyles.put(0,new JSyntaxStyle());
//        Properties styles = JarServiceProvider.readProperties(JSyntaxStyleManager.class);
//        syntaxstyles.mergeStyles(styles);
        return syntaxstyles;
    }

    public void setTokenIdStyle(int type, JSyntaxStyle style) {
        if (tokenIdStyles == null) {
            tokenIdStyles = new HashMap<Integer, JSyntaxStyle>();
        }
        tokenIdStyles.put(type, style);
    }

    public Color getRightMarginColor() {
        return rightMarginColor;
    }

    public JSyntaxStyleManager setRightMarginColor(Color rightMarginColor) {
        this.rightMarginColor = rightMarginColor;
        return this;
    }

    /**
     * Set the graphics font and others to the style for the given token
     * @param g
     * @param type
     * @deprecated
     */
    @Deprecated
    public void setGraphicsStyle(Graphics g, int type) {
        JSyntaxStyle ss = getTokenIdStyle(type);
        g.setFont(g.getFont().deriveFont(ss.getFontStyle()));
        g.setColor(ss.getColor().get());
    }

    /**
     * Return the style for the given TokenType
     * @param type
     * @return
     */
    public JSyntaxStyle getTokenIdStyle(int type) {
        if (tokenIdStyles.containsKey(type)) {
            return tokenIdStyles.get(type);
        } else {
            return DEFAULT_STYLE;
        }
    }

    /**
     * Draw the given Token.  This will simply find the proper JSyntaxStyle for
     * the TokenType and then asks the proper Style to draw the text of the
     * Token.
     * @param segment
     * @param x
     * @param y
     * @param graphics
     * @param e
     * @param token
     * @return
     */
    public int drawText(Segment segment, int x, int y,
                        Graphics graphics, TabExpander e, JToken token) {
        JSyntaxStyle s = getTokenIdStyle(token.def.id);
        if(s==null){
            s= getTokenIdStyle(0);
        }
        return s.drawText(segment, x, y, graphics, e, token.startCharacterNumber);
    }
}
