package net.thevpc.jeep.util;

import net.thevpc.jeep.JConverter;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypePattern;

import java.util.ArrayList;
import java.util.List;

public class JConverterList2 {
    private List<JConverter> elements =new ArrayList<>();
    private boolean shrinkHierarchy;
    private JTypePattern from;

    public JConverterList2(JTypePattern from, boolean shrinkHierarchy) {
        this.shrinkHierarchy = shrinkHierarchy;
        this.from = from;
    }

    public void add(JConverter converter){
        if(from.isLambda() || converter.targetType().isLambda()){
            elements.add(converter);
        }

        if(shrinkHierarchy) {
            JType targetType = converter.targetType().getType();
            JType fromType = from.getType();
            if(targetType.isAssignableFrom(fromType)){
                return;
            }
            int replaceThis = -1;
            for (int i = 0; i < elements.size(); i++) {
                JConverter old = elements.get(i);
                if (old.equals(converter)) {
                    return;
                } else if (old.targetType().isType() && old.targetType().getType().isAssignableFrom(targetType)) {
                    replaceThis = i;
                    break;
                } else if (old.targetType().isType() && targetType.isAssignableFrom(old.targetType().getType())) {
                    return;
                }
            }
            if (replaceThis>=0) {
                elements.set(replaceThis,converter);
            }else {
                elements.add(converter);
            }
        }else{
            elements.add(converter);
        }
    }
    public JConverter[] toArray(){
        return elements.toArray(new JConverter[0]);
    }
}
