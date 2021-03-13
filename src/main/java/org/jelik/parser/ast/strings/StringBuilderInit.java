package org.jelik.parser.ast.strings;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.types.JVMStringType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * AST node corresponding to new StringBuilder(), used for string appending
 *
 * @author Marcin Bukowiecki
 */
public class StringBuilderInit extends ExpressionWithType {

    public StringBuilderInit() {
        Type stringBuilder = DefaultImportedTypeResolver.getType("StringBuilder");
        this.nodeContext.setType(stringBuilder);
        this.nodeContext.setGenericType(stringBuilder);
    }

    @Override
    public Type getGenericReturnType() {
        return JVMStringType.INSTANCE;
    }

    @Override
    public Type getReturnType() {
        return JVMStringType.INSTANCE;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "new StringBuilder()" + getFurtherExpressionOpt().map(Object::toString).orElse("");
    }
}
