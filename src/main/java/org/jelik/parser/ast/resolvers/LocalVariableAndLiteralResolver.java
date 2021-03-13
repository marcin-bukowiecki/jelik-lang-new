package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.GetFieldNode;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.ParenthesisExpression;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.operators.AssignExpr;
import org.jelik.parser.ast.types.InferredTypeRef;
import org.jelik.parser.ast.types.TypeAccessNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeNodeRef;
import org.jelik.parser.ast.types.TypeRef;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.exceptions.SyntaxException;
import org.jetbrains.annotations.NotNull;

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
    public void visitValueDeclaration(@NotNull ValueDeclaration valueDeclaration, @NotNull CompilationContext compilationContext) {
        assert valueDeclaration.getFurtherExpressionOpt().isPresent();

        valueDeclaration.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
        TypeNode typeNode = valueDeclaration.getTypeNode();
        TypeRef typeRef;
        if (typeNode == UndefinedTypeNode.UNDEFINED_TYPE_NODE) {
            typeRef = new TypeNodeRef(valueDeclaration.getTypeNode());
        } else {
            typeRef = new InferredTypeRef(valueDeclaration.getFurtherExpressionOpt().get());
        }
        LocalVariable localVariable = new LocalVariable(valueDeclaration.getLiteralToken().getText(), typeRef, false);
        compilationContext.blockStack.getLast().getBlockContext().addLocal(localVariable.getName(), localVariable);
        FunctionDeclaration functionDeclaration = (FunctionDeclaration) compilationContext.currentCompilationUnit();
        functionDeclaration.getFunctionContext().addLocalVariable(localVariable);
        valueDeclaration.setLocalVariable(localVariable);
    }

    @Override
    public void visit(@NotNull BasicBlock basicBlock, @NotNull CompilationContext compilationContext) {
        if (compilationContext.blockStack.isEmpty()) {
            for (LocalVariable localsAsParameter : ((FunctionDeclaration) compilationContext.currentCompilationUnit()).getFunctionContext().getLocalsAsParameters()) {
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
        if (literalExpr.getParent() instanceof DotCallExpr && literalExpr.getParent().getParent() instanceof TypeAccessNode) {
            final GetFieldNode getFieldNode = new GetFieldNode(literalExpr.getLiteralToken());
            literalExpr.getParent().replaceWith(literalExpr, getFieldNode);
            literalExpr.getFurtherExpressionOpt().ifPresent(expr -> {
                getFieldNode.setFurtherExpression(expr);
                expr.visit(this, compilationContext);
            });
        } else {
            Expression newExpr = new NumberResolver(literalExpr).resolve(compilationContext).getNewNode();
            if (newExpr == null) {
                FindSymbolResult findResult = compilationContext
                        .currentCompilationUnit()
                        .findSymbol(literalExpr.getLiteralToken().getText(), compilationContext);

                if (findResult == null) {
                    throw new SyntaxException("Could not resolve symbol", literalExpr, compilationContext.getCurrentModule());
                }

                newExpr = findResult.replaceNode(literalExpr);
            }
            newExpr.visit(this, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr, @NotNull CompilationContext compilationContext) {
        if (arrayOrMapGetExpr.parent instanceof ArrayCreateExpr) {
            arrayOrMapGetExpr.arrayGet = true;
        }
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
    public void visit(AssignExpr assignExpr, CompilationContext compilationContext) {
        super.visit(assignExpr, compilationContext);
    }
}
