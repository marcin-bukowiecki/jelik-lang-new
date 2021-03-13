package org.jelik.parser.ast.locals;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.VarKeyword;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class VariableDeclaration extends ExpressionReferencingType {

    private final VarKeyword varKeyword;

    private final LiteralToken literalToken;

    private final TypeNode typeNode;

    private final AssignOperator assignOperator;

    public VariableDeclaration(VarKeyword varKeyword, LiteralToken literalToken, TypeNode typeNode, AssignOperator assignOperator, Expression expression) {
        this.varKeyword = varKeyword;
        this.literalToken = literalToken;
        this.typeNode = typeNode;
        this.assignOperator = assignOperator;
        setFurtherExpression(expression);
    }

    public Optional<TypeNode> getTypeNode() {
        return Optional.ofNullable(typeNode);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
