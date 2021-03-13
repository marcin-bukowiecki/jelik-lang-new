package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.branching.IfConditionExpression;
import org.jelik.parser.ast.branching.IfExpression;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.numbers.TrueNode;
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr;
import org.jelik.parser.ast.operators.AndExpr;
import org.jelik.parser.ast.operators.BooleanExprWrapper;
import org.jelik.parser.ast.operators.EqualExpr;
import org.jelik.parser.ast.operators.GreaterExpr;
import org.jelik.parser.ast.operators.GreaterOrEqualExpr;
import org.jelik.parser.ast.operators.JumpInstruction;
import org.jelik.parser.ast.operators.LesserExpr;
import org.jelik.parser.ast.operators.LesserOrEqualExpr;
import org.jelik.parser.ast.operators.NotEqualExpr;
import org.jelik.parser.ast.operators.OrExpr;
import org.jelik.parser.ast.resolvers.decoders.AndOpLabelDecoder;
import org.jelik.parser.ast.resolvers.decoders.OpLabelDecoder;
import org.jelik.parser.ast.resolvers.decoders.OrOpLabelDecoder;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.TrueToken;
import org.jelik.parser.token.operators.EqualOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class JumpLabelsResolver extends AstVisitor {

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionBody().visit(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(@NotNull IfExpression ifExpression, @NotNull CompilationContext compilationContext) {
        checkForBooleanExpression(ifExpression, compilationContext);
        JumpLabelContext jumpLabelContext = new JumpLabelContext();
        compilationContext.jumpLabelContexts.addLast(jumpLabelContext);
        ifExpression.getConditionExpression().visit(this, compilationContext);
        compilationContext.jumpLabelContexts.removeLast();
    }

    @Override
    public void visitAbstractLogicalOpExpr(@NotNull AbstractLogicalOpExpr expr, @NotNull CompilationContext compilationContext) {
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
    public void visit(@NotNull GreaterOrEqualExpr greaterOrEqualOperator, @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(greaterOrEqualOperator, compilationContext);
        super.visit(greaterOrEqualOperator, compilationContext);
    }

    @Override
    public void visit(@NotNull LesserOrEqualExpr lesserOrEqualOperator, @NotNull CompilationContext compilationContext) {
        OpLabelDecoder.INSTANCE.decode(lesserOrEqualOperator, compilationContext);
        super.visit(lesserOrEqualOperator, compilationContext);
    }

    @Override
    public void visit(@NotNull OrExpr orExpr, @NotNull CompilationContext compilationContext) {
        OrOpLabelDecoder.INSTANCE.decode(orExpr, compilationContext);
        super.visit(orExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull AndExpr andExpr, @NotNull CompilationContext compilationContext) {
        AndOpLabelDecoder.INSTANCE.decode(andExpr, compilationContext);
        super.visit(andExpr, compilationContext);
    }

    private void checkForBooleanExpression(@NotNull IfExpression ifExpression, @NotNull CompilationContext compilationContext) {
        final IfConditionExpression conditionExpression = ifExpression.getConditionExpression();
        final Expression furtherExpression = conditionExpression.getFurtherExpression();
        if (!(furtherExpression instanceof AbstractLogicalOpExpr)) {
            final TrueNode trueNode = new TrueNode(new TrueToken(-1, -1));
            trueNode.setIgnore(true);
            var newExpr = new BooleanExprWrapper(furtherExpression, new EqualOperator(-1, -1), trueNode);
            conditionExpression.replaceWith(furtherExpression, newExpr);
            newExpr.instructionToCall = JumpInstruction.isTrue;
        }
    }
}
