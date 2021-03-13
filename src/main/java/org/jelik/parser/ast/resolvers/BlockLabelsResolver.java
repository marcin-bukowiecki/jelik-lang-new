package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.branching.ElseExpression;
import org.jelik.parser.ast.branching.IfExpression;
import org.jelik.parser.ast.branching.IfNodeContext;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Resolves labels {@link org.objectweb.asm.Label} and {@link LabelNode}
 *
 * @author Marcin Bukowiecki
 */
public class BlockLabelsResolver extends AstVisitor {

    @Override
    public void visitValueDeclaration(@NotNull ValueDeclaration valueDeclaration, @NotNull CompilationContext compilationContext) {
        LabelNode labelNode = compilationContext.labelStack.getLast();
        valueDeclaration.getLocalVariable().setStart(labelNode);
        valueDeclaration.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionBody().visit(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(@NotNull FunctionBodyBlock fb, @NotNull CompilationContext compilationContext) {
        fb.setStartLabel(compilationContext.createLabel("entry-block"));
        compilationContext.labelStack.addLast(fb.getStartLabel());
        fb.getBb().visit(this, compilationContext);
        CompilationUnit compilationUnit = compilationContext.currentCompilationUnit();
        FunctionDeclaration functionDeclaration = (FunctionDeclaration) compilationUnit;
        functionDeclaration.getFunctionContext().getLocalVariableList().stream()
                .filter(LocalVariable::isParameter)
                .forEach(p -> {
                    p.setStart(fb.getStartLabel());
                    p.setEnd(fb.getStartLabel());
                });

    }

    @Override
    public void visit(@NotNull BasicBlock basicBlock, @NotNull CompilationContext compilationContext) {
        compilationContext.blockStack.addLast(basicBlock);
        for (Expression expression : basicBlock.getExpressions()) {
            expression.visit(this, compilationContext);
        }
        compilationContext.blockStack.removeLast();
    }

    @Override
    public void visit(@NotNull GetLocalNode getLocalNode, @NotNull CompilationContext compilationContext) {
        LabelNode labelNode = compilationContext.labelStack.getLast();
        Objects.requireNonNull(labelNode);
        getLocalNode.getLocalVariable().setEnd(labelNode);
    }

    @Override
    public void visit(IfExpression ifExpression, CompilationContext compilationContext) {
        ifExpression.getConditionExpression().visit(this, compilationContext);
        ifExpression.getBasicBlock().visit(this, compilationContext);
        IfNodeContext ifNodeContext = ((IfNodeContext) ifExpression.getNodeContext());
        ifExpression.getFurtherExpressionOpt().ifPresentOrElse(expr -> {
            expr.visit(this, compilationContext);
            ifNodeContext.setJumpOver(true);
        }, () -> ifNodeContext.setJumpOver(false));
    }

    @Override
    public void visit(ElseExpression elseExpression, CompilationContext compilationContext) {
        elseExpression.getBasicBlock().visit(this, compilationContext);
    }

    @Override
    public void visit(@NotNull TryExpression tryExpression, @NotNull CompilationContext compilationContext) {
        ((FunctionDeclaration) compilationContext.currentCompilationUnit()).getFunctionContext().addTryCatchBlock(tryExpression);
        tryExpression.getBlock().visit(this, compilationContext);
        tryExpression.getFurtherExpression().visit(this, compilationContext);
    }

    @Override
    public void visit(@NotNull CatchExpression catchExpression, @NotNull CompilationContext compilationContext) {
        catchExpression.getBlock().visit(this, compilationContext);
        for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            //functionParameter.localVariableRef.setStart(catchExpression.getBlock().startLabel);
        }
        for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            functionParameter.visit(this, compilationContext);
        }
        super.visit(catchExpression, compilationContext);
    }
}
