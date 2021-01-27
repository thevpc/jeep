package net.thevpc.jeep;

public interface JTypeName extends JTypeNameOrVariable {

    JTypeName rawType();

    JTypeName withSimpleName();

    int varsCount();

    JTypeNameOrVariable[] vars();

    JTypeNameOrVariable varAt(int i);

    String simpleName();

    boolean isArray();

    int arrayDimension();

    JTypeName toArray();

    JTypeName componentType();

    JTypeName rootComponentType();

    String fullName();

    JTypeName addArguments(JTypeNameOrVariable[] z);

    boolean isVarArg();

    JTypeName replaceVarArg();
}
