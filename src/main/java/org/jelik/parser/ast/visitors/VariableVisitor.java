package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.locals.VariableDeclaration;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.VarKeyword;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;

/**
 * Represents:
 *
 * var a = exp or var a Int = expr
 *
 * @author Marcin Bukowiecki
 */
public class VariableVisitor implements ParseVisitor<VariableDeclaration> {

    private final VarKeyword varKeyword;

    private AssignOperator assignOperator;

    private Expression expression;

    private TypeNode typeNode = UndefinedTypeNode.UNDEFINED_TYPE_NODE;

    public VariableVisitor(VarKeyword varKeyword) {
        this.varKeyword = varKeyword;
    }

    @Override
    public @NotNull VariableDeclaration visit(@NotNull ParseContext parseContext) {
        var lexer = parseContext.getLexer();
        var name = lexer.nextToken();
        var nextToken = lexer.nextToken();
        nextToken.visit(this, parseContext);
        return new VariableDeclaration(varKeyword, (LiteralToken) name, typeNode, assignOperator, expression);
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        this.typeNode = new TypeNodeVisitor(literalToken).visit(parseContext);
        parseContext.getLexer().nextToken().visit(this, parseContext);
    }

    @Override
    public void visitAssign(AssignOperator assignOperator, ParseContext parseContext) {
        this.assignOperator = assignOperator;
        var lexer = parseContext.getLexer();
        var nextToken = lexer.nextToken();
        this.expression = new ExpressionVisitor(nextToken).visit(parseContext);
    }
}
