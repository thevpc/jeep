/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JChildInfo;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author thevpc
 */
public abstract class AbstractJNode implements JNode {

    private JToken startToken;
    private JToken endToken;
    /**
     * information on this node when bound to Parent
     */
    private JToken[] separators;
    private JChildInfo childInfo;
    private Object exitContextObject;
    private JNode parentNode;
    private Map<String, Object> userObjects = new LinkedHashMap<>();

    public AbstractJNode() {
    }

    @Override
    public JToken getStartToken() {
        return startToken;
    }

    @Override
    public JToken getEndToken() {
        return endToken;
    }

    @Override
    public void setStartToken(JToken startToken) {
        this.startToken = startToken == null ? null : startToken.copy();
    }

    @Override
    public void setEndToken(JToken endToken) {
        this.endToken = endToken == null ? null : endToken.copy();
    }

    public Object getExitContextObject() {
        return exitContextObject;
    }

    public AbstractJNode setExitContextObject(Object exitContextObject) {
        this.exitContextObject = exitContextObject;
        return this;
    }

    @Override
    public boolean isExitContext() {
        return getExitContextObject() != null;
    }

    @Override
    public JNode getParentNode() {
        return parentNode;
    }

    public AbstractJNode parentNode(JNode parentNode) {
        this.parentNode = parentNode;
        return this;
    }

    @Override
    public Map<String, Object> getUserObjects() {
        return userObjects;
    }

    @Override
    public void setUserObject(String name, Object value) {
        if (value == null || (value instanceof Boolean && !(boolean) value)) {
            userObjects.remove(name);
        } else {
            userObjects.put(name, value);
        }
    }

    @Override
    public void setUserObject(String name, boolean valid) {
        if (valid) {
            userObjects.put(name, true);
        } else {
            userObjects.remove(name);
        }
    }

    @Override
    public void setUserObject(String name) {
        userObjects.put(name, true);
    }

    @Override
    public void unsetUserObject(String name) {
        userObjects.remove(name);
    }

    @Override
    public boolean isTestAndSetUserObject(String name) {
        if (!isSetUserObject(name)) {
            setUserObject(name);
            return true;
        }
        return false;
    }

    @Override
    public boolean isSetUserObject(String name) {
        return userObjects.containsKey(name);
    }

    public static JPositionStyle getPosition(JNode node, int caretOffset) {
        if (node == null) {
            return null;
        }
        return node.getPosition(caretOffset);
    }

    @Override
    public JPositionStyle getPosition(int caretOffset) {
        if (caretOffset < this.getStartToken().startCharacterNumber) {
            return JPositionStyle.BEFORE;
        } else if (caretOffset == this.getStartToken().startCharacterNumber) {
            return JPositionStyle.START;
        } else if (caretOffset < this.getEndToken().endCharacterNumber) {
            return JPositionStyle.MIDDLE;
        } else if (caretOffset == this.getEndToken().endCharacterNumber) {
            return JPositionStyle.END;
        } else {
            return JPositionStyle.AFTER;
        }
    }

    @Override
    public void visit(JNodeVisitor visitor) {
        visitor.startVisit(this);
        visitNext(visitor, getChildrenNodes());
        visitor.endVisit(this);
    }

    protected void visitNext(JNodeVisitor visitor, JNode child) {
        if (child != null) {
            (child).visit(visitor);
        }
    }

    protected void visitNext(JNodeVisitor visitor, JNode[] child) {
        if (child != null) {
            for (JNode c : child) {
                visitNext(visitor, c);
            }
        }
    }

    protected <T extends JNode> void visitNext(JNodeVisitor visitor, List<T> child) {
        if (child != null) {
            for (JNode c : child) {
                visitNext(visitor, c);
            }
        }
    }

    @Override
    public List<JNode> getChildrenNodes() {
        return Collections.emptyList();
    }

    @Override
    public void copyFrom(JNode other) {
        copyFrom(other, null);
    }

    @Override
    public void copyFrom(JNode other, JNodeCopyFactory copyFactory) {
        if (other != null) {
            setStartToken(other.getStartToken());
            setEndToken(other.getEndToken());
            setSeparators(other.getSeparators());
            setChildInfo(other.getChildInfo());
        }
    }

    @Override
    public JNode copy() {
        return copy(null);
    }

    @Override
    public JNode copy(JNodeCopyFactory copyFactory) {
        JNode n = null;
        try {
            Constructor<? extends AbstractJNode> dc = this.getClass().getDeclaredConstructor();
            dc.setAccessible(true);
            n = dc.newInstance();
        } catch (Exception e) {
            throw new JShouldNeverHappenException("Cannot coy without a default constructor for "+getClass().getName());
        }
        n.copyFrom(this,copyFactory);
        return n;
    }

    @Override
    public JNode findAndReplace(JNodeFindAndReplace findAndReplace) {
        boolean replaceFirst = findAndReplace.isReplaceFirst();
        if (replaceFirst) {
            if (findAndReplace.accept(this)) {
                JNode r = findAndReplace.replace(this);
                if (r != null) {
                    return r;
                }
            }
        }
        findAndReplaceChildren(findAndReplace);
        if (!replaceFirst) {
            if (findAndReplace.accept(this)) {
                JNode r = findAndReplace.replace(this);
                if (r != null) {
                    return r;
                }
            }
        }
        return this;
    }

//    public <T extends JNode> T bind(T child) {
//        if (child != null) {
//            ((AbstractJNode) child).parentNode(this);
//        }
//        return child;
//    }

//    public <T extends JNode> T bind(T child, String asName) {
//        if (child != null) {
//            ((AbstractJNode) child).parentNode(this);
//            ((AbstractJNode) child).childInfo(asName);
//        }
//        return child;
//    }

    protected void findAndReplaceChildren(JNodeFindAndReplace findAndReplace) {
        //should implement me
    }

    @Override
    public JNode setChildInfo(JChildInfo childInfo) {
        this.childInfo = childInfo;
        return this;
    }

    @Override
    public JChildInfo getChildInfo() {
        return childInfo;
    }

    @Override
    public boolean containsCaret(int caretOffset) {
        return caretOffset >= getStartToken().startCharacterNumber
                && caretOffset <= getEndToken().endCharacterNumber;
    }

    public static boolean containsCaret(JNode root, int caretOffset) {
        return (root != null && root.containsCaret(caretOffset));
    }

    @Override
    public JToken[] getSeparators() {
        return separators;
    }

    public AbstractJNode setSeparators(JToken[] separators) {
        this.separators = separators;
        return this;
    }

    public AbstractJNode setBounds(JTokenBounds bounds){
        if(bounds!=null) {
            setStartToken(bounds.getStartToken());
            setSeparators(bounds.getSeparators());
            setEndToken(bounds.getEndToken());
        }
        return this;
    }
}
