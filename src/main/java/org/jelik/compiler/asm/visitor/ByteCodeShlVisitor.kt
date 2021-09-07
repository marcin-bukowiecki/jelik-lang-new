package org.jelik.compiler.asm.visitor

import org.jelik.compiler.CompilationContext
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
 * Visitor for shift left operator
 *
 * @author Marcin Bukowiecki
 */
class ByteCodeShlVisitor(private val mv: MethodVisitorAdapter) : TypeVisitor() {

    override fun visit(type: JVMIntType, compilationContext: CompilationContext) {
        mv.visitIntShiftLeft()
    }

    override fun visit(type: JVMCharType, compilationContext: CompilationContext) {
        mv.visitIntShiftLeft()
    }

    override fun visit(type: JVMBooleanType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMDoubleType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMLongType, compilationContext: CompilationContext) {
        mv.visitLongShiftLeft()
    }

    override fun visit(type: JVMShortType, compilationContext: CompilationContext) {
        mv.visitIntShiftLeft()
    }

    override fun visit(type: JVMByteType, compilationContext: CompilationContext) {
        mv.visitIntShiftLeft()
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
