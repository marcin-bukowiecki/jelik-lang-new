/*
 * Copyright 2019 Marcin Bukowiecki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.asm.ClassWriterAdapter;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.compiler.asm.helpers.InvokeDynamicHelper;
import org.jelik.compiler.asm.utils.ASMUtils;
import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.passes.CodeBranchChecker;
import org.jelik.compiler.passes.ReturnStmtInserter;
import org.jelik.compiler.utils.Stateful;
import org.jelik.parser.ast.*;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arrays.*;
import org.jelik.parser.ast.blocks.BasicBlock;
import org.jelik.parser.ast.branching.ElifExpression;
import org.jelik.parser.ast.branching.ElseExpressionImpl;
import org.jelik.parser.ast.branching.IfExpressionImpl;
import org.jelik.parser.ast.casts.CastObjectToObjectNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.classes.InterfaceDeclaration;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.expression.*;
import org.jelik.parser.ast.functions.*;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.StoreLocalNode;
import org.jelik.parser.ast.locals.ValueDeclaration;
import org.jelik.parser.ast.locals.VariableDeclaration;
import org.jelik.parser.ast.loops.ForEachLoop;
import org.jelik.parser.ast.loops.WhileLoop;
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr;
import org.jelik.parser.ast.numbers.*;
import org.jelik.parser.ast.operators.*;
import org.jelik.parser.ast.strings.*;
import org.jelik.parser.ast.types.AbstractTypeRef;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMStringType;
import org.jelik.types.Type;
import org.jelik.types.jvm.IntegerWrapperType;
import org.jelik.types.jvm.JVMLongType;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import java.util.*;

/**
 * @author Marcin Bukowiecki
 */
@Stateful
public class ToByteCodeVisitor extends AstVisitor {

    private ClassWriterAdapter classWriterAdapter;

    private final List<ToByteCodeResult> toByteCodeResult = new ArrayList<>();

    public ToByteCodeVisitor() {

    }

    public List<ToByteCodeResult> getToByteCodeResult() {
        return Collections.unmodifiableList(toByteCodeResult);
    }

    public void addByteCodeResult(ToByteCodeResult toByteCodeResult) {
        this.toByteCodeResult.add(toByteCodeResult);
    }

    public ClassWriterAdapter getClassWriterAdapter() {
        return classWriterAdapter;
    }

    @Override
    public void visitModuleDeclaration(@NotNull ModuleDeclaration moduleDeclaration,
                                       @NotNull CompilationContext compilationContext) {
        var classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classWriter.visit(compilationContext.getJdkVersion(),
                Opcodes.ACC_PUBLIC,
                moduleDeclaration.getCanonicalNameForByteCode(),
                "",
                "java/lang/Object",
                null);
        this.classWriterAdapter = new ClassWriterAdapter(classWriter);
        compilationContext.pushClassWriter(this.classWriterAdapter);
        visitClassDeclaration(moduleDeclaration, compilationContext);

        for (var aClass : moduleDeclaration.getClasses()) {
            var opCodes = Opcodes.ACC_PUBLIC;
            if (aClass.isAbstract()) {
                opCodes = opCodes | Opcodes.ACC_ABSTRACT;
            }

            classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classWriter.visit(compilationContext.getJdkVersion(),
                    opCodes,
                    aClass.getCanonicalNameForByteCode(),
                    "",
                    aClass.getSuperInternalName(),
                    null);
            this.classWriterAdapter = new ClassWriterAdapter(classWriter);
            aClass.accept(this, compilationContext);
        }

        for (var anInterface : moduleDeclaration.getInterfaces()) {
            var opCodes = Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT;
            classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classWriter.visit(compilationContext.getJdkVersion(),
                    opCodes,
                    anInterface.getCanonicalNameForByteCode(),
                    "",
                    "java/lang/Object",
                    null);
            this.classWriterAdapter = new ClassWriterAdapter(classWriter);
            anInterface.accept(this, compilationContext);
        }

        compilationContext.popClassWriter();
    }

