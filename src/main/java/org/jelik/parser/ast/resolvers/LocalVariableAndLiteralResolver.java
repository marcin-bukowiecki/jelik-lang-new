package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.GetFieldNode;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.MapCreateExpr;
import org.jelik.parser.ast.ReferenceExpressionImpl;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapSetExpr;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ParenthesisExpressionWrapper;
import org.jelik.parser.ast.functions.ConstructorDeclaration;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.locals.ValueOrVariableDeclaration;
import org.jelik.parser.ast.locals.VariableDeclaration;
import org.jelik.parser.ast.loops.ForEachLoop;
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr;
import org.jelik.parser.ast.operators.AssignExpr;
import org.jelik.parser.ast.resolvers.constructors.ParentConstructorCallResolver;
import org.jelik.parser.ast.types.*;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public class LocalVariableAndLiteralResolver extends AstVisitor {

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
        ParentConstructorCallResolver.INSTANCE.resolveCall(constructorDeclaration, compilationContext);
        constructorDeclaration.getFunctionBody().accept(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(@NotNull ParenthesisExpressionWrapper parenthesisExpression,
                      @NotNull CompilationContext compilationContext) {

        parenthesisExpression.getParent().replaceWith(parenthesisExpression, parenthesisExpression.getExpression());
        parenthesisExpression.getExpression().accept(this, compilationContext);
    }

    @Override
    public void visitValueDeclaration(@NotNull ValueDeclaration valueDeclaration,
                                      @NotNull CompilationContext compilationContext) {

        visitWithLocalVariable(valueDeclaration, compilationContext);
    }

    @Override
    public void visitVariableDeclaration(@NotNull VariableDeclaration variableDeclaration,
                                         @NotNull CompilationContext compilationContext) {

        visitWithLocalVariable(variableDeclaration, compilationContext);
    }

    @Override
    public void visitWithLocalVariable(@NotNull ValueOrVariableDeclaration valueOrVariableDeclaration,
                                       @NotNull CompilationContext compilationContext) {

        Objects.requireNonNull(valueOrVariableDeclaration.getExpression());
        valueOrVariableDeclaration.getExpression().accept(this, compilationContext);
        var typeNode = valueOrVariableDeclaration.getTypeNode();
        final AbstractTypeRef typeRef;
        if (typeNode == UndefinedTypeNode.UNDEFINED_TYPE_NODE) {
            typeRef = new TypeNodeRef(valueOrVariableDeclaration.getTypeNode());
        } else {
            typeRef = new InferredTypeRef(valueOrVariableDeclaration.getExpression());
        }
        LocalVariable localVariable = new LocalVariable(valueOrVariableDeclaration.getLiteralToken().getText(),
                typeRef,
                false);
        compilationContext.blockStack.getLast().getBlockContext().addLocal(localVariable.getName(), localVariable);
        var functionDeclaration = (FunctionDeclaration) compilationContext.currentCompilationUnit();
        functionDeclaration.getFunctionContext().addLocalVariable(localVariable);
        valueOrVariableDeclaration.setLocalVariable(localVariable);
    }

    @Override
    public void visitNullSafeCall(@NotNull NullSafeCallExpr nullSafeCall, @NotNull CompilationContext compilationContext) {
        nullSafeCall.getReference().accept(this, compilationContext);
        nullSafeCall.getFurtherExpression().accept(this, compilationContext);
    }

    @Override
    public void visit(@NotNull FunctionBodyBlock fb, @NotNull CompilationContext compilationContext) {
        for (var localsAsParameter : ((FunctionDeclaration) compilationContext.currentCompilationUnit())
                .getFunctionContext()
                .getLocalsAsParameters()) {
            fb.getBasicBlock().getBlockContext().addLocal(localsAsParameter.getName(), localsAsParameter);
        }
        fb.getBasicBlock().accept(this, compilationContext);
    }

    @Override
    public void visitFunctionCall(@NotNull FunctionCallExpr functionCallExpr,
                                  @NotNull CompilationContext compilationContext) {
        for (var argument : functionCallExpr.getArgumentList().getArguments()) {
            argument.accept(this, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull Argument argument, @NotNull CompilationContext compilationContext) {
        argument.getExpression().accept(this, compilationContext);
    }

    @Override
    public void visit(@NotNull LiteralExpr literalExpr, @NotNull CompilationContext compilationContext) {
        if (literalExpr.getParent() instanceof ReferenceExpressionImpl &&
                ((ReferenceExpressionImpl) literalExpr.getParent()).getReference() instanceof TypeAccessNodeTyped) {

            literalExpr
                    .getParent()
                    .replaceWith(literalExpr, new GetFieldNode(literalExpr.getLiteralToken()));
        }
        else {
            Expression newExpr = new NumberResolver(literalExpr).resolve(compilationContext).getNewNode();
            if (newExpr == null) {
                newExpr = compilationContext
                        .currentCompilationUnit()
                        .findSymbol(literalExpr.getLiteralToken().getText(), compilationContext)
                        .map(s -> s.replaceNode(literalExpr))
                        .orElseThrow(() -> new SyntaxException(
                                "Could not resolve symbol: " + literalExpr.getLiteralToken().getText(),
                                literalExpr,
                                compilationContext.getCurrentModule()));
            }
            newExpr.accept(this, compilationContext);
        }
    }

    @Override
    public void visitArrayOrMapGetExpr(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr,
                                       @NotNull CompilationContext compilationContext) {

        if (arrayOrMapGetExpr.getParent() instanceof AssignExpr) {
            var newExpr = new ArrayOrMapSetExpr(arrayOrMapGetExpr.getLeftExpr(),
                    arrayOrMapGetExpr.getLeftBracketToken(),
                    arrayOrMapGetExpr.getExpression(),
                    arrayOrMapGetExpr.getRightBracketToken(),
                    ((AssignOperator) ((AssignExpr) arrayOrMapGetExpr.getParent()).getOp()),
                    ((AssignExpr) arrayOrMapGetExpr.getParent()).getRight());
            arrayOrMapGetExpr.getParent().getParent().replaceWith(((AssignExpr) arrayOrMapGetExpr.getParent()), newExpr);
            newExpr.accept(this, compilationContext);
            return;
        }
        if (arrayOrMapGetExpr.getLeftExpr() instanceof ArrayCreateExpr) {
            arrayOrMapGetExpr.arrayGet = true;
        }
        if (arrayOrMapGetExpr.getLeftExpr() instanceof MapCreateExpr) {
            arrayOrMapGetExpr.arrayGet = false;
        }
        arrayOrMapGetExpr.getLeftExpr().accept(this, compilationContext);
        arrayOrMapGetExpr.getExpression().accept(this, compilationContext);
    }

    @Override
    public void visitCatchExpression(@NotNull CatchExpression catchExpression, @NotNull CompilationContext compilationContext) {
        var functionDeclaration = (FunctionDeclaration) compilationContext.currentCompilationUnit();
        for (var functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            var localVariable = new LocalVariable(
                    functionParameter.getName().getText(),
                    new TypeNodeRef(functionParameter.getTypeNode()),
                    false);
            functionParameter.localVariableRef = localVariable;
            compilationContext.blockStack
                    .getLast()
                    .getBlockContext()
                    .addLocal(functionParameter.getName().getText(), localVariable);
            functionDeclaration.getFunctionContext().addLocalVariable(localVariable);
        }
        super.visitCatchExpression(catchExpression, compilationContext);
    }

    @Override
    public void visit(@NotNull AssignExpr assignExpr, @NotNull CompilationContext compilationContext) {
        super.visit(assignExpr, compilationContext);
    }

    @Override
    public void visitForEachLoop(@NotNull ForEachLoop forEachloop, @NotNull CompilationContext compilationContext) {
        forEachloop.getIterExpression().accept(this, compilationContext);
        var functionDeclaration = (FunctionDeclaration) compilationContext.currentCompilationUnit();
        var localVariables = forEachloop.createLocalVariables();
        localVariables.forEach(l -> {
            compilationContext.blockStack
                    .getLast()
                    .getBlockContext()
                    .addLocal(l.getName(), l);
            functionDeclaration.getFunctionContext().addLocalVariable(l);
        });
        forEachloop.setLocals(localVariables);
        forEachloop.getBlock().accept(this, compilationContext);
    }
}
