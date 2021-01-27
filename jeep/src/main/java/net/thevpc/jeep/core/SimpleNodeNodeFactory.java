package net.thevpc.jeep.core;

import net.thevpc.jeep.core.nodes.JNodeTokens;
import net.thevpc.jeep.core.nodes.JSimpleNode;
import net.thevpc.jeep.util.ListBuilder;
import net.thevpc.jeep.JParserNodeFactory;
import net.thevpc.jeep.JToken;

import java.util.List;

public class SimpleNodeNodeFactory implements JParserNodeFactory<JSimpleNode> {
    @Override
    public JSimpleNode createIdentifierNode(String name, JNodeTokens nodeTokens) {
        return new JSimpleNode("Identifier", nodeTokens.getStart().sval);
    }

    @Override
    public JSimpleNode createLiteralNode(Object literal, JNodeTokens nodeTokens) {
        return new JSimpleNode("Literal", literal);
    }

    @Override
    public JSimpleNode createParsNode(List<JSimpleNode> children, JNodeTokens nodeTokens) {
        return new JSimpleNode("Pars", (Object[]) children.toArray(new JSimpleNode[0]));
    }

    @Override
    public JSimpleNode createBracesNode(List<JSimpleNode> children, JNodeTokens nodeTokens) {
        return new JSimpleNode("Braces", (Object[]) children.toArray(new JSimpleNode[0]));
    }

    @Override
    public JSimpleNode createPrefixUnaryOperatorNode(JToken op, JSimpleNode child, JNodeTokens nodeTokens) {
        return new JSimpleNode("PrefixUnaryOperator", new ListBuilder<>().addEach(op, child).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createPostfixBracketsNode(JSimpleNode leftBaseChild, JSimpleNode rightIndexChildren, JNodeTokens nodeTokens) {
        return new JSimpleNode("PostfixBrackets", new ListBuilder<>().addEach(leftBaseChild, rightIndexChildren).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createPrefixBracketsNode(JSimpleNode leftIndexChildren, JSimpleNode rightBaseChild, JNodeTokens nodeTokens) {
        return new JSimpleNode( "PrefixBrackets", new ListBuilder<>().addEach(leftIndexChildren, rightBaseChild).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createPostfixParenthesisNode(JSimpleNode leftBaseChild, JSimpleNode rightIndexChild, JNodeTokens nodeTokens) {
        return new JSimpleNode( "PostfixParenthesis", new ListBuilder<>().addEach(leftBaseChild, rightIndexChild).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createPrefixParenthesisNode(JSimpleNode leftIndexChild, JSimpleNode rightBaseChild, JNodeTokens nodeTokens) {
        return new JSimpleNode( "PrefixParenthesis", new ListBuilder<>().addEach(leftIndexChild, rightBaseChild).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createPostfixBracesNode(JSimpleNode leftBaseChild, JSimpleNode rightIndexChild, JNodeTokens nodeTokens) {
        return new JSimpleNode("PostfixBraces", new ListBuilder<>().addEach(leftBaseChild, rightIndexChild).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createPrefixBracesNode(JSimpleNode leftIndexChild, JSimpleNode rightBaseChild, JNodeTokens nodeTokens) {
        return new JSimpleNode("PrefixBraces", new ListBuilder<>().addEach(leftIndexChild, rightBaseChild).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createBracketsNode(List<JSimpleNode> children, JNodeTokens nodeTokens) {
        return new JSimpleNode("Brackets", new ListBuilder<>().addAll((List)children).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createPostfixUnaryOperatorNode(JToken name, JSimpleNode argumentChild, JNodeTokens nodeTokens) {
        return new JSimpleNode("PostfixUnaryOperator", new ListBuilder<>().add(name).add(argumentChild).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createListOperatorNode(JToken token, List<JSimpleNode> argumentChildren, JNodeTokens nodeTokens) {
        return new JSimpleNode("ListOperator", new ListBuilder<>().add(token).addAll((List)argumentChildren).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createBinaryOperatorNode(JToken op, JSimpleNode leftChild, JSimpleNode rightChild, JNodeTokens nodeTokens) {
        return new JSimpleNode("BinaryOperator", new ListBuilder<>().add(op).add(leftChild).add(rightChild).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createImplicitOperatorNode(JSimpleNode leftChild, JSimpleNode rightChild, JNodeTokens nodeTokens) {
        return new JSimpleNode("ImplicitOperator", new ListBuilder<>().add(leftChild).add(rightChild).toObjectArray()
        );
    }

    @Override
    public JSimpleNode createAnnotatedNode(JSimpleNode node, JSimpleNode annotations, JNodeTokens nodeTokens) {
        throw new IllegalArgumentException("Not supported");
    }
}
