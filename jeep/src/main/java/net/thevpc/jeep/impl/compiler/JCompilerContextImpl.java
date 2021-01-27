package net.thevpc.jeep.impl.compiler;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class JCompilerContextImpl implements JCompilerContext {
    private final int stage;
    private final int iteration;
    private final JNodePath path;
    private final JImportInfo[] imports;
    private final JContext context;
    private final String packageName;
    private final JCompilerLog log;
    private final JCompilationUnit compilationUnit;
    private final JCompilerContext parent;


    public JCompilerContextImpl(int iteration, int stage, JNodePath path, JImportInfo[] imports, JContext context, String packageName, JCompilerLog log, JCompilationUnit compilationUnit, JCompilerContext parent) {
        this.iteration = iteration;
        this.stage = stage;
        this.path = path;
        this.imports = imports;
        this.context = context;
        this.packageName = packageName;
        this.log = log;
        this.parent = parent;
        this.compilationUnit = compilationUnit;
    }

    @Override
    public JCompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    @Override
    public JCompilerContext getParent() {
        return parent;
    }

    @Override
    public JCompilerContext setParent(int index) {
        if (index < 0) {
            return parent;
        } else if (parent != null) {
            return setParent(index - 1);
        } else {
            return null;
        }
    }

    public JCompilerLog getLog() {
        return log;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public JContext getContext() {
        return context;
    }

    @Override
    public boolean isStage(int stageId) {
        return this.stage == stageId;
    }

    @Override
    public int getIteration() {
        return iteration;
    }

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public JNodePath getPath() {
        return path;
    }


    @Override
    public JNode getNode() {
        return getPath().last();
    }

    @Override
    public JImportInfo[] getImports() {
        return imports;
    }

//    @Override
//    public JType typeForName(String name) {
//        JType type = typeForNameOrNull(name);
//        if (type == null) {
//            throw new JParseException("Unable toresolve type " + name);
//        }
//        return type;
//    }

//    @Override
//    public JType typeForName(JTypeName typename) {
//        if (typename == null) {
//            return null;
//        }
//        return typeForName(typename.name());
//    }
//
//    @Override
//    public JType typeForNameOrNull(String nameUsingImports) {
//        return linker().typeForNameOrNull(nameUsingImports);
//    }

//    public JNode lookupVarDeclaration(String name, JToken location) {
//        JNode info = lookupVarDeclarationOrNull(name, location);
//        if (info == null) {
//            throw new IllegalArgumentException("Cannot find symbol : " + node);
//        }
//        return info;
//    }

//    public abstract JNode lookupVarDeclarationOrNull(String name, JToken location);



    @Override
    public JCompilerContext appendImport(JImportInfo value) {
        if (value != null) {
            Set<JImportInfo> newImports = new LinkedHashSet<>(Arrays.asList(imports));
            newImports.add(value);
            return newInstance(
                    iteration, stage, path, newImports.toArray(new JImportInfo[0]), context, packageName, log,
                    compilationUnit,parent);
        }
        return this;
    }

//    @Override
//    public JCompilerContext node(JNode node) {
//        if (this.node != node) {
//            return newInstance(
//                    iteration, stage, path, node, imports, context, packageName, log,
//                    compilationUnit,parent);
//        }
//        return this;
//    }

    @Override
    public JCompilerContext dropNode() {
        return newInstance(
                iteration, stage, this.path.removeLast(), imports, context, packageName, log,
                compilationUnit,parent);
    }

    @Override
    public JCompilerContext setPackageName(String packageName) {
        if (this.packageName != packageName) {
            return newInstance(
                    iteration, stage, path, imports, context, packageName, log,
                    compilationUnit,parent);
        }
        return this;
    }

    @Override
    public JCompilerContext setLog(JCompilerLog log) {
        if (this.log != log) {
            return newInstance(
                    iteration, stage, path, imports, context, packageName, log,
                    compilationUnit,parent);
        }
        return this;
    }

    @Override
    public JCompilerContext setStage(int stageId) {
        if (this.stage != stageId) {
            return newInstance(
                    iteration, stageId, path,  imports, context, packageName, log,
                    compilationUnit,parent);
        }
        return this;
    }

    @Override
    public JCompilerContext setCompilationUnit(JCompilationUnit compilationUnit) {
        if (this.compilationUnit != compilationUnit) {
            return newInstance(
                    iteration, stage, path,  imports, context, packageName, log,
                    compilationUnit,parent);
        }
        return this;
    }

    @Override
    public JCompilerContext setIteration(int iteration) {
        if (this.iteration != iteration) {
            return newInstance(
                    iteration, stage, path,  imports, context, packageName, log,
                    compilationUnit,parent);
        }
        return this;
    }

    @Override
    public JCompilerContext nextNode(JNode node) {
        return newInstance(
                iteration, stage, this.path.append(node),imports, context, packageName, log,
                compilationUnit,this);
    }

    public abstract JCompilerContext newInstance(int iteration, int pass, JNodePath path, JImportInfo[] imports, JContext context, String packageName, JCompilerLog log, JCompilationUnit compilationUnit,JCompilerContext parent);

    @Override
    public String toString() {
        return "JCompilerContextImpl{" +
                "stage=" + stage +
                ", iteration=" + iteration +
                ", path=" + path +
                '}';
    }

}
