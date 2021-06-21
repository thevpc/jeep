package net.thevpc.jeep.core.tokens;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.tokens.AbstractTokenPattern;
import net.thevpc.jeep.impl.tokens.JNamedImage;
import net.thevpc.jeep.impl.tokens.JTokenId;
import net.thevpc.jeep.impl.tokens.JTypedImage;
import net.thevpc.jeep.core.DefaultCharTypes;

import java.util.TreeMap;

public class WordListPattern extends AbstractTokenPattern {

    private JTypedImage[] words;
    private JTokenDef[] wordInfos;
    private int startId;
    private boolean caseSensitive;

    public WordListPattern(String name, int startId, JTokenPatternOrder order, JTokenType ttype, boolean caseSensitive, String... words) {
        this(name, startId, order, caseSensitive, toJTypedImages(startId, ttype, words));
    }

    public WordListPattern(String name, int startId, JTokenPatternOrder order, JTokenType ttype, boolean caseSensitive, JNamedImage... words) {
        this(name, startId, order, caseSensitive, toJTypedImages(startId, ttype, words));
    }

    public WordListPattern(String name, JTokenPatternOrder order, JTokenType ttype, boolean caseSensitive, JNamedImage... words) {
        this(name, Integer.MIN_VALUE, order, caseSensitive, toJTypedImages(Integer.MIN_VALUE, ttype, words));
    }

    public WordListPattern(String name, int startId, JTokenPatternOrder order, boolean caseSensitive, JTypedImage... words) {
        super(order, name == null ? "Words" : name);
        this.startId = startId;
        //remove duplicates!
        TreeMap<String, JTypedImage> m = new TreeMap<>();
        for (JTypedImage word : words) {
            m.put(word.image(), word);
        }
        this.words = m.values().toArray(new JTypedImage[0]);
        this.wordInfos = new JTokenDef[this.words.length];
        int availableId = startId;
        HashSet<Integer> usedIds = new HashSet<>();
        for (int i = 0; i < this.words.length; i++) {
            JTypedImage word = this.words[i];
            int id = 0;
            if (word.hasPreferredId()) {
                id = word.getPreferredId();
                if (usedIds.contains(id)) {
                    throw new IllegalArgumentException("Invalid id, already used : " + id);
                } else {
                    usedIds.add(id);
                }
            } else {
                while (usedIds.contains(availableId)) {
                    availableId++;
                }
                usedIds.add(availableId);
                id = availableId;
                availableId++;
            }
            this.wordInfos[i] = new JTokenDef(
                    id,
                    word.getIdName(),
                    word.ttype(),
                    word.getTypeName(), word.image()
            );
        }
    }

    protected static JTypedImage[] toJTypedImages(int startId, JTokenType ttype, String[] words) {
        if (startId == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Missing PreferredId");
//            JTypedImage[] ti = new JTypedImage[words.length];
//            for (int i = 0; i < ti.length; i++) {
//                ti[i] = new JTypedImage(words[i], ttype, typeName);
//            }
//            return ti;
        } else {
            JTypedImage[] ti = new JTypedImage[words.length];
            for (int i = 0; i < ti.length; i++) {
                ti[i] = new JTypedImage(words[i], ttype.getValue(), ttype.getName(), JTokenId.getName(words[i]));
            }
            return ti;
        }
    }

    protected static JTypedImage[] toJTypedImages(int startId, JTokenType ttype, JNamedImage[] words) {
        if (startId == Integer.MIN_VALUE) {
            JTypedImage[] ti = new JTypedImage[words.length];
            for (int i = 0; i < ti.length; i++) {
                if (words[i].hasPreferredId()) {
                    ti[i] = new JTypedImage(words[i].image(), ttype.getValue(), ttype.getName(), words[i].getIdName(), words[i].getPreferredId());
                } else {
                    throw new IllegalArgumentException("Missing PreferredId");
                }
            }
            return ti;
        } else {
            JTypedImage[] ti = new JTypedImage[words.length];
            for (int i = 0; i < ti.length; i++) {
                ti[i] = new JTypedImage(words[i].image(), ttype.getValue(), ttype.getName(), words[i].getIdName(), words[i].getPreferredId());
            }
            return ti;
        }
    }

