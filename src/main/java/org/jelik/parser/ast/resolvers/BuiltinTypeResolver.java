package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class BuiltinTypeResolver extends AstVisitor {

    private Type type;

    @Override
    public void visitSingleTypeNode(@NotNull SingleTypeNode typeNode, @NotNull CompilationContext compilationContext) {
        String text = typeNode.getText();
        BuiltinTypeRegister.INSTANCE
                .checkForBuiltinByName(text)
                .ifPresent(t -> {
                    typeNode.setType(t);
                    typeNode.setGenericType(t);
                    type = t;
                });
    }

    public Optional<Type> getTypeOpt() {
        return Optional.ofNullable(type);
    }
}
