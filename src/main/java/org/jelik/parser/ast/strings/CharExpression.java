package org.jelik.parser.ast.strings;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.SingleApostropheToken;
import org.jelik.parser.token.Token;
import org.jelik.types.jvm.JVMCharType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class CharExpression extends ExpressionWithType {

    @Getter
    private final SingleApostropheToken leftApostropheToken;

    private final List<Token> stringTokens;

    @Getter
    private final SingleApostropheToken rightApostropheToken;

    public CharExpression(SingleApostropheToken leftApostropheToken,
                          List<Token> stringTokens,
                          SingleApostropheToken rightApostropheToken) {
        this.leftApostropheToken = leftApostropheToken;
        this.stringTokens = stringTokens;
        this.rightApostropheToken = rightApostropheToken;
        this.getNodeContext().setType(JVMCharType.INSTANCE);
        this.getNodeContext().setGenericType(JVMCharType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitCharExpression(this, compilationContext);
    }

    @Override
    public String toString() {
        return leftApostropheToken.toString() +
                stringTokens.stream().map(Token::toString).collect(Collectors.joining()) +
                rightApostropheToken.toString();
    }

    public List<Token> getStringTokens() {
        return stringTokens;
    }

    public String getString() {
        return stringTokens.stream().map(Token::getText).collect(Collectors.joining());
    }

    @Override
    public int getStartRow() {
        return leftApostropheToken.getRow();
    }

    @Override
    public int getEndRow() {
        return rightApostropheToken.getRow();
    }

    @Override
    public int getStartCol() {
        return leftApostropheToken.getCol();
    }

    @Override
    public int getEndCol() {
        return rightApostropheToken.getCol();
    }
}
