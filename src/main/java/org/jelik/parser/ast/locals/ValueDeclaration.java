package org.jelik.parser.ast.locals;

import lombok.Getter;
import lombok.Setter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.Statement;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWrapper;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.ValKeyword;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class ValueDeclaration extends ExpressionWrapper implements Statement, WithLocalVariableDeclaration {

    private final ValKeyword valKeyword;

    private final LiteralToken literalToken;

    private final TypeNode typeNode;

    private final AssignOperator assignOperator;

    @Setter
    private LocalVariable localVariable;

    public ValueDeclaration(@NotNull ValKeyword valKeyword,
                            @NotNull LiteralToken literalToken,
                            @NotNull TypeNode typeNode,
                            @NotNull AssignOperator assignOperator,
                            @NotNull Expression expression) {
        super(expression);
        this.valKeyword = valKeyword;
        this.literalToken = literalToken;
        this.typeNode = typeNode;
        this.assignOperator = assignOperator;
    }

    @Override
    public int getStartCol() {
        return valKeyword.getCol();
    }

    @Override
    public int getStartRow() {
        return valKeyword.getRow();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitValueDeclaration(this, compilationContext);
    }

    @Override
    public String toString() {
        return valKeyword.toString() + " " +
                literalToken.toString() + " " +
                (typeNode != null ? typeNode.toString() : "") +
                assignOperator.toString() + " " + getExpression().toString();
    }
}
