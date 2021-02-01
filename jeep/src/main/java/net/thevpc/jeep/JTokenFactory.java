package net.thevpc.jeep;

public interface JTokenFactory {
    JTokenizer create(JTokenizerReader reader, JTokenConfig config, JContext context);
}
