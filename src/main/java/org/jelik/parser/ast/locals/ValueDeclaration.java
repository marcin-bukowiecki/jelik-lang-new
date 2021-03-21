package org.jelik.parser.ast.locals;

import lombok.Getter;
import lombok.Setter;
import org.jelik.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.ast.expression.StackConsumer;
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
public class ValueDeclaration extends ExpressionReferencingType implements WithLocalVariableDeclaration, StackConsumer {

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

        this.valKeyword = valKeyword;
        this.literalToken = literalToken;
        this.typeNode = typeNode;
        this.assignOperator = assignOperator;
        setFurtherExpression(expression);
    }

    @Override
    public int getStartCol() {
        return valKeyword.getCol();
    }

    @Override
    public int getEndCol() {
        return getFurtherExpressionOpt().map(ASTNode::getEndCol).orElse(assignOperator.getEndCol());
    }

    @Override
    public int getEndRow() {
        return getFurtherExpressionOpt().map(ASTNode::getEndRow).orElse(assignOperator.getEndRow());
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        assert oldNode == getFurtherExpression();
        setFurtherExpression(newNode);
    }

    @Override
    public int getStartRow() {
        return valKeyword.getRow();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitValueDeclaration(this, compilationContext);
    }

    @Override
    public String toString() {
        return valKeyword.toString() + " " +
                literalToken.toString() + " " +
                assignOperator.toString() + " " +
                (typeNode != null ? typeNode.toString() : "") +
                getFurtherExpressionOpt().map(Object::toString).orElse("");
    }
}
