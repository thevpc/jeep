package net.thevpc.jeep;

import net.thevpc.jeep.core.nodes.JNodeTokens;

import java.util.List;

public interface JParserNodeFactory<T extends JNode> {
    default T createIdentifierNode(String name, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createIdentifierNode");
    }

    default T createLiteralNode(Object literal, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createLiteralNode");
    }

    default T createParsNode(List<T> children, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createParsNode");
    }

    default T createBracesNode(List<T> children, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createBracesNode");
    }

    default T createPrefixUnaryOperatorNode(JToken op, T child, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createPrefixUnaryOperatorNode");
    }

    default T createPostfixBracketsNode(T leftBaseChild, T rightIndexChildren, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createPostfixBracketsNode");
    }

    default T createPrefixBracketsNode(T leftIndexChildren, T rightBaseChild, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createPrefixBracketsNode");
    }

    default T createPostfixParenthesisNode(T leftBaseChild, T rightIndexChild, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createPostfixParenthesisNode");
    }

    default T createPrefixParenthesisNode(T leftIndexChild, T rightBaseChild, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createPrefixParenthesisNode");
    }

    default T createPostfixBracesNode(T leftBaseChild, T rightIndexChild, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createPostfixBracesNode");
    }

    default T createPrefixBracesNode(T leftIndexChild, T rightBaseChild, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createPrefixBracesNode");
    }


    default T createBracketsNode(List<T> children, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createBracketsNode");
    }

    default T createPostfixUnaryOperatorNode(JToken name, T argumentChild, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createPostfixUnaryOperatorNode");
    }

    default T createListOperatorNode(JToken token, List<T> argumentChildren, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createListOperatorNode");
    }

//    T createFunctionCallNode(String name, List<T> args, JToken startToken);


    default T createBinaryOperatorNode(JToken op, T leftChild, T rightChild, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createBinaryOperatorNode");
    }

    default T createImplicitOperatorNode(T leftChild, T rightChild, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createImplicitOperatorNode");
    }

    default T createAnnotatedNode(T node, T annotations, JNodeTokens nodeTokens){
        throw new UnsupportedOperationException("unsupported createAnnotatedNode");
    }
}
