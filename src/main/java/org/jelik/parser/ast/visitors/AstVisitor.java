package org.jelik.parser.ast.visitors;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.GetFieldNode;
import org.jelik.parser.ast.ImportDeclaration;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.NullExpr;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapSetExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateExpr;
import org.jelik.parser.ast.branching.ElseExpression;
import org.jelik.parser.ast.branching.IfConditionExpression;
import org.jelik.parser.ast.branching.IfExpression;
import org.jelik.parser.ast.casts.CastObjectToObjectNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.ParenthesisExpression;
import org.jelik.parser.ast.expression.ThrowExpression;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.functions.DefaultConstructorDeclaration;
import org.jelik.parser.ast.functions.FunctionBody;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.functions.FunctionParameterList;
import org.jelik.parser.ast.functions.FunctionReturn;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.StoreLocalNode;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.locals.VariableDeclaration;
import org.jelik.parser.ast.numbers.CastToNode;
import org.jelik.parser.ast.numbers.CharToInt32Node;
import org.jelik.parser.ast.numbers.CharToInt64Node;
import org.jelik.parser.ast.numbers.FalseNode;
import org.jelik.parser.ast.numbers.Float32Node;
import org.jelik.parser.ast.numbers.Float32ToFloat64Node;
import org.jelik.parser.ast.numbers.Float32ToInt32Node;
import org.jelik.parser.ast.numbers.Float32ToInt64Node;
import org.jelik.parser.ast.numbers.Float64ToFloat32;
import org.jelik.parser.ast.numbers.Float64ToInt32Node;
import org.jelik.parser.ast.numbers.Float64ToInt64Node;
import org.jelik.parser.ast.numbers.Int32Node;
import org.jelik.parser.ast.numbers.Int32ToCharNode;
import org.jelik.parser.ast.numbers.Int32ToFloat32Node;
import org.jelik.parser.ast.numbers.Int32ToFloat64Node;
import org.jelik.parser.ast.numbers.Int32ToInt16Node;
import org.jelik.parser.ast.numbers.Int32ToInt64Node;
import org.jelik.parser.ast.numbers.Int32ToInt8Node;
import org.jelik.parser.ast.numbers.Int32ToWrapperNode;
import org.jelik.parser.ast.numbers.Int64Node;
import org.jelik.parser.ast.numbers.Int64ToFloat32Node;
import org.jelik.parser.ast.numbers.Int64ToFloat64Node;
import org.jelik.parser.ast.numbers.Int64ToInt32Node;
import org.jelik.parser.ast.numbers.IntegerWrapperToInt32Node;
import org.jelik.parser.ast.numbers.ObjectToInt32Node;
import org.jelik.parser.ast.numbers.TrueNode;
import org.jelik.parser.ast.numbers.WrapperToPrimitiveNode;
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr;
import org.jelik.parser.ast.operators.AddExpr;
import org.jelik.parser.ast.operators.AndExpr;
import org.jelik.parser.ast.operators.AsExpr;
import org.jelik.parser.ast.operators.AssignExpr;
import org.jelik.parser.ast.operators.BitAndExpr;
import org.jelik.parser.ast.operators.BitOrExpr;
import org.jelik.parser.ast.operators.DecrExpr;
import org.jelik.parser.ast.operators.DivExpr;
import org.jelik.parser.ast.operators.EqualExpr;
import org.jelik.parser.ast.operators.GreaterExpr;
import org.jelik.parser.ast.operators.GreaterOrEqualExpr;
import org.jelik.parser.ast.operators.IncrExpr;
import org.jelik.parser.ast.operators.IsExpr;
import org.jelik.parser.ast.operators.LesserExpr;
import org.jelik.parser.ast.operators.LesserOrEqualExpr;
import org.jelik.parser.ast.operators.MulExpr;
import org.jelik.parser.ast.operators.NegExpr;
import org.jelik.parser.ast.operators.NotEqualExpr;
import org.jelik.parser.ast.operators.OrExpr;
import org.jelik.parser.ast.operators.RemExpr;
import org.jelik.parser.ast.operators.SubExpr;
import org.jelik.parser.ast.operators.XorExpr;
import org.jelik.parser.ast.strings.StringBuilderAppend;
import org.jelik.parser.ast.strings.StringBuilderInit;
import org.jelik.parser.ast.strings.StringBuilderToStringNode;
import org.jelik.parser.ast.strings.StringExpression;
import org.jelik.parser.ast.types.ArrayTypeNode;
import org.jelik.parser.ast.types.GenericTypeNode;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeAccessNode;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.ast.types.TypeVariableNode;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all Abstract Syntax Tree visitors
 *
 * @author Marcin Bukowiecki
 */
