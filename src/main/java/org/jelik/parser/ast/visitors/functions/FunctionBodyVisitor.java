package org.jelik.parser.ast.visitors.functions;

import org.jelik.compiler.utils.TokenUtils;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.visitors.blocks.BlockVisitor;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionBodyVisitor implements TokenVisitor<FunctionBodyBlock> {

    private final LeftCurlToken leftCurlToken;

    public FunctionBodyVisitor(LeftCurlToken leftCurlToken) {
        this.leftCurlToken = leftCurlToken;
    }

    @Override
    public @NotNull FunctionBodyBlock visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        BasicBlockImpl visit = new BlockVisitor(leftCurlToken).visit(parseContext);
        Token current = lexer.getCurrent();
        TokenUtils.checkCurrentNotMatching("rightCurl.expected", parseContext, RightCurlToken.class);
        return new FunctionBodyBlock(leftCurlToken, visit, ((RightCurlToken) current));
    }
}
