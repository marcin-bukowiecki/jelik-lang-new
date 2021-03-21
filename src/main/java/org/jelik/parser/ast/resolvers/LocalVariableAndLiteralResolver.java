package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.GetFieldNode;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.MapCreateExpr;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapSetExpr;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.ParenthesisExpression;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.locals.VariableDeclaration;
import org.jelik.parser.ast.locals.WithLocalVariableDeclaration;
import org.jelik.parser.ast.operators.AssignExpr;
import org.jelik.parser.ast.types.InferredTypeRef;
import org.jelik.parser.ast.types.TypeAccessNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeNodeRef;
import org.jelik.parser.ast.types.AbstractTypeRef;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public class LocalVariableAndLiteralResolver extends AstVisitor {

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionBody().visit(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(ParenthesisExpression parenthesisExpression, CompilationContext compilationContext) {
        parenthesisExpression.parent.replaceWith(parenthesisExpression, parenthesisExpression.getExpression());
        parenthesisExpression.getExpression().visit(this, compilationContext);
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
    public void visitWithLocalVariable(@NotNull WithLocalVariableDeclaration withLocalVariable,
                                       @NotNull CompilationContext compilationContext) {
        Objects.requireNonNull(withLocalVariable.getFurtherExpression());
        withLocalVariable.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
        TypeNode typeNode = withLocalVariable.getTypeNode();
        AbstractTypeRef typeRef;
        if (typeNode == UndefinedTypeNode.UNDEFINED_TYPE_NODE) {
            typeRef = new TypeNodeRef(withLocalVariable.getTypeNode());
        } else {
            typeRef = new InferredTypeRef(withLocalVariable.getFurtherExpressionOpt().get());
        }
        LocalVariable localVariable = new LocalVariable(withLocalVariable.getLiteralToken().getText(), typeRef, false);
        compilationContext.blockStack.getLast().getBlockContext().addLocal(localVariable.getName(), localVariable);
        FunctionDeclaration functionDeclaration = (FunctionDeclaration) compilationContext.currentCompilationUnit();
        functionDeclaration.getFunctionContext().addLocalVariable(localVariable);
        withLocalVariable.setLocalVariable(localVariable);
    }

    @Override
    public void visit(@NotNull BasicBlock basicBlock,
                      @NotNull CompilationContext compilationContext) {
        if (compilationContext.blockStack.isEmpty()) {
            for (LocalVariable localsAsParameter : ((FunctionDeclaration) compilationContext.currentCompilationUnit())
                    .getFunctionContext()
                    .getLocalsAsParameters()) {
                basicBlock.getBlockContext().addLocal(localsAsParameter.getName(), localsAsParameter);
            }
        }
        super.visit(basicBlock, compilationContext);
    }

    @Override
    public void visit(@NotNull FunctionBodyBlock fb, @NotNull CompilationContext compilationContext) {
        fb.getBb().visit(this, compilationContext);
    }

    @Override
    public void visit(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
            argument.visit(this, compilationContext);
        }
        functionCallExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull Argument argument, @NotNull CompilationContext compilationContext) {
        argument.getExpression().visit(this, compilationContext);
    }

    @Override
    public void visit(@NotNull LiteralExpr literalExpr, @NotNull CompilationContext compilationContext) {
        if (literalExpr.getParent() instanceof DotCallExpr && ((DotCallExpr) literalExpr.getParent()).getSubject() instanceof TypeAccessNode) {
            final GetFieldNode getFieldNode = new GetFieldNode(literalExpr.getLiteralToken());
            literalExpr.getParent().replaceWith(literalExpr, getFieldNode);
            literalExpr.getFurtherExpressionOpt().ifPresent(expr -> {
                getFieldNode.setFurtherExpression(expr);
                expr.visit(this, compilationContext);
            });
        } else {
            Expression newExpr = new NumberResolver(literalExpr).resolve(compilationContext).getNewNode();
            if (newExpr == null) {
                newExpr = compilationContext
                        .currentCompilationUnit()
                        .findSymbol(literalExpr.getLiteralToken().getText(), compilationContext)
                        .map(s -> s.replaceNode(literalExpr))
                        .orElseThrow(() -> new SyntaxException("Could not resolve symbol", literalExpr, compilationContext.getCurrentModule()));
            }
            newExpr.visit(this, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr, @NotNull CompilationContext compilationContext) {
        if (arrayOrMapGetExpr.parent instanceof AssignExpr) {
            var newExpr = new ArrayOrMapSetExpr(arrayOrMapGetExpr.getLeftExpr(),
                    arrayOrMapGetExpr.getLeftBracketToken(),
                    arrayOrMapGetExpr.getExpression(),
                    arrayOrMapGetExpr.getRightBracketToken(),
                    ((AssignOperator) ((AssignExpr) arrayOrMapGetExpr.parent).getOp()),
                    ((AssignExpr) arrayOrMapGetExpr.parent).getRight());
            arrayOrMapGetExpr.parent.parent.replaceWith(((AssignExpr) arrayOrMapGetExpr.parent), newExpr);
            newExpr.visit(this, compilationContext);
            return;
        }
        if (arrayOrMapGetExpr.getLeftExpr() instanceof ArrayCreateExpr) {
            arrayOrMapGetExpr.arrayGet = true;
        }
        if (arrayOrMapGetExpr.getLeftExpr() instanceof MapCreateExpr) {
            arrayOrMapGetExpr.arrayGet = false;
        }
        arrayOrMapGetExpr.getLeftExpr().visit(this, compilationContext);
        arrayOrMapGetExpr.getExpression().visit(this, compilationContext);
        arrayOrMapGetExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull TryExpression tryExpression, @NotNull CompilationContext compilationContext) {
        super.visit(tryExpression, compilationContext);
    }

    @Override
    public void visit(@NotNull CatchExpression catchExpression, @NotNull CompilationContext compilationContext) {
        FunctionDeclaration functionDeclaration = (FunctionDeclaration) compilationContext.currentCompilationUnit();
        for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            LocalVariable localVariable = new LocalVariable(
                    functionParameter.getName().getText(),
                    new TypeNodeRef(functionParameter.getTypeNode()),
                    false);
            functionParameter.localVariableRef = localVariable;
            compilationContext.blockStack.getLast().getBlockContext().addLocal(functionParameter.getName().getText(), localVariable);
            functionDeclaration.getFunctionContext().addLocalVariable(localVariable);
        }
        super.visit(catchExpression, compilationContext);
    }

    @Override
    public void visit(@NotNull AssignExpr assignExpr, @NotNull CompilationContext compilationContext) {
        super.visit(assignExpr, compilationContext);
    }
}
