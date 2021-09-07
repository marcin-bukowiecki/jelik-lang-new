package org.jelik.parser.ast.strings;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.TypedExpression;
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
public class CharTypedExpression extends TypedExpression {

    private final SingleApostropheToken leftApostropheToken;

    private final List<Token> stringTokens;

    private final SingleApostropheToken rightApostropheToken;

    public CharTypedExpression(SingleApostropheToken leftApostropheToken,
                               List<Token> stringTokens,
                               SingleApostropheToken rightApostropheToken) {
        this.leftApostropheToken = leftApostropheToken;
        this.stringTokens = stringTokens;
        this.rightApostropheToken = rightApostropheToken;
        this.getNodeContext().setType(JVMCharType.INSTANCE);
        this.getNodeContext().setGenericType(JVMCharType.INSTANCE);
    }

    @NotNull
    public SingleApostropheToken getLeftApostropheToken() {
        return leftApostropheToken;
    }

    @NotNull
    public SingleApostropheToken getRightApostropheToken() {
        return rightApostropheToken;
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
    public int getStartOffset() {
        return getLeftApostropheToken().getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return getRightApostropheToken().getEndOffset();
    }
}
