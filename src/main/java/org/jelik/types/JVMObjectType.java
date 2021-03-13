package org.jelik.types;

import org.jelik.CompilationContext;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.common.TypeEnum;

/**
 * @author Marcin Bukowiecki
 */
public class JVMObjectType extends Type {

    public static final Type INSTANCE = new JVMObjectType();

    public JVMObjectType() {
        super("Object", "java.lang.Object", TypeEnum.objectT);
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public void visit(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }
}
