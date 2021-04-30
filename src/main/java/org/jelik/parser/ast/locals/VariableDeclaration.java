package org.jelik.parser.ast.locals;

import lombok.Getter;
import lombok.Setter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.ConsumingExpression;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWrapper;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.VarKeyword;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;

/**
 * Represents:
 *
 * var a = expr
 *
 * var a Int = expr
 *
 * @author Marcin Bukowiecki
 */
@Getter
public class VariableDeclaration extends ExpressionWrapper implements WithLocalVariableDeclaration, ConsumingExpression, StackConsumer {

    private final VarKeyword varKeyword;

    private final LiteralToken literalToken;

    private final TypeNode typeNode;

    private final AssignOperator assignOperator;

    @Setter
    private LocalVariable localVariable;

    public VariableDeclaration(@NotNull VarKeyword varKeyword,
                               @NotNull LiteralToken literalToken,
                               @NotNull TypeNode typeNode,
                               @NotNull AssignOperator assignOperator,
                               @NotNull Expression expression) {
        super(expression);
        this.varKeyword = varKeyword;
        this.literalToken = literalToken;
        this.typeNode = typeNode;
        this.assignOperator = assignOperator;
    }

    @Override
    public int getStartCol() {
        return varKeyword.getCol();
    }

    @Override
    public int getStartRow() {
        return varKeyword.getRow();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitVariableDeclaration(this, compilationContext);
    }

    @Override
    public String toString() {
        return varKeyword.toString() + " " +
                literalToken.toString() + " " +
                (typeNode instanceof UndefinedTypeNode ? "" : typeNode.toString() + " ") +
                assignOperator.toString() + " " + getExpression();
    }
}
