package net.thevpc.jeep;

import net.thevpc.jeep.impl.functions.JSignature;

public class JInvokablePrefilled {
    private JInvokable invokable;
    private JEvaluable[] evaluables;
    private String name;

    public JInvokablePrefilled(JInvokable invokable, JEvaluable[] evaluables) {
        this.invokable = invokable;
        this.evaluables = evaluables;
        name=invokable.getSignature().name();
    }

//    public JInvokablePrefilled(JInvokable invokable, JNode... nodes) {
//        this.invokable = invokable;
//        this.evaluables = JNodeUtils.getEvaluatables(nodes);
//        JNodeUtils.getTypes(nodes);//just to check types!!
//        name=invokable.signature().name();
//    }


    public JSignature signature(){
        return getInvokable().getSignature();
    }

    public JInvokable getInvokable() {
        return invokable;
    }

    public JEvaluable[] getEvaluables() {
        return evaluables;
    }

//    public JType[] getTypes() {
//        return types;
//    }

    public Object invoke(JInvokeContext context){
        return invokable.invoke(
                context.builder().setArguments(evaluables).setName(name)
                .build()
        );
    }
}
