package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.GetFieldNode;
import org.jelik.parser.ast.KeyValueExpr;
import org.jelik.parser.ast.MapCreateExpr;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapSetExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateExpr;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.functions.FunctionReferenceNode;
import org.jelik.parser.ast.functions.FunctionReturn;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.locals.VariableDeclaration;
import org.jelik.parser.ast.locals.WithLocalVariableDeclaration;
import org.jelik.parser.ast.numbers.Int32Node;
import org.jelik.parser.ast.operators.AddExpr;
import org.jelik.parser.ast.operators.AsExpr;
import org.jelik.parser.ast.operators.AssignExpr;
import org.jelik.parser.ast.operators.BitAndExpr;
import org.jelik.parser.ast.operators.BitOrExpr;
import org.jelik.parser.ast.operators.DecrExpr;
import org.jelik.parser.ast.operators.DivExpr;
import org.jelik.parser.ast.operators.EqualExpr;
import org.jelik.parser.ast.operators.IncrExpr;
import org.jelik.parser.ast.operators.MulExpr;
import org.jelik.parser.ast.operators.NegExpr;
import org.jelik.parser.ast.operators.RemExpr;
import org.jelik.parser.ast.operators.SubExpr;
import org.jelik.parser.ast.operators.XorExpr;
import org.jelik.parser.ast.types.ArrayTypeNode;
import org.jelik.parser.ast.types.CovariantTypeNode;
import org.jelik.parser.ast.types.GenericTypeNode;
import org.jelik.parser.ast.types.InferredTypeRef;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeNodeRef;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.compiler.exceptions.SyntaxException;
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
import org.jelik.types.resolver.SubOperatorTypeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public class TypeResolver extends AstVisitor {

    @Override
    public void visit(@NotNull GenericTypeNode genericTypeNode,
                      @NotNull CompilationContext compilationContext) {
        genericTypeNode.getSingleTypeNode().visit(this, compilationContext);
        for (TypeNode typeVariable : genericTypeNode.getTypeVariables().getTypes()) {
            typeVariable.visit(this, compilationContext);
        }
    }

    @Override
    public void visitCovariantTypeNode(@NotNull CovariantTypeNode covariantTypeNode,
                                       @NotNull CompilationContext compilationContext) {

        covariantTypeNode.getParentTypeNode().visit(this, compilationContext);
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
    public void visitWithLocalVariable(@NotNull WithLocalVariableDeclaration withLocalVariable,
                                       @NotNull CompilationContext compilationContext) {
        withLocalVariable.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
        withLocalVariable.getTypeNode().visit(this, compilationContext);
        if (withLocalVariable.getTypeNode() == UndefinedTypeNode.UNDEFINED_TYPE_NODE) {
            withLocalVariable.getFurtherExpressionOpt().ifPresent(expr -> {
                final InferredTypeRef inferredTypeRef = new InferredTypeRef(expr);
                withLocalVariable.getLocalVariable().setTypeRef(inferredTypeRef);
            });
        } else {
            withLocalVariable.getLocalVariable().setTypeRef(new TypeNodeRef(withLocalVariable.getTypeNode()));
        }
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration,
                                         @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);
        functionDeclaration.getFunctionBody().visit(this, compilationContext);
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(@NotNull FunctionBodyBlock fb, @NotNull CompilationContext compilationContext) {
        compilationContext.blockStack.addLast(fb.getBb());
        for (Expression expression : fb.getBb().getExpressions()) {
            expression.visit(this, compilationContext);
        }
        compilationContext.blockStack.removeLast();
        /*
        if (fb.getBb().getExpressions().isEmpty()) {
            fb.getBb().setType(JVMVoidType.INSTANCE);
            fb.setType(JVMVoidType.INSTANCE);
        } else {
            int size = fb.getBb().getExpressions().size();
            Expression expression = fb.getBb().getExpressions().get(size - 1);
            Type returnType = expression.getType();
            fb.getBb().setType(returnType);
            fb.setType(returnType);
        }

         */
    }

    @Override
    public void visitReturnExpr(@NotNull ReturnExpr re, @NotNull CompilationContext compilationContext) {
        re.getFurtherExpressionOpt().ifPresent(expression -> {
            expression.visit(this, compilationContext);
            re.setType(expression.getReturnType());
            re.setGenericType(expression.getGenericReturnType().deepGenericCopy());
        });
    }

    /*
            Operators
     */
    @Override
    public void visit(@NotNull DecrExpr decrExpr, @NotNull CompilationContext compilationContext) {
        decrExpr.getFurtherExpression().visit(this, compilationContext);
        Type genericReturnType = decrExpr.getGenericReturnType();
        if (genericReturnType.isWrapper()) {

        } else {

        }
    }

    @Override
    public void visit(@NotNull IncrExpr incrExpr, @NotNull CompilationContext compilationContext) {
        incrExpr.getRight().visit(this, compilationContext);
        var rt = incrExpr.getRight().getGenericReturnType();
        incrExpr.setType(incrExpr.getRight().getType());
        incrExpr.setGenericType(rt.deepGenericCopy());
    }
    @Override
    public void visit(EqualExpr equalExpr, CompilationContext compilationContext) {
        equalExpr.getLeft().visit(this, compilationContext);
        equalExpr.getRight().visit(this, compilationContext);
    }

    @Override
    public void visit(@NotNull AddExpr addExpr, @NotNull CompilationContext compilationContext) {
        addExpr.getLeft().visit(this, compilationContext);
        addExpr.getRight().visit(this, compilationContext);
        var rt = AddOperatorTypeResolver.resolve(addExpr, compilationContext);
        addExpr.setType(rt);
        addExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull BitAndExpr expr, @NotNull CompilationContext compilationContext) {
        expr.getLeft().visit(this, compilationContext);
        expr.getRight().visit(this, compilationContext);
        var rt = new BitAndTypeResolver().resolve(expr, compilationContext);
        expr.setType(rt);
        expr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull BitOrExpr expr, @NotNull CompilationContext compilationContext) {
        expr.getLeft().visit(this, compilationContext);
        expr.getRight().visit(this, compilationContext);
        var rt = new BitOrTypeResolver().resolve(expr, compilationContext);
        expr.setType(rt);
        expr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull RemExpr remExpr, @NotNull CompilationContext compilationContext) {
        remExpr.getLeft().visit(this, compilationContext);
        remExpr.getRight().visit(this, compilationContext);
        var rt = new RemTypeResolver().resolve(remExpr, compilationContext);
        remExpr.setType(rt);
        remExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull SubExpr subExpr, @NotNull CompilationContext compilationContext) {
        subExpr.getLeft().visit(this, compilationContext);
        subExpr.getRight().visit(this, compilationContext);
        var rt = new SubOperatorTypeResolver().resolve(subExpr, compilationContext);
        subExpr.setType(rt);
        subExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull NegExpr negExpr, @NotNull CompilationContext compilationContext) {
        negExpr.getRight().visit(this, compilationContext);
        var rt = new SubOperatorTypeResolver().resolve(negExpr, compilationContext);
        negExpr.setType(rt);
        negExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull MulExpr mulExpr, @NotNull CompilationContext compilationContext) {
        mulExpr.getLeft().visit(this, compilationContext);
        mulExpr.getRight().visit(this, compilationContext);
        var rt = new MulTypeResolver().resolve(mulExpr, compilationContext);
        mulExpr.setType(rt);
        mulExpr.setGenericType(rt.deepGenericCopy());
    }

    @Override
    public void visit(@NotNull DivExpr divExpr, @NotNull CompilationContext compilationContext) {
        divExpr.getLeft().visit(this, compilationContext);
        divExpr.getRight().visit(this, compilationContext);
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
        fr.getTypeNode().visit(this, compilationContext);
    }

    @Override
    public void visit(@NotNull FunctionParameter fp, @NotNull CompilationContext compilationContext) {
        fp.getTypeNode().visit(this, compilationContext);
    }

    @Override
    public void visitSingleTypeNode(@NotNull SingleTypeNode typeNode, @NotNull CompilationContext compilationContext) {
        BuiltinTypeResolver builtinTypeResolver = new BuiltinTypeResolver();
        builtinTypeResolver.visitSingleTypeNode(typeNode, compilationContext);
        if (builtinTypeResolver.getTypeOpt().isEmpty()) {
            DefaultImportedTypeResolver defaultImportedTypeResolver = new DefaultImportedTypeResolver();
            defaultImportedTypeResolver.visitSingleTypeNode(typeNode, compilationContext);
            if (defaultImportedTypeResolver.getTypeOpt().isEmpty()) {
                compilationContext.currentCompilationUnit().findType(typeNode, compilationContext).ifPresentOrElse(t -> {
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
    public void visit(@NotNull GetLocalNode getLocalNode, @NotNull CompilationContext compilationContext) {
        getLocalNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull Argument argument, @NotNull CompilationContext compilationContext) {
        argument.getExpression().visit(this, compilationContext);
    }

    @Override
    public void visit(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        functionCallExpr
                .getLiteralExpr()
                .getTypeParameterListNode()
                .getTypes()
                .forEach(t -> t.visit(this, compilationContext));

        for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
            argument.visit(this, compilationContext);
        }
        if (functionCallExpr.getParent() instanceof DotCallExpr) {
            Type type = ((DotCallExpr) functionCallExpr.getParent()).getSubject().getGenericType();
            functionCallExpr.setOwner(type);
        }

        var targetFunctionCall = new FunctionCallResolver()
                .resolveCall(functionCallExpr, compilationContext)
                .orElseThrow(() -> new CompileException("Could not find method", functionCallExpr, compilationContext.getCurrentModule()));

        targetFunctionCall.resolveCallback(functionCallExpr, compilationContext);

        if (functionCallExpr.getOwner() == null) {
            functionCallExpr.setOwner(targetFunctionCall.getMethodData().getOwner());
        }
        functionCallExpr.setTargetFunctionCall(targetFunctionCall);

        Type returnType = targetFunctionCall.getMethodData().getReturnType();
        Type genericReturnType = targetFunctionCall.getMethodData().getGenericReturnType();
        functionCallExpr.setType(returnType.deepGenericCopy());
        functionCallExpr.setGenericType(genericReturnType);

        functionCallExpr
                .getLiteralExpr()
                .getTypeParameterListNode()
                .getTypes()
                .forEach(t -> t.visit(this, compilationContext));

        new GenericTypeResolver().visit(functionCallExpr, compilationContext);

        functionCallExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull Int32Node int32Node, @NotNull CompilationContext compilationContext) {
        int32Node.setType(JVMIntType.INSTANCE);
        int32Node.setGenericType(JVMIntType.INSTANCE);
    }

    @Override
    public void visit(@NotNull ArrayCreateExpr arrayCreateExpr, @NotNull CompilationContext compilationContext) {
        arrayCreateExpr.getExpressions().forEach(expr -> expr.visit(this, compilationContext));
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
        arrayCreateExpr.getNodeContext().setType(new JVMArrayType(Objects.requireNonNull(arrayType)));
        arrayCreateExpr.getNodeContext().setGenericType(new JVMArrayType(Objects.requireNonNull(genericArrayType).deepGenericCopy()));
        arrayCreateExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull TypedArrayCreateExpr arrayCreateExpr, @NotNull CompilationContext compilationContext) {
        arrayCreateExpr.getExpressions().forEach(expr -> expr.visit(this, compilationContext));
        arrayCreateExpr.getTypeNode().visit(this, compilationContext);
        arrayCreateExpr.getNodeContext().setType(new JVMArrayType(Objects.requireNonNull(arrayCreateExpr.getTypeNode().getType())));
        arrayCreateExpr.getNodeContext().setGenericType(new JVMArrayType(Objects.requireNonNull(arrayCreateExpr.getTypeNode().getGenericType()).deepGenericCopy()));
        arrayCreateExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull ArrayTypeNode arrayTypeNode, @NotNull CompilationContext compilationContext) {
        TypeNode innerType = arrayTypeNode.getInnerType();
        innerType.visit(this, compilationContext);
        arrayTypeNode.setType(new JVMArrayType(innerType.getType()));
        arrayTypeNode.setGenericType(new JVMArrayType(innerType.getGenericType().deepGenericCopy()));
    }

    @Override
    public void visit(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr, @NotNull CompilationContext compilationContext) {
        arrayOrMapGetExpr.getLeftExpr().visit(this, compilationContext);
        if (arrayOrMapGetExpr.isArrayGet()) {
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
            } else {
                throw new SyntaxException("Unexpected expression", arrayOrMapGetExpr, compilationContext.getCurrentModule());
            }
        }
        arrayOrMapGetExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull ArrayOrMapSetExpr arrayOrMapSetExpr, @NotNull CompilationContext compilationContext) {
        super.visit(arrayOrMapSetExpr, compilationContext);
        var genericType = arrayOrMapSetExpr.getRef().getGenericType();
        if (genericType.isArray()) {
            arrayOrMapSetExpr.arraySet = true;
        }
    }

    @Override
    public void visit(@NotNull AsExpr asExpr, @NotNull CompilationContext compilationContext) {
        super.visit(asExpr, compilationContext);
        AsOperatorTypeResolver.INSTANCE.resolve(asExpr, compilationContext);
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
        var genericReturnType = ((DotCallExpr) getFieldNode.getParent()).getSubject().getGenericType();
        var fieldByName = genericReturnType.findClassData(compilationContext).findFieldByName(getFieldNode.getName());
        if (fieldByName.isEmpty()) {
            throw new CompileException("Symbol not found", getFieldNode, compilationContext.getCurrentModule());
        }
        getFieldNode.setType(fieldByName.get(0).getType());
        getFieldNode.setGenericType(fieldByName.get(0).getGenericType());
        getFieldNode.setRef(fieldByName.get(0));
        super.visit(getFieldNode, compilationContext);
    }

    @Override
    public void visitFunctionReference(@NotNull FunctionReferenceNode functionReferenceNode, @NotNull CompilationContext compilationContext) {
        super.visitFunctionReference(functionReferenceNode, compilationContext);
    }

    @Override
    public void visitMapCreateExpr(@NotNull MapCreateExpr mapCreateExpr, @NotNull CompilationContext compilationContext) {
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
}
