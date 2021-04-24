package net.thevpc.jeep.editor;

import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;
import java.awt.*;

public final class JSyntaxStyle {

    public static final int BOLD = Font.BOLD;
    public static final int ITALIC = Font.ITALIC;
    public static final int PLAIN = Font.PLAIN;
    public static final int BOXED = 8;
    public static final int FILLED = 16;
    public static final int JAGGED = 32;
    public static final int UNDERLINE = 64;
    public static final int CROSS_OUT = 128;
    private ColorResource color;
    private ColorResource boxColor = ColorResource.of(Color.RED);
    private ColorResource fillColor = ColorResource.of(Color.decode("#EEEEEE"));
    private int fontStyle;

    public JSyntaxStyle() {
        super();
    }

    public JSyntaxStyle(ColorResource color, boolean bold, boolean italic) {
        super();
        this.color = color;
        setBold(bold);
        setItalic(italic);
    }

    public JSyntaxStyle(ColorResource color, int fontStyle) {
        super();
        this.color = color;
        this.fontStyle = fontStyle;
    }

    JSyntaxStyle(String str) {
        String[] parts = str.split("\\s*,\\s*");
        if (parts.length != 2) {
            throw new IllegalArgumentException("style not correct format: " + str);
        }
        this.color = ColorResource.of(new Color(Integer.decode(parts[0])));
        this.fontStyle = Integer.decode(parts[1]);
    }

    public boolean isBold() {
        return (fontStyle & Font.BOLD) != 0;
    }

    public JSyntaxStyle setBold(boolean bold) {
        if (bold) {
            fontStyle |= Font.BOLD;
        } else {
            int mask = -1 ^ Font.BOLD;
            fontStyle = (fontStyle & (mask));
        }
        return this;
    }

    public String getColorString() {
        return String.format("0x%06x", color.get().getRGB() & 0x00ffffff);
    }

    public JSyntaxStyle setColorString(ColorResource color) {
        this.color = color;
        return this;
    }
    public JSyntaxStyle setColorString(String color) {
        this.color = ColorResource.of(Color.decode(color));
        return this;
    }

    public ColorResource getBoxColor() {
        return boxColor;
    }

    public JSyntaxStyle setBoxColor(ColorResource boxColor) {
        this.boxColor = boxColor;
        return this;
    }

    public ColorResource getFillColor() {
        return fillColor;
    }

    public JSyntaxStyle setFillColor(ColorResource fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public Boolean isItalic() {
        return (fontStyle & Font.ITALIC) != 0;
    }

    public JSyntaxStyle setItalic(boolean italic) {
        if (italic) {
            fontStyle |= Font.ITALIC;
        } else {
            fontStyle = (fontStyle & (-1 ^ Font.ITALIC));
        }
        return this;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public JSyntaxStyle setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public ColorResource getColor() {
        return color;
    }

    public JSyntaxStyle setColor(ColorResource color) {
        this.color = color;
        return this;
    }

    /**
     * Draw text.  This can directly call the Utilities.drawTabbedText.
     * Sub-classes can override this method to provide any other decorations.
     *
     * @param segment     - the source of the text
     * @param x           - the X origin &gt;= 0
     * @param y           - the Y origin &gt;= 0
     * @param graphics    - the graphics context
     * @param e           - how to expand the tabs. If this value is null, tabs will be
     *                    expanded as a space character.
     * @param startOffset - starting offset of the text in the document &gt;= 0
     * @return size
     */
    public int drawText(Segment segment, int x, int y,
                        Graphics graphics, TabExpander e, int startOffset) {
        graphics.setFont(graphics.getFont().deriveFont(getFontStyle()));
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int a = fontMetrics.getAscent();
        int h = a + fontMetrics.getDescent();
        int w = Utilities.getTabbedTextWidth(segment, fontMetrics, 0, e, startOffset);
        int rX = x - 1;
        int rY = y - a;
        int rW = w + 2;
        int rH = h;
        if ((getFontStyle() & FILLED) != 0) {
            ColorResource cr = getFillColor();
            Color c = cr==null?null:cr.get();
            if (c == null) {
                c = Color.decode("#EEEEEE");
            }
            graphics.setColor(c);
            graphics.fillRect(rX, rY, rW, rH);
        }
        graphics.setColor(getColor()==null?null:getColor().get());
        x = Utilities.drawTabbedText(segment, x, y, graphics, e, startOffset);
        if ((getFontStyle() & BOXED) != 0) {
            Color c = getBoxColor()==null?null:getBoxColor().get();
            if (c == null) {
                c = Color.RED;
            }
            graphics.setColor(c);
            graphics.drawRect(rX, rY, rW, rH);
        }
        if ((getFontStyle() & JAGGED) != 0) {
            Color c = getBoxColor()==null?null:getBoxColor().get();
            if (c == null) {
                c = Color.RED;
            }
            paintJaggedLine(graphics, rY + rH, rX, rX + rW, c);
        }
        if ((getFontStyle() & UNDERLINE) != 0) {
            Color c = getBoxColor()==null?null:getBoxColor().get();
            if (c == null) {
                c = Color.RED;
            }
            paintUnderline(graphics, rY + rH, rX, rX + rW, c);
        }
        if ((getFontStyle() & CROSS_OUT) != 0) {
            Color c = getBoxColor()==null?null:getBoxColor().get();
            if (c == null) {
                c = Color.RED;
            }
            paintUnderline(graphics, (rY + rH)/2, rX, rX + rW, c);
        }
        return x;
    }

    public void paintJaggedLine(Graphics g, int y, int x1, int x2, Color color) {
        Color old = g.getColor();
        g.setColor(color);
        for (int i = x1; i + 12 <= x2; i += 6) {
            g.drawArc(i + 3, y - 3, 3, 3, 0, 180);
            g.drawArc(i + 6, y - 3, 3, 3, 180, 181);
        }
        g.setColor(old);
    }

    public void paintUnderline(Graphics g, int y, int x1, int x2, Color color) {
        Color old = g.getColor();
        g.setColor(color);
        g.drawLine(x1,y,x2,y);
        g.setColor(old);
    }

}