package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.KeyValueExpr;
import org.jelik.parser.ast.MapCreateExpr;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.operators.EqualExpr;
import org.jelik.parser.ast.operators.GreaterExpr;
import org.jelik.parser.ast.operators.GreaterOrEqualExpr;
import org.jelik.parser.ast.operators.LesserExpr;
import org.jelik.parser.ast.operators.LesserOrEqualExpr;
import org.jelik.parser.ast.operators.NotEqualExpr;
import org.jelik.parser.ast.resolvers.decoders.EqualOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.GreaterOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.GreaterOrEqualOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.LesserOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.LesserOrEqualOpTypeDecoder;
import org.jelik.parser.ast.resolvers.decoders.NotEqualOpTypeDecoder;
import org.jelik.parser.ast.strings.StringBuilderAppend;
import org.jelik.parser.ast.strings.StringBuilderInit;
import org.jelik.parser.ast.strings.StringBuilderToStringNode;
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
        returnExpr.getFurtherExpression().visit(this, compilationContext);
        FunctionDeclaration functionDeclaration = (FunctionDeclaration) compilationContext.currentCompilationUnit();
        Type targetType = functionDeclaration.getReturnType();
        returnExpr.getFurtherExpression().getGenericReturnType().visit(new CastToVisitor(
                returnExpr.getFurtherExpression(),
                targetType), compilationContext);
    }

    @Override
    public void visit(@NotNull StringBuilderInit stringBuilderInit, @NotNull CompilationContext compilationContext) {
        stringBuilderInit.getFurtherExpression().visit(this, compilationContext);
        stringBuilderInit.parent.replaceWith(stringBuilderInit, new StringBuilderToStringNode(stringBuilderInit));
    }

    @Override
    public void visit(@NotNull StringBuilderAppend stringBuilderAppend, @NotNull CompilationContext compilationContext) {
        Expression subject = stringBuilderAppend.getSubject();
        var targetFunctionCall = stringBuilderAppend.getTargetFunctionCall();
        Type target = targetFunctionCall.getMethodData().getParameterTypes().get(0);
        subject.getReturnType().visit(new CastToVisitor(subject, target), compilationContext);
    }

    @Override
    public void visit(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        int index = 0;
        for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
            argument.visit(this, compilationContext);
            if (argument.getGenericReturnType().isPrimitive()) {
                final Type target = functionCallExpr.getTargetFunctionCall().getMethodData().getParameterTypes().get(index);
                argument.getGenericReturnType().visit(new CastToVisitor(argument.getExpression(), target), compilationContext);
            }
            index++;
        }

        Type genericReturnType = functionCallExpr.getGenericType();
        Type returnType = functionCallExpr.getType();

        Expression furtherExpression = functionCallExpr.getFurtherExpression();
        returnType.visit(new CastToVisitor(functionCallExpr, genericReturnType), compilationContext);

        if (furtherExpression != null) {
            furtherExpression.visit(this, compilationContext);
        }
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
    public void visitMapCreateExpr(@NotNull MapCreateExpr mapCreateExpr, @NotNull CompilationContext compilationContext) {
        Type target = JVMObjectType.INSTANCE;

        for (KeyValueExpr entry : mapCreateExpr.getEntries()) {
            Expression subject = entry.getKey();
            subject.getReturnType().visit(new CastToVisitor(subject, target), compilationContext);

            subject = entry.getValue();
            subject.getReturnType().visit(new CastToVisitor(subject, target), compilationContext);
        }
    }

    @Override
    public void visit(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr, @NotNull CompilationContext compilationContext) {
        arrayOrMapGetExpr.getLeftExpr().visit(this, compilationContext);
        arrayOrMapGetExpr.getExpression().visit(this, compilationContext);
        final Type returnType = arrayOrMapGetExpr.getReturnType();
        final Type genericReturnType = arrayOrMapGetExpr.getGenericReturnType();
        returnType.visit(new CastToVisitor(arrayOrMapGetExpr, genericReturnType), compilationContext);
    }
}
