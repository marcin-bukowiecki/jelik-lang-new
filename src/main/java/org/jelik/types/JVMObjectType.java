package org.jelik.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.common.TypeEnum;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Marcin Bukowiecki
 */
public class JVMObjectType extends Type {

    public static final Type INSTANCE = new JVMObjectType();

    public JVMObjectType() {
        super("Object", "java.lang.Object", TypeEnum.objectT);
    }

    public JVMObjectType(String name) {
        super(name, name, TypeEnum.objectT);
    }

    @Override
    public Optional<Type> getParentType(CompilationContext compilationContext) {
        return Optional.empty();
    }

    @Override
    public List<Type> getInterfaceTypes(CompilationContext compilationContext) {
        return Collections.emptyList();
    }

    @Override
    public Set<Type> getAssignableToTypes(CompilationContext compilationContext) {
        return Collections.emptySet();
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }
}
