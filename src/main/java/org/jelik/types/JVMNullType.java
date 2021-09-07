package org.jelik.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.runtime.TypeEnum;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class JVMNullType extends Type {

    public static final JVMNullType INSTANCE = new JVMNullType();

    public JVMNullType() {
        super("null", "null", TypeEnum.nullT);
    }

    @Override
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    public boolean isAssignableTo(@NotNull Type type, @NotNull CompilationContext compilationContext) {
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
