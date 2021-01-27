package net.thevpc.jeep;

public interface JCompilerContext {
    JCompilerContext getParent();

    JCompilationUnit getCompilationUnit();

    JCompilerContext setParent(int index);

    JCompilerLog getLog();
    String getPackageName();

    JContext getContext();

    boolean isStage(int stageId);

    int getIteration();

    int getStage();

    JNodePath getPath();

    JNode getNode();

    JImportInfo[] getImports();

//    JType typeForName(String name);

//    JType typeForName(JTypeName typename);

//    JType typeForNameOrNull(String nameUsingImports);


//    JNode lookupVarDeclaration(String name, JToken location);

//    JNode lookupVarDeclarationOrNull(String name, JToken location);


    default JCompilerContext addImport(JImportInfo value){
        return appendImport(value);
    }

    JCompilerContext appendImport(JImportInfo value);

//    JCompilerContext node(JNode node);

    JCompilerContext dropNode();


    JCompilerContext setPackageName(String packageName);

    JCompilerContext setLog(JCompilerLog log);

    JCompilerContext setStage(int stageId);

    JCompilerContext setIteration(int iterationNumber);

    JCompilerContext nextNode(JNode node);

    JCompilerContext setCompilationUnit(JCompilationUnit compilationUnit);
}
