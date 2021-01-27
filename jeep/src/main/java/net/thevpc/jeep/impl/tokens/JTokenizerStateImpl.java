/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.tokens.JTokenDef;

import java.util.*;

/**
 * @author thevpc
 */
public class JTokenizerStateImpl implements JTokenizerState {

    int stateId;
    String stateName;
    char escape;
    private LinkedList<JTokenMatcher> matchers = new LinkedList<>();
    private JTokenizerImpl tokenizer;
    private JTokenDef eof;

    public JTokenizerStateImpl(int stateId, String stateName, JTokenPattern[] patterns, char escape) {
        this.stateId = stateId;
        this.escape = escape;
        this.stateName = stateName;
        eof = new JTokenDef(
                -1,
                "EOF",
                -1,
                "EOF",
                "<EOF>",
                stateId,
                stateName
        );
        for (JTokenPattern pattern : patterns) {
            ((AbstractTokenPattern) pattern).bindToState(this);
        }
        for (JTokenPattern matcher : patterns) {
            addMatcher(matcher.matcher());
        }
    }

    public JTokenizerStateImpl setTokenizer(JTokenizerImpl tokenizer) {
        this.tokenizer = tokenizer;
        return this;
    }

    public int getId() {
        return stateId;
    }

    @Override
    public String getName() {
        return stateName;
    }

    public JTokenPattern[] patterns() {
        return matchers.stream().map((x) -> x.pattern()).toArray(JTokenPattern[]::new);
    }

    public JTokenDef[] tokenDefinitions() {
        List<JTokenDef> tokens = new ArrayList<>();
        Map<Integer, JTokenDef> stateTokIds = new HashMap<>();
        for (JTokenMatcher matcher : matchers) {
            for (JTokenDef token : matcher.pattern().tokenDefinitions()) {
                if (stateTokIds.containsKey(token.id)) {
                    //throw new IllegalStateException("token id " + token.id + " for (" + token + ") already registered for (" + stateTokIds.get(token.id) + ")");
                }else {
                    stateTokIds.put(token.id, token);
                    tokens.add(token);
                }
            }
        }
        return tokens.toArray(new JTokenDef[0]);
    }

    public void addMatcher(JTokenMatcher matcher) {
        matchers.add(matcher);
        matchers.sort(Comparator.comparing(JTokenMatcher::order));
    }

    public JToken read(JTokenizerReader reader) {
        //        System.err.println(">> "+back.size()+" :: "+back);
        //        String curr=describeReader();
        JToken token = new JToken();
        for (JTokenMatcher matcher : matchers) {
            if (matcher.reset().matches(reader, token)) {
                return token;
            }
        }
        int cc = reader.read();
        if (cc == -1) {
            token.def = eof;
            return token;
        }
        token.image = String.valueOf((char) cc);
        token.def = new JTokenDef(
                30000 + (cc),
                "CHAR_" + JTokenId.getName(""+((char) cc)),
                JTokenType.TT_NOTHING,
                "TT_NOTHING",
                ""+((char) cc),
                getId(),
                getName()
        );
//        token.id = token.def.id;
//        token.ttype = token.def.ttype;
        return token;
    }

    public JTokenMatcher[] matchers() {
        return matchers.toArray(new JTokenMatcher[0]);
    }

    @Override
    public String toString() {
        return "JTokenizerState{" + getId() + "," + getName()+"}";
    }

}
