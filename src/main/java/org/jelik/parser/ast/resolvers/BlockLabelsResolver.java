package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.blocks.BasicBlock;
import org.jelik.parser.ast.branching.ElifExpression;
import org.jelik.parser.ast.branching.ElseExpressionImpl;
import org.jelik.parser.ast.branching.IfExpressionImpl;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.functions.ConstructorDeclaration;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.locals.VariableDeclaration;
import org.jelik.parser.ast.loops.ForEachLoop;
import org.jelik.parser.ast.loops.WhileLoop;
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr;
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
    public void visitValueDeclaration(@NotNull ValueDeclaration valueDeclaration,
                                      @NotNull CompilationContext compilationContext) {
        LabelNode labelNode = compilationContext.labelStack.getLast();
        valueDeclaration.getLocalVariable().setStart(labelNode);
        valueDeclaration.getLocalVariable().setEnd(labelNode);
        valueDeclaration.getExpression().accept(this, compilationContext);
    }

    @Override
    public void visitVariableDeclaration(@NotNull VariableDeclaration variableDeclaration,
                                         @NotNull CompilationContext compilationContext) {
        LabelNode labelNode = compilationContext.labelStack.getLast();
        variableDeclaration.getLocalVariable().setStart(labelNode);
        variableDeclaration.getLocalVariable().setEnd(labelNode);
        variableDeclaration.getExpression().accept(this, compilationContext);
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration,
                                         @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionBody().accept(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitConstructorDeclaration(@NotNull ConstructorDeclaration constructorDeclaration,
                                            @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(constructorDeclaration);
        constructorDeclaration.getFunctionBody().accept(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(@NotNull FunctionBodyBlock fb,
                      @NotNull CompilationContext compilationContext) {

        final LabelNode label = compilationContext.createLabel("entry-block");
        fb.setStartLabel(label);
        compilationContext.currentFunction()
                .getFunctionParameterList()
                .getFunctionParameters()
                .forEach(p -> p.localVariableRef.setStart(label));
        compilationContext.labelStack.addLast(fb.getStartLabel());
        fb.getBasicBlock().accept(this, compilationContext);
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
    public void visitBasicBlock(@NotNull BasicBlock basicBlock, @NotNull CompilationContext compilationContext) {
        compilationContext.blockStack.addLast(basicBlock);
        for (var expression : basicBlock.getExpressions()) {
            expression.accept(this, compilationContext);
        }
        compilationContext.blockStack.removeLast();
    }

    @Override
    public void visit(@NotNull GetLocalNode getLocalNode,
                      @NotNull CompilationContext compilationContext) {

        LabelNode labelNode = compilationContext.labelStack.getLast();
        Objects.requireNonNull(labelNode);
        getLocalNode.getLocalVariable().setEnd(labelNode);
    }

    @Override
    public void visitNullSafeCall(@NotNull NullSafeCallExpr nullSafeCall,
                                  @NotNull CompilationContext compilationContext) {

        nullSafeCall.getReference().accept(this, compilationContext);
        nullSafeCall.getFurtherExpression().accept(this, compilationContext);
    }

    @Override
    public void visit(@NotNull IfExpressionImpl ifExpression,
                      @NotNull CompilationContext compilationContext) {

        ifExpression.getConditionExpression().accept(this, compilationContext);
        ifExpression.getBasicBlock().accept(this, compilationContext);
        ifExpression.getElseExpressionOpt().ifPresentOrElse(expr -> {
            expr.accept(this, compilationContext);
            ifExpression.getContext().setJumpOver(true);
        }, () -> ifExpression.getContext().setJumpOver(false));
    }

    @Override
    public void visitElifExpression(@NotNull ElifExpression elifExpression,
                                    @NotNull CompilationContext compilationContext) {
        elifExpression.getConditionExpression().accept(this, compilationContext);
        elifExpression.getBasicBlock().accept(this, compilationContext);
        elifExpression.getElseExpressionOpt().ifPresentOrElse(expr -> {
            expr.accept(this, compilationContext);
            elifExpression.getContext().setJumpOver(true);
        }, () -> elifExpression.getContext().setJumpOver(false));
    }

    @Override
    public void visit(@NotNull ElseExpressionImpl elseExpression,
                      @NotNull CompilationContext compilationContext) {
        elseExpression.getBasicBlock().accept(this, compilationContext);
    }

    @Override
    public void visitTryExpression(@NotNull TryExpression tryExpression,
                                   @NotNull CompilationContext compilationContext) {
        ((FunctionDeclaration) compilationContext.currentCompilationUnit())
                .getFunctionContext()
                .addTryCatchBlock(tryExpression);
        tryExpression.setStartLabel(compilationContext.createLabel("try-start"));
        tryExpression.getBlock().accept(this, compilationContext);
        tryExpression.setEndLabel(compilationContext.createLabel("try-end"));

        final CatchExpression catchExpression = tryExpression.getCatchExpression();
        final LabelNode startLabel = compilationContext.createLabel("catch-start");
        final LabelNode endLabel = compilationContext.createLabel("catch-end");
        catchExpression.setStartLabel(startLabel);
        catchExpression.setEndLabel(endLabel);
        if (!catchExpression.getBlock().getExpressions().isEmpty()) {
            final LabelNode inner = compilationContext.createLabel("catch-inner");
            catchExpression.setInnerLabel(compilationContext.createLabel("catch-inner"));
            for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
                functionParameter.localVariableRef.setStart(inner);
                functionParameter.localVariableRef.setEnd(endLabel);
            }
        }
        catchExpression.accept(this, compilationContext);
    }

    @Override
    public void visitCatchExpression(@NotNull CatchExpression catchExpression, @NotNull CompilationContext compilationContext) {
        catchExpression.getBlock().accept(this, compilationContext);
        for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            //functionParameter.localVariableRef.setStart(catchExpression.getBlock().startLabel);
        }
        for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            functionParameter.accept(this, compilationContext);
        }
        super.visitCatchExpression(catchExpression, compilationContext);
    }

    @Override
    public void visitForEachLoop(@NotNull ForEachLoop forEachloop, @NotNull CompilationContext compilationContext) {
        final LabelNode startLabel = compilationContext.createLabel("loop-start");
        final LabelNode startBodyLabel = compilationContext.createLabel("loop-body-start");
        final LabelNode endLabel = compilationContext.createLabel("loop-end");
        forEachloop.setLoopStart(startLabel);
        forEachloop.setLoopBodyStart(startBodyLabel);
        forEachloop.setLoopEnd(endLabel);
        forEachloop.getLocals().forEach(l -> {
            l.setStart(startLabel);
            l.setEnd(endLabel);
        });
        super.visitForEachLoop(forEachloop, compilationContext);
    }

    @Override
    public void visitWhileLoop(@NotNull WhileLoop whileLoop, @NotNull CompilationContext compilationContext) {
        final LabelNode startLabel = compilationContext.createLabel("loop-start");
        whileLoop.setLoopStart(startLabel);
        super.visitWhileLoop(whileLoop, compilationContext);
    }
}
