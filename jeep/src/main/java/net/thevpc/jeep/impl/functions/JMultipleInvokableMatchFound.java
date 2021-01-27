package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JInvokable;
import net.thevpc.jeep.JParseException;

public class JMultipleInvokableMatchFound extends JParseException {
    private JInvokable[] allPossibilities;
    private String signature;

    public JMultipleInvokableMatchFound(String signature, JInvokable[] allPossibilities) {
        super(buildMessage(signature,allPossibilities));
        this.signature = signature;
        this.allPossibilities = allPossibilities;
    }

    private static String buildMessage(String signature,JInvokable[] allPossibilities){
        StringBuilder sb=new StringBuilder("Multiple matches were detected for "+signature);
        sb.append("\nall the followings match : ");
        for (JInvokable allPossibility : allPossibilities) {
            sb.append("\n ").append(allPossibility);
        }
        return sb.toString();
    }

    public JInvokable[] getAllPossibilities() {
        return allPossibilities;
    }

    public String getSignature() {
        return signature;
    }
}
