/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.tokens.*;
import net.thevpc.jeep.impl.io.JTokenizerReaderEscape;

import java.util.*;
import java.util.function.Predicate;

import net.thevpc.jeep.core.JTokenState;
import net.thevpc.common.textsource.JTextSourcePositionTracker;

/**
 * @author thevpc
 */
public class JTokenizerImpl extends AbstractJTokenizer {

    private JTextSourcePositionTracker positionTracker = new JTextSourcePositionTracker();
//    private int currentRowNumber = 0;
//    private int currentColumnNumber = 0;
//    private int currentCharNumber = 0;
//    private int currentTokenNumber = 0;
    private final LinkedList<JToken> back = new LinkedList<>();
    private final List<JTokenizerSnapshotImpl> snapshots = new ArrayList<>();
    private JTokenizerReader reader;
    private JTokenizerReaderEscape readerEscape;
    private JTokenizerStateImpl stateCache;
    private final Stack<JTokenizerState> stateStack = new Stack<>();
    private final Map<Integer, JTokenizerState> states = new HashMap<>();
    private JToken lastToken;

    public JTokenizerImpl(JTokenizerReader reader, JTokenConfig config) {
        this(
                reader,
                toPatterns(config)
        );
    }

    public JTokenizerImpl(JTokenizerReader reader, JTokenPattern[] matchers) {
        this.reader = reader;
        addState(1, "DEFAULT", matchers, '\0');
        pushState(1);
    }

    public JTokenizerImpl(JTokenizerReader reader) {
        this.reader = reader;
    }

    public static JTokenPattern[] toPatterns(JTokenConfig config0) {
        JTokenConfig config = config0 == null ? JTokenConfigDefinition.DEFAULT : config0;
        List<JTokenPattern> matchers = new ArrayList<>();
        if (config.getLineComment() != null && config.getLineComment().length() > 0) {
            matchers.add(new LineCommentsPattern(config.getLineComment()));
        }
        if (config.getBlockCommentStart() != null && config.getBlockCommentStart().length() > 0
                && config.getBlockCommentEnd() != null && config.getBlockCommentEnd().length() > 0) {
            matchers.add(new BlockCommentsPattern(config.getBlockCommentStart(), config.getBlockCommentEnd()));
        }
        if (config.isParseSimpleQuotesString()) {
            matchers.add(StringPattern.SIMPLE_QUOTES());
        }
        if (config.isParseDoubleQuotesString()) {
            matchers.add(StringPattern.DOUBLE_QUOTES());
        }
        if (config.isParseAntiQuotesString()) {
            matchers.add(StringPattern.ANTI_QUOTES());
        }
        Set<String> operators = config.getOperators();
        if (operators.size() > 0) {
            matchers.add(new OperatorsPattern(operators.toArray(new String[0])));
        }
        Set<String> keywords = config.getKeywords();
        if (keywords.size() > 0) {
            matchers.add(new KeywordsPattern(config.isCaseSensitive(), keywords.toArray(new String[0])));
        }
        if (config.isParsetIntNumber() || config.isParsetInfinity() || config.isParseFloatNumber()) {
            matchers.add(new NumberPattern(null,
                    config.isParsetIntNumber(),
                    config.isParseFloatNumber(),
                    config.isParsetInfinity(),
                    config.getNumberSuffixes(),
                    config.getNumberEvaluator()
            ));
        }
        if (config.getIdPattern() != null) {
            matchers.add(config.getIdPattern());
        }
        if (config.isParseWhitespaces()) {
            matchers.add(new WhitespacePattern());
        }
        for (JTokenPattern pattern : config.getPatterns()) {
            matchers.add(pattern);
        }
        return matchers.toArray(new JTokenPattern[0]);
    }

    public JTokenizerState peekState() {
        return stateStack.peek();
    }

    public void addState(JTokenState state, JTokenPattern[] patterns) {
        addState(state.getValue(), state.getName(), patterns);
    }

    public void addState(int stateId, String stateName, JTokenPattern[] matchers) {
        addState(stateId, stateName, matchers, '\0');
    }

