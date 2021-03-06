package org.jelik.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.runtime.TypeEnum;
import org.jelik.types.jvm.BooleanWrapperType;
import org.jelik.types.jvm.NumberType;

/**
 * @author Marcin Bukowiecki
 */
public class JVMBooleanType extends Type {

    public static final JVMBooleanType INSTANCE = new JVMBooleanType();

    public JVMBooleanType() {
        super("boolean", "Z", TypeEnum.booleanT);
    }

    @Override
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    public NumberType getWrapperType() {
        return BooleanWrapperType.INSTANCE;
    }

    @Override
    public String getDescriptor() {
        return "Z";
    }

    @Override
    public String toString() {
        return "boolean";
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }
}
