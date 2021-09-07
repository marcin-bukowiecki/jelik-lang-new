package org.jelik.parser.ast.resolvers.types;

import org.jelik.compiler.runtime.TypeEnum;
import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.compiler.helper.CompilerHelper;
import org.jelik.compiler.passes.TypeChecker;
import org.jelik.parser.ast.GetFieldNode;
import org.jelik.parser.ast.KeyValueExpr;
import org.jelik.parser.ast.MapCreateExpr;
import org.jelik.parser.ast.ReferenceExpression;
import org.jelik.parser.ast.ReferenceExpressionImpl;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapSetExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateWithSizeExprTyped;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.functions.FunctionReferenceNode;
import org.jelik.parser.ast.functions.FunctionReturn;
import org.jelik.parser.ast.functions.LambdaDeclarationExpression;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.locals.ValueOrVariableDeclaration;
import org.jelik.parser.ast.locals.VariableDeclaration;
import org.jelik.parser.ast.loops.ForEachLoop;
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr;
import org.jelik.parser.ast.numbers.Int32Node;
import org.jelik.parser.ast.operators.*;
import org.jelik.parser.ast.resolvers.BuiltinTypeResolver;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.parser.ast.resolvers.FunctionCallResolver;
import org.jelik.parser.ast.resolvers.GenericTypeResolver;
import org.jelik.parser.ast.resolvers.ImportedTypeResolver;
import org.jelik.parser.ast.strings.StringBuilderAppend;
import org.jelik.parser.ast.types.ArrayTypeNode;
import org.jelik.parser.ast.types.CompositeTypeNode;
import org.jelik.parser.ast.types.CovariantTypeNode;
import org.jelik.parser.ast.types.FunctionTypeNode;
import org.jelik.parser.ast.types.GenericTypeNode;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeNodeRef;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.ast.utils.ASTUtils;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMArrayType;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMObjectType;
import org.jelik.types.Type;
import org.jelik.types.resolver.AddOperatorTypeResolver;
import org.jelik.types.resolver.AsOperatorTypeResolver;
import org.jelik.types.resolver.BitAndTypeResolver;
import org.jelik.types.resolver.BitOrTypeResolver;
import org.jelik.types.resolver.BitXorTypeResolver;
import org.jelik.types.resolver.DivTypeResolver;
import org.jelik.types.resolver.MulTypeResolver;
import org.jelik.types.resolver.RemTypeResolver;
import org.jelik.types.resolver.ShlTypeResolver;
import org.jelik.types.resolver.ShrTypeResolver;
import org.jelik.types.resolver.SubOperatorTypeResolver;
import org.jelik.types.resolver.UshrTypeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public class TypeResolver extends AstVisitor {

    @Override
    public void visitCompositeTypeNode(@NotNull CompositeTypeNode compositeTypeNode,
                                       @NotNull CompilationContext compilationContext) {
        final Type resolvedType = new CompositeTypeResolver(compilationContext).resolve(compositeTypeNode);
        if (resolvedType == null) {
            CompilerHelper.INSTANCE.raiseTypeCompileError("type.unresolved", compositeTypeNode);
            return;
        }
        compositeTypeNode.setType(resolvedType);
        compositeTypeNode.setGenericType(resolvedType.deepGenericCopy());
    }

    @Override
    public void visitFunctionType(@NotNull FunctionTypeNode functionTypeNode,
                                  @NotNull CompilationContext compilationContext) {
        super.visitFunctionType(functionTypeNode, compilationContext);
    }

    @Override
    public void visit(@NotNull GenericTypeNode genericTypeNode,
                      @NotNull CompilationContext compilationContext) {
        genericTypeNode.getSingleTypeNode().accept(this, compilationContext);
        for (TypeNode typeVariable : genericTypeNode.getTypeVariables().getTypes()) {
            typeVariable.accept(this, compilationContext);
        }
    }

    @Override
    public void visitCovariantTypeNode(@NotNull CovariantTypeNode covariantTypeNode,
                                       @NotNull CompilationContext compilationContext) {

        covariantTypeNode.getParentTypeNode().accept(this, compilationContext);
        if (covariantTypeNode.getParentTypeNode().getType() == null) {
            throw new CompileException("Unresolved type",
                    covariantTypeNode.getParentTypeNode(),
                    compilationContext.getCurrentModule());
        }
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
    public void visitWithLocalVariable(@NotNull ValueOrVariableDeclaration withLocalVariable,
                                       @NotNull CompilationContext compilationContext) {
        withLocalVariable.getExpression().accept(this, compilationContext);
        withLocalVariable.getTypeNode().accept(this, compilationContext);
        if (withLocalVariable.getTypeNode() == UndefinedTypeNode.UNDEFINED_TYPE_NODE) {
            withLocalVariable.getLocalVariable().setTypeRef(withLocalVariable
                    .getExpression().createInferredTypeRef());
        } else {
            withLocalVariable.getLocalVariable().setTypeRef(new TypeNodeRef(withLocalVariable.getTypeNode()));
        }
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration,
                                         @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionBody().accept(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(@NotNull FunctionBodyBlock fb, @NotNull CompilationContext compilationContext) {
        compilationContext.blockStack.addLast(fb.getBasicBlock());
        for (Expression expression : fb.getBasicBlock().getExpressions()) {
            expression.accept(this, compilationContext);
        }
        compilationContext.blockStack.removeLast();
    }

    @Override
    public void visitReturnExpr(@NotNull ReturnExpr re, @NotNull CompilationContext compilationContext) {
        re.getExpression().accept(this, compilationContext);
        if (compilationContext.getSession().isReplMode()) {
            //TODO handle repl mode
            var functionDeclaration = Objects.requireNonNull(ASTUtils.getFunctionDeclaration(re));

        }
    }

    @Override
    public void visit(@NotNull DecrExpr decrExpr, @NotNull CompilationContext compilationContext) {
        decrExpr.getExpression().accept(this, compilationContext);
    }

    @Override
    public void visit(@NotNull IncrExpr incrExpr, @NotNull CompilationContext compilationContext) {
        incrExpr.getRight().accept(this, compilationContext);
        var rt = incrExpr.getRight().getGenericReturnType();
        incrExpr.setType(incrExpr.getRight().getType());
        incrExpr.setGenericType(rt.deepGenericCopy());
    }
    @Override
    public void visit(@NotNull EqualExpr equalExpr, @NotNull CompilationContext compilationContext) {
        equalExpr.getLeft().accept(this, compilationContext);
        equalExpr.getRight().accept(this, compilationContext);
    }

    @Override
    public void visit(@NotNull AddExpr addExpr, @NotNull CompilationContext compilationContext) {
        if (addExpr.getParent() instanceof StringBuilderAppend) {
            addExpr.getLeft().accept(this, compilationContext);
            var stringBuilderAppend = new StringBuilderAppend(addExpr.getLeft(), compilationContext);
            ((StringBuilderAppend) addExpr.getParent()).setFurtherExpression(stringBuilderAppend);
            addExpr.getRight().accept(this, compilationContext);
            stringBuilderAppend.setFurtherExpression(new StringBuilderAppend(addExpr.getRight(), compilationContext));
            return;
        }

        addExpr.getLeft().accept(this, compilationContext);
        final Type genericReturnType = addExpr.getLeft().getGenericReturnType();
        if (genericReturnType.getTypeEnum() == TypeEnum.string && addExpr.getRight() instanceof AddExpr) {
            AddOperatorTypeResolver
                    .setupStringBuilder(compilationContext.currentFunction(), addExpr, compilationContext);
            addExpr.getRight().accept(this, compilationContext);
            return;
        }
        addExpr.getRight().accept(this, compilationContext);
        var rt = AddOperatorTypeResolver.resolve(addExpr, compilationContext);
        addExpr.setType(rt);
        addExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visitBitShlExpr(@NotNull BitShlExpr bitShlExpr, @NotNull CompilationContext compilationContext) {
        super.visitBitShlExpr(bitShlExpr, compilationContext);
        var rt = ShlTypeResolver.INSTANCE.resolve(bitShlExpr, compilationContext);
        bitShlExpr.setType(rt);
        bitShlExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visitBitShrExpr(@NotNull BitShrExpr bitShrExpr, @NotNull CompilationContext compilationContext) {
        super.visitBitShrExpr(bitShrExpr, compilationContext);
        var rt = ShrTypeResolver.INSTANCE.resolve(bitShrExpr, compilationContext);
        bitShrExpr.setType(rt);
        bitShrExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visitBitUshrExpr(@NotNull BitUshrExpr bitUshrExpr, @NotNull CompilationContext compilationContext) {
        super.visitBitUshrExpr(bitUshrExpr, compilationContext);
        var rt = UshrTypeResolver.INSTANCE.resolve(bitUshrExpr, compilationContext);
        bitUshrExpr.setType(rt);
        bitUshrExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull BitAndExpr expr, @NotNull CompilationContext compilationContext) {
        expr.getLeft().accept(this, compilationContext);
        expr.getRight().accept(this, compilationContext);
        var rt = new BitAndTypeResolver().resolve(expr, compilationContext);
        expr.setType(rt);
        expr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull BitOrExpr expr, @NotNull CompilationContext compilationContext) {
        expr.getLeft().accept(this, compilationContext);
        expr.getRight().accept(this, compilationContext);
        var rt = new BitOrTypeResolver().resolve(expr, compilationContext);
        expr.setType(rt);
        expr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull RemExpr remExpr, @NotNull CompilationContext compilationContext) {
        remExpr.getLeft().accept(this, compilationContext);
        remExpr.getRight().accept(this, compilationContext);
        var rt = new RemTypeResolver().resolve(remExpr, compilationContext);
        remExpr.setType(rt);
        remExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull SubExpr subExpr, @NotNull CompilationContext compilationContext) {
        subExpr.getLeft().accept(this, compilationContext);
        subExpr.getRight().accept(this, compilationContext);
        var rt = new SubOperatorTypeResolver().resolve(subExpr, compilationContext);
        subExpr.setType(rt);
        subExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull NegExpr negExpr, @NotNull CompilationContext compilationContext) {
        negExpr.getRight().accept(this, compilationContext);
        var rt = new SubOperatorTypeResolver().resolve(negExpr, compilationContext);
        negExpr.setType(rt);
        negExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull MulExpr mulExpr, @NotNull CompilationContext compilationContext) {
        mulExpr.getLeft().accept(this, compilationContext);
        mulExpr.getRight().accept(this, compilationContext);
        var rt = new MulTypeResolver().resolve(mulExpr, compilationContext);
        mulExpr.setType(rt);
        mulExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull DivExpr divExpr, @NotNull CompilationContext compilationContext) {
        divExpr.getLeft().accept(this, compilationContext);
        divExpr.getRight().accept(this, compilationContext);
        var rt = new DivTypeResolver().resolve(divExpr, compilationContext);
        divExpr.setType(rt);
        divExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull AssignExpr assignExpr, @NotNull CompilationContext compilationContext) {
        super.visit(assignExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull FunctionReturn fr, @NotNull CompilationContext compilationContext) {
        fr.getTypeNode().accept(this, compilationContext);
    }

    @Override
    public void visit(@NotNull FunctionParameter fp, @NotNull CompilationContext compilationContext) {
        fp.getTypeNode().accept(this, compilationContext);
    }

    @Override
    public void visitSingleTypeNode(@NotNull SingleTypeNode typeNode, @NotNull CompilationContext compilationContext) {
        BuiltinTypeResolver builtinTypeResolver = new BuiltinTypeResolver();
        builtinTypeResolver.visitSingleTypeNode(typeNode, compilationContext);
        if (builtinTypeResolver.getTypeOpt().isEmpty()) {
            DefaultImportedTypeResolver defaultImportedTypeResolver = new DefaultImportedTypeResolver();
            defaultImportedTypeResolver.visitSingleTypeNode(typeNode, compilationContext);
            if (defaultImportedTypeResolver.getTypeOpt().isEmpty()) {
                compilationContext.currentCompilationUnit()
                        .findType(typeNode, compilationContext).ifPresentOrElse(t -> {
                    typeNode.setType(t);
                    typeNode.setGenericType(t);
                }, () -> {
                    ImportedTypeResolver importedTypeResolver = new ImportedTypeResolver();
                    importedTypeResolver.visitSingleTypeNode(typeNode, compilationContext);
                });
            }
        }
    }

    @Override
    public void visit(@NotNull Argument argument, @NotNull CompilationContext compilationContext) {
        argument.getExpression().accept(this, compilationContext);
    }

    @Override
    public void visitNullSafeCall(@NotNull NullSafeCallExpr nullSafeCall, @NotNull CompilationContext compilationContext) {
        nullSafeCall.getReference().accept(this, compilationContext);
        nullSafeCall.getFurtherExpression().accept(this, compilationContext);
    }

    @Override
    public void visitFunctionCall(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        functionCallExpr
                .getLiteralExpr()
                .getTypeParameterListNode()
                .getTypes()
                .forEach(t -> t.accept(this, compilationContext));

        for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
            argument.accept(this, compilationContext);
        }
        if (functionCallExpr.getParent() instanceof ReferenceExpression) {
            Type type = ((ReferenceExpressionImpl) functionCallExpr.getParent()).getReference().getGenericReturnType();
            functionCallExpr.setOwner(type);
        }

        var targetFunctionCall = new FunctionCallResolver()
                .resolveCall(functionCallExpr, compilationContext)
                .orElseThrow(() -> new CompileException("Could not find method",
                        functionCallExpr,
                        compilationContext.getCurrentModule()));

        targetFunctionCall.resolveCallback(functionCallExpr, compilationContext);

        if (functionCallExpr.getOwner() == null) {
            functionCallExpr.setOwner(targetFunctionCall.getMethodData().getOwner());
        }
        functionCallExpr.setTargetFunctionCallProvider(targetFunctionCall);

        Type returnType = targetFunctionCall.getMethodData().getReturnType();
        Type genericReturnType = targetFunctionCall.getMethodData().getGenericReturnType();
        functionCallExpr.setType(returnType.deepGenericCopy());
        functionCallExpr.setGenericType(genericReturnType);

        functionCallExpr
                .getLiteralExpr()
                .getTypeParameterListNode()
                .getTypes()
                .forEach(t -> t.accept(this, compilationContext));

        new GenericTypeResolver().visitFunctionCall(functionCallExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull Int32Node int32Node, @NotNull CompilationContext compilationContext) {
        int32Node.setType(JVMIntType.INSTANCE);
        int32Node.setGenericType(JVMIntType.INSTANCE);
    }

    @Override
    public void visitArrayCreateExpr(@NotNull ArrayCreateExpr arrayCreateExpr,
                                     @NotNull CompilationContext compilationContext) {
        arrayCreateExpr.getExpressions().forEach(expr -> expr.accept(this, compilationContext));
        Type arrayType = null;
        Type genericArrayType = null;
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            Type returnType = expression.getReturnType();
            Type genericReturnType = expression.getGenericReturnType();
            if (arrayType == null) {
                arrayType = returnType;
                genericArrayType = genericReturnType;
            } else {
                if (!genericReturnType.isAssignableTo(arrayType, compilationContext)) {
                    arrayType = JVMObjectType.INSTANCE;
                    genericArrayType = JVMObjectType.INSTANCE;
                    break;
                }
            }
        }
        arrayCreateExpr.getNodeContext()
                .setType(new JVMArrayType(Objects.requireNonNull(arrayType)));
        arrayCreateExpr.getNodeContext()
                .setGenericType(new JVMArrayType(Objects.requireNonNull(genericArrayType).deepGenericCopy()));
    }

    @Override
    public void visitTypedArrayCreateExpr(@NotNull TypedArrayCreateExpr arrayCreateExpr,
                                          @NotNull CompilationContext compilationContext) {
        arrayCreateExpr.getExpressions().forEach(expr -> expr.accept(this, compilationContext));
        arrayCreateExpr.getTypeNode().accept(this, compilationContext);
        arrayCreateExpr.getNodeContext()
                .setType(new JVMArrayType(Objects.requireNonNull(arrayCreateExpr.getTypeNode().getType())));
        arrayCreateExpr.getNodeContext().setGenericType(new JVMArrayType(Objects.requireNonNull(arrayCreateExpr
                .getTypeNode().getGenericType()).deepGenericCopy()));
    }

    @Override
    public void visitTypedArrayCreateWithSizeExpr(@NotNull TypedArrayCreateWithSizeExprTyped arrayCreateExpr,
                                                  @NotNull CompilationContext compilationContext) {
        arrayCreateExpr.getTypeNode().accept(this, compilationContext);
        arrayCreateExpr.getNodeContext()
                .setType(new JVMArrayType(Objects.requireNonNull(arrayCreateExpr.getTypeNode().getType())));
        arrayCreateExpr.getNodeContext().setGenericType(new JVMArrayType(Objects.requireNonNull(arrayCreateExpr
                .getTypeNode().getGenericType()).deepGenericCopy()));
        arrayCreateExpr.getExpression().accept(this, compilationContext);
    }

    @Override
    public void visit(@NotNull ArrayTypeNode arrayTypeNode, @NotNull CompilationContext compilationContext) {
        TypeNode innerType = arrayTypeNode.getInnerType();
        innerType.accept(this, compilationContext);
        arrayTypeNode.setType(new JVMArrayType(innerType.getType()));
        arrayTypeNode.setGenericType(new JVMArrayType(innerType.getGenericType().deepGenericCopy()));
    }

    @Override
    public void visitArrayOrMapGetExpr(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr,
                                       @NotNull CompilationContext compilationContext) {
        arrayOrMapGetExpr.getLeftExpr().accept(this, compilationContext);
        if (arrayOrMapGetExpr.isArraySlice()) {
            Expression parent = arrayOrMapGetExpr.getLeftExpr();
            arrayOrMapGetExpr.setType(parent.getType());
            arrayOrMapGetExpr.setGenericType(parent.getGenericType());
        } else if (arrayOrMapGetExpr.isArrayGet()) {
            Expression parent = arrayOrMapGetExpr.getLeftExpr();
            arrayOrMapGetExpr.setType(parent.getType().getInnerType(0));
            arrayOrMapGetExpr.setGenericType(parent.getGenericType().getInnerType(0));
        } else {
            if (arrayOrMapGetExpr.getLeftExpr() instanceof GetLocalNode) {
                GetLocalNode parent = (GetLocalNode) arrayOrMapGetExpr.getLeftExpr();
                Type genericReturnType = parent.getGenericType();
                if (genericReturnType.isArray()) {
                    arrayOrMapGetExpr.arrayGet = true;
                    arrayOrMapGetExpr.setType(parent.getType().getInnerType(0));
                    arrayOrMapGetExpr.setGenericType(genericReturnType.getInnerType(0).deepGenericCopy());
                }
            } else if (arrayOrMapGetExpr.getLeftExpr() instanceof MapCreateExpr) {
                arrayOrMapGetExpr.setType(JVMObjectType.INSTANCE);
                arrayOrMapGetExpr.setGenericType(arrayOrMapGetExpr.getLeftExpr()
                        .getGenericReturnType().getTypeVariables().get(1).deepGenericCopy());
            } else if (arrayOrMapGetExpr.getLeftExpr() instanceof FunctionCallExpr) {
                FunctionCallExpr parent = (FunctionCallExpr) arrayOrMapGetExpr.getLeftExpr();
                Type genericReturnType = parent.getGenericType();
                if (genericReturnType.isArray()) {
                    final JVMArrayType arrayType = (JVMArrayType) genericReturnType;
                    final Type elementType = arrayType.getElementType();
                    arrayOrMapGetExpr.arrayGet = true;
                    arrayOrMapGetExpr.setType(elementType);
                    arrayOrMapGetExpr.setGenericType(elementType.deepGenericCopy());
                }
            } else {
                throw new SyntaxException(
                        "Unexpected expression",
                        arrayOrMapGetExpr,
                        compilationContext.getCurrentModule()
                );
            }
        }
    }

    @Override
    public void visitArrayOrMapSetExpr(@NotNull ArrayOrMapSetExpr arrayOrMapSetExpr,
                                       @NotNull CompilationContext compilationContext) {
        super.visitArrayOrMapSetExpr(arrayOrMapSetExpr, compilationContext);
        var genericType = arrayOrMapSetExpr.getRef().getGenericType();
        if (genericType.isArray()) {
            arrayOrMapSetExpr.arraySet = true;
        }
    }

    @Override
    public void visit(@NotNull AsExpr asExpr, @NotNull CompilationContext compilationContext) {
        super.visit(asExpr, compilationContext);
        AsOperatorTypeResolver.INSTANCE.resolve(asExpr, compilationContext);
        asExpr.setType(asExpr.getReturnType());
        asExpr.setGenericType(asExpr.getGenericReturnType().deepGenericCopy());
    }

    @Override
    public void visit(@NotNull XorExpr xorExpr, @NotNull CompilationContext compilationContext) {
        super.visit(xorExpr, compilationContext);
        var t = BitXorTypeResolver.INSTANCE.resolve(xorExpr, compilationContext);
        xorExpr.setType(t);
        xorExpr.setGenericType(t.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull GetFieldNode getFieldNode, @NotNull CompilationContext compilationContext) {
        var genericReturnType = getFieldNode.getCallingReference().getGenericReturnType();
        var fieldByName = genericReturnType.findClassData(compilationContext)
                .findFieldByName(getFieldNode.getName());
        if (fieldByName.isEmpty()) {
            throw new CompileException("Symbol not found", getFieldNode, compilationContext.getCurrentModule());
        }
        getFieldNode.setType(fieldByName.get(0).getType());
        getFieldNode.setGenericType(fieldByName.get(0).getGenericType());
        getFieldNode.setRef(fieldByName.get(0));
        super.visit(getFieldNode, compilationContext);
    }

    @Override
    public void visitFunctionReference(@NotNull FunctionReferenceNode functionReferenceNode,
                                       @NotNull CompilationContext compilationContext) {
        super.visitFunctionReference(functionReferenceNode, compilationContext);
    }

    @Override
    public void visitMapCreateExpr(@NotNull MapCreateExpr mapCreateExpr,
                                   @NotNull CompilationContext compilationContext) {
        super.visitMapCreateExpr(mapCreateExpr, compilationContext);
        mapCreateExpr.setType(Type.of(Map.class));
        final Type of = Type.of(Map.class);
        if (!mapCreateExpr.getEntries().isEmpty()) {
            //TODO union type
            final KeyValueExpr keyValueExpr = mapCreateExpr.getEntries().get(0);
            final Type keyType = keyValueExpr.getKey().getGenericReturnType().getWrapperType();
            final Type valueType = keyValueExpr.getValue().getGenericReturnType().getWrapperType();
            of.setTypeVariable(of.getTypeParameters().get(0), keyType);
            of.setTypeVariable(of.getTypeParameters().get(1), valueType);
        }
        mapCreateExpr.setGenericType(of);
    }

    @Override
    public void visitForEachLoop(@NotNull ForEachLoop forEachloop,
                                 @NotNull CompilationContext compilationContext) {
        forEachloop.getIterExpression().accept(this, compilationContext);
        TypeChecker.INSTANCE.visitForEachLoop(forEachloop, compilationContext);
        forEachloop.getVarExpr().accept(this, compilationContext);
        forEachloop.getBlock().accept(this, compilationContext);
    }

    @Override
    public void visitDefaultValueExpr(@NotNull DefaultValueExpr defaultValueExpr,
                                      @NotNull CompilationContext compilationContext) {
        defaultValueExpr.getLeft().accept(this, compilationContext);
        defaultValueExpr.getRight().accept(this, compilationContext);
        final Type returnType = defaultValueExpr.getRight().getReturnType();
        final Type genericReturnType = defaultValueExpr.getRight().getGenericReturnType();
        defaultValueExpr.setType(returnType);
        defaultValueExpr.setGenericType(genericReturnType.deepGenericCopy());
    }

    @Override
    public void visitLambdaDeclarationExpression(@NotNull LambdaDeclarationExpression lambdaDeclarationExpression,
                                                 @NotNull CompilationContext compilationContext) { }
}