public abstract class AstVisitor {

    public void visitImportDeclaration(@NotNull ImportDeclaration importDeclaration, @NotNull CompilationContext compilationContext) {
        
    }

    public void visitAbstractLogicalOpExpr(@NotNull AbstractLogicalOpExpr expr, @NotNull CompilationContext compilationContext) {
        expr.getLeft().visit(this, compilationContext);
        expr.getRight().visit(this, compilationContext);
    }

    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(classDeclaration);
        for (FunctionDeclaration functionDeclaration : classDeclaration.getFunctionDeclarations()) {
            functionDeclaration.visit(this, compilationContext);
        }
        compilationContext.popCompilationUnit();
    }

    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionParameterList().visit(this, compilationContext);
        functionDeclaration.getFunctionBody().visit(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    public void visitFunctionParameterList(@NotNull FunctionParameterList fpl, @NotNull CompilationContext compilationContext) {
        for (FunctionParameter functionParameter : fpl.getFunctionParameters()) {
            functionParameter.visit(this, compilationContext);
        }
    }

    public void visit(@NotNull FunctionParameter fp, @NotNull CompilationContext compilationContext) {
        fp.getTypeNode().visit(this, compilationContext);
    }

    public void visit(@NotNull FunctionReturn functionReturn, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull SingleTypeNode typeNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull FunctionBodyBlock fb, @NotNull CompilationContext compilationContext) {
        fb.getBb().visit(this, compilationContext);
    }

    public void visit(@NotNull FunctionBody fb, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull LiteralExpr literalExpr, @NotNull CompilationContext compilationContext) {
        literalExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull GetLocalNode getLocalNode, @NotNull CompilationContext compilationContext) {
        getLocalNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visitReturnExpr(@NotNull ReturnExpr re, @NotNull CompilationContext compilationContext) {
        re.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull AddExpr addExpr, @NotNull CompilationContext compilationContext) {
        addExpr.getLeft().visit(this, compilationContext);
        addExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull RemExpr remExpr, @NotNull CompilationContext compilationContext) {
        remExpr.getLeft().visit(this, compilationContext);
        remExpr.getRight().visit(this, compilationContext);
    }

    public void visitDotCall(@NotNull DotCallExpr dotCallExpr, @NotNull CompilationContext compilationContext) {
        dotCallExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull GenericTypeNode genericTypeNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull TypeVariableNode typeVariableNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        functionCallExpr.getArgumentList().getArguments().forEach(arg -> arg.visit(this, compilationContext));
        functionCallExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Argument argument, @NotNull CompilationContext compilationContext) {
        argument.getExpression().visit(this, compilationContext);
    }

    public void visit(@NotNull TypeAccessNode typeAccessNode, @NotNull CompilationContext compilationContext) {
        typeAccessNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int32Node int32Node, @NotNull CompilationContext compilationContext) {
        int32Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull BasicBlock basicBlock, @NotNull CompilationContext compilationContext) {
        compilationContext.blockStack.addLast(basicBlock);
        basicBlock.getExpressions().forEach(e -> e.visit(this, compilationContext));
        compilationContext.blockStack.removeLast();
    }

    public void visit(SubExpr subExpr, CompilationContext compilationContext) {
        subExpr.getLeft().visit(this, compilationContext);
        subExpr.getRight().visit(this, compilationContext);
    }

    public void visit(MulExpr mulExpr, CompilationContext compilationContext) {
        mulExpr.getLeft().visit(this, compilationContext);
        mulExpr.getRight().visit(this, compilationContext);
    }

    public void visit(DivExpr divExpr, CompilationContext compilationContext) {
        divExpr.getLeft().visit(this, compilationContext);
        divExpr.getRight().visit(this, compilationContext);
    }

    public void visit(VariableDeclaration variableDeclaration, CompilationContext compilationContext) {
        variableDeclaration.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visitValueDeclaration(@NotNull ValueDeclaration valueDeclaration, @NotNull CompilationContext compilationContext) {
        valueDeclaration.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(IfExpression ifExpression, CompilationContext compilationContext) {
        ifExpression.getConditionExpression().visit(this, compilationContext);
        ifExpression.getBasicBlock().visit(this, compilationContext);
        ifExpression.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(ElseExpression elseExpression, CompilationContext compilationContext) {
        elseExpression.getBasicBlock().visit(this, compilationContext);
    }

    public void visit(GreaterExpr greaterExpr, CompilationContext compilationContext) {
        greaterExpr.getLeft().visit(this, compilationContext);
        greaterExpr.getRight().visit(this, compilationContext);
    }

    public void visit(ParenthesisExpression parenthesisExpression, CompilationContext compilationContext) {
        parenthesisExpression.getExpression().visit(this, compilationContext);
    }

    public void visit(LesserOrEqualExpr lesserOrEqualExpr, CompilationContext compilationContext) {
        lesserOrEqualExpr.getLeft().visit(this, compilationContext);
        lesserOrEqualExpr.getRight().visit(this, compilationContext);
    }

    public void visit(GreaterOrEqualExpr greaterOrEqualExpr, CompilationContext compilationContext) {
        greaterOrEqualExpr.getLeft().visit(this, compilationContext);
        greaterOrEqualExpr.getRight().visit(this, compilationContext);
    }

    public void visit(StringExpression stringExpression, CompilationContext compilationContext) {

    }

    public void visit(AssignExpr assignExpr, CompilationContext compilationContext) {
        assignExpr.getLeft().visit(this, compilationContext);
        assignExpr.getRight().visit(this, compilationContext);
    }

    public void visit(StringBuilderInit stringBuilderInit, CompilationContext compilationContext) {
        stringBuilderInit.getFurtherExpression().visit(this, compilationContext);
    }

    public void visit(StringBuilderAppend stringBuilderAppend, CompilationContext compilationContext) {
        stringBuilderAppend.getSubject().visit(this, compilationContext);
        stringBuilderAppend.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(Int32ToWrapperNode int32ToWrapperNode, CompilationContext compilationContext) {
        int32ToWrapperNode.getSubject().visit(this, compilationContext);
        int32ToWrapperNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(ObjectToInt32Node objectToInt32Node, CompilationContext compilationContext) {
        objectToInt32Node.getSubject().visit(this, compilationContext);
        objectToInt32Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(CastObjectToObjectNode castObjectToObjectNode, CompilationContext compilationContext) {
        castObjectToObjectNode.getSubject().visit(this, compilationContext);
        castObjectToObjectNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(EqualExpr equalExpr, CompilationContext compilationContext) {
        equalExpr.getLeft().visit(this, compilationContext);
        equalExpr.getRight().visit(this, compilationContext);
    }

    public void visit(IfConditionExpression ifConditionExpression, CompilationContext compilationContext) {
        ifConditionExpression.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(IncrExpr incrExpr, CompilationContext compilationContext) {
        incrExpr.getRight().visit(this, compilationContext);
    }

    public void visit(DecrExpr decrExpr, CompilationContext compilationContext) {
        //decrExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull NotEqualExpr notEqualExpr, @NotNull CompilationContext compilationContext) {
        notEqualExpr.getLeft().visit(this, compilationContext);
        notEqualExpr.getRight().visit(this, compilationContext);
    }

    public void visit(IntegerWrapperToInt32Node integerWrapperToInt32Node, CompilationContext compilationContext) {
        integerWrapperToInt32Node.getSubject().visit(this, compilationContext);
        integerWrapperToInt32Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(WrapperToPrimitiveNode wrapperToPrimitiveNode, CompilationContext compilationContext) {

    }

    public void visit(@NotNull StringBuilderToStringNode stringBuilderToStringNode, @NotNull CompilationContext compilationContext) {
        stringBuilderToStringNode.getSubject().visit(this, compilationContext);
    }

    public void visit(@NotNull StoreLocalNode storeLocalNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull LesserExpr lesserExpr, @NotNull CompilationContext compilationContext) {
        lesserExpr.getLeft().visit(this, compilationContext);
        lesserExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull NullExpr nullExpr, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull ArrayTypeNode arrayTypeNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull ArrayCreateExpr arrayCreateExpr, @NotNull CompilationContext compilationContext) {
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            expression.visit(this, compilationContext);
        }
        arrayCreateExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull TypedArrayCreateExpr arrayCreateExpr, @NotNull CompilationContext compilationContext) {
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            expression.visit(this, compilationContext);
        }
        arrayCreateExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr, @NotNull CompilationContext compilationContext) {
        arrayOrMapGetExpr.getExpression().visit(this, compilationContext);
        arrayOrMapGetExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull OrExpr orExpr, @NotNull CompilationContext compilationContext) {
        orExpr.getLeft().visit(this, compilationContext);
        orExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull AndExpr andExpr, @NotNull CompilationContext compilationContext) {
        andExpr.getLeft().visit(this, compilationContext);
        andExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull ThrowExpression throwExpression, @NotNull CompilationContext compilationContext) {
        throwExpression.getExpression().visit(this, compilationContext);
    }

    public void visit(@NotNull TryExpression tryExpression, @NotNull CompilationContext compilationContext) {
        tryExpression.getBlock().visit(this, compilationContext);
        tryExpression.getFurtherExpression().visit(this, compilationContext);
    }

    public void visit(@NotNull CatchExpression catchExpression, @NotNull CompilationContext compilationContext) {
        for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            functionParameter.visit(this, compilationContext);
        }
        catchExpression.getBlock().visit(this, compilationContext);
        catchExpression.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull CastToNode castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int64ToFloat64Node node, @NotNull CompilationContext compilationContext) {
        node.getSubject().visit(this, compilationContext);
        node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int64ToInt32Node node, @NotNull CompilationContext compilationContext) {
        node.getSubject().visit(this, compilationContext);
        node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int64ToFloat32Node node, @NotNull CompilationContext compilationContext) {
        node.getSubject().visit(this, compilationContext);
        node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Float64ToFloat32 castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int32ToInt16Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Float64ToInt32Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Float64ToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int32ToFloat32Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int32ToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull CharToInt32Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull CharToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        castToNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int32ToFloat64Node int32ToFloat64Node, @NotNull CompilationContext compilationContext) {
        int32ToFloat64Node.getSubject().visit(this, compilationContext);
        int32ToFloat64Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Float32ToFloat64Node float32ToFloat64Node, @NotNull CompilationContext compilationContext) {
        float32ToFloat64Node.getSubject().visit(this, compilationContext);
        float32ToFloat64Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Float32ToInt32Node toInt32Node, @NotNull CompilationContext compilationContext) {
        toInt32Node.getSubject().visit(this, compilationContext);
        toInt32Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Float32ToInt64Node toInt64Node, @NotNull CompilationContext compilationContext) {
        toInt64Node.getSubject().visit(this, compilationContext);
        toInt64Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull ArrayOrMapSetExpr arrayOrMapSetExpr, @NotNull CompilationContext compilationContext) {
        arrayOrMapSetExpr.getRef().visit(this, compilationContext);
        arrayOrMapSetExpr.getIndex().visit(this, compilationContext);
        arrayOrMapSetExpr.getRightExpression().visit(this, compilationContext);
    }

    public void visit(@NotNull AsExpr asExpr, @NotNull CompilationContext compilationContext) {
        asExpr.getLeft().visit(this, compilationContext);
        asExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull Int32ToInt8Node int32ToInt8Node, @NotNull CompilationContext compilationContext) {
        int32ToInt8Node.getSubject().visit(this, compilationContext);
    }

    public void visit(@NotNull Int32ToCharNode int32ToCharNode, @NotNull CompilationContext compilationContext) {
        int32ToCharNode.getSubject().visit(this, compilationContext);
    }


    public void visit(@NotNull NegExpr negExpr, @NotNull CompilationContext compilationContext) {
        negExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull Float32Node float32Node, @NotNull CompilationContext compilationContext) {
        float32Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull Int64Node int64Node, @NotNull CompilationContext compilationContext) {
        int64Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull GetFieldNode getFieldNode, @NotNull CompilationContext compilationContext) {
        getFieldNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull BitAndExpr bitAndExpr, @NotNull CompilationContext compilationContext) {
        bitAndExpr.getLeft().visit(this, compilationContext);
        bitAndExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull FalseNode falseNode, @NotNull CompilationContext compilationContext) {
        falseNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull TrueNode trueNode, @NotNull CompilationContext compilationContext) {
        trueNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    public void visit(@NotNull BitOrExpr bitOrExpr, @NotNull CompilationContext compilationContext) {
        bitOrExpr.getLeft().visit(this, compilationContext);
        bitOrExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull XorExpr xorExpr, @NotNull CompilationContext compilationContext) {
        xorExpr.getLeft().visit(this, compilationContext);
        xorExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull IsExpr isExpr, @NotNull CompilationContext compilationContext) {
        isExpr.getLeft().visit(this, compilationContext);
        isExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull TypeParameterListNode typeParameterListNode, @NotNull CompilationContext compilationContext) {
        typeParameterListNode.getTypes().forEach(t -> t.visit(this, compilationContext));
    }

    public void visitModuleDeclaration(@NotNull ModuleDeclaration moduleDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(moduleDeclaration);
        moduleDeclaration.getImports().forEach(i -> i.visit(this, compilationContext));
        moduleDeclaration.getClasses().forEach(c -> c.visit(this, compilationContext));
        moduleDeclaration.getFunctionDeclarations().forEach(f -> f.visit(this, compilationContext));
        compilationContext.popCompilationUnit();
    }

    public void visitDefaultConstructor(DefaultConstructorDeclaration defaultConstructorDeclaration, CompilationContext compilationContext) {
        visitFunctionDeclaration(defaultConstructorDeclaration, compilationContext);
    }
}
