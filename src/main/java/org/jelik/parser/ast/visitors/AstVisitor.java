package org.jelik.parser.ast.visitors;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.*;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arguments.ArgumentList;
import org.jelik.parser.ast.arrays.*;
import org.jelik.parser.ast.blocks.BasicBlock;
import org.jelik.parser.ast.branching.*;
import org.jelik.parser.ast.casts.CastObjectToObjectNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.classes.FieldDeclaration;
import org.jelik.parser.ast.classes.InterfaceDeclaration;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.common.DupNodeImpl;
import org.jelik.parser.ast.expression.*;
import org.jelik.parser.ast.functions.*;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.locals.*;
import org.jelik.parser.ast.loops.ForEachLoop;
import org.jelik.parser.ast.loops.LoopVar;
import org.jelik.parser.ast.loops.WhileLoop;
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr;
import org.jelik.parser.ast.numbers.*;
import org.jelik.parser.ast.operators.*;
import org.jelik.parser.ast.strings.*;
import org.jelik.parser.ast.types.*;
import org.jelik.parser.token.LiteralToken;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all Abstract Syntax Tree visitors
 *
 * @author Marcin Bukowiecki
 */
public abstract class AstVisitor {

    public void visitImportDeclaration(@NotNull ImportDeclaration importDeclaration,
                                       @NotNull CompilationContext compilationContext) {
        
    }

