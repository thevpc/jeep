/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

import net.thevpc.jeep.core.JChildInfo;

import java.util.List;
import java.util.Map;

/**
 * @author thevpc
 */
public interface JNode {

    JNode getParentNode();

    JToken getStartToken();

    JToken getEndToken();

    void setStartToken(JToken startToken);

    void setEndToken(JToken endToken);

    default boolean isExitContext() {
        return false;
    }

    Map<String, Object> getUserObjects();

    void setUserObject(String name, Object value);

    void setUserObject(String name, boolean valid);

    void setUserObject(String name);

    void unsetUserObject(String name);

    boolean isTestAndSetUserObject(String name);

    boolean isSetUserObject(String name);

    //    JType getType(JContext context);
    JPositionStyle getPosition(int caretOffset);

    void visit(JNodeVisitor visitor);

    JNode setChildInfo(JChildInfo childInfo);

    JChildInfo getChildInfo();

    List<JNode> getChildrenNodes();

    void copyFrom(JNode other, JNodeCopyFactory copyFactory);

    void copyFrom(JNode other);

    JNode copy();

    JNode copy(JNodeCopyFactory copyFactory);

    JNode findAndReplace(JNodeFindAndReplace findAndReplace);

    boolean containsCaret(int caretOffset);
    JToken[] getSeparators();
}
