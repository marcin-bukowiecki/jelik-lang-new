package org.jelik.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.asm.visitor.TypeVisitor;
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
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
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
