//package net.thevpc.jeep.util;
//
//import net.thevpc.jeep.JConverter1;
//import net.thevpc.jeep.JType;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class JConverterList {
//    private List<JConverter1> elements =new ArrayList<>();
//    private boolean shrinkHierarchy;
//    private JType from;
//
//    public JConverterList(JType from,boolean shrinkHierarchy) {
//        this.shrinkHierarchy = shrinkHierarchy;
//        this.from = from;
//    }
//
//    public void add(JConverter1 converter){
//        if(shrinkHierarchy) {
//            if(converter.targetType().isAssignableFrom(from)){
//                return;
//            }
//            int replaceThis = -1;
//            for (int i = 0; i < elements.size(); i++) {
//                JConverter1 old = elements.get(i);
//                if (old.equals(converter)) {
//                    return;
//                } else if (old.targetType().isAssignableFrom(converter.targetType())) {
//                    replaceThis = i;
//                    break;
//                } else if (converter.targetType().isAssignableFrom(old.targetType())) {
//                    return;
//                }
//            }
//            if (replaceThis>=0) {
//                elements.set(replaceThis,converter);
//            }else {
//                elements.add(converter);
//            }
//        }else{
//            elements.add(converter);
//        }
//    }
//    public JConverter1[] toArray(){
//        return elements.toArray(new JConverter1[0]);
//    }
//}
