package org.jelik.parser.ast.strings;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.token.ApostropheToken;
import org.jelik.parser.token.Token;
import org.jelik.types.JVMStringType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class StringExpression extends ExpressionWithType {

    @Getter
    private final ApostropheToken leftApostropheToken;

    @Getter
    private final List<Token> stringTokens;

    @Getter
    private final ApostropheToken rightApostropheToken;

    public StringExpression(ApostropheToken leftApostropheToken, List<Token> stringTokens, ApostropheToken rightApostropheToken) {
        this.leftApostropheToken = leftApostropheToken;
        this.stringTokens = stringTokens;
        this.rightApostropheToken = rightApostropheToken;
        this.getNodeContext().setType(JVMStringType.INSTANCE);
        this.getNodeContext().setGenericType(JVMStringType.INSTANCE);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
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
