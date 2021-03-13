package org.jelik.types;

import org.jelik.CompilationContext;
import org.jelik.compiler.common.TypeEnum;

/**
 * @author Marcin Bukowiecki
 */
public class JVMNullType extends Type {

    public static final JVMNullType INSTANCE = new JVMNullType();

    public JVMNullType() {
        super("null", "null", TypeEnum.nullT);
    }

    @Override
    public boolean isAssignableTo(Type type, CompilationContext compilationContext) {
        return true;
    }

    @Override
    public String getDescriptor() {
        return "null";
    }

    @Override
    public String getInternalName() {
        return "null";
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }
}
