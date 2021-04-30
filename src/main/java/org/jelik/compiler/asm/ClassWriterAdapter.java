package org.jelik.compiler.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

/**
 * @author Marcin Bukowiecki
 */
public class ClassWriterAdapter {

    private final ClassWriter cw;

    private MethodVisitorAdapter currentMethodVisitor;

    public ClassWriterAdapter(ClassWriter cw) {
        this.cw = cw;
    }

    public MethodVisitor visitMethod(int access,
                                     String name,
                                     String descriptor,
                                     String signature,
                                     String[] exceptions) {
        return cw.visitMethod(access, name, descriptor, signature, exceptions);
    }

    public byte[] getBytes() {
        return cw.toByteArray();
    }

    public void visitEnd() {
        cw.visitEnd();
    }

    public void visitStart() {

    }

    public void setCurrentMethodVisitor(MethodVisitorAdapter currentMethodVisitor) {
        this.currentMethodVisitor = currentMethodVisitor;
    }

    public MethodVisitorAdapter getCurrentMethodVisitor() {
        return currentMethodVisitor;
    }
}
