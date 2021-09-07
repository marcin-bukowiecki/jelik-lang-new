package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.visitor.FalseLabelExtractor;
import org.jelik.parser.ast.branching.BreakExpr;
import org.jelik.parser.ast.branching.ContinueExpr;
import org.jelik.parser.ast.branching.ElifExpression;
import org.jelik.parser.ast.branching.ElseExpressionImpl;
import org.jelik.parser.ast.branching.IfExpressionImpl;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.loops.ForEachLoop;
import org.jelik.parser.ast.loops.WhileLoop;
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr;
import org.jelik.parser.ast.operators.*;
import org.jelik.parser.ast.resolvers.decoders.AndOpLabelDecoder;
import org.jelik.parser.ast.resolvers.decoders.BooleanExprWrapperChecker;
import org.jelik.parser.ast.resolvers.decoders.OpLabelDecoder;
import org.jelik.parser.ast.resolvers.decoders.OrOpLabelDecoder;
import org.jelik.parser.ast.utils.ASTUtils;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class JumpLabelsResolver extends AstVisitor {

    @Override
    public void visitNotExpr(@NotNull NotExpr notExpr, @NotNull CompilationContext compilationContext) {
        notExpr.getExpression().accept(this, compilationContext);
        var created = BooleanExprWrapperChecker.INSTANCE.checkForWrapping(notExpr);
        OpLabelDecoder.INSTANCE.decode(created, compilationContext);
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration,
                                         @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionBody().accept(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitNullSafeCall(@NotNull NullSafeCallExpr nullSafeCall,
                                  @NotNull CompilationContext compilationContext) {
        LabelNode endLabelNode;
        if (nullSafeCall.getParent() instanceof NullSafeCallExpr) {
            endLabelNode = ((NullSafeCallExpr) nullSafeCall.getParent()).getEndLabel();
        } else {
            endLabelNode = compilationContext.createLabel("null-safe-end-label");
            nullSafeCall.setEndLabel(endLabelNode);
            nullSafeCall.setFinishLabel(compilationContext.createLabel("null-safe-call-finish"));
        }
        assert endLabelNode != null;
        BooleanExprWrapperChecker.INSTANCE.wrapWithIsNullCheck(nullSafeCall, nullSafeCall.getReference(), endLabelNode);
        nullSafeCall.getReference().accept(this, compilationContext);
        nullSafeCall.getFurtherExpression().accept(this, compilationContext);
    }

    @Override
    public void visit(@NotNull IfExpressionImpl ifExpression, @NotNull CompilationContext compilationContext) {
        BooleanExprWrapperChecker.INSTANCE.checkConditionForBooleanExpression(ifExpression.getConditionExpression());
        ifExpression.getConditionExpression().accept(this, compilationContext);
        ifExpression.getBasicBlock().accept(this, compilationContext);
        if (ifExpression.getElseExpression() != null) {
            ifExpression.getElseExpression().accept(this, compilationContext);
        }
    }

    @Override
    public void visitWhileLoop(@NotNull WhileLoop whileLoop, @NotNull CompilationContext compilationContext) {
        BooleanExprWrapperChecker.INSTANCE.checkConditionForBooleanExpression(whileLoop.getCondition());
        whileLoop.getCondition().accept(this, compilationContext);
        whileLoop.getBlock().accept(this, compilationContext);
        final FalseLabelExtractor falseLabelExtractor = new FalseLabelExtractor();
        whileLoop.getCondition().accept(falseLabelExtractor, compilationContext);
        whileLoop.setLoopEnd(falseLabelExtractor.getResult());
    }

    @Override
    public void visitBreakExpression(@NotNull BreakExpr breakExpr, @NotNull CompilationContext compilationContext) {
        final WhileLoop whileLoop = ASTUtils.findParent(breakExpr, WhileLoop.class);

        if (whileLoop != null) {
            final FalseLabelExtractor falseLabelExtractor = new FalseLabelExtractor();
            whileLoop.getCondition().accept(falseLabelExtractor, compilationContext);
            breakExpr.setJumpTo(falseLabelExtractor.getResult());
            return;
        }

        final ForEachLoop forEachLoop = ASTUtils.findParent(breakExpr, ForEachLoop.class);
        if (forEachLoop != null) {
            breakExpr.setJumpTo(forEachLoop.getLoopEnd());
        }
    }

    @Override
    public void visitContinueExpression(@NotNull ContinueExpr continueExpr,
                                        @NotNull CompilationContext compilationContext) {
        final WhileLoop whileLoop = ASTUtils.findParent(continueExpr, WhileLoop.class);

        if (whileLoop != null) {
            continueExpr.setJumpTo(whileLoop.getLoopStart());
            return;
        }

        final ForEachLoop forEachLoop = ASTUtils.findParent(continueExpr, ForEachLoop.class);
        if (forEachLoop != null) {
            continueExpr.setJumpTo(forEachLoop.getLoopStart());
        }
    }

    @Override
    public void visit(@NotNull ElseExpressionImpl elseExpression,
                      @NotNull CompilationContext compilationContext) {
        elseExpression.getBasicBlock().accept(this, compilationContext);
        elseExpression.getContext().setFinishLabel(compilationContext.createLabel("end-else"));
    }

    @Override
    public void visitElifExpression(@NotNull ElifExpression elifExpression, 
                                    @NotNull CompilationContext compilationContext) {
        BooleanExprWrapperChecker.INSTANCE.checkConditionForBooleanExpression(elifExpression.getConditionExpression());
        elifExpression.getConditionExpression().accept(this, compilationContext);
        elifExpression.getBasicBlock().accept(this, compilationContext);
        elifExpression.getElseExpressionOpt()
                .ifPresentOrElse(expr -> expr.accept(this, compilationContext), () -> {
                    final FalseLabelExtractor falseLabelExtractor = new FalseLabelExtractor();
                    elifExpression.getConditionExpression().accept(falseLabelExtractor, compilationContext);
                    elifExpression.getContext().setFinishLabel(falseLabelExtractor.getResult());
                });
    }

    @Override
    public void visitAbstractLogicalOpExpr(@NotNull AbstractLogicalOpExpr expr,
                                           @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(expr, compilationContext);
        super.visitAbstractLogicalOpExpr(expr, compilationContext);
    }

    @Override
    public void visit(@NotNull EqualExpr equalExpr, @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(equalExpr, compilationContext);
        super.visit(equalExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull NotEqualExpr equalExpr, @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(equalExpr, compilationContext);
        super.visit(equalExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull GreaterExpr greaterExpr, @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(greaterExpr, compilationContext);
        super.visit(greaterExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull LesserExpr lesserExpr, @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(lesserExpr, compilationContext);
        super.visit(lesserExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull GreaterOrEqualExpr greaterOrEqualOperator,
                      @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(greaterOrEqualOperator, compilationContext);
        super.visit(greaterOrEqualOperator, compilationContext);
    }

    @Override
    public void visit(@NotNull LesserOrEqualExpr lesserOrEqualOperator,
                      @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(lesserOrEqualOperator, compilationContext);
        super.visit(lesserOrEqualOperator, compilationContext);
    }

    @Override
    public void visitOrExpr(@NotNull OrExpr orExpr, @NotNull CompilationContext compilationContext) {
        BooleanExprWrapperChecker.INSTANCE.checkForWrapping(orExpr);
        OrOpLabelDecoder.INSTANCE.decode(orExpr, compilationContext);
        super.visitOrExpr(orExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull AndExpr andExpr, @NotNull CompilationContext compilationContext) {
        BooleanExprWrapperChecker.INSTANCE.checkForWrapping(andExpr);
        AndOpLabelDecoder.INSTANCE.decode(andExpr, compilationContext);
        super.visit(andExpr, compilationContext);
    }

    @Override
    public void visitDefaultValueExpr(@NotNull DefaultValueExpr defaultValueExpr,
                                      @NotNull CompilationContext compilationContext) {
        super.visitDefaultValueExpr(defaultValueExpr, compilationContext);
        final NullSafeCallExpr left = (NullSafeCallExpr) defaultValueExpr.getLeft();
        defaultValueExpr.setNullLabel(left.getEndLabel());
        defaultValueExpr.setNotNullLabel(left.getFinishLabel());
    }
}
