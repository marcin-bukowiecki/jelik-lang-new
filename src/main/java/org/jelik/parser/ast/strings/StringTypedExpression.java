package org.jelik.parser.ast.strings;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.token.ApostropheToken;
import org.jelik.parser.token.Token;
import org.jelik.types.JVMStringType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class StringTypedExpression extends TypedExpression {

    private final ApostropheToken leftApostropheToken;

    private final List<Token> stringTokens;

    private final ApostropheToken rightApostropheToken;

    public StringTypedExpression(ApostropheToken leftApostropheToken,
                                 List<Token> stringTokens,
                                 ApostropheToken rightApostropheToken) {
        this.leftApostropheToken = leftApostropheToken;
        this.stringTokens = stringTokens;
        this.rightApostropheToken = rightApostropheToken;
        this.getNodeContext().setType(JVMStringType.INSTANCE);
        this.getNodeContext().setGenericType(JVMStringType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return leftApostropheToken.toString() + stringTokens.stream().map(Token::toString).collect(Collectors.joining()) + rightApostropheToken.toString();
    }

    public String getString() {
        return stringTokens.stream().map(Token::getText).collect(Collectors.joining());
    }

    @Override
    public int getStartOffset() {
        return leftApostropheToken.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return rightApostropheToken.getEndOffset();
    }
}