    public void addState(int stateId, String stateName, JTokenPattern[] patterns, char escape) {
        addState(new JTokenizerStateImpl(stateId, stateName, patterns, escape));
    }

    public void addState(int stateId, String stateName, JTokenConfig config) {
        addState(stateId, stateName, config, '\0');
    }

    public void addState(JTokenState state, JTokenConfig config) {
        addState(state.getValue(), state.getName(), config, '\0');
    }

    public void addState(int stateId, String stateName, JTokenConfig config, char escape) {
        if (config == null) {
            config = JTokenConfigDefinition.DEFAULT;
        }
        addState(new JTokenizerStateImpl(stateId, stateName, toPatterns(config), escape));
    }

    public void popState() {
//        System.out.println("pop state");
        stateStack.pop();
        if (!stateStack.isEmpty()) {
            stateCache = (JTokenizerStateImpl) stateStack.peek();
        }
    }

    public void pushState(JTokenState state) {
        pushState(state.getValue());
    }

    public void pushState(int stateId) {
//        System.out.println("push state "+stateId);
        stateStack.push(stateCache = (JTokenizerStateImpl) getState(stateId));
        if (stateCache.escape != '\0') {
            if (readerEscape == null) {
                readerEscape = new JTokenizerReaderEscape(reader, stateCache.escape);
            } else {
                readerEscape.setEscape(stateCache.escape);
            }
        }
    }

    public void addState(JTokenizerState s) {
        if (states.containsKey(s.getId())) {
            throw new JParseException("State already exists");
        }
        ((JTokenizerStateImpl) s).setTokenizer(this);
        states.put(s.getId(), s);
    }

    @Override
    public JToken peek() {
        //i have a problem when back holds spaces
//        if (!back.isEmpty()) {
//            return back.getFirst();
//        }
        JToken t = next();
        if (t != null) {
            pushBack(t);
        }
        return t;
    }

    public JTokenizerState getState() {
        return peekState0();
    }

    @Override
    public JTokenizerState getState(int id) {
        JTokenizerState s = states.get(id);
        if (s == null) {
            throw new JParseException("State not found : " + id);
        }
        return s;
    }

    @Override
    public void pushBack(JToken token) {
        //        System.out.println("PUSH "+token);
        if (token != null) {
            if (!back.isEmpty()
                    && back.getFirst().tokenNumber >= 0 // not a synthetic token
                    && token.tokenNumber > back.getFirst().tokenNumber) {
                throw new JParseException("Invalid Order Detected near : " + peek());
            }
            back.addFirst(token.copy());
        }
    }

    @Override
    public JToken next() {
        final boolean skipEof = isSkipEof();
        final boolean skipComments = isSkipComments();
        final boolean skipSpaces = isSkipSpaces();
        while (true) {
            JToken t = read();
            if (t.isComments()) {
                if (skipComments) {
                    continue;
                } else {
                    lastToken = t;
                    return t;
                }
            } else if (t.isWhiteSpace()) {
                if (skipSpaces) {
                    continue;
                } else {
                    lastToken = t;
                    return t;
                }
            } else if (t.def.id == JTokenId.EOF) {
                if (skipEof) {
                    return null;
                } else {
                    return t;
                }
            } else {
                lastToken = t;
                return t;
            }
        }
    }

    @Override
    public JToken read() {
        if (!back.isEmpty()) {
            return back.removeFirst();
        }
        JTokenizerStateImpl state = peekState0();
        JToken t = state.read(
                state.escape != '\0' ? readerEscape : reader
        );
        updatePositions(t, state);
        switch (t.pushState) {
            case 0: {
                break;
            }
            case -1: {
                popState();
                break;
            }
            default: {
                pushState(t.pushState);
            }
        }
        return t;
    }

    @Override
    public JToken lastToken() {
        return lastToken;
    }

    @Override
    public JTokenizerReader reader() {
        JTokenizerStateImpl state = peekState0();
        return state.escape != '\0' ? readerEscape : reader;
    }

    @Override
    public JTokenizerSnapshot snapshot() {
        JTokenizerSnapshotImpl s = new JTokenizerSnapshotImpl(this, positionTracker.getCurrentTokenNumber());
        snapshots.add(s);
        for (JToken jToken : back) {
            s.save(jToken);
        }
        return s;
    }

