package org.jelik.compiler.asm;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jelik.compiler.asm.utils.ByteCodeLogger;
import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.types.JVMBooleanType;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jelik.types.jvm.JVMCharType;
import org.jelik.types.jvm.JVMDoubleType;
import org.jelik.types.jvm.JVMFloatType;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.*;

/**
 * @author Marcin Bukowiecki
 */
public class MethodVisitorAdapter {

    private final MethodVisitor mv;

    private int maxStackSize = 0;

    private int currentStackSize = 0;

    private int maxLocals = 0;

    private final StackMap stackMap = new StackMap();

    private final LocalsMap localsMap = new LocalsMap();

    private final LinkedList<Label> visitedLabelsStack = Lists.newLinkedList();

    private final CompilationContext compilationContext;

    private final Set<Integer> visitedLineNumbers = Sets.newHashSet();

    public MethodVisitorAdapter(MethodVisitor mv, CompilationContext compilationContext) {
        this.mv = mv;
        this.compilationContext = compilationContext;
    }

    public CompilationContext getCompilationContext() {
        return compilationContext;
    }

    public void visitLineNumber(int lineNumber) {

    }

    public void setupInitialLocals(List<LocalVariable> initialLocals) {
        for (LocalVariable initialLocal : initialLocals) {
            localsMap.addLocal(initialLocal.getDescriptor());
            maxLocals++;
            if (initialLocal.isDouble() || initialLocal.isLong()) {
                localsMap.addLocal(initialLocal.getDescriptor());
                maxLocals++;
            }
        }
    }

    public void visitLabel(Label label) {
        mv.visitLabel(label);
        visitedLabelsStack.addLast(label);
    }

    public Label visitLabel() {
        Label label = new Label();
        mv.visitLabel(label);
        visitedLabelsStack.addLast(label);
        return label;
    }

    public void longLoad(int localIndex) {
        incrStackSize(2);
        stackMap.pushOnStack("J");
        stackMap.pushOnStack("J");
        mv.visitIntInsn(Opcodes.LLOAD, localIndex);
    }

    public void longStore(int localIndex) {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitIntInsn(Opcodes.LSTORE, localIndex);
    }

    public void longSub() {
        stackMap.popFromStack();
        String left = stackMap.popFromStack();
        stackMap.popFromStack();
        String right = stackMap.popFromStack();
        decrStackSize(2);
        assert "J".equals(left) : "left expected long for long sub operation";
        assert "J".equals(right) : "right expected long for long sub operation";
        stackMap.pushOnStack("J");
        mv.visitInsn(Opcodes.LSUB);
    }

    public void longAdd() {
        stackMap.popFromStack();
        String left = stackMap.popFromStack();
        stackMap.popFromStack();
        String right = stackMap.popFromStack();
        decrStackSize(2);
        assert "J".equals(left) : "left expected long for long add operation";
        assert "J".equals(right) : "right expected long for long add operation";
        stackMap.pushOnStack("J");
        mv.visitInsn(Opcodes.LADD);
    }

    public void visitLongDiv() {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(2);
        stackMap.pushOnStack("D");
        stackMap.pushOnStack("D");
        mv.visitInsn(Opcodes.LDIV);
    }

    public void longMul() {
        stackMap.popFromStack();
        String left = stackMap.popFromStack();
        stackMap.popFromStack();
        String right = stackMap.popFromStack();
        decrStackSize(2);
        assert "J".equals(left) : "left expected long for long mul operation";
        assert "J".equals(right) : "right expected long for long mul operation";
        stackMap.pushOnStack("J");
        mv.visitInsn(Opcodes.LMUL);
    }

    public void intLoad(int localIndex) {
        incrStackSize(1);
        stackMap.pushOnStack("I");
        mv.visitIntInsn(Opcodes.ILOAD, localIndex);
    }

    public void visitIntAdd() {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(1);
        stackMap.pushOnStack("I");
        mv.visitInsn(Opcodes.IADD);
    }

    public void visitIntMul() {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(1);
        stackMap.pushOnStack("I");
        mv.visitInsn(Opcodes.IMUL);
    }

