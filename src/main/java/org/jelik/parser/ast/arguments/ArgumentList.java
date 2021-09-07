package org.jelik.parser.ast.arguments;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jelik.types.FunctionType;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents (expr, expr, expr)
 *
 * @author Marcin Bukowiecki
 */
public class ArgumentList extends ASTNodeImpl implements Expression {

    public static ArgumentList EMPTY = new ArgumentList(
            new LeftParenthesisToken(-1),
            Collections.emptyList(),
            new RightParenthesisToken(-1)
    );

    private final LeftParenthesisToken leftParenthesisToken;

    private final List<Argument> arguments;

    private final RightParenthesisToken rightParenthesisToken;

    public ArgumentList(@NotNull LeftParenthesisToken leftParenthesisToken,
                        @NotNull List<@NotNull Argument> arguments,
                        @NotNull RightParenthesisToken rightParenthesisToken) {
        this.leftParenthesisToken = leftParenthesisToken;
        this.arguments = arguments;
        this.rightParenthesisToken = rightParenthesisToken;
        for (var argument : arguments) {
            argument.setParent(getParent());
        }
    }

    public ArgumentList(@NotNull Argument argument) {
        this.leftParenthesisToken = new LeftParenthesisToken(-1);
        this.arguments = Collections.singletonList(argument);
        this.rightParenthesisToken = new RightParenthesisToken(-1);
        argument.setParent(this);
    }

    public @NotNull LeftParenthesisToken getLeftParenthesisToken() {
        return leftParenthesisToken;
    }

    public @NotNull List<@NotNull Argument> getArguments() {
        return arguments;
    }

    public @NotNull RightParenthesisToken getRightParenthesisToken() {
        return rightParenthesisToken;
    }

    @Override
    public int getStartOffset() {
        return leftParenthesisToken.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return rightParenthesisToken.getEndOffset();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitArgumentList(this, compilationContext);
    }

    @Override
    public String toString() {
        return leftParenthesisToken.toString() +
                arguments.stream().map(Object::toString).collect(Collectors.joining()) +
                rightParenthesisToken.toString();
    }

    @Override
    public Type getReturnType() {
        return FunctionType.Companion.getFunctionType(arguments.size(), JVMVoidType.INSTANCE);
    }

    @Override
    public Type getGenericReturnType() {
        return FunctionType.Companion.getFunctionType(arguments.size(), JVMVoidType.INSTANCE);
    }

    @Override
    public Type getType() {
        return FunctionType.Companion.getFunctionType(arguments.size(), JVMVoidType.INSTANCE);
    }

    @Override
    public Type getGenericType() {
        return FunctionType.Companion.getFunctionType(arguments.size(), JVMVoidType.INSTANCE);
    }
}
