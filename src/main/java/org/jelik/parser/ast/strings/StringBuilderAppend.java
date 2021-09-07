package org.jelik.parser.ast.strings;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.functions.FunctionCall;
import org.jelik.parser.ast.functions.providers.TargetFunctionCallProvider;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.parser.ast.resolvers.FunctionCallResolver;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * AST node corresponding to StringBuilder.append(...) method, used for string appending
 *
 * @author Marcin Bukowiecki
 */
public class StringBuilderAppend extends TypedExpression implements FunctionCall {

    private Expression subject;

    private final TargetFunctionCallProvider<?> targetFunctionCall;

    private Expression furtherExpression;

    public StringBuilderAppend(@NotNull Expression subject, @NotNull CompilationContext compilationContext) {
        this.subject = subject;
        subject.setParent(this);
        Type stringBuilder = DefaultImportedTypeResolver.getType("StringBuilder");
        this.nodeContext.setType(stringBuilder);
        this.nodeContext.setGenericType(stringBuilder);
        this.targetFunctionCall = new FunctionCallResolver()
                .resolveCall(this, compilationContext)
                .orElseThrow();
    }

    @NotNull
    public TargetFunctionCallProvider<?> getTargetFunctionCall() {
        return targetFunctionCall;
    }

    public Expression getFurtherExpression() {
        return furtherExpression;
    }

    public void setFurtherExpression(@NotNull Expression furtherExpression) {
        this.furtherExpression = furtherExpression;
        this.furtherExpression.setParent(this);
    }

    public Optional<Expression> getFurtherExpressionOpt() {
        return Optional.ofNullable(furtherExpression);
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        this.subject = newNode;
        newNode.setParent(this);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    public Expression getSubject() {
        return subject;
    }

    @Override
    public Type getOwner() {
        return this.nodeContext.getType();
    }

    @Override
    public List<Type> getArgumentTypes() {
        return Collections.singletonList(this.subject.getGenericType());
    }

    @Override
    public String getName() {
        return "append";
    }

    @Override
    public String toString() {
        return ".append(" + this.subject.toString() + ")";
    }
}
