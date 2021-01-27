package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.JToken;

import java.util.HashMap;
import java.util.Map;

public class JTokensStringFormatHtml extends AbstractJTokensStringFormat {
    public static final int PLAIN=0;
    public static final int BOLD=1;
    public static final int ITALIC=2;
    private Map<Integer,HtmlStyle> stylesById=new HashMap<>();
    private Map<Integer,HtmlStyle> stylesByType=new HashMap<>();
    private Map<Integer,HtmlStyle> stylesByCat=new HashMap<>();
    private static HtmlStyle DEFAULT=new HtmlStyle(null,0);

    public JTokensStringFormatHtml addIdStyle(int id, HtmlStyle style){
        stylesById.put(id,style);
        return this;
    }
    public JTokensStringFormatHtml addTypeStyle(int ttype, HtmlStyle style){
        stylesByType.put(ttype,style);
        return this;
    }
    public JTokensStringFormatHtml addCatStyle(int cat, HtmlStyle style){
        stylesByCat.put(cat,style);
        return this;
    }
    public HtmlStyle getStyle(int id,int ttype){
        HtmlStyle s = stylesByCat.get(id);
        if(s!=null){
            return s;
        }
        s = stylesById.get(id);
        if(s!=null){
            return s;
        }
        s = stylesByType.get(ttype);
        if(s!=null){
            return s;
        }
        return DEFAULT;
    }

    @Override
    public String format(Iterable<JToken> tokensb){
        StringBuilder sb=new StringBuilder();
        for (JToken token : tokensb) {
            sb.append(format(token));
        }
        return sb.toString();
    }

    @Override
    public String formatDocument(Iterable<JToken> tokensb){
        StringBuilder sb=new StringBuilder();
        sb.append("<html>");
        for (JToken token : tokensb) {
            sb.append(format(token));
        }
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    public String format(JToken token){
        if(token==null){
            return "";
        }
        HtmlStyle s = getStyle(token.def.id, token.def.ttype);
        StringBuilder htmlStyle=new StringBuilder();
        if((s.fontStyle&BOLD)!=0){
            if(htmlStyle.length()>0){
                htmlStyle.append(";");
            }
            htmlStyle.append("font-weight: bold");
        }
        if((s.fontStyle&ITALIC)!=0){
            if(htmlStyle.length()>0){
                htmlStyle.append(";");
            }
            htmlStyle.append("font-style: italic");
        }
        if(s.color!=null && s.color.length()>0){
            if(htmlStyle.length()>0){
                htmlStyle.append(";");
            }
            htmlStyle.append("color: ").append(s.color);
        }
        if(htmlStyle.length()>0){
            return "<span style='"+htmlStyle+"'>"+htmlEncode(token.image)+"</span>";
        }
        return htmlEncode(token.image);
    }
    private String htmlEncode(String image){
        StringBuilder sb=new StringBuilder(image.length());
        for (char c : image.toCharArray()) {
            switch (c){
                case '<':{
                    sb.append("&lt;");
                    break;
                }
                case '>':{
                    sb.append("&gt;");
                    break;
                }
                default:{
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static class HtmlStyle{
        private String color;
        private int fontStyle;

        public HtmlStyle(String color, int fontStyle) {
            this.color = color;
            this.fontStyle = fontStyle;
        }

        public String getColor() {
            return color;
        }

        public int getFontStyle() {
            return fontStyle;
        }
    }
}
