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
 * @author Marcin Bukowiecki
 */
class ByteCodeArrayStoreVisitor(private val mv: MethodVisitorAdapter) : TypeVisitor() {

    override fun visit(type: JVMIntType, compilationContext: CompilationContext) {
        mv.visitIntArrayStore()
    }

    override fun visit(type: JVMCharType, compilationContext: CompilationContext) {
        mv.visitCharArrayStore()
    }

    override fun visit(type: JVMBooleanType, compilationContext: CompilationContext) {
        mv.visitIntArrayStore()
    }

    override fun visit(type: JVMDoubleType, compilationContext: CompilationContext) {
        mv.visitDoubleArrayStore()
    }

    override fun visit(type: JVMLongType, compilationContext: CompilationContext) {
        mv.visitLongArrayStore()
    }

    override fun visit(type: JVMShortType, compilationContext: CompilationContext) {
        mv.visitShortArrayStore()
    }

    override fun visit(type: JVMByteType, compilationContext: CompilationContext) {
        mv.visitByteArrayStore()
    }

    override fun visit(type: JVMFloatType, compilationContext: CompilationContext) {
        mv.visitFloatArrayStore()
    }

    override fun visit(type: JVMObjectType, compilationContext: CompilationContext) {
        mv.visitArrayStore()
    }

    override fun visit(type: Type, compilationContext: CompilationContext) {
        mv.visitArrayStore()
    }
}