    public void visitAbstractLogicalOpExpr(@NotNull AbstractLogicalOpExpr expr,
                                           @NotNull CompilationContext compilationContext) {

        expr.getLeft().accept(this, compilationContext);
        expr.getRight().accept(this, compilationContext);
    }

    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration,
                                      @NotNull CompilationContext compilationContext) {

        compilationContext.pushCompilationUnit(classDeclaration);
        classDeclaration.getFunctionDeclarations().forEach(fd -> fd.accept(this, compilationContext));
        classDeclaration.getConstructorDeclarations().forEach(cd -> cd.accept(this, compilationContext));
        compilationContext.popCompilationUnit();
    }

    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration,
                                         @NotNull CompilationContext compilationContext) {

        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionParameterList().accept(this, compilationContext);
        functionDeclaration.getFunctionBody().accept(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    public void visitFunctionParameterList(@NotNull FunctionParameterList fpl,
                                           @NotNull CompilationContext compilationContext) {

        for (FunctionParameter functionParameter : fpl.getFunctionParameters()) {
            functionParameter.accept(this, compilationContext);
        }
    }

    public void visit(@NotNull FunctionParameter fp,
                      @NotNull CompilationContext compilationContext) {
        fp.getTypeNode().accept(this, compilationContext);
    }

    public void visit(@NotNull FunctionReturn functionReturn,
                      @NotNull CompilationContext compilationContext) {

    }

    public void visitSingleTypeNode(@NotNull SingleTypeNode typeNode,
                                    @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull FunctionBodyBlock fb,
                      @NotNull CompilationContext compilationContext) {
        fb.getBasicBlock().accept(this, compilationContext);
    }

    public void visit(@NotNull FunctionBody fb, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull LiteralExpr literalExpr, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull GetLocalNode getLocalNode, @NotNull CompilationContext compilationContext) {

    }

    public void visitReturnExpr(@NotNull ReturnExpr re, @NotNull CompilationContext compilationContext) {
        re.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull AddExpr addExpr, @NotNull CompilationContext compilationContext) {
        addExpr.getLeft().accept(this, compilationContext);
        addExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull RemExpr remExpr, @NotNull CompilationContext compilationContext) {
        remExpr.getLeft().accept(this, compilationContext);
        remExpr.getRight().accept(this, compilationContext);
    }

    public void visitReferenceExpression(@NotNull ReferenceExpressionImpl referenceExpression,
                                         @NotNull CompilationContext compilationContext) {
        referenceExpression.getReference().accept(this, compilationContext);
        referenceExpression.getFurtherExpression().accept(this, compilationContext);
        if (referenceExpression.getReference() instanceof Int32Node &&
            referenceExpression.getFurtherExpression() instanceof Int32Node) {

            var text = referenceExpression.getText();
            var float64 = new Float64Node(
                    new LiteralToken(referenceExpression.getStartOffset(), text),
                    Double.parseDouble(text)
            );
            referenceExpression.replace(float64);
        }
    }

    public void visit(@NotNull GenericTypeNode genericTypeNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull TypeVariableNode typeVariableNode, @NotNull CompilationContext compilationContext) {

    }

    public void visitFunctionCall(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        functionCallExpr.getArgumentList().getArguments().forEach(arg -> arg.accept(this, compilationContext));
    }

    public void visit(@NotNull Argument argument, @NotNull CompilationContext compilationContext) {
        argument.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull TypeAccessNodeTyped typeAccessNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull Int32Node int32Node, @NotNull CompilationContext compilationContext) {

    }

    public void visitWithLocalVariable(@NotNull ValueOrVariableDeclaration valueOrVariableDeclaration,
                                       @NotNull CompilationContext compilationContext) {

    }

    public void visitBasicBlock(@NotNull BasicBlock basicBlock, @NotNull CompilationContext compilationContext) {
        compilationContext.blockStack.addLast(basicBlock);
        basicBlock.getChildren().forEach(e -> e.accept(this, compilationContext));
        compilationContext.blockStack.removeLast();
    }

    public void visit(@NotNull SubExpr subExpr, @NotNull CompilationContext compilationContext) {
        subExpr.getLeft().accept(this, compilationContext);
        subExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull MulExpr mulExpr, @NotNull CompilationContext compilationContext) {
        mulExpr.getLeft().accept(this, compilationContext);
        mulExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull DivExpr divExpr, @NotNull CompilationContext compilationContext) {
        divExpr.getLeft().accept(this, compilationContext);
        divExpr.getRight().accept(this, compilationContext);
    }

    public void visitVariableDeclaration(@NotNull VariableDeclaration variableDeclaration,
                                         @NotNull CompilationContext compilationContext) {
        variableDeclaration.getExpression().accept(this, compilationContext);
    }

    public void visitValueDeclaration(@NotNull ValueDeclaration valueDeclaration,
                                      @NotNull CompilationContext compilationContext) {
        valueDeclaration.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull IfExpressionImpl ifExpression, @NotNull CompilationContext compilationContext) {
        ifExpression.getConditionExpression().accept(this, compilationContext);
        ifExpression.getBasicBlock().accept(this, compilationContext);
        ifExpression.getElseExpressionOpt().ifPresent(expr -> expr.accept(this, compilationContext));
    }

    public void visit(@NotNull ElseExpressionImpl elseExpression, @NotNull CompilationContext compilationContext) {
        elseExpression.getBasicBlock().accept(this, compilationContext);
    }

    public void visit(@NotNull GreaterExpr greaterExpr, @NotNull CompilationContext compilationContext) {
        greaterExpr.getLeft().accept(this, compilationContext);
        greaterExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull ParenthesisExpressionWrapper parenthesisExpression,
                      @NotNull CompilationContext compilationContext) {

        parenthesisExpression.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull LesserOrEqualExpr lesserOrEqualExpr, @NotNull CompilationContext compilationContext) {
        lesserOrEqualExpr.getLeft().accept(this, compilationContext);
        lesserOrEqualExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull GreaterOrEqualExpr greaterOrEqualExpr, @NotNull CompilationContext compilationContext) {
        greaterOrEqualExpr.getLeft().accept(this, compilationContext);
        greaterOrEqualExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull StringTypedExpression stringExpression, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull AssignExpr assignExpr, @NotNull CompilationContext compilationContext) {
        assignExpr.getLeft().accept(this, compilationContext);
        assignExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull StringBuilderInit stringBuilderInit, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull StringBuilderAppend stringBuilderAppend,
                      @NotNull CompilationContext compilationContext) {
        stringBuilderAppend.getSubject().accept(this, compilationContext);
    }

    public void visit(@NotNull Int32ToWrapperNode int32ToWrapperNode, @NotNull CompilationContext compilationContext) {
        int32ToWrapperNode.getSubject().accept(this, compilationContext);
    }

    public void visit(@NotNull ObjectToInt32Node objectToInt32Node, @NotNull CompilationContext compilationContext) {
        objectToInt32Node.getSubject().accept(this, compilationContext);
    }

    public void visit(@NotNull CastObjectToObjectNode castObjectToObjectNode,
                      @NotNull CompilationContext compilationContext) {
        castObjectToObjectNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull EqualExpr equalExpr, @NotNull CompilationContext compilationContext) {
        equalExpr.getLeft().accept(this, compilationContext);
        equalExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull IfConditionExpressionWrapperImpl ifConditionExpression,
                      @NotNull CompilationContext compilationContext) {
        ifConditionExpression.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull IncrExpr incrExpr, @NotNull CompilationContext compilationContext) {
        incrExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull DecrExpr decrExpr, @NotNull CompilationContext compilationContext) {
        //decrExpr.getRight().visit(this, compilationContext);
    }

    public void visit(@NotNull NotEqualExpr notEqualExpr, @NotNull CompilationContext compilationContext) {
        notEqualExpr.getLeft().accept(this, compilationContext);
        notEqualExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull IntegerToInt32NodeWrapper integerWrapperToInt32Node,
                      @NotNull CompilationContext compilationContext) {
        integerWrapperToInt32Node.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull ToPrimitiveNodeWrapper wrapperToPrimitiveNode,
                      @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull StringBuilderToStringNode stringBuilderToStringNode,
                      @NotNull CompilationContext compilationContext) {
        stringBuilderToStringNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull StoreLocalNode storeLocalNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull LesserExpr lesserExpr, @NotNull CompilationContext compilationContext) {
        lesserExpr.getLeft().accept(this, compilationContext);
        lesserExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull NullExpr nullExpr, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull ArrayTypeNode arrayTypeNode, @NotNull CompilationContext compilationContext) {

    }

    public void visitArrayCreateExpr(@NotNull ArrayCreateExpr arrayCreateExpr,
                                     @NotNull CompilationContext compilationContext) {
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            expression.accept(this, compilationContext);
        }
    }

    public void visitTypedArrayCreateExpr(@NotNull TypedArrayCreateExpr arrayCreateExpr,
                                          @NotNull CompilationContext compilationContext) {
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            expression.accept(this, compilationContext);
        }
    }

    public void visitArrayOrMapGetExpr(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr,
                                       @NotNull CompilationContext compilationContext) {
        arrayOrMapGetExpr.getLeftExpr().accept(this, compilationContext);
        arrayOrMapGetExpr.getExpression().accept(this, compilationContext);
    }

    public void visitOrExpr(@NotNull OrExpr orExpr, @NotNull CompilationContext compilationContext) {
        orExpr.getLeft().accept(this, compilationContext);
        orExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull AndExpr andExpr, @NotNull CompilationContext compilationContext) {
        andExpr.getLeft().accept(this, compilationContext);
        andExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull ThrowExpressionWrapper throwExpression, @NotNull CompilationContext compilationContext) {
        throwExpression.getExpression().accept(this, compilationContext);
    }

    public void visitTryExpression(@NotNull TryExpression tryExpression, @NotNull CompilationContext compilationContext) {
        for (ASTNode child : tryExpression.getChildren()) {
            child.accept(this, compilationContext);
        }
    }

    public void visitCatchExpression(@NotNull CatchExpression catchExpression, @NotNull CompilationContext compilationContext) {
        for (ASTNode child : catchExpression.getChildren()) {
            child.accept(this, compilationContext);
        }
    }

    public void visit(@NotNull CastToNode castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Int64ToFloat64Node node, @NotNull CompilationContext compilationContext) {
        node.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Int64ToInt32Node node, @NotNull CompilationContext compilationContext) {
        node.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Int64ToFloat32Node node, @NotNull CompilationContext compilationContext) {
        node.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Float64ToFloat32 castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Int32ToInt16Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Float64ToInt32Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Float64ToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Int32ToFloat32Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Int32ToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull CharToInt32Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull CharToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Int32ToFloat64Node int32ToFloat64Node,
                      @NotNull CompilationContext compilationContext) {
        int32ToFloat64Node.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Float32ToFloat64Node float32ToFloat64Node,
                      @NotNull CompilationContext compilationContext) {
        float32ToFloat64Node.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Float32ToInt32Node toInt32Node, @NotNull CompilationContext compilationContext) {
        toInt32Node.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Float32ToInt64Node toInt64Node, @NotNull CompilationContext compilationContext) {
        toInt64Node.getExpression().accept(this, compilationContext);
    }

    public void visitArrayOrMapSetExpr(@NotNull ArrayOrMapSetExpr arrayOrMapSetExpr, @NotNull CompilationContext compilationContext) {
        arrayOrMapSetExpr.getRef().accept(this, compilationContext);
        arrayOrMapSetExpr.getIndex().accept(this, compilationContext);
        arrayOrMapSetExpr.getRightExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull AsExpr asExpr, @NotNull CompilationContext compilationContext) {
        asExpr.getLeft().accept(this, compilationContext);
        asExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull Int32ToInt8Node int32ToInt8Node, @NotNull CompilationContext compilationContext) {
        int32ToInt8Node.getExpression().accept(this, compilationContext);
    }

    public void visit(@NotNull Int32ToCharNode int32ToCharNode, @NotNull CompilationContext compilationContext) {
        int32ToCharNode.getExpression().accept(this, compilationContext);
    }


    public void visit(@NotNull NegExpr negExpr, @NotNull CompilationContext compilationContext) {
        negExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull Float32Node float32Node, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull Float64Node float64Node, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull Int64Node int64Node, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull GetFieldNode getFieldNode, @NotNull CompilationContext compilationContext) {

    }

    public void visit(@NotNull BitAndExpr bitAndExpr, @NotNull CompilationContext compilationContext) {
        bitAndExpr.getLeft().accept(this, compilationContext);
        bitAndExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull FalseNode falseNode, @NotNull CompilationContext compilationContext) {
    }

    public void visit(@NotNull TrueNode trueNode, @NotNull CompilationContext compilationContext) {
    }

    public void visit(@NotNull BitOrExpr bitOrExpr, @NotNull CompilationContext compilationContext) {
        bitOrExpr.getLeft().accept(this, compilationContext);
        bitOrExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull XorExpr xorExpr, @NotNull CompilationContext compilationContext) {
        xorExpr.getLeft().accept(this, compilationContext);
        xorExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull IsExpr isExpr, @NotNull CompilationContext compilationContext) {
        isExpr.getLeft().accept(this, compilationContext);
        isExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull TypeParameterListNode typeParameterListNode,
                      @NotNull CompilationContext compilationContext) {
        typeParameterListNode.getTypes().forEach(t -> t.accept(this, compilationContext));
    }

    public void visitModuleDeclaration(@NotNull ModuleDeclaration moduleDeclaration,
                                       @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(moduleDeclaration);
        moduleDeclaration.getImports().forEach(i -> i.accept(this, compilationContext));
        moduleDeclaration.getClasses().forEach(c -> c.accept(this, compilationContext));
        moduleDeclaration.getInterfaces().forEach(i -> i.accept(this, compilationContext));
        moduleDeclaration.getFunctionDeclarations().forEach(f -> f.accept(this, compilationContext));
        moduleDeclaration.getConstructorDeclarations().forEach(c -> c.accept(this, compilationContext));
        compilationContext.popCompilationUnit();
    }

    public void visitDefaultConstructor(@NotNull DefaultConstructorDeclaration defaultConstructorDeclaration,
                                        @NotNull CompilationContext compilationContext) {

    }

    public void visitFunctionReference(@NotNull FunctionReferenceNode functionReferenceNodeImpl,
                                       @NotNull CompilationContext compilationContext) {

    }

    public void visitWildCardTypeNode(@NotNull WildCardTypeNode wildCardTypeNode,
                                      @NotNull CompilationContext compilationContext) {

    }

    public void visitCovariantTypeNode(@NotNull CovariantTypeNode covariantTypeNode,
                                       @NotNull CompilationContext compilationContext) {

    }

    public void visitFieldDeclaration(@NotNull FieldDeclaration fieldDeclaration,
                                      @NotNull CompilationContext compilationContext) {
        fieldDeclaration.getExpression().accept(this, compilationContext);
    }

    public void visitMapCreateExpr(@NotNull MapCreateExpr mapCreateExpr,
                                   @NotNull CompilationContext compilationContext) {
        for (KeyValueExpr entry : mapCreateExpr.getEntries()) {
            entry.accept(this, compilationContext);
        }
    }

    public void visitKeyValueExpr(@NotNull KeyValueExpr keyValueExpr,
                                  @NotNull CompilationContext compilationContext) {
        keyValueExpr.getKey().accept(this, compilationContext);
        keyValueExpr.getValue().accept(this, compilationContext);
    }

    public void visitConstructorDeclaration(@NotNull ConstructorDeclaration constructorDeclaration,
                                            @NotNull CompilationContext compilationContext) {
        visitFunctionDeclaration(constructorDeclaration, compilationContext);
    }

    public void visit(@NotNull SuperCallExpr superCallExpr,
                      @NotNull CompilationContext compilationContext) {
        superCallExpr.getArgumentList().getArguments().forEach(arg -> arg.accept(this, compilationContext));
    }

    public void visitBitUshrExpr(@NotNull BitUshrExpr bitUshrExpr,
                                 @NotNull CompilationContext compilationContext) {
        bitUshrExpr.getLeft().accept(this, compilationContext);
        bitUshrExpr.getRight().accept(this, compilationContext);
    }

    public void visitBitShrExpr(@NotNull BitShrExpr bitShrExpr,
                                @NotNull CompilationContext compilationContext) {
        bitShrExpr.getLeft().accept(this, compilationContext);
        bitShrExpr.getRight().accept(this, compilationContext);
    }

    public void visitBitShlExpr(@NotNull BitShlExpr bitShlExpr,
                                @NotNull CompilationContext compilationContext) {
        bitShlExpr.getLeft().accept(this, compilationContext);
        bitShlExpr.getRight().accept(this, compilationContext);
    }

    public void visit(@NotNull Float32ToWrapperNode float32ToWrapperNode,
                      @NotNull CompilationContext compilationContext) {
        float32ToWrapperNode.getSubject().accept(this, compilationContext);
    }

    public void visit(@NotNull Float64ToWrapperNode float64ToWrapperNode,
                      @NotNull CompilationContext compilationContext) {
        float64ToWrapperNode.getSubject().accept(this, compilationContext);
    }

    public void visitForEachLoop(@NotNull ForEachLoop forEachloop,
                                 @NotNull CompilationContext compilationContext) {
        forEachloop.getVarExpr().accept(this, compilationContext);
        forEachloop.getIterExpression().accept(this, compilationContext);
        forEachloop.getBlock().accept(this, compilationContext);
    }

    public void visitWhileLoop(@NotNull WhileLoop whileLoop,
                               @NotNull CompilationContext compilationContext) {
        whileLoop.getCondition().accept(this, compilationContext);
        whileLoop.getBlock().accept(this, compilationContext);
    }

    public void visitLoopVar(@NotNull LoopVar loopVar,
                             @NotNull CompilationContext compilationContext) {
    }

    public void visitInExpr(@NotNull InExpr inExpr,
                            @NotNull CompilationContext compilationContext) {
        inExpr.getLeft().accept(this, compilationContext);
        inExpr.getRight().accept(this, compilationContext);
    }

    public void visitSliceExpr(@NotNull SliceExpr sliceExpr,
                               @NotNull CompilationContext compilationContext) {
        sliceExpr.getLeft().accept(this, compilationContext);
        sliceExpr.getRight().accept(this, compilationContext);
    }

    public void visitTypedArrayCreateWithSizeExpr(@NotNull TypedArrayCreateWithSizeExprTyped typedArrayCreateWithSizeExpr,
                                                  @NotNull CompilationContext compilationContext) {
        typedArrayCreateWithSizeExpr.getExpression().accept(this, compilationContext);
    }

    public void visitLambdaParameterList(@NotNull LambdaParameterList lambdaParameterList,
                                         @NotNull CompilationContext compilationContext) {
        lambdaParameterList.getFunctionParameters().forEach(p -> p.accept(this, compilationContext));
    }

    public void visitInferredTypeNode(@NotNull InferredTypeNode inferredTypeNode,
                                      @NotNull CompilationContext compilationContext) {

    }

    public void visitLambdaDeclaration(@NotNull LambdaDeclaration lambdaDeclaration,
                                       @NotNull CompilationContext compilationContext) {

    }

    public void visitInferredLambdaParameter(@NotNull InferredLambdaParameter inferredLambdaParameter,
                                             @NotNull CompilationContext compilationContext) {

    }

    public void visitCharExpression(@NotNull CharTypedExpression charExpression,
                                    @NotNull CompilationContext compilationContext) {

    }

    public void visitInt64ToWrapperNode(@NotNull Int64ToWrapperNode int64ToWrapperNode,
                                        @NotNull CompilationContext compilationContext) {
        int64ToWrapperNode.accept(this, compilationContext);
    }

    public void visitLambdaDeclarationExpression(@NotNull LambdaDeclarationExpression lambdaDeclarationExpression,
                                                 @NotNull CompilationContext compilationContext) {

    }

    public void visitElifExpression(@NotNull ElifExpression elifExpression,
                                    @NotNull CompilationContext compilationContext) {
        elifExpression.getConditionExpression().accept(this, compilationContext);
        elifExpression.getBasicBlock().accept(this, compilationContext);
        elifExpression.getElseExpressionOpt().ifPresent(expr -> expr.accept(this, compilationContext));
    }

    public void visitNotExpr(@NotNull NotExpr notExpr, @NotNull CompilationContext compilationContext) {
        notExpr.getExpression().accept(this, compilationContext);
    }

    public void visitNullSafeCall(@NotNull NullSafeCallExpr nullSafeCall,
                                  @NotNull CompilationContext compilationContext) {
        //nullSafeCall.getChildren().forEach(ch -> ch.accept(this, compilationContext));
    }

    public void visitLabelNode(LabelNode labelNode, CompilationContext compilationContext) {

    }

    public void visitElvisExpr(ElvisExpr elvisExpr, CompilationContext compilationContext) {
        visit(elvisExpr, compilationContext);
    }

    public void visit(ASTNode astNode, CompilationContext compilationContext) {
        astNode.getChildren().forEach(ch -> ch.accept(this, compilationContext));
    }

    public void visitExtFunction(@NotNull ExtensionFunctionDeclarationImpl extensionFunctionDeclaration,
                                 @NotNull CompilationContext compilationContext) {
        visitFunctionDeclaration(extensionFunctionDeclaration, compilationContext);
    }

    public void visitInterfaceDeclaration(@NotNull InterfaceDeclaration interfaceDeclaration,
                                          @NotNull  CompilationContext compilationContext) {
        visitClassDeclaration(interfaceDeclaration, compilationContext);
    }

    public void visitFunctionType(@NotNull FunctionTypeNode functionTypeNode,
                                  @NotNull CompilationContext compilationContext) {
        functionTypeNode.getParameterTypes().forEach(t -> t.accept(this, compilationContext));
        functionTypeNode.getReturnType().accept(this, compilationContext);
    }

    public void visitArgumentList(@NotNull ArgumentList argumentList,
                                  @NotNull CompilationContext compilationContext) {
        argumentList.getArguments().forEach(arg -> arg.accept(this, compilationContext));
    }

    public void visitBreakExpression(@NotNull BreakExpr breakExpr,
                                     @NotNull CompilationContext compilationContext) { }

    public void visitContinueExpression(@NotNull ContinueExpr continueExpr,
                                        @NotNull CompilationContext compilationContext) { }

    public void visitCompositeTypeNode(@NotNull CompositeTypeNode compositeTypeNode,
                                       @NotNull CompilationContext compilationContext) {

    }

    public void visitDupNode(@NotNull DupNodeImpl dupNodeImpl,
                             @NotNull CompilationContext compilationContext) {
        dupNodeImpl.getExpression().accept(this, compilationContext);
    }

    public void visitNullSafeBooleanExprWrapper(@NotNull NullSafeCheckExprWrapper nullSafeBooleanExprWrapper,
                                                @NotNull CompilationContext compilationContext) {
        nullSafeBooleanExprWrapper.getChildren().forEach(ch -> ch.accept(this, compilationContext));
    }

    public void visitDefaultValueExpr(@NotNull DefaultValueExpr defaultValueExpr,
                                      @NotNull CompilationContext compilationContext) {
        defaultValueExpr.getChildren().forEach(ch -> ch.accept(this, compilationContext));
    }

    public void visitInterfaceMethodDeclaration(@NotNull InterfaceMethodDeclaration interfaceMethodDeclaration,
                                                @NotNull CompilationContext compilationContext) {
        visitFunctionDeclaration(interfaceMethodDeclaration, compilationContext);
    }
}


