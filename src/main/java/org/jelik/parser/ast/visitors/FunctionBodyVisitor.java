package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionBodyVisitor implements ParseVisitor<FunctionBodyBlock> {

    private final LeftCurlToken leftCurlToken;

    public FunctionBodyVisitor(LeftCurlToken leftCurlToken) {
        this.leftCurlToken = leftCurlToken;
    }

    @Override
    public @NotNull FunctionBodyBlock visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        BasicBlock visit = new BlockVisitor().visit(parseContext);
        Token current = lexer.getCurrent();
        return new FunctionBodyBlock(leftCurlToken, visit, ((RightCurlToken) current));
    }
}
