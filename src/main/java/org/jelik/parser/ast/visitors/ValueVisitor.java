package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ValKeyword;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ValueVisitor implements TokenVisitor<ValueDeclaration> {

    private final ValKeyword valKeyword;

    private AssignOperator assignOperator;

    private Expression expression;

    private TypeNode typeNode = UndefinedTypeNode.UNDEFINED_TYPE_NODE;

    public ValueVisitor(ValKeyword valKeyword) {
        this.valKeyword = valKeyword;
    }

    @Override
    public @NotNull ValueDeclaration visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        Token name = lexer.nextToken();
        Token nextToken = lexer.nextToken();
        nextToken.accept(this, parseContext);
        return new ValueDeclaration(valKeyword, (LiteralToken) name, typeNode, assignOperator, expression);
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        this.typeNode = new TypeNodeVisitor(literalToken).visit(parseContext);
        parseContext.getLexer().nextToken().accept(this, parseContext);
    }

    @Override
    public void visitAssign(AssignOperator assignOperator, ParseContext parseContext) {
        this.assignOperator = assignOperator;
        Lexer lexer = parseContext.getLexer();
        Token nextToken = lexer.nextToken();
        this.expression = new ExpressionVisitor(nextToken).visit(parseContext);
    }
}
