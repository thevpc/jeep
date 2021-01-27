package net.thevpc.jeep.util;

import java.lang.reflect.Array;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.nodes.AbstractJNode;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.DefaultJChildInfo;
import net.thevpc.jeep.core.nodes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class JNodeUtils {

    public static void bindChildToParent(JNode child, JNode parent) {
        if (child != null) {
            ((AbstractJNode) child).parentNode(parent);
        }
    }

    public static JNode copy(JNode child, JNodeCopyFactory factory) {
        return child == null ? null
                : factory != null ? factory.copy(child)
                        : child.copy();
    }

    public static JNode copy(JNode child) {
        return child == null ? null : child.copy();
    }

    public static JToken copy(JToken child) {
        return child == null ? null : ((JToken) child).copy();
    }

    public static JNode[] copy(JNode[] children, JNodeCopyFactory factory) {
        if (children == null) {
            return null;
        }
        JNode[] o = new JNode[children.length];
        for (int i = 0; i < o.length; i++) {
            o[i] = copy(children[i], factory);
        }
        return o;
    }

    public static JNode[] copy(JNode[] children) {
        if (children == null) {
            return null;
        }
        JNode[] o = new JNode[children.length];
        for (int i = 0; i < o.length; i++) {
            o[i] = copy(children[i]);
        }
        return o;
    }

    public static <T> T[] copy(JNode[] children, Class<T> type) {
        if (children == null) {
            return null;
        }
        T[] o = (T[]) Array.newInstance(type, children.length);
        for (int i = 0; i < o.length; i++) {
            o[i] = (T) copy(children[i]);
        }
        return o;
    }

    public static <T> T[] copy(JNode[] children, Class<T> type, JNodeCopyFactory factory) {
        if (children == null) {
            return null;
        }
        T[] o = (T[]) Array.newInstance(type, children.length);
        for (int i = 0; i < o.length; i++) {
            o[i] = (T) copy(children[i], factory);
        }
        return o;
    }

    public static <T extends JNode> List<T> copy(List<T> children) {
        if (children == null) {
            return null;
        }
        List<T> li = new ArrayList<>();
        for (T child : children) {
            li.add((T) copy(child));
        }
        return li;
    }

    public static <T extends JNode> List<T> copy(List<T> children, JNodeCopyFactory factory) {
        if (children == null) {
            return null;
        }
        List<T> li = new ArrayList<>();
        for (T child : children) {
            li.add((T) copy(child, factory));
        }
        return li;
    }

    public static void bindChildToParent(JNode[] child, JNode parent) {
        if (child != null) {
            for (JNode c : child) {
                bindChildToParent(c, parent);
            }
        }
    }

    public static JNode findFirstParent(JNode n, Predicate<JNode> filter) {
        JNode p = n.getParentNode();
        while (p != null) {
            if (filter.test(p)) {
                return p;
            }
            p = p.getParentNode();
        }
        return null;
    }

    public static JNode findFirstChild(JNode n, Predicate<JNode> filter) {
        List<JNode> used = new ArrayList<>();
        class Found extends RuntimeException {

        }
        JNodeVisitor v = new JNodeVisitor() {
            @Override
            public void startVisit(JNode node) {
                if (filter.test(node)) {
                    used.add(node);
                    throw new Found();
                }
            }
        };
        try {
            n.visit(v);
        } catch (Found e) {
            //
            return used.get(0);
        }
        return null;
    }

    public static JNode[] findNodes(JNode n, Predicate<JNode> filter) {
        List<JNode> used = new ArrayList<>();
        JNodeVisitor v = new JNodeVisitor() {
            @Override
            public void startVisit(JNode node) {
                if (filter.test(node)) {
                    used.add((JNode) node);
                }
            }
        };
        ((JNode) n).visit(v);
        return used.toArray(new JNode[0]);
    }

    public static <T extends JNode> T findAndReplaceNext(JNode parentNode, JNodeFindAndReplace nodeFindAndReplace, Supplier<T> getter, Consumer<T> setter) {
        T initialValue = getter.get();
        if (initialValue == null) {
            return null;
        }
        T newValue = (T) ((JNode) initialValue).findAndReplace(nodeFindAndReplace);
        if (newValue != initialValue && newValue != null) {
            setter.accept(newValue);
            return newValue;
        } else {
            return initialValue;
        }
    }

    public static <T extends JNode> void findAndReplaceNext(JNode parentNode, JNodeFindAndReplace nodeFindAndReplace, List<T> nargs) {
        if (nargs != null) {
            for (int i = 0; i < nargs.size(); i++) {
                final int ii = i;
                JNode p = ((JNode) nargs.get(ii)).findAndReplace(nodeFindAndReplace);
                if (p == null) {
                    throw new JFixMeLaterException();
                }
                if (p.getParentNode() != parentNode) {
                    ((AbstractJNode) p).parentNode(parentNode);
                }
                nargs.set(ii, (T) p);
            }
        }
    }

    public static <T extends JNode> void findAndReplaceNext(JNode parentNode, JNodeFindAndReplace nodeFindAndReplace, JNode[] nargs) {
        if (nargs != null) {
            for (int i = 0; i < nargs.length; i++) {
                final int ii = i;
                JNode p = ((JNode) nargs[ii]).findAndReplace(nodeFindAndReplace);
                if (p == null) {
                    throw new JFixMeLaterException();
                }
                if (p.getParentNode() != parentNode) {
                    ((AbstractJNode) p).parentNode(parentNode);
                }
                nargs[ii] = (T) p;
            }
        }
    }

//    public static JEvaluable[] getEvaluatables(JNode[] args) {
//        JType[] ntypes = getTypes(args);
//        JEvaluable[] ev = new JEvaluable[args.length];
//        for (int i = 0; i < ev.length; i++) {
//            ev[i] = new JEvaluableNode(args[i], ntypes[i]);
//        }
//        return ev;
//    }
//    public static JType[] getTypes(JNode[] args) {
//        if (args == null) {
//            return null;
//        }
//        JType[] argTypes = new JType[args.length];
//        for (int i = 0; i < argTypes.length; i++) {
//            argTypes[i] = getType(args[i]);
//        }
//        return argTypes;
//    }
//    public static JType getType(JNode arg) {
//        if (arg == null) {
//            return null;
//        }
//        return arg.getType();
//    }
//    public static JType[] getTypesOrNulls(JNode[] args) {
//        if (args == null) {
//            return null;
//        }
//        JType[] argTypes = new JType[args.length];
//        for (int i = 0; i < argTypes.length; i++) {
//            argTypes[i] = args[i] == null ? null : args[i].getType();
//        }
//        return argTypes;
//    }
    public static String toPar(JNode e) {
        return "(" + e.toString() + ")";
    }

    public static <T extends JNode> List<T> bindCopy(JNode parent, JNodeCopyFactory factory, List<T> children) {
        return bind(parent,JNodeUtils.copy(children,factory));
    }

    public static <T extends JNode> T[] bindCopy(JNode parent, JNodeCopyFactory factory, JNode[] children, Class<T> type) {
        return (T[]) bind(parent,JNodeUtils.copy(children, type,factory));
    }

    public static JNode[] bindCopy(JNode parent, JNodeCopyFactory factory, JNode[] children) {
        return bind(parent,JNodeUtils.copy(children,factory));
    }

    public static <T extends JNode> T bindCopy(JNode parent, JNodeCopyFactory factory, JNode children) {
        return (T) bind(parent,JNodeUtils.copy(children,factory));
    }




    public static <T extends JNode> T bind(JNode parent,T child) {
        if (child != null) {
            ((AbstractJNode) child).parentNode(parent);
        }
        return child;
    }

    public static <T extends JNode> T bind(JNode parent,T child, String asName) {
        if (child != null) {
            ((AbstractJNode) child).parentNode(parent);
            ((AbstractJNode) child).setChildInfo(new DefaultJChildInfo(asName,null));
        }
        return child;
    }

    public static <T extends JNode> T bind(JNode parent,T child, String asName, int index) {
        if (child != null) {
            ((AbstractJNode) child).parentNode(parent);
            child.setChildInfo(new DefaultJChildInfo(asName,String.valueOf(index)));
        }
        return child;
    }

    public static <T extends JNode> List<T> bind(JNode parent,List<T> children) {
        if (children != null) {
            for (T child : children) {
                bind(parent,child);
            }
        }
        return children;
    }

    public static <T extends JNode> List<T> bind(JNode parent,List<T> children, String asName) {
        if (children != null) {
            int index = 0;
            for (T child : children) {
                index++;
                bind(parent,child, asName + "[" + index + "]");
            }
        }
        return children;
    }


    public static JNode[] bind(JNode parent,JNode[] children) {
        if (children != null) {
            for (JNode c : children) {
                bind(parent,c);
            }
        }
        return children;
    }

//    public static JNode[] bind(JNode parent,JNode[] children, String name) {
//        if (children != null) {
//            for (int i = 0; i < children.length; i++) {
//                JNode c = children[i];
//                bind(parent,c, name + "[" + i + "]");
//            }
//        }
//        return children;
//    }
    public static <T extends JNode> T[] bind(JNode parent,T[] children, String name) {
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                T c = children[i];
                bind(parent,(JNode) c, name + "[" + i + "]");
            }
        }
        return (T[])children;
    }
}
