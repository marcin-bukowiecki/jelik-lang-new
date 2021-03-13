package org.jelik.types;

import org.jelik.compiler.common.TypeEnum;

/**
 * @author Marcin Bukowiecki
 */
public class JVMVoidType extends Type {

    public static final JVMVoidType INSTANCE = new JVMVoidType();

    public JVMVoidType() {
        super("void", "void", TypeEnum.voidT);
    }

    @Override
    public String getDescriptor() {
        return "V";
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }
}
