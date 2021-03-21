package org.jelik.parser.ast.strings;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.functions.FunctionCall;
import org.jelik.parser.ast.functions.TargetFunctionCallProvider;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.parser.ast.resolvers.FunctionCallResolver;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * AST node corresponding to StringBuilder.append(...) method, used for string appending
 *
 * @author Marcin Bukowiecki
 */
@Getter
public class StringBuilderAppend extends ExpressionWithType implements FunctionCall {

    private Expression subject;

    private final TargetFunctionCallProvider targetFunctionCall;

    public StringBuilderAppend(Expression subject, CompilationContext compilationContext) {
        this.subject = subject;
        subject.setParent(this);
        Type stringBuilder = DefaultImportedTypeResolver.getType("StringBuilder");
        this.nodeContext.setType(stringBuilder);
        this.nodeContext.setGenericType(stringBuilder);
        this.targetFunctionCall = new FunctionCallResolver().resolveCall(this, compilationContext).orElseThrow();
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        this.subject = newNode;
        newNode.setParent(this);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
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
        return ".append(" + this.subject.toString() + ")" + getFurtherExpressionOpt().map(Object::toString).orElse("");
    }
}
