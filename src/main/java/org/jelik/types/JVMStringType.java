package org.jelik.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.common.TypeEnum;

public class JVMStringType extends Type {

    public static final JVMStringType INSTANCE = new JVMStringType();

    public JVMStringType() {
        super("String", "java.lang.String", TypeEnum.string);
    }

    @Override
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    public String getDescriptor() {
        return "Ljava/lang/String;";
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }
}
