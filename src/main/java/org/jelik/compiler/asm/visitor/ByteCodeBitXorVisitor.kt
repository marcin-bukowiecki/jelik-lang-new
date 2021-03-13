package org.jelik.compiler.asm.visitor

import org.jelik.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.types.JVMBooleanType
import org.jelik.types.JVMIntType
import org.jelik.types.JVMObjectType
import org.jelik.types.Type
import org.jelik.types.jvm.JVMByteType
import org.jelik.types.jvm.JVMCharType
import org.jelik.types.jvm.JVMDoubleType
import org.jelik.types.jvm.JVMFloatType
import org.jelik.types.jvm.JVMLongType
import org.jelik.types.jvm.JVMShortType

/**
 * Visitor for bit xor operation
 *
 * @author Marcin Bukowiecki
 */
class ByteCodeBitXorVisitor(private val mv: MethodVisitorAdapter) : TypeVisitor() {

    override fun visit(type: JVMIntType, compilationContext: CompilationContext) {
        mv.visitIntXor()
    }

    override fun visit(type: JVMCharType, compilationContext: CompilationContext) {
        mv.visitIntXor()
    }

    override fun visit(type: JVMBooleanType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMDoubleType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMLongType, compilationContext: CompilationContext) {
        mv.visitLongXor()
    }

    override fun visit(type: JVMShortType, compilationContext: CompilationContext) {
        mv.visitIntXor()
    }

    override fun visit(type: JVMByteType, compilationContext: CompilationContext) {
        mv.visitIntXor()
    }

    override fun visit(type: JVMFloatType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMObjectType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: Type, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }
}
