package org.jelik.compiler.asm.visitor;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.compiler.asm.ClassWriterAdapter;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.compiler.asm.helpers.InvokeDynamicHelper;
import org.jelik.compiler.asm.utils.ASMUtils;
import org.jelik.compiler.asm.utils.ByteCodeLogger;
import org.jelik.compiler.data.JelikBuiltinFunction;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.utils.Stateful;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.ConsumingExpression;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.GetFieldNode;
import org.jelik.parser.ast.KeyValueExpr;
import org.jelik.parser.ast.MapCreateExpr;
import org.jelik.parser.ast.NullExpr;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapSetExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateExpr;
import org.jelik.parser.ast.branching.IfExpression;
import org.jelik.parser.ast.casts.CastObjectToObjectNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.functions.FunctionBodyBlock;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.functions.FunctionReferenceNode;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.StoreLocalNode;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.locals.VariableDeclaration;
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
import org.jelik.parser.ast.types.AbstractTypeRef;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMObjectType;
import org.jelik.types.JVMStringType;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jelik.types.jvm.IntegerWrapperType;
import org.jelik.types.jvm.JVMLongType;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Bukowiecki
 */
@Stateful
public class ToByteCodeVisitor extends AstVisitor {

    private ClassWriterAdapter classWriterAdapter;

    @Getter
    private final List<ToByteCodeResult> toByteCodeResult = new ArrayList<>();

    @Override
    public void visitModuleDeclaration(@NotNull ModuleDeclaration moduleDeclaration, @NotNull CompilationContext compilationContext) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classWriter.visit(Opcodes.V11,
                Opcodes.ACC_PUBLIC,
                moduleDeclaration.getCanonicalName(),
                "",
                "java/lang/Object",
                null);
        this.classWriterAdapter = new ClassWriterAdapter(classWriter);
        visitClassDeclaration(moduleDeclaration, compilationContext);

