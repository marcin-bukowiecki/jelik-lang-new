package org.jelik.parser.ast.resolvers.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.KeyValueExpr;
import org.jelik.parser.ast.MapCreateExpr;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapSetExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateExpr;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.loops.ForEachLoop;
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr;
import org.jelik.parser.ast.operators.EqualExpr;
import org.jelik.parser.ast.operators.GreaterExpr;
import org.jelik.parser.ast.operators.GreaterOrEqualExpr;
import org.jelik.parser.ast.operators.LesserExpr;
import org.jelik.parser.ast.operators.LesserOrEqualExpr;
import org.jelik.parser.ast.operators.NotEqualExpr;
import org.jelik.parser.ast.resolvers.CastToVisitor;
import org.jelik.parser.ast.resolvers.decoders.EqualOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.GreaterOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.GreaterOrEqualOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.LesserOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.LesserOrEqualOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.NotEqualOpTypeDecoder;
import org.jelik.parser.ast.strings.StringBuilderAppend;
import org.jelik.parser.ast.strings.StringBuilderInit;
import org.jelik.parser.ast.strings.StringBuilderToStringNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMObjectType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Adds proper casting AST nodes i.e. cast to Integer from int, int to Integer, element in {@link java.util.Collection}
 * from Object to proper element type etc.
 *
 * @author Marcin Bukowiecki
 */
public class TypeCastResolver extends AstVisitor {

    @Override
    public void visitReturnExpr(@NotNull ReturnExpr returnExpr, @NotNull CompilationContext compilationContext) {
        if (returnExpr.isEmpty()) {
            return;
        }
        returnExpr.getExpression().accept(this ,compilationContext);
        var functionDeclaration = (FunctionDeclaration) compilationContext.currentCompilationUnit();
        var targetType = functionDeclaration.getReturnType();
        returnExpr.getExpression().getGenericReturnType().accept(new CastToVisitor(
                returnExpr.getExpression(),
                targetType), compilationContext);
    }

    @Override
    public void visit(@NotNull StringBuilderInit stringBuilderInit, @NotNull CompilationContext compilationContext) {
        stringBuilderInit.getParent().replaceWith(stringBuilderInit, new StringBuilderToStringNode(stringBuilderInit));
    }

    @Override
    public void visit(@NotNull StringBuilderAppend stringBuilderAppend,
                      @NotNull CompilationContext compilationContext) {
        var subject = stringBuilderAppend.getSubject();
        var targetFunctionCall = stringBuilderAppend.getTargetFunctionCall();
        var target = targetFunctionCall.getMethodData().getParameterTypes().get(0);
        subject.getReturnType().accept(new CastToVisitor(subject, target), compilationContext);
    }

    @Override
    public void visitFunctionCall(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        int index = 0;
        for (var argument : functionCallExpr.getArgumentList().getArguments()) {
            argument.accept(this, compilationContext);
            if (argument.getGenericReturnType().isPrimitive()) {
                final Type target = functionCallExpr
                        .getTargetFunctionCallProvider()
                        .getMethodData()
                        .getParameterTypes()
                        .get(index);

                argument
                        .getGenericReturnType()
                        .accept(new CastToVisitor(argument.getExpression(), target), compilationContext);
            }
            index++;
        }

        var genericReturnType = functionCallExpr.getGenericType();
        var returnType = functionCallExpr.getType();
        returnType.accept(new CastToVisitor(functionCallExpr, genericReturnType), compilationContext);
    }

