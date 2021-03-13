package org.jelik.parser.ast.branching;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.parser.token.keyword.EndKeyword;
import org.jetbrains.annotations.NotNull;

public class ElseExpression extends ExpressionReferencingType {

    @Getter
    private final ElseKeyword elseKeyword;

    @Getter
    private final BasicBlock basicBlock;

    @Getter
    private final EndKeyword endKeyword;

    public ElseExpression(ElseKeyword elseKeyword, BasicBlock block, EndKeyword endKeyword) {
        super(new ElseNodeContext());
        this.elseKeyword = elseKeyword;
        this.basicBlock = block;
        this.endKeyword = endKeyword;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
