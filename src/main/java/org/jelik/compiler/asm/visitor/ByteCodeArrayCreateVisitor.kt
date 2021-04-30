package org.jelik.compiler.asm.visitor

import org.jelik.compiler.config.CompilationContext
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
 * @author Marcin Bukowiecki
 */
class ByteCodeArrayCreateVisitor(private val mv: MethodVisitorAdapter) : TypeVisitor() {

    override fun visit(type: JVMIntType, compilationContext: CompilationContext) {
        mv.newIntArray()
    }

    override fun visit(type: JVMCharType, compilationContext: CompilationContext) {
        mv.newCharArray()
    }

    override fun visit(type: JVMBooleanType, compilationContext: CompilationContext) {
        mv.newBooleanArray()
    }

    override fun visit(type: JVMDoubleType, compilationContext: CompilationContext) {
        mv.newDoubleArray()
    }

    override fun visit(type: JVMLongType, compilationContext: CompilationContext) {
        mv.newLongArray()
    }

    override fun visit(type: JVMShortType, compilationContext: CompilationContext) {
        mv.newShortArray()
    }

    override fun visit(type: JVMByteType, compilationContext: CompilationContext) {
        mv.newByteArray()
    }

    override fun visit(type: JVMFloatType, compilationContext: CompilationContext) {
        mv.newFloatArray()
    }

    override fun visit(type: JVMObjectType, compilationContext: CompilationContext) {
        mv.newArray(type)
    }

    override fun visit(type: Type, compilationContext: CompilationContext) {
        mv.newArray(type)
    }
}
