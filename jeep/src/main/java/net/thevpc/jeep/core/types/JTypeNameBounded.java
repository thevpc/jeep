package net.thevpc.jeep.core.types;

import net.thevpc.jeep.JTypeNameOrVariable;
import net.thevpc.jeep.JTypeName;

public class JTypeNameBounded implements JTypeNameOrVariable {
    private String name;
    private JTypeName[] lowerBounds;
    private JTypeName[] upperBounds;
    private int array;

    public JTypeNameBounded(String name, JTypeName[] lowerBounds, JTypeName[] upperBounds) {
        this(name, lowerBounds, upperBounds,0);
    }
    public JTypeNameBounded(String name, JTypeName[] lowerBounds, JTypeName[] upperBounds,int array) {
        this.name = name;
        this.lowerBounds = lowerBounds == null ? new JTypeName[0] : lowerBounds;
        this.upperBounds = upperBounds == null ? new JTypeName[0] : upperBounds;
        this.array = array;
    }

    public String name() {
        return name;
    }

    public JTypeName[] getLowerBound() {
        return lowerBounds;
    }

    public JTypeName[] getUpperBound() {
        return upperBounds;
    }

    @Override
    public JTypeNameOrVariable withSimpleName() {
        String name = DefaultTypeName.simpleNameOf(this.name);
        JTypeName[] lowerBounds = new JTypeName[this.lowerBounds.length];
        JTypeName[] upperBounds = new JTypeName[this.upperBounds.length];
        for (int i = 0; i < lowerBounds.length; i++) {
            lowerBounds[i] = (JTypeName) this.lowerBounds[i].withSimpleName();
        }
        for (int i = 0; i < upperBounds.length; i++) {
            upperBounds[i] = (JTypeName) this.upperBounds[i].withSimpleName();
        }
        return new JTypeNameBounded(name, lowerBounds, upperBounds,array);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name == null) {
            sb.append("?");
        } else {
            sb.append(name);
        }
        if (lowerBounds.length > 0) {
            sb.append(" extends");
            for (int i = 0; i < lowerBounds.length; i++) {
                sb.append(" ");
                if (i > 0) {
                    sb.append("& ");
                }
                sb.append(lowerBounds[i]);
            }
        }
        if (upperBounds.length > 0) {
            sb.append(" super");
            for (int i = 0; i < upperBounds.length; i++) {
                sb.append(" ");
                if (i > 0) {
                    sb.append("& ");
                }
                sb.append(upperBounds[i]);
            }
        }
        return sb.toString();
    }

    @Override
    public JTypeNameOrVariable toArray() {
        return new JTypeNameBounded(name, lowerBounds, upperBounds, array+1);
    }
}
