package org.jelik.types;

import org.jelik.compiler.common.TypeEnum;

public class UndefinedType extends Type {

    public static final UndefinedType INSTANCE = new UndefinedType();

    public UndefinedType() {
        super("UNDEFINED", "UNDEFINED", TypeEnum.undefined);
    }
}
