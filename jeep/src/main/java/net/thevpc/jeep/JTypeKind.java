package net.thevpc.jeep;

import net.thevpc.jeep.impl.JEnum;
import net.thevpc.jeep.impl.JEnumDefinition;
import net.thevpc.jeep.impl.JEnumTypeRegistry;

public final class JTypeKind extends JEnum {
    public static final class Ids {
        public static final int CLASS=1;
        public static final int INTERFACE=2;
        public static final int ENUM=3;
        public static final int ANNOTATION=4;
        public static final int EXCEPTION=5;
        private Ids() {
        }
    }

    public static final JEnumDefinition<JTypeKind> _DEFINITIONS = JEnumTypeRegistry.INSTANCE.register(JTypeKind.class)
            .addConstIntFields(JTypeKind.Ids.class, f -> true);

    public static final JTypeKind CLASS =   _DEFINITIONS.valueOf("CLASS");
    public static final JTypeKind INTERFACE=   _DEFINITIONS.valueOf("INTERFACE");
    public static final JTypeKind ENUM=   JTypeKind._DEFINITIONS.valueOf("ENUM");
    public static final JTypeKind ANNOTATION=   JTypeKind._DEFINITIONS.valueOf("ANNOTATION");
    public static final JTypeKind EXCEPTION=   JTypeKind._DEFINITIONS.valueOf("EXCEPTION");


    protected JTypeKind(JEnumDefinition type, String name, int value) {
        super(type, name, value);
    }

}