    public void visitIntDiv() {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(1);
        stackMap.pushOnStack("I");
        mv.visitInsn(Opcodes.IDIV);
    }

    public void visitFloatDiv() {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(1);
        stackMap.pushOnStack("F");
        mv.visitInsn(Opcodes.FDIV);
    }

    public void visitIntMod() {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(1);
        stackMap.pushOnStack("I");
        mv.visitInsn(Opcodes.IREM);
    }

    public void visitIReturn() {
        String i = stackMap.popFromStack();
        decrStackSize(1);
        mv.visitInsn(Opcodes.IRETURN);
    }

    public void visitLocals(List<LocalVariable> localVariableList) {
        for (var localVariable : localVariableList) {
            mv.visitLocalVariable(
                    localVariable.getName(),
                    localVariable.getDescriptor(),
                    localVariable.getSignature(),
                    localVariable.getStart().getLabel(),
                    localVariable.getEnd().getLabel(),
                    localVariable.getIndex()
            );
        }
    }

    public void visitMax() {
        try {
            mv.visitMaxs(maxStackSize, 12);
        } catch (Exception e) {
            var cw = this.compilationContext.currentClassWriter();
            var bytes = cw.getBytes();
            ByteCodeLogger.logASM(bytes);
            throw new RuntimeException(e);
        }
    }

    private void incrStackSize(int value) {
        currentStackSize+=value;
        if (currentStackSize > maxStackSize) {
            maxStackSize = currentStackSize;
        }
    }

    private void decrStackSize(int value) {
        currentStackSize-=value;
    }

    public void visitEnd() {
        mv.visitEnd();
    }

    public void visitCode() {
        mv.visitCode();
    }

    public void visitReturn() {
        mv.visitInsn(Opcodes.RETURN);
    }

    public void aload(int i) {
        incrStackSize(1);
        stackMap.pushOnStack("Ljava/lang/Object;");
        mv.visitVarInsn(Opcodes.ALOAD, i);
    }

