package net.thevpc.jeep.core.tokens;

import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.JToken;
import net.thevpc.jeep.JTokenizer;
import net.thevpc.jeep.JTokenizerSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractJTokenizer implements JTokenizer {

    private List<JTokenizerSnapshot> snapshots = new ArrayList<>();
    private boolean skipComments;
    private boolean skipSpaces;
    private boolean skipEof;

    @Override
    public Stream<JToken> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public void pushBackAll(Collection<JToken> t) {
        List<JToken> list;
        if (t != null) {
            if (t instanceof List) {
                list = (List<JToken>) t;
            } else {
                list = new ArrayList<>(t);
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                pushBack(list.get(i));
            }
        }
    }

    @Override
    public JToken peek() {
        JToken t = next();
        pushBack(t);
        return t;
    }

    @Override
    public JToken[] peek(int count) {
        List<JToken> found = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            JToken n = next();
            if (n == null || n.isEOF()) {
                break;
            }
            found.add(n);
        }
        for (int i = found.size() - 1; i >= 0; i--) {
            pushBack(found.get(i));
        }
        return found.toArray(new JToken[0]);
    }

    @Override
    public Iterator<JToken> iterator() {
        return new Iterator<JToken>() {
            boolean ended = false;
            JToken t = null;

            @Override
            public boolean hasNext() {
                if (ended) {
                    return false;
                }
                t = AbstractJTokenizer.this.next();
                if (t == null) {
                    ended = true;
                    return false;
                } else if (t.def.id == JTokenId.EOF) {
                    ended = true;
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public JToken next() {
                return t;
            }
        };
    }

    @Override
    public void skipUntil(Predicate<JToken> t) {
        while (true) {
            JToken p = peek();
            if (p == null || p.isEOF()) {
                break;
            }
            if (t.test(p)) {
                break;
            }
            skip();
        }
    }

    @Override
    public void skipWhile(Predicate<JToken> t) {
        while (true) {
            JToken p = peek();
            if (p == null || p.isEOF()) {
                break;
            }
            if (!t.test(p)) {
                break;
            }
            skip();
        }
    }

    @Override
    public void skip() {
        next();
    }

    @Override
    public void skip(int count) {
        for (int i = 0; i < count; i++) {
            skip();
        }
    }

    @Override
    public boolean isSkipComments() {
        return skipComments;
    }

    @Override
    public boolean isSkipEof() {
        return skipEof;
    }

    @Override
    public boolean isSkipSpaces() {
        return skipSpaces;
    }

    @Override
    public JTokenizer setSkipComments(boolean skipComments) {
        this.skipComments = skipComments;
        return this;
    }

    @Override
    public JTokenizer setSkipEof(boolean skipEof) {
        this.skipEof = skipEof;
        return this;
    }

    @Override
    public JTokenizer setSkipSpaces(boolean skipSpaces) {
        this.skipSpaces = skipSpaces;
        return this;
    }
}
