package org.jelik.compiler.asm;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class ClassWriterAdapter {

    private final ClassWriter cw;

    @Setter
    @Getter
    private MethodVisitorAdapter currentMethodVisitor;

    public ClassWriterAdapter(ClassWriter cw) {
        this.cw = cw;
    }

    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
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
}