    @Override
    public JTokenPattern[] getPatterns() {
        return peekState0().patterns();
    }

    @Override
    public JTokenizerState[] getStates() {
        return states.values().toArray(new JTokenizerState[0]);
    }

    @Override
    public JTokenDef[] getTokenDefinitions() {
        List<JTokenDef> tokens = new ArrayList<>();
        for (JTokenizerState value : states.values()) {
            tokens.addAll(Arrays.asList(value.tokenDefinitions()));
        }
        return tokens.toArray(new JTokenDef[0]);
    }

    @Override
    public JTokenDef[] getTokenDefinitions(Predicate<JTokenDef> filter) {
        List<JTokenDef> tokens = new ArrayList<>();
        for (JTokenizerState value : states.values()) {
            for (JTokenDef jTokenDef : value.tokenDefinitions()) {
                if (filter == null || filter.test(jTokenDef)) {
                    tokens.add(jTokenDef);
                }
            }
        }
        return tokens.toArray(new JTokenDef[0]);
    }

    @Override
    public JTokenDef getFirstTokenDefinition(Predicate<JTokenDef> filter) {
        for (JTokenizerState value : states.values()) {
            for (JTokenDef jTokenDef : value.tokenDefinitions()) {
                if (filter == null || filter.test(jTokenDef)) {
                    return jTokenDef;
                }
            }
        }
        throw new NoSuchElementException("No Matching Token Definition for " + filter);
    }

    protected JTokenizerStateImpl peekState0() {
        return stateCache;
//        return (JTokenizerStateImpl) peekState();
    }

    private String describeReader() {
        StringBuilder sb = new StringBuilder();
        int cc0 = 0;
        char ccc = '\0';
//        try {
//            cc0 = reader.read();
//            if(cc0!=-1){
//                reader.unread(cc0);
//            }
//            ccc=(char) cc0;
//        } catch (IOException e) {
//            ///
//        }
        sb.append(ccc).append("=").append(cc0);
        return sb.toString();
    }

    @Override
    public String toString() {
        String state = "s=<none>";
        if (!stateStack.isEmpty()) {
            JTokenizerStateImpl s = peekState0();
            state = "s=" + s.getId() + ":" + s.getName();
        }
        return "JTokenizer{" + state + "," + positionTracker
                + ", back=" + back
                + ", snapshotsCount=" + snapshots.size()
                + "}";
    }

    JToken updatePositions(JToken token, JTokenizerState state) {
        String image = token.image;
        token.startLineNumber = positionTracker.getCurrentRowNumber();
        token.startColumnNumber = positionTracker.getCurrentColumnNumber();
        token.startCharacterNumber = positionTracker.getCurrentCharNumber();
        token.tokenNumber = positionTracker.getCurrentTokenNumber();
        if (!token.isEOF()) {
            char[] chars = image.toCharArray();
            positionTracker.onReadChars(chars);
        }
        token.endLineNumber = positionTracker.getCurrentRowNumber();
        token.endColumnNumber = positionTracker.getCurrentColumnNumber();
        token.endCharacterNumber = positionTracker.getCurrentCharNumber();
        for (JTokenizerSnapshotImpl snapshot : snapshots) {
            snapshot.save(token);
        }
        return token;
    }

    public void onRollBackSnapshot(JTokenizerSnapshot snapshot, List<JToken> tokens, int startTokenId) {
        back.clear();
        pushBackAll(tokens);
        tokens.clear();
        for (int i = snapshots.size() - 1; i >= 0; i--) {
            JTokenizerSnapshot os = snapshots.get(i);
            if (os != snapshot) {
                if (startTokenId >= os.getStartTokenNumber()) {
                    //no problem;
                } else {
                    os.invalidate();
                }
            } else {
                snapshots.remove(i);
            }
        }
    }

    public void onDisposeSnapshot(JTokenizerSnapshot snapshot) {
        for (int i = this.snapshots.size() - 1; i >= 0; i--) {
            JTokenizerSnapshot os = this.snapshots.get(i);
            if (os == snapshot) {
                this.snapshots.remove(i);
            }
        }
    }
}
