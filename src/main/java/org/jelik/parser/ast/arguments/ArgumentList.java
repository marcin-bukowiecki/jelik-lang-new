package org.jelik.parser.ast.arguments;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents (expr, expr, expr)
 *
 * @author Marcin Bukowiecki
 */
@Getter
public class ArgumentList extends ExpressionReferencingType {

    public static ArgumentList EMPTY = new ArgumentList(new LeftParenthesisToken(-1,-1), Collections.emptyList(), new RightParenthesisToken(-1, -1));

    private final LeftParenthesisToken leftParenthesisToken;

    private final List<Argument> arguments;

    private final RightParenthesisToken rightParenthesisToken;

    public ArgumentList(LeftParenthesisToken leftParenthesisToken, List<Argument> arguments, RightParenthesisToken rightParenthesisToken) {
        this.leftParenthesisToken = leftParenthesisToken;
        this.arguments = arguments;
        this.rightParenthesisToken = rightParenthesisToken;
        for (Argument argument : arguments) {
            argument.parent = parent;
        }
    }

    @Override
    public int getStartCol() {
        return leftParenthesisToken.getCol();
    }

    @Override
    public int getEndCol() {
        return rightParenthesisToken.getCol();
    }

    @Override
    public int getStartRow() {
        return leftParenthesisToken.getRow();
    }

    @Override
    public int getEndRow() {
        return rightParenthesisToken.getRow();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }

    @Override
    public String toString() {
        return leftParenthesisToken.toString() +
                arguments.stream().map(Object::toString).collect(Collectors.joining()) +
                rightParenthesisToken.toString() +
                getFurtherExpressionOpt().map(Object::toString).orElse("");
    }
}
