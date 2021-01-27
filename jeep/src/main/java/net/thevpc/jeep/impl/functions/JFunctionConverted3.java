package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JFunctionBase;
import net.thevpc.jeep.impl.eval.JEvaluableConverter;

public class JFunctionConverted3 extends JFunctionBase {
    private JConverter[] argConverters;
    private JConverter resultConverter;
    private JFunction other;
    //return type was Object.class, fix it!
    public JFunctionConverted3(JFunction other,
                               JConverter[] argConverters,
                               JConverter resultConverter,
                               JType returnType) {
        super(other.getName(),
                returnType,
                convArgTypes(other.getSignature().argTypes(), argConverters)
                ,false,other.getSourceName()
        );
        this.other = other;
        this.argConverters = argConverters;
        this.resultConverter = resultConverter;
    }

    public JConverter[] getArgConverters() {
        return argConverters;
    }

    public JConverter getResultConverter() {
        return resultConverter;
    }

    public JFunction getOther() {
        return other;
    }

    @Override
    public JType getReturnType() {
        return resultConverter!=null?resultConverter.targetType().getType():other.getReturnType();
    }

    private static JType[] convArgTypes(JType[] argTypes, JConverter[] converters){
        JType[] newArgTypes=new JType[argTypes.length];
        for (int i = 0; i < newArgTypes.length; i++) {
            if(converters!=null && converters[i]!=null){
                newArgTypes[i]=converters[i].originalType().getType();
                if(newArgTypes[i]==null){
                    newArgTypes[i]=argTypes[i];
                }
            }else{
                newArgTypes[i]=argTypes[i];
            }
        }
        return newArgTypes;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        JEvaluable[] args = icontext.getArguments();
        JEvaluable[] args2=new JEvaluable[args.length];
        JType[] oldTypes=icontext.getArgumentTypes();
        JType[] types2=new JType[args.length];
        for (int i = 0; i < args.length; i++) {
            if(argConverters!=null && argConverters[i]!=null){
                args2[i]=new JEvaluableConverter(argConverters[i],args[i]);
                types2[i]=argConverters[i].targetType().getType();
            }else{
                args2[i]=args[i];
                types2[i]=oldTypes[i];
            }
        }
        Object v = other.invoke(
                icontext.builder()
                        .setArguments(args2)
                        .build());
        if(resultConverter!=null){
            v=resultConverter.convert(v, icontext);
        }
        return v;
    }

    @Override
    public JTypes getTypes() {
        return getReturnType().getTypes();
    }
}