    public void invokeSpecial(String owner, String name, String descriptor) {
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, owner, name, descriptor, false);
        incrStackSize(1);
        stackMap.pushOnStack(owner);
    }

    public void visitMaxes(int stack, int locals) {
        mv.visitMaxs(stack, locals);
    }

    public void visitLocalVariable(String name,
                                   String typeDescriptor,
                                   String signature,
                                   Label start,
                                   Label end,
                                   int index) {
        mv.visitLocalVariable(name, typeDescriptor, signature, start, end, index);
    }

    public void visitAReturn() {
        mv.visitInsn(Opcodes.ARETURN);
    }

    public void intStore(int index) {
        //stackMap.popFromStack();
        mv.visitVarInsn(Opcodes.ISTORE, index);
    }

    public void getStatic(Type owner, String name, Type fieldType) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, owner.getInternalName(), name, fieldType.getDescriptor());

        if (fieldType.isDouble() || fieldType.isLong()) {
            incrStackSize(2);
            stackMap.pushOnStack(fieldType.getDescriptor());
        } else {
            incrStackSize(1);
        }
        stackMap.pushOnStack(fieldType.getDescriptor());
    }

    public void getField(Type owner, String name, Type fieldType) {
        stackMap.popFromStack();
        decrStackSize(1);

        mv.visitFieldInsn(Opcodes.GETFIELD, owner.getInternalName(), name, fieldType.getDescriptor());

        if (fieldType.isDouble() || fieldType.isLong()) {
            incrStackSize(2);
            stackMap.pushOnStack(fieldType.getDescriptor());
        } else {
            incrStackSize(1);
        }
        stackMap.pushOnStack(fieldType.getDescriptor());
    }

    public void visitConstant(Type typeOfConstant, Object constant) {
        if (typeOfConstant.isLong()) {
            long l = (Long) constant;
            if (l == 0L) {
                mv.visitInsn(Opcodes.LCONST_0);
            } else if (l == 1L) {
                mv.visitInsn(Opcodes.LCONST_1);
            } else {
                mv.visitLdcInsn(constant);
            }
        } else if (typeOfConstant.isDouble()) {
            double d = (Double) constant;
            if (d == 0.0d) {
                mv.visitInsn(Opcodes.DCONST_0);
            } else if (d == 1.0d) {
                mv.visitInsn(Opcodes.DCONST_1);
            } else {
                mv.visitLdcInsn(constant);
            }
        } else if (typeOfConstant.isFloat()) {
            float d = (Float) constant;
            if (d == 0.0f) {
                mv.visitInsn(Opcodes.FCONST_0);
            } else if (d == 1.0f) {
                mv.visitInsn(Opcodes.FCONST_1);
            } else if (d == 2.0f) {
                mv.visitInsn(Opcodes.FCONST_2);
            } else {
                mv.visitLdcInsn(constant);
            }
        } else {
            mv.visitLdcInsn(constant);
        }

        if (typeOfConstant.isDouble() || typeOfConstant.isLong()) {
            incrStackSize(2);
            stackMap.pushOnStack(typeOfConstant.getDescriptor());
        } else {
            incrStackSize(1);
        }
        stackMap.pushOnStack(typeOfConstant.getDescriptor());
    }

    public void invokeInstance(Type owner,
                               String name,
                               String descriptor) {
        invokeInstance(owner.getInternalName(),
                name,
                descriptor,
                JVMVoidType.INSTANCE,
                Collections.emptyList(),
                false);
    }

    public void invokeInstance(String owner,
                               String name,
                               String descriptor,
                               boolean itf) {
        invokeInstance(owner,
                name,
                descriptor,
                JVMVoidType.INSTANCE,
                Collections.emptyList(),
                itf);
    }

    public void invokeInstance(Type owner,
                               String name,
                               String descriptor,
                               Type returnType,
                               List<Type> argsTypes) {
        invokeInstance(owner.getInternalName(), name, descriptor, returnType, argsTypes, false);
    }

    public void invokeInstance(String internalName,
                               String name,
                               String descriptor,
                               Type returnType,
                               List<Type> argsTypes,
                               boolean isInterface) {
        //decrStackSize(1);
        //stackMap.popFromStack();//instance

        for (Type argType : argsTypes) {
            if (argType.isLong() || argType.isDouble()) {
                decrStackSize(1);
                stackMap.popFromStack();
            }

            decrStackSize(1);
            stackMap.popFromStack();
        }
        if (isInterface) {
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, internalName, name, descriptor, true);
        } else {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, internalName, name, descriptor, false);
        }


        if (returnType.isDouble() || returnType.isLong()) {
            incrStackSize(1);
            stackMap.pushOnStack(returnType.getDescriptor());
        }

        incrStackSize(1);
        stackMap.pushOnStack(returnType.getDescriptor());
    }

    public void callNew(Type type) {
        mv.visitTypeInsn(Opcodes.NEW, type.getInternalName());
        incrStackSize(1);
        stackMap.pushOnStack(type.getDescriptor());
    }

    public void dup() {
        if (stackMap.last().equals("D") || stackMap.last().equals("J")) {
            incrStackSize(1);
            stackMap.pushOnStack(stackMap.last());
        }
        incrStackSize(1);
        stackMap.pushOnStack(stackMap.last());
        mv.visitInsn(Opcodes.DUP);
    }

    public void invokeStatic(String internalName,
                             String name,
                             String descriptor) {
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, internalName, name, descriptor, false);
    }


    public void invokeStatic(String internalName,
                             String name,
                             String descriptor,
                             Type type,
                             List<Type> argsTypes,
                             boolean isInterface) {

        for (Type argType : argsTypes) {
            if (argType.isLong() || argType.isDouble()) {
                decrStackSize(1);
                stackMap.popFromStack();
            }

            decrStackSize(1);
            stackMap.popFromStack();
        }
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, internalName, name, descriptor, isInterface);

        if (type.isDouble() || type.isLong()) {
            incrStackSize(1);
            stackMap.pushOnStack(type.getDescriptor());
        }

        incrStackSize(1);
        stackMap.pushOnStack(type.getDescriptor());
    }

    public void pushInt(int value) {
        switch (value) {
            case -1:
                mv.visitInsn(Opcodes.ICONST_M1);
                break;
            case 0:
                mv.visitInsn(Opcodes.ICONST_0);
                break;
            case 1:
                mv.visitInsn(Opcodes.ICONST_1);
                break;
            case 2:
                mv.visitInsn(Opcodes.ICONST_2);
                break;
            case 3:
                mv.visitInsn(Opcodes.ICONST_3);
                break;
            case 4:
                mv.visitInsn(Opcodes.ICONST_4);
                break;
            case 5:
                mv.visitInsn(Opcodes.ICONST_5);
                break;
            default:
                if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
                    mv.visitIntInsn(Opcodes.BIPUSH, value);
                } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
                    mv.visitIntInsn(Opcodes.SIPUSH, value);
                } else {
                    mv.visitLdcInsn(value);
                }
        }
        incrStackSize(1);
        stackMap.pushOnStack(JVMIntType.INSTANCE.getDescriptor());
    }

    public void objectStore(int index) {
        stackMap.popFromStack();
        mv.visitVarInsn(Opcodes.ASTORE, index);
    }

    public void objectStoreIgnoreStack(int index) {
        mv.visitVarInsn(Opcodes.ASTORE, index);
    }

    public void checkCast(Type type) {
        stackMap.popFromStack();
        mv.visitTypeInsn(Opcodes.CHECKCAST, type.getInternalName());
        stackMap.pushOnStack(type.getDescriptor());
    }

    public void visitLReturn() {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(2);
        mv.visitInsn(Opcodes.LRETURN);
    }

    public void visitDReturn() {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(2);
        mv.visitInsn(Opcodes.DRETURN);
    }

    public void visitFReturn() {
        stackMap.popFromStack();
        decrStackSize(1);
        mv.visitInsn(Opcodes.FRETURN);
    }

    public void floatStore(int index) {
        stackMap.popFromStack();
        decrStackSize(1);
        mv.visitVarInsn(Opcodes.FSTORE, index);
    }

    public void doubleStore(int index) {
        stackMap.popFromStack();
        stackMap.popFromStack();
        decrStackSize(2);
        mv.visitVarInsn(Opcodes.DSTORE, index);
    }

    public void floatLoad(int index) {
        incrStackSize(1);
        stackMap.pushOnStack(JVMFloatType.INSTANCE.getDescriptor());
        mv.visitVarInsn(Opcodes.FLOAD, index);
    }

    public void doubleLoad(int index) {
        incrStackSize(2);
        stackMap.pushOnStack(JVMDoubleType.INSTANCE.getDescriptor());
        stackMap.pushOnStack(JVMDoubleType.INSTANCE.getDescriptor());
        mv.visitVarInsn(Opcodes.DLOAD, index);
    }

    public void incr(int index, int value) {
        mv.visitIincInsn(index, value);
    }

    public void visitDoubleAdd() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.DADD);
    }

    public void visitFloatAdd() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.FADD);
    }

    public void visitLongAdd() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LADD);
    }

    public void visitIntSub() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.ISUB);
    }

    public void visitIntNeg() {
        mv.visitInsn(Opcodes.INEG);
    }

    public void visitFloatSub() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.FSUB);
    }

    public void visitFloatNeg() {
        mv.visitInsn(Opcodes.FNEG);
    }

    public void visitDoubleSub() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.DSUB);
    }

    public void visitDoubleNeg() {
        mv.visitInsn(Opcodes.DNEG);
    }

    public void visitLongSub() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LSUB);
    }

    public void visitLongNeg() {
        mv.visitInsn(Opcodes.LNEG);
    }

    public void visitPop() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.POP);
    }

    public void visitPop2() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.POP2);
    }

    public void visitNull() {
        incrStackSize(1);
        stackMap.pushOnStack("NULL");
        mv.visitInsn(Opcodes.ACONST_NULL);
    }

    public void newArray(Type type) {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitTypeInsn(Opcodes.ANEWARRAY, type.getInternalName());
        incrStackSize(1);
        stackMap.pushOnStack("L" + type.getInternalName());
    }

    public void newIntArray() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
        incrStackSize(1);
        stackMap.pushOnStack("[I");
    }

    public void newCharArray() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_CHAR);
        incrStackSize(1);
        stackMap.pushOnStack("[C");
    }

    public void visitIntArrayStore() {
        decrStackSize(3);
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.IASTORE);
    }

    public void visitShortArrayStore() {
        decrStackSize(3);
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.SASTORE);
    }

    public void visitByteArrayStore() {
        decrStackSize(3);
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.BASTORE);
    }

    public void visitFloatArrayStore() {
        decrStackSize(3);
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.FASTORE);
    }

    public void visitArrayStore() {
        decrStackSize(3);
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.AASTORE);
    }

    public void visitArrayIntGet() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.IALOAD);
        incrStackSize(1);
        stackMap.pushOnStack(JVMIntType.INSTANCE.getInternalName());
    }

    public void visitArrayShortGet() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.SALOAD);
        incrStackSize(1);
        stackMap.pushOnStack(JVMIntType.INSTANCE.getInternalName());
    }

    public void visitArrayByteGet() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.BALOAD);
        incrStackSize(1);
        stackMap.pushOnStack(JVMIntType.INSTANCE.getInternalName());
    }

    public void visitArrayGet() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.AALOAD);
        incrStackSize(1);
        stackMap.pushOnStack("Ljava/lang/Object;");
    }

    public void visitArrayDoubleGet() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.DALOAD);
        incrStackSize(2);
        stackMap.pushOnStack(JVMDoubleType.INSTANCE.getInternalName());
        stackMap.pushOnStack(JVMDoubleType.INSTANCE.getInternalName());
    }

    public void visitArrayFloatGet() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.FALOAD);
        incrStackSize(1);
        stackMap.pushOnStack(JVMFloatType.INSTANCE.getInternalName());
    }

    public void visitArrayLongGet() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LALOAD);
        incrStackSize(2);
        stackMap.pushOnStack(JVMFloatType.INSTANCE.getInternalName());
        stackMap.pushOnStack(JVMFloatType.INSTANCE.getInternalName());
    }

    public void newFloatArray() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_FLOAT);
        incrStackSize(1);
        stackMap.pushOnStack("[F");
    }


    public void newLongArray() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_LONG);
        incrStackSize(1);
        stackMap.pushOnStack("[J");
    }

    public void newDoubleArray() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_DOUBLE);
        incrStackSize(1);
        stackMap.pushOnStack("[D");
    }

    public void newShortArray() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_SHORT);
        incrStackSize(1);
        stackMap.pushOnStack("[S");
    }

    public void newByteArray() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_BYTE);
        incrStackSize(1);
        stackMap.pushOnStack("[B");
    }

    public void newBooleanArray() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitTypeInsn(Opcodes.NEWARRAY, JVMBooleanType.INSTANCE.getInternalName());
        incrStackSize(1);
        stackMap.pushOnStack("[Z");
    }

    public void visitJumpInstruction(int value, @NotNull LabelNode labelNode) {
        if (value == Opcodes.DCMPL) {
            decrStackSize(4);
            stackMap.popFromStack();
            stackMap.popFromStack();
            stackMap.popFromStack();
            stackMap.popFromStack();
            mv.visitInsn(Opcodes.DCMPL);
            incrStackSize(1);
            stackMap.pushOnStack("I");
            return;
        }
        if (value == Opcodes.FCMPL) {
            decrStackSize(2);
            stackMap.popFromStack();
            stackMap.popFromStack();
            mv.visitInsn(Opcodes.FCMPL);
            incrStackSize(1);
            stackMap.pushOnStack("I");
            return;
        }
        if (value == Opcodes.FCMPG) {
            decrStackSize(2);
            stackMap.popFromStack();
            stackMap.popFromStack();
            mv.visitInsn(Opcodes.FCMPG);
            incrStackSize(1);
            stackMap.pushOnStack("I");
            return;
        }
        if (value == Opcodes.IF_ICMPEQ) {
            decrStackSize(2);
            stackMap.popFromStack();
            stackMap.popFromStack();
        }
        if (value == Opcodes.LCMP) {
            decrStackSize(4);
            stackMap.popFromStack();
            stackMap.popFromStack();
            stackMap.popFromStack();
            stackMap.popFromStack();
            mv.visitInsn(Opcodes.LCMP);
            incrStackSize(1);
            stackMap.pushOnStack("I");
            return;
        }
        mv.visitJumpInsn(value, labelNode.getLabel());
    }

    public void visitArrayLength() {
        incrStackSize(1);
        stackMap.pushOnStack("I");
        mv.visitInsn(Opcodes.ARRAYLENGTH);
    }

    public void visitTryCatchBlock(List<TryExpression> tryExpressionList) {
        for (var tryExpression : tryExpressionList) {
            var catchExpression = (CatchExpression) tryExpression.getCatchExpression();
            for (FunctionParameter functionParameter : catchExpression.getArgs().getFunctionParameters()) {
                String internalName = functionParameter.getTypeNode().getType().getInternalName();
                mv.visitTryCatchBlock(
                        Objects.requireNonNull(tryExpression.getStartLabel()).getLabel(),
                        Objects.requireNonNull(tryExpression.getEndLabel()).getLabel(),
                        Objects.requireNonNull(catchExpression.getStartLabel()).getLabel(),
                        Objects.requireNonNull(internalName));
            }
        }
    }

    public void visitDoubleDiv() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.DDIV);
    }

    public void visitDoubleMul() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.DMUL);
    }

    public void visitLongMul() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LMUL);
    }

    public void visitFloatMul() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.FMUL);
    }

    public void visitIntToDouble() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.I2D);
        incrStackSize(2);
        stackMap.pushOnStack("D");
        stackMap.pushOnStack("D");
    }

    public void visitIntToFloat() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.I2F);
        incrStackSize(1);
        stackMap.pushOnStack("F");
    }

    public void visitFloatToDouble() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.F2D);
        incrStackSize(2);
        stackMap.pushOnStack("D");
        stackMap.pushOnStack("D");
    }

    public void visitIntToByte() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.I2B);
        incrStackSize(1);
        stackMap.pushOnStack("B");
    }

    public void visitArrayCharGet() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.CALOAD);
        incrStackSize(1);
        stackMap.pushOnStack(JVMCharType.INSTANCE.getInternalName());
    }

    public void visitCharArrayStore() {
        decrStackSize(3);
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.CASTORE);
    }

    public void visitIntToChar() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.I2C);
        incrStackSize(1);
        stackMap.pushOnStack("C");
    }

    public void visitIntToLong() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.I2L);
        incrStackSize(2);
        stackMap.pushOnStack("J");
        stackMap.pushOnStack("J");
    }

    public void charToLong() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.I2L);
        incrStackSize(2);
        stackMap.pushOnStack("J");
        stackMap.pushOnStack("J");
    }

    public void visitDoubleArrayStore() {
        decrStackSize(4);
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.DASTORE);
    }

    public void visitLongArrayStore() {
        decrStackSize(4);
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LASTORE);
    }

    public void doubleToFloat() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.D2F);
        incrStackSize(1);
        stackMap.pushOnStack("F");
    }

    public void doubleToLong() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.D2L);
        incrStackSize(1);
        incrStackSize(1);
        stackMap.pushOnStack("J");
        stackMap.pushOnStack("J");
    }

    public void doubleToInt() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.D2I);
        incrStackSize(1);
        stackMap.pushOnStack("I");
    }

    public void visitFloatToLong() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.F2L);
        incrStackSize(2);
        stackMap.pushOnStack("J");
        stackMap.pushOnStack("J");
    }

    public void visitFloatToInt() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.F2I);
        incrStackSize(1);
        stackMap.pushOnStack("I");
    }

    public void visitIntRem() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.IREM);
    }

    public void visitFloatRem() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.FREM);
    }

    public void visitDoubleRem() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.DREM);
    }

    public void visitLongRem() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LREM);
    }

    public void visitIntToShort() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.I2S);
        incrStackSize(1);
        stackMap.pushOnStack("I");
    }

    public void visitIntAnd() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.IAND);
        incrStackSize(1);
        stackMap.pushOnStack("I");
    }

    public void visitLongAnd() {
        decrStackSize(1);
        decrStackSize(1);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LAND);
    }

    public void longToDouble() {
        decrStackSize(1);
        decrStackSize(1);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.L2D);
        incrStackSize(1);
        stackMap.pushOnStack("D");
        incrStackSize(1);
        stackMap.pushOnStack("D");
    }

    public void longToInt() {
        decrStackSize(1);
        decrStackSize(1);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.L2I);
        incrStackSize(1);
        stackMap.pushOnStack("I");
    }

    public void longToFloat() {
        decrStackSize(1);
        decrStackSize(1);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.L2F);
        incrStackSize(1);
        stackMap.pushOnStack("F");
    }

    public void dup2() {
        mv.visitInsn(Opcodes.DUP2);
        final String last = stackMap.last();
        incrStackSize(1);
        incrStackSize(1);
        stackMap.pushOnStack(last);
        stackMap.pushOnStack(last);
    }

    public void visitIntOr() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.IOR);
    }

    public void visitLongOr() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LOR);
    }

    public void visitIntXor() {
        decrStackSize(1);
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.IXOR);
    }

    public void visitLongXor() {
        decrStackSize(2);
        stackMap.popFromStack();
        stackMap.popFromStack();
        mv.visitInsn(Opcodes.LXOR);
    }

    public void instanceOf(@NotNull Type type) {
        mv.visitTypeInsn(Opcodes.INSTANCEOF, type.getInternalName());
    }

    public void visitInvokeDynamicInsn(@NotNull String name,
                                       @NotNull String invokeDynamicSignature,
                                       @NotNull Handle lambdaMetaFactoryHandler,
                                       @NotNull org.objectweb.asm.Type methodType,
                                       @NotNull Handle targetFunctionHandler,
                                       @NotNull org.objectweb.asm.Type methodTyp2,
                                       int size,
                                       @NotNull Type instance) {
        mv.visitInvokeDynamicInsn(
                name,
                invokeDynamicSignature,
                lambdaMetaFactoryHandler,
                methodType,
                targetFunctionHandler,
                methodTyp2
        );

        incrStackSize(1);
        stackMap.pushOnStack(methodTyp2.getDescriptor());
    }

    public void invokeInterface(@NotNull String owner,
                                @NotNull String name,
                                @NotNull String descriptor,
                                @NotNull Type returnType,
                                @NotNull List<? extends Type> parameterTypes) {
        parameterTypes.forEach(this::pop);
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, owner, name, descriptor, true);
        if (returnType.isDouble() || returnType.isLong()) {
            incrStackSize(2);
            stackMap.pushOnStack(returnType.getDescriptor());
        } else {
            incrStackSize(1);
        }
        stackMap.pushOnStack(returnType.getDescriptor());
    }

    public void visitIntShiftLeft() {
        mv.visitInsn(Opcodes.ISHL);
    }

    public void visitLongShiftLeft() {
        mv.visitInsn(Opcodes.LSHL);
    }

    public void visitIntShiftRight() {
        mv.visitInsn(Opcodes.ISHR);
    }

    public void visitLongShiftRight() {
        mv.visitInsn(Opcodes.LSHR);
    }

    public void visitIntUnsignedShiftRight() {
        mv.visitInsn(Opcodes.IUSHR);
    }

    public void visitLongUnsignedShiftRight() {
        mv.visitInsn(Opcodes.LUSHR);
    }

    private void pop(Type type) {
        if (type.isDouble() || type.isLong()) {
            stackMap.popFromStack();
            stackMap.popFromStack();
        } else {
            stackMap.popFromStack();
        }
    }

    public void visitThis(Type genericType) {
        stackMap.pushOnStack(genericType.getCanonicalName());
        mv.visitVarInsn(Opcodes.ALOAD, 0);
    }
}
