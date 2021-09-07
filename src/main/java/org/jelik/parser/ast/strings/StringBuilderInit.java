package org.jelik.parser.ast.strings;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.types.JVMStringType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * AST node corresponding to new StringBuilder(), used for string appending
 *
 * @author Marcin Bukowiecki
 */
public class StringBuilderInit extends TypedExpression {

    private Expression furtherExpression;

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
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "new StringBuilder()";
    }

    public void setFurtherExpression(@NotNull StringBuilderAppend stringBuilderAppend) {
        this.furtherExpression = stringBuilderAppend;
        this.furtherExpression.setParent(this);
    }

    public Optional<Expression> getFurtherExpressionOpt() {
        return Optional.ofNullable(furtherExpression);
    }
}