    @Override
    public void visitInterfaceDeclaration(@NotNull InterfaceDeclaration interfaceDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(interfaceDeclaration);

        classWriterAdapter.visitStart();

        for (var functionDeclaration : interfaceDeclaration.getFunctionDeclarations()) {
            functionDeclaration.accept(this, compilationContext);
        }

        for (var methodDeclaration : interfaceDeclaration.getMethodDeclarations()) {
            methodDeclaration.accept(this, compilationContext);
        }

        classWriterAdapter.visitEnd();

        byte[] bytes = classWriterAdapter.getBytes();
        this.toByteCodeResult.add(new ToByteCodeResult(interfaceDeclaration.getType(), bytes, false));

        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration,
                                      @NotNull CompilationContext compilationContext) {

        compilationContext.pushCompilationUnit(classDeclaration);

        classWriterAdapter.visitStart();

        for (var constructorDeclaration : classDeclaration.getConstructorDeclarations()) {
            constructorDeclaration.accept(this, compilationContext);
        }

        for (var functionDeclaration : classDeclaration.getFunctionDeclarations()) {
            functionDeclaration.accept(this, compilationContext);
        }
        classWriterAdapter.visitEnd();

        byte[] bytes = classWriterAdapter.getBytes();
        this.toByteCodeResult.add(new ToByteCodeResult(classDeclaration.getType(), bytes, classDeclaration.hasMain()));

        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitConstructorDeclaration(@NotNull ConstructorDeclaration constructorDeclaration,
                                            @NotNull CompilationContext compilationContext) {

        compilationContext.pushCompilationUnit(constructorDeclaration);
        constructorDeclaration.getFunctionContext().shiftIndexesAccordingToType();

        var mv = new MethodVisitorAdapter(classWriterAdapter.visitMethod(
                Opcodes.ACC_PUBLIC,
                constructorDeclaration.getName(),
                constructorDeclaration.getDescriptor(),
                constructorDeclaration.getSignature(),
                constructorDeclaration.getExceptions()), compilationContext);

        mv.setupInitialLocals(constructorDeclaration.getFunctionContext().getLocalsAsParameters());

        classWriterAdapter.setCurrentMethodVisitor(mv);

        mv.visitCode();

        mv.visitTryCatchBlock(constructorDeclaration.getFunctionContext().getTryExpressionList());
        constructorDeclaration.getFunctionBody().accept(this, compilationContext);

        mv.visitMax();

        List<LocalVariable> localVariables = constructorDeclaration.getLocalVariables();
        mv.visitLocals(localVariables);

        mv.visitEnd();
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitValueDeclaration(@NotNull ValueDeclaration valueDeclaration,
                                      @NotNull CompilationContext compilationContext) {
        valueDeclaration.getExpression().accept(this, compilationContext);
        var storeLocalByteCodeVisitor = new StoreLocalByteCodeVisitor(
                valueDeclaration.getLocalVariable(),
                this.classWriterAdapter.getCurrentMethodVisitor()
        );
        valueDeclaration.getLocalVariable().getGenericType().accept(storeLocalByteCodeVisitor, compilationContext);
    }

    @Override
    public void visitVariableDeclaration(@NotNull VariableDeclaration variableDeclaration,
                                         @NotNull CompilationContext compilationContext) {
        variableDeclaration.getExpression().accept(this, compilationContext);
        StoreLocalByteCodeVisitor storeLocalByteCodeVisitor = new StoreLocalByteCodeVisitor(
                variableDeclaration.getLocalVariable(),
                this.classWriterAdapter.getCurrentMethodVisitor()
        );
        variableDeclaration.getLocalVariable().getGenericType().accept(storeLocalByteCodeVisitor, compilationContext);
    }

    @Override
    public void visitDefaultConstructor(@NotNull DefaultConstructorDeclaration defaultConstructorDeclaration,
                                        @NotNull CompilationContext compilationContext) {

        var constructor = new MethodVisitorAdapter(classWriterAdapter.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                "",
                null), compilationContext);
        Label start = constructor.visitLabel();
        constructor.aload(0);
        constructor.invokeSpecial("java/lang/Object", "<init>", "()V");
        constructor.visitReturn();
        Label end = constructor.visitLabel();
        constructor.visitLocalVariable("this", defaultConstructorDeclaration.getOwner().getDescriptor(),
                "", start, end, 0);
        constructor.visitMaxes(1, 1);
        constructor.visitEnd();

    }

    @Override
    public void visitInterfaceMethodDeclaration(@NotNull InterfaceMethodDeclaration interfaceMethodDeclaration,
                                                @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(interfaceMethodDeclaration);

        var mv = new MethodVisitorAdapter(classWriterAdapter.visitMethod(
                Opcodes.ACC_ABSTRACT | Opcodes.ACC_PUBLIC,
                interfaceMethodDeclaration.getName(),
                interfaceMethodDeclaration.getDescriptor(),
                interfaceMethodDeclaration.getSignature(),
                interfaceMethodDeclaration.getExceptions()), compilationContext);

        mv.visitCode();
        mv.visitEnd();

        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration,
                                         @NotNull CompilationContext compilationContext) {

        compilationContext.pushCompilationUnit(functionDeclaration);

        ReturnStmtInserter.INSTANCE.visitFunctionDeclaration(functionDeclaration, compilationContext);
        CodeBranchChecker.INSTANCE.visitFunctionDeclaration(functionDeclaration, compilationContext);

        functionDeclaration.getFunctionContext().shiftIndexesAccordingToType();

        var mv = new MethodVisitorAdapter(classWriterAdapter.visitMethod(
                Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC,
                functionDeclaration.getName(),
                functionDeclaration.getDescriptor(),
                functionDeclaration.getSignature(),
                functionDeclaration.getExceptions()), compilationContext);

        mv.setupInitialLocals(functionDeclaration.getFunctionContext().getLocalsAsParameters());

        classWriterAdapter.setCurrentMethodVisitor(mv);

        mv.visitCode();

        mv.visitTryCatchBlock(functionDeclaration.getFunctionContext().getTryExpressionList());
        functionDeclaration.getFunctionBody().accept(this, compilationContext);

        mv.visitMax();

        List<LocalVariable> localVariables = functionDeclaration.getLocalVariables();
        mv.visitLocals(localVariables);

        mv.visitEnd();

        compilationContext.popCompilationUnit();
    }

    @Override
    public void visit(@NotNull FunctionBodyBlock fb, @NotNull CompilationContext compilationContext) {
        if (fb.getBasicBlock().getExpressions().isEmpty()) {
            classWriterAdapter.getCurrentMethodVisitor().visitReturn();
            final LabelNode label = compilationContext.createLabel("end-function");
            classWriterAdapter.getCurrentMethodVisitor().visitLabel(label.getLabel());
            compilationContext
                    .currentFunction()
                    .getFunctionParameterList()
                    .getFunctionParameters().forEach(p -> p.localVariableRef.setEnd(label));
            return;
        }

        compilationContext.blockStack.addLast(fb.getBasicBlock());
        classWriterAdapter.getCurrentMethodVisitor().visitLabel(fb.getStartLabel().getLabel());
        for (Expression expression : fb.getBasicBlock().getExpressions()) {
            expression.accept(this, compilationContext);
            if (expression instanceof StackConsumer) {
                continue;
            }
            if (expression instanceof Statement) {
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
    public void visitBasicBlock(@NotNull BasicBlock basicBlock, @NotNull  CompilationContext compilationContext) {
        MethodVisitorAdapter mv = classWriterAdapter.getCurrentMethodVisitor();
        for (Expression expression : basicBlock.getExpressions()) {
            //mv.visitLineNumber
            expression.accept(this, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull AssignExpr assignExpr, @NotNull CompilationContext compilationContext) {
        if (assignExpr.getLeft() instanceof StoreLocalNode) {
            assignExpr.getRight().accept(this, compilationContext);
            if (assignExpr.getParent() instanceof ConsumingExpression) {
                classWriterAdapter.getCurrentMethodVisitor().dup();
            }
            assignExpr.getLeft().getType().accept(new StoreLocalByteCodeVisitor(
                    ((StoreLocalNode) assignExpr.getLeft()).getLocalVariable(),
                    classWriterAdapter.getCurrentMethodVisitor()
            ), compilationContext);
            ((StoreLocalNode) assignExpr.getLeft()).getLocalVariable().setEnd(compilationContext.labelStack.getLast());
        }
    }

    @Override
    public void visit(@NotNull GetLocalNode getLocalNode, @NotNull CompilationContext compilationContext) {
        AbstractTypeRef typeRef = getLocalNode.getLocalVariable().getTypeRef();
        Type genericType = typeRef.getGenericType();
        genericType.accept(new ByteCodeGetLocalVariableVisitor(getLocalNode,
                        classWriterAdapter.getCurrentMethodVisitor()),
                compilationContext);
    }

    @Override
    public void visitReturnExpr(@NotNull ReturnExpr returnExpr, @NotNull CompilationContext compilationContext) {
        returnExpr.getExpression().accept(this, compilationContext);
        if (returnExpr.getParent().getParent() instanceof TryExpressionImpl) {
            return;
        }
        var genericType = ((FunctionDeclaration) compilationContext.currentCompilationUnit())
                .getFunctionReturn()
                .getTypeNode()
                .getType();
        genericType.accept(new ByteCodeReturnInstructionVisitor(classWriterAdapter.getCurrentMethodVisitor()),
                compilationContext);
    }

    @Override
    public void visit(@NotNull AddExpr addExpr, @NotNull CompilationContext compilationContext) {
        addExpr.getLeft().accept(this, compilationContext);
        addExpr.getRight().accept(this, compilationContext);
        var genericType = addExpr.getGenericType();
        genericType.accept(new ByteCodeAddOpVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull SubExpr subExpr, @NotNull CompilationContext compilationContext) {
        subExpr.getLeft().accept(this, compilationContext);
        subExpr.getRight().accept(this, compilationContext);
        var genericType = subExpr.getGenericType();
        genericType.accept(new ByteCodeSubOpVisitor(subExpr, classWriterAdapter.getCurrentMethodVisitor()),
                compilationContext);
    }

    @Override
    public void visit(@NotNull DivExpr divExpr, @NotNull CompilationContext compilationContext) {
        divExpr.getLeft().accept(this, compilationContext);
        divExpr.getRight().accept(this, compilationContext);
        var genericType = divExpr.getGenericType();
        genericType.accept(new ByteCodeDivOpVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull MulExpr mulExpr, @NotNull CompilationContext compilationContext) {
        mulExpr.getLeft().accept(this, compilationContext);
        mulExpr.getRight().accept(this, compilationContext);
        var genericType = mulExpr.getGenericType();
        genericType.accept(new ByteCodeMulOpVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visit(@NotNull IfExpressionImpl ifExpression,
                      @NotNull CompilationContext compilationContext) {

        ifExpression.getConditionExpression().accept(this, compilationContext);
        ifExpression.getBasicBlock().accept(this, compilationContext);
        if (ifExpression.getContext().isJumpOver()) {
            final LabelNode finishLabel = ifExpression.getFinishLabel();
            Objects.requireNonNull(finishLabel);
            if (!ifExpression.endsWithReturnStmt()) {
                classWriterAdapter.getCurrentMethodVisitor().visitJumpInstruction(Opcodes.GOTO, finishLabel);
            }
        }
        final FalseLabelExtractor falseLabelExtractor = new FalseLabelExtractor();
        ifExpression.getConditionExpression().accept(falseLabelExtractor, compilationContext);
        classWriterAdapter.getCurrentMethodVisitor().visitLabel(falseLabelExtractor.result.getLabel());
        if (ifExpression.getElseExpression() != null) {
            ifExpression.getElseExpression().accept(this, compilationContext);
        }
    }

    @Override
    public void visitElifExpression(@NotNull ElifExpression elifExpression,
                                    @NotNull CompilationContext compilationContext) {

        elifExpression.getConditionExpression().accept(this, compilationContext);
        elifExpression.getBasicBlock().accept(this, compilationContext);
        if (elifExpression.getContext().isJumpOver()) {
            final LabelNode finishLabel = elifExpression.getFinishLabel();
            Objects.requireNonNull(finishLabel);
            if (!elifExpression.endsWithReturnStmt()) {
                classWriterAdapter.getCurrentMethodVisitor().visitJumpInstruction(Opcodes.GOTO, finishLabel);
            }
        }

        final FalseLabelExtractor falseLabelExtractor = new FalseLabelExtractor();
        elifExpression.getConditionExpression().accept(falseLabelExtractor, compilationContext);
        classWriterAdapter.getCurrentMethodVisitor().visitLabel(falseLabelExtractor.result.getLabel());
        elifExpression.getElseExpressionOpt().ifPresent(expr -> expr.accept(this, compilationContext));
    }

    @Override
    public void visit(@NotNull ElseExpressionImpl elseExpressionImpl,
                      @NotNull CompilationContext compilationContext) {
        super.visit(elseExpressionImpl, compilationContext);
        classWriterAdapter
                .getCurrentMethodVisitor()
                .visitLabel(Objects.requireNonNull(elseExpressionImpl.getFinishLabel()).getLabel());
    }

    @Override
    public void visit(@NotNull StringTypedExpression stringExpression, @NotNull CompilationContext compilationContext) {
        classWriterAdapter
                .getCurrentMethodVisitor()
                .visitConstant(JVMStringType.INSTANCE, stringExpression.getString());
    }

    @Override
    public void visit(@NotNull StringBuilderInit stringBuilderInit,
                      @NotNull CompilationContext compilationContext) {
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        Type type = stringBuilderInit.getType();
        currentMethodVisitor.callNew(type);
        currentMethodVisitor.dup();
        currentMethodVisitor.invokeSpecial(type.getInternalName(), "<init>", "()V");
        stringBuilderInit.getFurtherExpressionOpt().ifPresent(expr -> expr.accept(this, compilationContext));
    }

    @Override
    public void visit(@NotNull StringBuilderAppend stringBuilderAppend,
                      @NotNull CompilationContext compilationContext) {
        stringBuilderAppend.getSubject().accept(this, compilationContext);

        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        var type = stringBuilderAppend.getType();
        var targetFunctionCall = stringBuilderAppend.getTargetFunctionCall();
        currentMethodVisitor.invokeInstance(type.getInternalName(),
                "append",
                targetFunctionCall.getMethodData().getDescriptor(),
                stringBuilderAppend.getType(),
                stringBuilderAppend.getArgumentTypes(), false);
        stringBuilderAppend.getFurtherExpressionOpt().ifPresent(expr -> expr.accept(this, compilationContext));
    }

    @Override
    public void visit(@NotNull Int32ToWrapperNode int32ToWrapperNode, @NotNull CompilationContext compilationContext) {
        int32ToWrapperNode.getSubject().accept(this, compilationContext);
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.invokeStatic(int32ToWrapperNode.getType().getInternalName(),
                "valueOf", "(I)Ljava/lang/Integer;",
                IntegerWrapperType.INSTANCE, Collections.singletonList(JVMIntType.INSTANCE), false);
    }

    @Override
    public void visitFunctionCall(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        var targetFunctionCall = functionCallExpr.getTargetFunctionCallProvider();
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        targetFunctionCall.getCodeGenProvider().invoke(functionCallExpr, this, currentMethodVisitor);
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
    public void visit(@NotNull Float64Node float64Node, @NotNull CompilationContext compilationContext) {
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.visitConstant(float64Node.getType(), float64Node.getValue());
    }

    @Override
    public void visit(@NotNull AsExpr asExpr, @NotNull CompilationContext compilationContext) {
        asExpr.getLeft().accept(this, compilationContext);
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.checkCast(asExpr.getRight().getGenericType());
    }

    @Override
    public void visit(@NotNull ObjectToInt32Node objectToInt32Node,
                      @NotNull CompilationContext compilationContext) {
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        objectToInt32Node.getSubject().accept(this, compilationContext);
        currentMethodVisitor.checkCast(IntegerWrapperType.INSTANCE);
        currentMethodVisitor.invokeInstance(IntegerWrapperType.INSTANCE.getInternalName(),
                "intValue", "()I",
                JVMIntType.INSTANCE,
                Collections.emptyList(), false);
    }

    @Override
    public void visit(@NotNull CastObjectToObjectNode castObjectToObjectNode,
                      @NotNull CompilationContext compilationContext) {
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        castObjectToObjectNode.getExpression().accept(this, compilationContext);
        currentMethodVisitor.checkCast(castObjectToObjectNode.getTo());
    }

    @Override
    public void visit(@NotNull IncrExpr incrExpr, @NotNull CompilationContext compilationContext) {
        if (incrExpr.getRight() instanceof GetLocalNode && incrExpr.getRight().getGenericReturnType() instanceof JVMIntType) {
            incrExpr.getRight().getGenericType().accept(new IncrByteCodeVisitor(incrExpr, incrExpr.getRight(),
                    classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
        } else {
            incrExpr.getRight().accept(this, compilationContext);
            incrExpr.getRight().getGenericType().accept(new IncrByteCodeVisitor(incrExpr, incrExpr.getRight(),
                    classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
        }
    }

    @Override
    public void visit(@NotNull DecrExpr decrExpr, @NotNull CompilationContext compilationContext) {
        new DecrByteCodeVisitor(decrExpr, classWriterAdapter.getCurrentMethodVisitor()).visit(decrExpr.getGenericType(),
                compilationContext);
    }

    @Override
    public void visit(@NotNull IntegerToInt32NodeWrapper integerWrapperToInt32Node,
                      @NotNull CompilationContext compilationContext) {
        super.visit(integerWrapperToInt32Node, compilationContext);
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.invokeInstance("java/lang/Integer", "intValue", "()I",
                JVMIntType.INSTANCE, Collections.emptyList(), false);
    }

    @Override
    public void visit(@NotNull StringBuilderToStringNode stringBuilderToStringNode,
                      @NotNull CompilationContext compilationContext) {
        stringBuilderToStringNode.getExpression().accept(this, compilationContext);
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.invokeInstance("java/lang/StringBuilder", "toString",
                "()Ljava/lang/String;", JVMStringType.INSTANCE, Collections.emptyList(), false);
    }

    @Override
    public void visit(@NotNull NullExpr nullExpr, @NotNull CompilationContext compilationContext) {
        if (nullExpr.getIgnore()) {
            return;
        }
        classWriterAdapter.getCurrentMethodVisitor().visitNull();
    }

    @Override
    public void visitArrayCreateExpr(@NotNull ArrayCreateExpr arrayCreateExpr, @NotNull CompilationContext compilationContext) {
        Type type = arrayCreateExpr.getType();
        int size = arrayCreateExpr.getExpressions().size();
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.pushInt(size);
        type.getInnerType(0).accept(new ByteCodeArrayCreateVisitor(currentMethodVisitor), compilationContext);

        int index = 0;
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            currentMethodVisitor.dup();
            currentMethodVisitor.pushInt(index);
            expression.accept(this, compilationContext);
            type.getInnerType(0).accept(new ByteCodeArrayStoreVisitor(currentMethodVisitor), compilationContext);
            index++;
        }
    }

    @Override
    public void visitTypedArrayCreateExpr(@NotNull TypedArrayCreateExpr arrayCreateExpr,
                                          @NotNull CompilationContext compilationContext) {

        var type = arrayCreateExpr.getType();
        var size = arrayCreateExpr.getExpressions().size();
        var currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.pushInt(size);
        type.getInnerType(0).accept(new ByteCodeArrayCreateVisitor(currentMethodVisitor), compilationContext);

        int index = 0;
        for (Expression expression : arrayCreateExpr.getExpressions()) {
            currentMethodVisitor.dup();
            currentMethodVisitor.pushInt(index);
            expression.accept(this, compilationContext);
            type.getInnerType(0).accept(new ByteCodeArrayStoreVisitor(currentMethodVisitor), compilationContext);
            index++;
        }
    }

    @Override
    public void visitTypedArrayCreateWithSizeExpr(@NotNull TypedArrayCreateWithSizeExprTyped arrayCreateExpr,
                                                  @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        var type = arrayCreateExpr.getType().getInnerType(0);
        final Expression expression = arrayCreateExpr.getExpression();
        expression.accept(this, compilationContext);
        type.accept(new ByteCodeArrayCreateVisitor(mv), compilationContext);
    }

    @Override
    public void visitArrayOrMapGetExpr(@NotNull ArrayOrMapGetExpr arrayOrMapGetExpr,
                                       @NotNull CompilationContext compilationContext) {

        arrayOrMapGetExpr.getNextElementProvider().getElement(this,
                classWriterAdapter.getCurrentMethodVisitor());

        /*
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
        */
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
    public void visitOrExpr(@NotNull OrExpr orExpr, @NotNull CompilationContext compilationContext) {
        super.visitOrExpr(orExpr, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (orExpr.getParent() instanceof ConsumingExpression) {
            ASMUtils.visitSingleExpr(mv, orExpr, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull AndExpr andExpr, @NotNull CompilationContext compilationContext) {
        super.visit(andExpr, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (andExpr.getParent() instanceof ConsumingExpression) {
            ASMUtils.visitSingleExpr(mv, andExpr, compilationContext);
        }
    }

    @Override
    public void visitTryExpression(@NotNull TryExpression tryExpression, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitLabel(Objects.requireNonNull(tryExpression.getStartLabel()).getLabel());
        tryExpression.getBlock().accept(this, compilationContext);
        mv.visitLabel(Objects.requireNonNull(tryExpression.getEndLabel()).getLabel());

        var catchExpression = tryExpression.getCatchExpression();
        mv.visitJumpInstruction(Opcodes.GOTO, Objects.requireNonNull(catchExpression.getEndLabel()));

        mv.visitLabel(Objects.requireNonNull(catchExpression.getStartLabel()).getLabel());
        if (!catchExpression.getBlock().getExpressions().isEmpty()) {
            if (catchExpression.getInnerLabel() == null) {
                throw new IllegalArgumentException("Inner label not set");
            }
            mv.visitLabel(catchExpression.getInnerLabel().getLabel());
        }
        tryExpression.getCatchExpression().accept(this, compilationContext);
        mv.visitLabel(catchExpression.getEndLabel().getLabel());
    }

    @Override
    public void visitCatchExpression(@NotNull CatchExpression catchExpression, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        //mv.visitLabel(catchExpression.getBlock().startLabel.getLabel());
        for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
            mv.objectStoreIgnoreStack(functionParameter.localVariableRef.index);
        }
        compilationContext.blockStack.addLast(catchExpression.getBlock());
        for (Expression expression : catchExpression.getBlock().getExpressions()) {
            expression.accept(this, compilationContext);
        }
        compilationContext.blockStack.removeLast();
        //mv.visitLabel(catchExpression.getBlock().endLabel.getLabel());
    }

    @Override
    public void visit(@NotNull Float32ToInt64Node float32ToInt64Node, @NotNull CompilationContext compilationContext) {
        float32ToInt64Node.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitFloatToLong();
    }

    @Override
    public void visit(@NotNull Float32ToInt32Node float32ToInt32Node, @NotNull CompilationContext compilationContext) {
        float32ToInt32Node.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitFloatToInt();
    }

    @Override
    public void visit(@NotNull Int32ToFloat64Node int32ToFloat64Node, @NotNull CompilationContext compilationContext) {
        int32ToFloat64Node.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToDouble();
    }

    @Override
    public void visit(@NotNull Int32ToInt16Node int32ToInt16Node,
                      @NotNull CompilationContext compilationContext) {
        int32ToInt16Node.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToShort();
    }

    @Override
    public void visit(@NotNull Int32ToFloat32Node int32ToFloat32Node,
                      @NotNull CompilationContext compilationContext) {
        int32ToFloat32Node.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToFloat();
    }

    @Override
    public void visit(@NotNull Float32ToFloat64Node float32ToFloat64Node,
                      @NotNull CompilationContext compilationContext) {
        float32ToFloat64Node.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitFloatToDouble();
    }

    @Override
    public void visitArrayOrMapSetExpr(@NotNull ArrayOrMapSetExpr arrayOrMapSetExpr, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (arrayOrMapSetExpr.isArraySet()) {
            arrayOrMapSetExpr.getRef().accept(this, compilationContext);
            arrayOrMapSetExpr.getIndex().accept(this, compilationContext);
            arrayOrMapSetExpr.getRightExpression().accept(this, compilationContext);
            arrayOrMapSetExpr.getRef().getGenericType().getInnerType(0).accept(new ByteCodeArrayStoreVisitor(mv),
                    compilationContext);
        } else {
            arrayOrMapSetExpr.getRef().accept(this, compilationContext);
            arrayOrMapSetExpr.getIndex().accept(this, compilationContext);
            arrayOrMapSetExpr.getRightExpression().accept(this, compilationContext);
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
        int32ToInt8Node.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToByte();
    }

    @Override
    public void visit(@NotNull Int32ToCharNode int32ToCharNode, @NotNull CompilationContext compilationContext) {
        int32ToCharNode.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToChar();
    }

    @Override
    public void visit(@NotNull NegExpr negExpr, @NotNull CompilationContext compilationContext) {
        negExpr.getRight().accept(this, compilationContext);
        var genericType = negExpr.getGenericReturnType();
        genericType.accept(new ByteCodeSubOpVisitor(negExpr, classWriterAdapter.getCurrentMethodVisitor()),
                compilationContext);
    }

    @Override
    public void visit(@NotNull Int32ToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitIntToLong();
    }

    @Override
    public void visit(@NotNull CharToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.charToLong();
    }

    @Override
    public void visit(@NotNull Float64ToFloat32 castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.doubleToFloat();
    }

    @Override
    public void visit(@NotNull Float64ToInt64Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.doubleToLong();
    }

    @Override
    public void visit(@NotNull Float64ToInt32Node castToNode, @NotNull CompilationContext compilationContext) {
        castToNode.getExpression().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.doubleToInt();
    }

    @Override
    public void visit(@NotNull RemExpr remExpr, @NotNull CompilationContext compilationContext) {
        remExpr.getLeft().accept(this, compilationContext);
        remExpr.getRight().accept(this, compilationContext);
        remExpr.getGenericType().accept(new ByteCodeRemOpVisitor(classWriterAdapter.getCurrentMethodVisitor()),
                compilationContext);
    }

    @Override
    public void visit(@NotNull Int64Node int64Node, @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitConstant(JVMLongType.INSTANCE, int64Node.getValue());
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
        bitAndExpr.getLeft().accept(this, compilationContext);
        bitAndExpr.getRight().accept(this, compilationContext);
        bitAndExpr.getGenericType().accept(new ByteCodeBitAndVisitor(classWriterAdapter.getCurrentMethodVisitor()),
                compilationContext);
    }

    @Override
    public void visit(@NotNull BitOrExpr bitOrExpr, @NotNull CompilationContext compilationContext) {
        bitOrExpr.getLeft().accept(this, compilationContext);
        bitOrExpr.getRight().accept(this, compilationContext);
        bitOrExpr
                .getGenericType()
                .accept(new ByteCodeBitOrVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visitBitUshrExpr(@NotNull BitUshrExpr bitUshrExpr, @NotNull CompilationContext compilationContext) {
        super.visitBitUshrExpr(bitUshrExpr, compilationContext);
        bitUshrExpr
                .getGenericType()
                .accept(new ByteCodeUshrVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visitBitShrExpr(@NotNull BitShrExpr bitShrExpr, @NotNull CompilationContext compilationContext) {
        super.visitBitShrExpr(bitShrExpr, compilationContext);
        bitShrExpr
                .getGenericType()
                .accept(new ByteCodeShrVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
    }

    @Override
    public void visitBitShlExpr(@NotNull BitShlExpr bitShlExpr, @NotNull CompilationContext compilationContext) {
        super.visitBitShlExpr(bitShlExpr, compilationContext);
        bitShlExpr
                .getGenericType()
                .accept(new ByteCodeShlVisitor(classWriterAdapter.getCurrentMethodVisitor()), compilationContext);
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
        xorExpr.getGenericReturnType().accept(new ByteCodeBitXorVisitor(mv), compilationContext);
    }

    @Override
    public void visitAbstractLogicalOpExpr(@NotNull AbstractLogicalOpExpr expr,
                                           @NotNull CompilationContext compilationContext) {
        if (!expr.getLeft().isIgnored()) {
            expr.getLeft().accept(this, compilationContext);
        }
        if (!expr.getRight().isIgnored()) {
            expr.getRight().accept(this, compilationContext);
        }
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        if (expr.isNegated()) {
            expr.instructionToCall.getNegated().forEach(ins -> mv.visitJumpInstruction(ins, expr.falseLabelNode));
        } else {
            expr.instructionToCall.getValue().forEach(ins -> mv.visitJumpInstruction(ins, expr.trueLabelNode));
        }
        if (expr.getParent() instanceof ConsumingExpression) {
            ASMUtils.visitSingleExpr(mv, expr, compilationContext);
        }
    }

    @Override
    public void visit(@NotNull IsExpr isExpr, @NotNull CompilationContext compilationContext) {
        isExpr.getLeft().accept(this, compilationContext);
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        isExpr.getRight().getGenericReturnType().accept(new ByteCodeIsOperatorVisitor(mv, isExpr), compilationContext);
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
                targetFunction.getFunctionType().getFunctionalInterfaceMethod(compilationContext).getName()
                );
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
            entry.getKey().accept(this, compilationContext);
            entry.getValue().accept(this, compilationContext);
            mv.invokeInterface(Type.of(Map.class).getInternalName(),
                    "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    entry.getValue().getGenericReturnType(),
                    Arrays.asList(entry.getValue().getGenericReturnType(),
                            entry.getValue().getGenericReturnType()));
            mv.visitPop();
        }
    }

    @Override
    public void visit(@NotNull SuperCallExpr superCallExpr,
                      @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.aload(0);
        for (Argument argument : superCallExpr.getArgumentList().getArguments()) {
            argument.accept(this, compilationContext);
        }
        mv.invokeSpecial(superCallExpr.getOwner().getInternalName(),
                "<init>",
                superCallExpr.getTargetFunctionCall()
                        .getMethodData()
                        .getDescriptor());
    }

    @Override
    public void visitForEachLoop(@NotNull ForEachLoop forEachloop,
                                 @NotNull CompilationContext compilationContext) {

        var mv = classWriterAdapter.getCurrentMethodVisitor();
        var iterExpression = forEachloop.getIterExpression();
        var forEachASMProvider = forEachloop.getForEachASMProvider(compilationContext);
        mv.visitLabel(forEachloop.getLoopStart().getLabel());
        iterExpression.accept(this, compilationContext);
        forEachASMProvider.newIterator(mv);
        mv.visitLabel(forEachloop.getLoopBodyStart().getLabel());
        forEachASMProvider.hasNext(mv);
        forEachASMProvider.nextElement(this, mv);
        forEachloop.getBlock().accept(this, compilationContext);
        mv.visitJumpInstruction(Opcodes.GOTO, forEachloop.getLoopBodyStart());
        mv.visitLabel(forEachloop.getLoopEnd().getLabel());
    }

    @Override
    public void visitWhileLoop(@NotNull WhileLoop whileLoop,
                               @NotNull CompilationContext compilationContext) {

        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.visitLabel(whileLoop.getLoopStart().getLabel());
        whileLoop.getCondition().accept(this, compilationContext);/*
        if (!(whileLoop.getCondition() instanceof AbstractLogicalOpExpr)) {
            mv.visitJumpInstruction(Opcodes.IFEQ, whileLoop.getLoopEnd());
        }*/
        whileLoop.getBlock().accept(this, compilationContext);
        mv.visitJumpInstruction(Opcodes.GOTO, whileLoop.getLoopStart());
        mv.visitLabel(whileLoop.getLoopEnd().getLabel());
    }

    @Override
    public void visitCharExpression(@NotNull CharTypedExpression charExpression,
                                    @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        mv.pushInt(charExpression.getStringTokens().get(0).getText().charAt(0));
    }

    @Override
    public void visitInExpr(@NotNull InExpr inExpr,
                            @NotNull CompilationContext compilationContext) {
        inExpr.containsElementProvider().visit(this, compilationContext);
    }

    @Override
    public void visitInt64ToWrapperNode(@NotNull Int64ToWrapperNode int64ToWrapperNode,
                                        @NotNull CompilationContext compilationContext) {
        int64ToWrapperNode.getSubject().accept(this, compilationContext);
        MethodVisitorAdapter currentMethodVisitor = classWriterAdapter.getCurrentMethodVisitor();
        currentMethodVisitor.invokeStatic(int64ToWrapperNode.getType().getInternalName(),
                "valueOf", "(J)Ljava/lang/Long;",
                IntegerWrapperType.INSTANCE, Collections.singletonList(JVMIntType.INSTANCE), false);
    }

    @Override
    public void visitNullSafeCall(@NotNull NullSafeCallExpr nullSafeCall,
                                  @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        nullSafeCall.getReference().accept(this, compilationContext);
        nullSafeCall.getFurtherExpression().accept(this, compilationContext);
        if (nullSafeCall.isLast() && !(nullSafeCall.getParent() instanceof DefaultValueExpr)) {
            mv.visitJumpInstruction(Opcodes.GOTO, Objects.requireNonNull(nullSafeCall.getFinishLabel()));
            Objects.requireNonNull(nullSafeCall.getEndLabel()).accept(this, compilationContext);
            mv.visitPop();
            mv.visitNull();
            mv.checkCast(nullSafeCall.getFurtherExpression().getGenericReturnType());
            Objects.requireNonNull(nullSafeCall.getFinishLabel()).accept(this, compilationContext);
        }
        if (nullSafeCall.getParent() instanceof DefaultValueExpr) {
            final DefaultValueExpr parent = (DefaultValueExpr) nullSafeCall.getParent();
            mv.visitJumpInstruction(Opcodes.GOTO, parent.getNotNullLabel());
        }
    }

    @Override
    public void visitNullSafeBooleanExprWrapper(@NotNull NullSafeCheckExprWrapper expr,
                                                @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        expr.getLeft().accept(this, compilationContext);
        mv.dup();
        expr.instructionToCall.getValue().forEach(ins -> mv.visitJumpInstruction(ins, expr.getEndLabel()));
    }

    @Override
    public void visitLabelNode(LabelNode labelNode, CompilationContext compilationContext) {
        classWriterAdapter.getCurrentMethodVisitor().visitLabel(labelNode.getLabel());
    }

    @Override
    public void visitLambdaDeclarationExpression(@NotNull LambdaDeclarationExpression lambdaDeclarationExpression,
                                                 @NotNull CompilationContext compilationContext) {
        var targetFunction = lambdaDeclarationExpression.getLambdaDeclaration();
        InvokeDynamicHelper.INSTANCE.codeGen(classWriterAdapter.getCurrentMethodVisitor(),
                Objects.requireNonNull(targetFunction),
                targetFunction.getFunctionType().getFunctionalInterfaceMethod(compilationContext).getName()
        );
    }

    @Override
    public void visitDefaultValueExpr(@NotNull DefaultValueExpr defaultValueExpr,
                                      @NotNull CompilationContext compilationContext) {
        var mv = classWriterAdapter.getCurrentMethodVisitor();
        defaultValueExpr.getLeft().accept(this, compilationContext);
        defaultValueExpr.getNullLabel().accept(this, compilationContext);
        mv.visitPop();
        defaultValueExpr.getRight().accept(this, compilationContext);
        defaultValueExpr.getNotNullLabel().accept(this, compilationContext);
    }
}
