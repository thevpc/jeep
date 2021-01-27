//package net.thevpc.jeep.editor;
//
//import net.thevpc.jeep.JToken;
//import net.thevpc.jeep.impl.tokens.JTokensStringFormatHtml;
//import net.hl.compiler.HCompletionProposals;
//
//public class JTokensStringFormatHtml2 extends JTokensStringFormatHtml {
//    public JTokensStringFormatHtml2() {
//        addCatStyle(HCompletionProposals.CAT_KEYWORD,new HtmlStyle("blue",JTokensStringFormatHtml.BOLD));
//        addCatStyle(HCompletionProposals.CAT_CLASS,new HtmlStyle(null,JTokensStringFormatHtml.BOLD));
//        addCatStyle(HCompletionProposals.CAT_FIELD,new HtmlStyle("#9876aa",JTokensStringFormatHtml.BOLD));
//        addCatStyle(HCompletionProposals.CAT_METHOD,new HtmlStyle("yellow",JTokensStringFormatHtml.BOLD));
//        addCatStyle(HCompletionProposals.CAT_PARAMETER,new HtmlStyle("gray",JTokensStringFormatHtml.PLAIN));
//    }
//
//    @Override
//    public String format(JToken token) {
//        if(token.catId==HCompletionProposals.CAT_CLASS){
//            String typeName = token.image;
//            String namePart = typeName;
//            int dot = typeName.lastIndexOf('.');
//            if (dot > 0) {
//                namePart = typeName.substring(dot + 1);
//                return namePart +"   <span style=\"color:gray\">" + typeName + "</span></html>";
//            }
//        }
//        return super.format(token);
//    }
//}
