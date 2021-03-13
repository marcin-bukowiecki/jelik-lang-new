package org.jelik.parser.ast.functions;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.RightCurlToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionBodyBlock extends FunctionBody {

    public static final FunctionBody EMPTY = new FunctionBodyBlock(new LeftCurlToken(-1,-1),
            BasicBlock.EMPTY,
            new RightCurlToken(-1,-1));

    private final LeftCurlToken leftCurlToken;

    private final RightCurlToken rightCurlToken;

    private final BasicBlock bb;

    private LabelNode startLabel;

    public FunctionBodyBlock(LeftCurlToken leftCurlToken, BasicBlock bb, RightCurlToken rightCurlToken) {
        this.leftCurlToken = leftCurlToken;
        this.rightCurlToken = rightCurlToken;
        this.bb = bb;
        bb.parent = this;
    }

    public void setStartLabel(LabelNode startLabel) {
        this.startLabel = startLabel;
    }

    public LabelNode getStartLabel() {
        return startLabel;
    }

    public LeftCurlToken getLeftCurlToken() {
        return leftCurlToken;
    }

    public RightCurlToken getRightCurlToken() {
        return rightCurlToken;
    }

    public BasicBlock getBb() {
        return bb;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return leftCurlToken.toString() + "\n" +
               bb.toString() +
               rightCurlToken.toString() + "\n";
    }

    @Override
    public String getSymbol() {
        throw new UnsupportedOperationException();
    }
}
