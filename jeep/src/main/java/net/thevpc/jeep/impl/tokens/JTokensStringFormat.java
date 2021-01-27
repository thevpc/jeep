package net.thevpc.jeep.impl.tokens;

import net.thevpc.jeep.JToken;

public interface JTokensStringFormat {
    String format(Iterable<JToken> tokensb);

    String formatDocument(Iterable<JToken> tokensb);

    String format(JToken token);
}
