package org.jelik.parser.ast.functions;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.RightCurlToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionBodyBlock extends ASTNodeImpl implements FunctionBody {

    private final LeftCurlToken leftCurlToken;

    private final RightCurlToken rightCurlToken;

    private final BasicBlockImpl bb;

    private LabelNode startLabel;

    public FunctionBodyBlock(final @NotNull LeftCurlToken leftCurlToken,
                             final @NotNull BasicBlockImpl bb,
                             final @NotNull RightCurlToken rightCurlToken) {
        this.leftCurlToken = leftCurlToken;
        this.rightCurlToken = rightCurlToken;
        this.bb = bb;
        bb.setParent(this);
    }

    public static @NotNull FunctionBodyBlock createEmpty() {
        return new FunctionBodyBlock(LeftCurlToken.DUMMY, BasicBlockImpl.createEmpty(), RightCurlToken.DUMMY);
    }

    public void setStartLabel(@NotNull LabelNode startLabel) {
        this.startLabel = startLabel;
    }

    public @NotNull LabelNode getStartLabel() {
        return startLabel;
    }

    public LeftCurlToken getLeftCurlToken() {
        return leftCurlToken;
    }

    public RightCurlToken getRightCurlToken() {
        return rightCurlToken;
    }

    @Override
    public BasicBlockImpl getBasicBlock() {
        return bb;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        if (bb.getExpressions().isEmpty()) {
            return "{}";
        }
        return leftCurlToken.toString() + "\n" +
               bb.toString() +
               rightCurlToken.toString() + "\n";
    }

    @Override
    public int getEndRow() {
        return rightCurlToken.getEndRow();
    }
}
