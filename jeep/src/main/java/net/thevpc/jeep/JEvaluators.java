package net.thevpc.jeep;

public interface JEvaluators {
    JEvaluatorFactory getFactory();

    JEvaluators setFactory(JEvaluatorFactory factory);

    JEvaluator newEvaluator();

}
