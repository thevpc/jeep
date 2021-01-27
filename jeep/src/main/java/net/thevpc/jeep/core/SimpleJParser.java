package net.thevpc.jeep.core;

import net.thevpc.jeep.core.nodes.JSimpleNode;
import net.thevpc.jeep.JCompilationUnit;
import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JTokenizer;

public class SimpleJParser extends DefaultJParser<JSimpleNode,JExpressionOptions<JExpressionOptions>> {
    public SimpleJParser(JTokenizer tokenizer, JCompilationUnit compilationUnit, JContext context) {
        super(tokenizer, compilationUnit, context, new SimpleNodeNodeFactory());
    }
}
