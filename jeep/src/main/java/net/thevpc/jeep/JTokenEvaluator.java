package net.thevpc.jeep;

public interface JTokenEvaluator {
    /**
     *
     * @param id token id
     * @param image contains token image including white characters (such as _ in numbers)
     * @param cleanImage contains image with removed white characters (such as _ in numbers)
     * @param typeName name for the image as defined by the matcher
     * @return
     */
    Object eval(int id, String image, String cleanImage, String typeName);
}