    @Override
    public void visit(@NotNull EqualExpr equalExpr, @NotNull CompilationContext compilationContext) {
        super.visit(equalExpr, compilationContext);
        EqualOpTypeDecoder.INSTANCE.decode(equalExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull GreaterExpr greaterExpr, @NotNull CompilationContext compilationContext) {
        super.visit(greaterExpr, compilationContext);
        GreaterOpTypeDecoder.INSTANCE.decode(greaterExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull GreaterOrEqualExpr greaterOrEqualExpr, @NotNull CompilationContext compilationContext) {
        super.visit(greaterOrEqualExpr, compilationContext);
        GreaterOrEqualOpTypeDecoder.INSTANCE.decode(greaterOrEqualExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull LesserOrEqualExpr lesserOrEqualExpr, @NotNull CompilationContext compilationContext) {
        super.visit(lesserOrEqualExpr, compilationContext);
        LesserOrEqualOpTypeDecoder.INSTANCE.decode(lesserOrEqualExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull LesserExpr lesserExpr, @NotNull CompilationContext compilationContext) {
        super.visit(lesserExpr, compilationContext);
        LesserOpTypeDecoder.INSTANCE.decode(lesserExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull NotEqualExpr notEqualExpr, @NotNull CompilationContext compilationContext) {
        super.visit(notEqualExpr, compilationContext);
        NotEqualOpTypeDecoder.INSTANCE.decode(notEqualExpr, compilationContext);
    }

    @Override
    public void visitMapCreateExpr(@NotNull MapCreateExpr mapCreateExpr,
                                   @NotNull CompilationContext compilationContext) {
        Type target = JVMObjectType.INSTANCE;

        for (KeyValueExpr entry : mapCreateExpr.getEntries()) {
            Expression subject = entry.getKey();
            subject.getReturnType().accept(new CastToVisitor(subject, target), compilationContext);

            subject = entry.getValue();
            subject.getReturnType().accept(new CastToVisitor(subject, target), compilationContext);
        }
    }

    @Override
    public void visitArrayOrMapGetExpr(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr,
                                       @NotNull CompilationContext compilationContext) {
        arrayOrMapGetExpr.getLeftExpr().accept(this, compilationContext);
        arrayOrMapGetExpr.getExpression().accept(this, compilationContext);
        final Type returnType = arrayOrMapGetExpr.getReturnType();
        final Type genericReturnType = arrayOrMapGetExpr.getGenericReturnType();
        returnType.accept(new CastToVisitor(arrayOrMapGetExpr, genericReturnType), compilationContext);
    }

    @Override
    public void visitArrayOrMapSetExpr(@NotNull ArrayOrMapSetExpr arrayOrMapSetExpr,
                                       @NotNull CompilationContext compilationContext) {
        arrayOrMapSetExpr.getIndex().accept(this, compilationContext);
        arrayOrMapSetExpr.getRef().accept(this, compilationContext);
        arrayOrMapSetExpr.getRightExpression().accept(this, compilationContext);
        if (!arrayOrMapSetExpr.isArraySet()) {
            var expectedType = arrayOrMapSetExpr.getKeyType().getWrapperType();
            arrayOrMapSetExpr
                    .getIndex()
                    .getGenericReturnType()
                    .accept(new CastToVisitor(arrayOrMapSetExpr.getIndex(), expectedType), compilationContext);
        }
        var expectedType = arrayOrMapSetExpr.getValueType();
        if (!arrayOrMapSetExpr.isArraySet()) {
            expectedType = expectedType.getWrapperType();
        }
        arrayOrMapSetExpr
                .getRightExpression()
                .getGenericReturnType()
                .accept(new CastToVisitor(arrayOrMapSetExpr.getRightExpression(), expectedType), compilationContext);
    }

    @Override
    public void visitTypedArrayCreateExpr(@NotNull TypedArrayCreateExpr typedArrayCreateExpr,
                                          @NotNull CompilationContext compilationContext) {
        super.visitTypedArrayCreateExpr(typedArrayCreateExpr, compilationContext);
        final TypeNode target = typedArrayCreateExpr.getTypeNode();
        for (Expression expression : typedArrayCreateExpr.getExpressions()) {
            final Type given = expression.getGenericReturnType();
            given.accept(new CastToVisitor(expression, target.getGenericType()), compilationContext);
        }
    }

    @Override
    public void visitForEachLoop(@NotNull ForEachLoop forEachloop, @NotNull CompilationContext compilationContext) {
        super.visitForEachLoop(forEachloop, compilationContext);
        forEachloop.getForEachASMProvider(compilationContext).resolveTypeCast(compilationContext);
    }
}