        for (ClassDeclaration aClass : moduleDeclaration.getClasses()) {
            classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classWriter.visit(Opcodes.V11,
                    Opcodes.ACC_PUBLIC,
                    aClass.getCanonicalName(),
                    "",
                    "java/lang/Object",
                    null);
            this.classWriterAdapter = new ClassWriterAdapter(classWriter);
            aClass.visit(this, compilationContext);
        }
    }

    @Override
    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(classDeclaration);

        classWriterAdapter.visitStart();

        var constructor = new MethodVisitorAdapter(classWriterAdapter.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                "",
                null));
        Label start = constructor.visitLabel();
        constructor.aload(0);
        constructor.invokeSpecial("java/lang/Object", "<init>", "()V");
        constructor.visitReturn();
        Label end = constructor.visitLabel();
        constructor.visitLocalVariable("this", "LTest;", "", start, end, 0);
        constructor.visitMaxes(1, 1);
        constructor.visitEnd();


        for (FunctionDeclaration functionDeclaration : classDeclaration.getFunctionDeclarations()) {
            functionDeclaration.visit(this, compilationContext);
        }
        classWriterAdapter.visitEnd();

        
        byte[] bytes = classWriterAdapter.getBytes();
        ByteCodeLogger.logASM(bytes);

        this.toByteCodeResult.add(new ToByteCodeResult(classDeclaration, bytes));

        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitValueDeclaration(@NotNull ValueDeclaration valueDeclaration,
                                      @NotNull CompilationContext compilationContext) {
        valueDeclaration.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
        StoreLocalByteCodeVisitor storeLocalByteCodeVisitor = new StoreLocalByteCodeVisitor(valueDeclaration,
                this.classWriterAdapter.getCurrentMethodVisitor());
        valueDeclaration.getLocalVariable().getGenericType().visit(storeLocalByteCodeVisitor, compilationContext);
    }

    @Override
    public void visitVariableDeclaration(@NotNull VariableDeclaration variableDeclaration,
                                         @NotNull CompilationContext compilationContext) {
        variableDeclaration.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
        StoreLocalByteCodeVisitor storeLocalByteCodeVisitor = new StoreLocalByteCodeVisitor(variableDeclaration,
                this.classWriterAdapter.getCurrentMethodVisitor());
        variableDeclaration.getLocalVariable().getGenericType().visit(storeLocalByteCodeVisitor, compilationContext);
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);

        functionDeclaration.getFunctionContext().shiftIndexesAccordingToType();

        var mv = new MethodVisitorAdapter(classWriterAdapter.visitMethod(
                Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC,
                functionDeclaration.getFunctionName(),
                functionDeclaration.getDescriptor(),
                functionDeclaration.getSignature(),
                functionDeclaration.getExceptions()));

        mv.setupInitialLocals(functionDeclaration.getFunctionContext().getLocalsAsParameters());

        classWriterAdapter.setCurrentMethodVisitor(mv);

        mv.visitCode();

        mv.visitTryCatchBlock(functionDeclaration.getFunctionContext().getTryExpressionList());
        functionDeclaration.getFunctionBody().visit(this, compilationContext);

        mv.visitMax();

        List<LocalVariable> localVariables = functionDeclaration.getLocalVariables();
        mv.visitLocals(localVariables);

        mv.visitEnd();

        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(@NotNull FunctionBodyBlock fb, @NotNull CompilationContext compilationContext) {
        compilationContext.blockStack.addLast(fb.getBb());
        for (Expression expression : fb.getBb().getExpressions()) {
            classWriterAdapter.getCurrentMethodVisitor().visitLineNumber(expression.getStartRow());
            expression.visit(this, compilationContext);
            if (expression instanceof StackConsumer) {
                continue;
            }
            if (!expression.getReturnType().isVoid()) {
                if (expression.getReturnType().isLong() || expression.getReturnType().isDouble()) {
                    classWriterAdapter.getCurrentMethodVisitor().visitPop2();
                } else {
                    classWriterAdapter.getCurrentMethodVisitor().visitPop();
                }
            }
        }
        compilationContext.blockStack.removeLast();
    }

    @Override
    public void visit(@NotNull BasicBlock basicBlock, @NotNull  CompilationContext compilationContext) {
        MethodVisitorAdapter mv = classWriterAdapter.getCurrentMethodVisitor();
        for (Expression expression : basicBlock.getExpressions()) {
            mv.visitLineNumber(expression.getStartRow());
            expression.visit(this, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull AssignExpr assignExpr, @NotNull CompilationContext compilationContext) {
        if (assignExpr.getLeft() instanceof StoreLocalNode) {
            assignExpr.getRight().visit(this, compilationContext);
            if (assignExpr.parent instanceof ConsumingExpression) {
                classWriterAdapter.getCurrentMethodVisitor().dup();
            }
            assignExpr.getLeft().getType().visit(new StoreLocalByteCodeVisitor((StoreLocalNode) assignExpr.getLeft(),
                    classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
            ((StoreLocalNode) assignExpr.getLeft()).getLocalVariable().setEnd(compilationContext.labelStack.getLast());
        }
    }

    @Override
    public void visit(@NotNull GetLocalNode getLocalNode, @NotNull CompilationContext compilationContext) {
        AbstractTypeRef typeRef = getLocalNode.getLocalVariable().getTypeRef();
        Type genericType = typeRef.getGenericType();
        genericType.visit(new ByteCodeGetLocalVariableVisitor(getLocalNode, classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
        getLocalNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visitReturnExpr(@NotNull ReturnExpr returnExpr, @NotNull CompilationContext compilationContext) {
        returnExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
        if (returnExpr.parent.parent instanceof TryExpression) {
            return;
        }
        Type genericType = ((FunctionDeclaration) compilationContext.currentCompilationUnit()).getFunctionReturn().map(rt -> rt.getTypeNode().getType()).orElse(JVMVoidType.INSTANCE);
        genericType.visit(new ByteCodeReturnInstructionVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull AddExpr addExpr, @NotNull CompilationContext compilationContext) {
        addExpr.getLeft().visit(this, compilationContext);
        addExpr.getRight().visit(this, compilationContext);
        var genericType = addExpr.getGenericType();
        genericType.visit(new ByteCodeAddOpVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull SubExpr subExpr, @NotNull CompilationContext compilationContext) {
        subExpr.getLeft().visit(this, compilationContext);
        subExpr.getRight().visit(this, compilationContext);
        var genericType = subExpr.getGenericType();
        genericType.visit(new ByteCodeSubOpVisitor(subExpr, classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull DivExpr divExpr, @NotNull CompilationContext compilationContext) {
        divExpr.getLeft().visit(this, compilationContext);
        divExpr.getRight().visit(this, compilationContext);
        var genericType = divExpr.getGenericType();
        genericType.visit(new ByteCodeDivOpVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull MulExpr mulExpr, @NotNull CompilationContext compilationContext) {
        mulExpr.getLeft().visit(this, compilationContext);
        mulExpr.getRight().visit(this, compilationContext);
        var genericType = mulExpr.getGenericType();
        genericType.visit(new ByteCodeMulOpVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull IfExpression ifExpression, @NotNull CompilationContext compilationContext) {
        ifExpression.getConditionExpression().visit(this, compilationContext);
        ifExpression.getBasicBlock().visit(this, compilationContext);
        FalseLabelExtractor falseLabelExtractor = new FalseLabelExtractor();
        ifExpression.getConditionExpression().visit(falseLabelExtractor, compilationContext);
        classWriterAdapter.getCurrentMethodVisitor().visitLabel(falseLabelExtractor.result.getLabel());
        ifExpression.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull StringExpression stringExpression, @NotNull CompilationContext compilationContext) {
        classWriterAdapter
                .getCurrentMethodVisitor()
                .visitConstant(JVMStringType.INSTANCE, stringExpression.getString());
        stringExpression.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull StringBuilderInit stringBuilderInit, @NotNull CompilationContext compilationContext) {
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        Type type = stringBuilderInit.getType();
        currentMethodVisitor.callNew(type);
        currentMethodVisitor.dup();
        currentMethodVisitor.invokeSpecial(type.getInternalName(), "<init>", "()V");
        stringBuilderInit.getFurtherExpression().visit(this, compilationContext);
    }

    @Override
    public void visit(@NotNull StringBuilderAppend stringBuilderAppend, @NotNull CompilationContext compilationContext) {
        stringBuilderAppend.getSubject().visit(this, compilationContext);

        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        var type = stringBuilderAppend.getType();
        var targetFunctionCall = stringBuilderAppend.getTargetFunctionCall();
        currentMethodVisitor.invokeInstance(type.getInternalName(),
                "append",
                targetFunctionCall.getMethodData().getDescriptor(),
                stringBuilderAppend.getType(),
                stringBuilderAppend.getArgumentTypes(), false);

        stringBuilderAppend.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull Int32ToWrapperNode int32ToWrapperNode, @NotNull CompilationContext compilationContext) {
        int32ToWrapperNode.getSubject().visit(this, compilationContext);
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.invokeStatic(int32ToWrapperNode.getType().getInternalName(),
                "valueOf", "(I)Ljava/lang/Integer;",
                IntegerWrapperType.INSTANCE, Collections.singletonList(JVMIntType.INSTANCE), false);
        int32ToWrapperNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        var targetFunctionCall = functionCallExpr.getTargetFunctionCall();
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();

        if (targetFunctionCall.getMethodData().isBuiltin()) {
            for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
                argument.visit(this, compilationContext);
            }
            ((JelikBuiltinFunction) targetFunctionCall.getMethodData()).getByteCodeData().accept(currentMethodVisitor);
        } else {
            targetFunctionCall.getCodeGenProvider().invoke(currentMethodVisitor, this, functionCallExpr, compilationContext);
        }
        functionCallExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull Int32Node int32Node, @NotNull CompilationContext compilationContext) {
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.pushInt(int32Node.getValue());
    }

    @Override
    public void visit(@NotNull Float32Node float32Node, @NotNull CompilationContext compilationContext) {
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.visitConstant(float32Node.getType(), float32Node.getValue());
    }

    @Override
    public void visit(@NotNull AsExpr asExpr, @NotNull CompilationContext compilationContext) {
        asExpr.getLeft().visit(this, compilationContext);
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.checkCast(asExpr.getRight().getGenericType());
    }

    @Override
    public void visit(@NotNull ObjectToInt32Node objectToInt32Node, @NotNull CompilationContext compilationContext) {
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        objectToInt32Node.getSubject().visit(this, compilationContext);
        currentMethodVisitor.checkCast(IntegerWrapperType.INSTANCE);
        currentMethodVisitor.invokeInstance(IntegerWrapperType.INSTANCE.getInternalName(),
                "intValue", "()I",
                JVMIntType.INSTANCE,
                Collections.emptyList(), false);
        objectToInt32Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull CastObjectToObjectNode castObjectToObjectNode, @NotNull CompilationContext compilationContext) {
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        castObjectToObjectNode.getSubject().visit(this, compilationContext);
        currentMethodVisitor.checkCast(castObjectToObjectNode.getTo());
        castObjectToObjectNode.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull IncrExpr incrExpr, @NotNull CompilationContext compilationContext) {
        if (incrExpr.getRight() instanceof GetLocalNode && incrExpr.getRight().getGenericReturnType() instanceof JVMIntType) {
            incrExpr.getRight().getGenericType().visit(new IncrByteCodeVisitor(incrExpr, incrExpr.getRight(), classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
        } else {
            incrExpr.getRight().visit(this, compilationContext);
            incrExpr.getRight().getGenericType().visit(new IncrByteCodeVisitor(incrExpr, incrExpr.getRight(), classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
        }
    }

    @Override
    public void visit(@NotNull DecrExpr decrExpr, @NotNull CompilationContext compilationContext) {
        new DecrByteCodeVisitor(decrExpr, classWriterAdapter.getCurrentMethodVisitor()).visit(decrExpr.getGenericType(), compilationContext);
    }

    @Override
    public void visit(@NotNull IntegerWrapperToInt32Node integerWrapperToInt32Node, @NotNull CompilationContext compilationContext) {
        super.visit(integerWrapperToInt32Node, compilationContext);
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.invokeInstance("java/lang/Integer", "intValue", "()I", JVMIntType.INSTANCE, Collections.emptyList(), false);
    }

    @Override
    public void visit(@NotNull StringBuilderToStringNode stringBuilderToStringNode, @NotNull CompilationContext compilationContext) {
        stringBuilderToStringNode.getSubject().visit(this, compilationContext);
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.invokeInstance("java/lang/StringBuilder", "toString", "()Ljava/lang/String;", JVMStringType.INSTANCE, Collections.emptyList(), false);
    }

    @Override
    public void visit(@NotNull NullExpr nullExpr, @NotNull CompilationContext compilationContext) {
        if (nullExpr.getIgnore()) {
            return;
        }
        classWriterAdapter.getCurrentMethodVisitor().visitNull();
    }

    @Override
    public void visit(@NotNull ArrayCreateExpr arrayCreateExpr, @NotNull CompilationContext compilationContext) {
        Type type = arrayCreateExpr.getType();
        int size = arrayCreateExpr.getExpressions().size();
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.pushInt(size);
        type.getInnerType(0).visit(new ByteCodeArrayCreateVisitor(currentMethodVisitor), compilationContext);

        int index = 0;
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            currentMethodVisitor.dup();
            currentMethodVisitor.pushInt(index);
            expression.visit(this, compilationContext);
            type.getInnerType(0).visit(new ByteCodeArrayStoreVisitor(currentMethodVisitor), compilationContext);
            index++;
        }

        arrayCreateExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull TypedArrayCreateExpr arrayCreateExpr, @NotNull CompilationContext compilationContext) {
        Type type = arrayCreateExpr.getType();
        int size = arrayCreateExpr.getExpressions().size();
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.pushInt(size);
        type.getInnerType(0).visit(new ByteCodeArrayCreateVisitor(currentMethodVisitor), compilationContext);

        int index = 0;
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            currentMethodVisitor.dup();
            currentMethodVisitor.pushInt(index);
            expression.visit(this, compilationContext);
            type.getInnerType(0).visit(new ByteCodeArrayStoreVisitor(currentMethodVisitor), compilationContext);
            index++;
        }

        arrayCreateExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr, @NotNull CompilationContext compilationContext) {
        if (arrayOrMapGetExpr.isArrayGet()) {
            arrayOrMapGetExpr.getLeftExpr().visit(this, compilationContext);
            Type type = arrayOrMapGetExpr.getType();
            arrayOrMapGetExpr.getExpression().visit(this, compilationContext);
            type.visit(new ByteCodeArrayGetVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
        } else {
            arrayOrMapGetExpr.getLeftExpr().visit(this, compilationContext);
            arrayOrMapGetExpr.getExpression().visit(this, compilationContext);
            var mv = classWriterAdapter.getCurrentMethodVisitor();
            mv.invokeInterface(Type.of(Map.class).getInternalName(),
                    "get",
                    "(Ljava/lang/Object;)Ljava/lang/Object;",
                    JVMObjectType.INSTANCE,
                    Collections.singletonList(JVMObjectType.INSTANCE));
        }
        arrayOrMapGetExpr.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull EqualExpr equalExpr, @NotNull CompilationContext compilationContext) {
        this.visitAbstractLogicalOpExpr(equalExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull GreaterExpr greaterExpr, @NotNull CompilationContext compilationContext) {
        this.visitAbstractLogicalOpExpr(greaterExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull GreaterOrEqualExpr greaterOrEqualExpr, @NotNull CompilationContext compilationContext) {
        this.visitAbstractLogicalOpExpr(greaterOrEqualExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull LesserOrEqualExpr lesserOrEqualExpr, @NotNull CompilationContext compilationContext) {
        this.visitAbstractLogicalOpExpr(lesserOrEqualExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull LesserExpr lesserExpr, @NotNull CompilationContext compilationContext) {
        this.visitAbstractLogicalOpExpr(lesserExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull NotEqualExpr notEqualExpr, @NotNull CompilationContext compilationContext) {
        this.visitAbstractLogicalOpExpr(notEqualExpr, compilationContext);
    }

    @Override
    public void visit(@NotNull OrExpr orExpr, @NotNull CompilationContext compilationContext) {
        super.visit(orExpr, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (orExpr.parent instanceof ConsumingExpression) {
            ASMUtils.visitSingleExpr(mv, orExpr, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull AndExpr andExpr, @NotNull CompilationContext compilationContext) {
        super.visit(andExpr, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (andExpr.parent instanceof ConsumingExpression) {
            ASMUtils.visitSingleExpr(mv, andExpr, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull TryExpression tryExpression, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitLabel(tryExpression.getStartLabel().getLabel());
        tryExpression.getBlock().visit(this, compilationContext);
        mv.visitLabel(tryExpression.getEndLabel().getLabel());

        final CatchExpression catchExpression = ((CatchExpression) tryExpression.getFurtherExpression());
        mv.visitJumpInstruction(Opcodes.GOTO, catchExpression.getEndLabel());

        mv.visitLabel(catchExpression.getStartLabel().getLabel());
        if (!catchExpression.getBlock().getExpressions().isEmpty()) {
            if (catchExpression.getInnerLabel() == null) {
                throw new IllegalArgumentException("Inner label not set");
            }
            mv.visitLabel(catchExpression.getInnerLabel().getLabel());
        }
        tryExpression.getFurtherExpression().visit(this, compilationContext);
        mv.visitLabel(catchExpression.getEndLabel().getLabel());
    }

    @Override
    public void visit(@NotNull CatchExpression catchExpression, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        //mv.visitLabel(catchExpression.getBlock().startLabel.getLabel());
         for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            mv.objectStoreIgnoreStack(functionParameter.localVariableRef.index);
        }
        compilationContext.blockStack.addLast(catchExpression.getBlock());
        for (Expression expression : catchExpression.getBlock().getExpressions()) {
            expression.visit(this, compilationContext);
        }
        compilationContext.blockStack.removeLast();
        //mv.visitLabel(catchExpression.getBlock().endLabel.getLabel());
    }

    @Override
    public void visit(@NotNull Float32ToInt64Node float32ToInt64Node, @NotNull CompilationContext compilationContext) {
        float32ToInt64Node.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitFloatToLong();
    }

    @Override
    public void visit(@NotNull Float32ToInt32Node float32ToInt32Node, @NotNull CompilationContext compilationContext) {
        float32ToInt32Node.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitFloatToInt();
    }

    @Override
    public void visit(@NotNull Int32ToFloat64Node int32ToFloat64Node, @NotNull CompilationContext compilationContext) {
        int32ToFloat64Node.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToDouble();
    }

    @Override
    public void visit(@NotNull Int32ToInt16Node int32ToInt16Node, @NotNull CompilationContext compilationContext) {
        int32ToInt16Node.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToShort();
    }

    @Override
    public void visit(@NotNull Int32ToFloat32Node int32ToFloat32Node, @NotNull CompilationContext compilationContext) {
        int32ToFloat32Node.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToFloat();
    }

    @Override
    public void visit(@NotNull Float32ToFloat64Node float32ToFloat64Node, @NotNull CompilationContext compilationContext) {
        float32ToFloat64Node.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitFloatToDouble();
    }

    @Override
    public void visit(@NotNull ArrayOrMapSetExpr arrayOrMapSetExpr, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (arrayOrMapSetExpr.isArraySet()) {
            arrayOrMapSetExpr.getRef().visit(this, compilationContext);
            arrayOrMapSetExpr.getIndex().visit(this, compilationContext);
            arrayOrMapSetExpr.getRightExpression().visit(this, compilationContext);
            arrayOrMapSetExpr.getRef().getGenericType().getInnerType(0).visit(new ByteCodeArrayStoreVisitor(mv), compilationContext);
        } else {
            arrayOrMapSetExpr.getRef().visit(this, compilationContext);
            arrayOrMapSetExpr.getIndex().visit(this, compilationContext);
            arrayOrMapSetExpr.getRightExpression().visit(this, compilationContext);
            mv.invokeInterface(Type.of(Map.class).getInternalName(),
                    "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    arrayOrMapSetExpr.getRightExpression().getGenericReturnType(),
                    Arrays.asList(arrayOrMapSetExpr.getIndex().getGenericReturnType(),
                            arrayOrMapSetExpr.getRightExpression().getGenericReturnType()));
        }
    }

    @Override
    public void visit(@NotNull Int32ToInt8Node int32ToInt8Node, @NotNull CompilationContext compilationContext) {
        int32ToInt8Node.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToByte();
    }

    @Override
    public void visit(@NotNull Int32ToCharNode int32ToCharNode, @NotNull CompilationContext compilationContext) {
        int32ToCharNode.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToChar();
    }

    @Override
    public void visit(@NotNull NegExpr negExpr, @NotNull CompilationContext compilationContext) {
        negExpr.getRight().visit(this, compilationContext);
        var genericType = negExpr.getGenericType();
        genericType.visit(new ByteCodeSubOpVisitor(negExpr, classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull Int32ToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToLong();
    }

    @Override
    public void visit(@NotNull CharToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.charToLong();
    }

    @Override
    public void visit(@NotNull Float64ToFloat32 castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.doubleToFloat();
    }

    @Override
    public void visit(@NotNull Float64ToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.doubleToLong();
    }

    @Override
    public void visit(@NotNull Float64ToInt32Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getSubject().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.doubleToInt();
    }

    @Override
    public void visit(@NotNull RemExpr remExpr, @NotNull CompilationContext compilationContext) {
        remExpr.getLeft().visit(this, compilationContext);
        remExpr.getRight().visit(this, compilationContext);
        remExpr.getGenericType().visit(new ByteCodeRemOpVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull Int64Node int64Node, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitConstant(JVMLongType.INSTANCE, int64Node.getValue());
        int64Node.getFurtherExpressionOpt().ifPresent(expr -> expr.visit(this, compilationContext));
    }

    @Override
    public void visit(@NotNull GetFieldNode getFieldNode, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (getFieldNode.getRef().isStatic()) {
            mv.getStatic(getFieldNode.getRef().getOwner().getType(), getFieldNode.getName(), getFieldNode.getType());
        } else {
            mv.getField(getFieldNode.getRef().getOwner().getType(), getFieldNode.getName(), getFieldNode.getType());
        }
        super.visit(getFieldNode, compilationContext);
    }

    @Override
    public void visit(@NotNull BitAndExpr bitAndExpr, @NotNull CompilationContext compilationContext) {
        bitAndExpr.getLeft().visit(this, compilationContext);
        bitAndExpr.getRight().visit(this, compilationContext);
        bitAndExpr.getGenericType().visit(new ByteCodeBitAndVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull BitOrExpr bitOrExpr, @NotNull CompilationContext compilationContext) {
        bitOrExpr.getLeft().visit(this, compilationContext);
        bitOrExpr.getRight().visit(this, compilationContext);
        bitOrExpr.getGenericType().visit(new ByteCodeBitOrVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull Int64ToFloat64Node node, @NotNull CompilationContext compilationContext) {
        super.visit(node, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.longToDouble();
    }

    @Override
    public void visit(@NotNull Int64ToFloat32Node node, @NotNull CompilationContext compilationContext) {
        super.visit(node, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.longToFloat();
    }

    @Override
    public void visit(@NotNull Int64ToInt32Node node, @NotNull CompilationContext compilationContext) {
        super.visit(node, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.longToInt();
    }

    @Override
    public void visit(@NotNull TrueNode trueNode, @NotNull CompilationContext compilationContext) {
        if (trueNode.isIgnore()) {
            return;
        }
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.pushInt(1);
    }

    @Override
    public void visit(@NotNull FalseNode falseNode, @NotNull CompilationContext compilationContext) {
        if (falseNode.isIgnore()) {
            return;
        }
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.pushInt(0);
    }

    @Override
    public void visit(@NotNull XorExpr xorExpr, @NotNull CompilationContext compilationContext) {
        super.visit(xorExpr, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        xorExpr.getGenericReturnType().visit(new ByteCodeBitXorVisitor(mv), compilationContext);
    }

    @Override
    public void visitAbstractLogicalOpExpr(@NotNull AbstractLogicalOpExpr expr, @NotNull CompilationContext compilationContext) {
        if (!expr.getLeft().ignored) {
            expr.getLeft().visit(this, compilationContext);
        }
        if (!expr.getRight().ignored) {
            expr.getRight().visit(this, compilationContext);
        }
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (expr.isNegated()) {
            expr.instructionToCall.getNegated().forEach(ins -> mv.visitJumpInstruction(ins, expr.falseLabelNode));
        } else {
            expr.instructionToCall.getValue().forEach(ins -> mv.visitJumpInstruction(ins, expr.trueLabelNode));
        }
        if (expr.parent instanceof ConsumingExpression) {
            ASMUtils.visitSingleExpr(mv, expr, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull IsExpr isExpr, @NotNull CompilationContext compilationContext) {
        isExpr.getLeft().visit(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        isExpr.getRight().getGenericReturnType().visit(new ByteCodeIsOperatorVisitor(mv, isExpr), compilationContext);
    }

    @Override
    public void visitFunctionReference(@NotNull FunctionReferenceNode functionReferenceNode,
                                       @NotNull CompilationContext compilationContext) {
        var possibleFunctionsToCall = functionReferenceNode.getPossibleFunctionsToCall();
        if (possibleFunctionsToCall.size() != 1) {
            throw new CompileException("Could not resolve function reference call",
                    functionReferenceNode,
                    compilationContext.getCurrentModule());
        }
        var targetFunction = possibleFunctionsToCall.get(0);
        InvokeDynamicHelper.INSTANCE.codeGen(classWriterAdapter.getCurrentMethodVisitor(),
                targetFunction,
                functionReferenceNode.lambdaMethod);
        super.visitFunctionReference(functionReferenceNode, compilationContext);
    }

    @Override
    public void visitMapCreateExpr(@NotNull MapCreateExpr mapCreateExpr,
                                   @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        final Type of = Type.of(HashMap.class);
        mv.callNew(of);
        mv.dup();
        mv.invokeSpecial(of.getInternalName(), "<init>", "()V");
        for (KeyValueExpr entry : mapCreateExpr.getEntries()) {
            mv.dup();
            entry.getKey().visit(this, compilationContext);
            entry.getValue().visit(this, compilationContext);
            mv.invokeInterface(Type.of(Map.class).getInternalName(),
                    "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    entry.getValue().getGenericReturnType(),
                    Arrays.asList(entry.getValue().getGenericReturnType(),
                            entry.getValue().getGenericReturnType()));
            mv.visitPop();
        }
    }
}
