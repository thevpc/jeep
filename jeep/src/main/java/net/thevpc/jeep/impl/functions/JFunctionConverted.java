package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JFunctionBase;
import net.thevpc.jeep.impl.eval.JEvaluableConverter;

public class JFunctionConverted extends JFunctionBase {
    private JConverter[] argConverters;
    private JConverter resultConverter;
    private JFunction other;
    //return type was Object.class, fix it!
    public JFunctionConverted(JFunction other,
                              JConverter[] argConverters,
                              JConverter resultConverter) {
        super(other.getName(),
                convRetType(JTypePattern.of(other.getReturnType()), resultConverter).getType(),
                JTypeUtils.typesOrError(
                        convArgTypes(JTypeUtils.typesOrLambdas(other.getSignature().argTypes()), argConverters)
                )
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
        return resultConverter!=null?
                resultConverter.targetType().getType()
                :other.getReturnType();
    }

    private static JTypePattern convRetType(JTypePattern a, JConverter c){
        if(c==null){
            return a;
        }
        return c.targetType();
    }
    
    private static JTypePattern[] convArgTypes(JTypePattern[] argTypes, JConverter[] converters){
        JTypePattern[] newArgTypes=new JTypePattern[argTypes.length];
        for (int i = 0; i < newArgTypes.length; i++) {
            if(converters!=null && converters[i]!=null){
                newArgTypes[i]=converters[i].originalType();
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
        JTypePattern[] oldTypes= JTypeUtils.typesOrLambdas(icontext.getArgumentTypes());
        JTypePattern[] types2=new JTypePattern[args.length];
        for (int i = 0; i < args.length; i++) {
            if(argConverters!=null && argConverters[i]!=null){
                args2[i]=new JEvaluableConverter(argConverters[i],args[i]);
                types2[i]=argConverters[i].targetType();
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
}