    public int getStartId() {
        return startId;
    }

    @Override
    public void bindToState(JTokenizerState state) {
        super.bindToState(state);
        for (int i = 0; i < wordInfos.length; i++) {
            wordInfos[i] = wordInfos[i].bindToState(state);
        }
    }

    @Override
    public JTokenDef[] tokenDefinitions() {
        return wordInfos;
    }

    @Override
    public JTokenMatcher matcher() {
        return new AbstractJTokenMatcher(order()) {
            int from = 0;
            int to = words.length;
            int pos = 0;
            boolean error = false;
            boolean caseSensitive;
            {
                this.caseSensitive=WordListPattern.this.caseSensitive;
            }
            @Override
            public JTokenPattern pattern() {
                return WordListPattern.this;
            }

            @Override
            public JTokenMatcher reset() {
                error = false;
                from = 0;
                to = words.length;
                pos = 0;
                return super.reset();
            }

            @Override
            protected void fillToken(JToken token, JTokenizerReader reader) {
                token.image = image();
                token.sval = token.image;
                fill(wordInfos[from], token);
            }

            @Override
            public boolean valid() {
                return !error && (words[from].image().length() == pos);
            }

            @Override
            public boolean matches(char c) {
                int start = from;
                while (start < to) {
                    if (ok(start, c)) {
                        int end = start + 1;
                        while (end < to) {
                            if (ok(end, c)) {
                                end++;
                            } else {
                                break;
                            }
                        }
                        from = start;
                        image.append(c);
                        pos++;
                        to = end;
                        return true;
                    } else {
                        start++;
                    }
                }
                if (isError(image, c)) {
                    error = true;
                }
                return false;
            }

            private boolean ok(int i, char c) {
                String w = words[i].image();
                if (pos < w.length()) {
                    char c0 = w.charAt(pos);
                    if(caseSensitive) {
                        return c0 == c;
                    }else{
                        return Character.toUpperCase(c0)==Character.toUpperCase(c);
                    }
                }
                return false;
            }

            @Override
            public Object value() {
                return words[from];
            }
        };
    }

    protected boolean isError(CharSequence image, char nextChar) {
        int imageLength = image.length();
        if (imageLength == 0) {
            return false;
        }
        int ct0 = DefaultCharTypes.defaultCharTypeOf(image.charAt(imageLength - 1));
        if (ct0 == DefaultCharTypes.DEFAULT_TYPE_LETTER) {
            int ct = DefaultCharTypes.defaultCharTypeOf(nextChar);
            switch (ct) {
                case DefaultCharTypes.DEFAULT_TYPE_OTHER:
                case DefaultCharTypes.DEFAULT_TYPE_DIGIT:
                case DefaultCharTypes.DEFAULT_TYPE_LETTER: {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString());
        sb.append(":").append(getClass().getSimpleName());
        sb.append(" {");
        sb.append("\n\tstartId:").append(startId);
        Map<String, List<JTokenDef>> groups = new LinkedHashMap<>();
        for (JTokenDef wordInfo : wordInfos) {
            List<JTokenDef> list = groups.computeIfAbsent(wordInfo.ttypeName + ":" + wordInfo.ttype, k -> new ArrayList<>());
            list.add(wordInfo);
        }
        for (Map.Entry<String, List<JTokenDef>> entry : groups.entrySet()) {
            sb.append("\n\ttype:").append(entry.getKey()).append(" {");
            for (JTokenDef jTokenDef : entry.getValue()) {
                sb.append("\n\t\t").append(JToken.quoteString(jTokenDef.idName)).append(" ").append(jTokenDef.id);
            }
            sb.append("\n\t}");
        }
        sb.append("\n}");

        return sb.toString();
    }

}
