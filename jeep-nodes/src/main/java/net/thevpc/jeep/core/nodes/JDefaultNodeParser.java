package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JCompilationUnit;
import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JTokenizer;
import net.thevpc.jeep.core.DefaultJParser;
import net.thevpc.jeep.core.JExpressionOptions;
import net.thevpc.jeep.core.DefaultJParserNodeFactory;

public class JDefaultNodeParser extends DefaultJParser<JDefaultNode, JExpressionOptions<JExpressionOptions>> {
    public JDefaultNodeParser(JTokenizer tokenizer, JCompilationUnit compilationUnit, JContext context) {
        super(tokenizer, compilationUnit, context, new DefaultJParserNodeFactory(compilationUnit,context));
    }
}
