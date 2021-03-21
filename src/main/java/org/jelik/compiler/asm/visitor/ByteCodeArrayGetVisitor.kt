package org.jelik.compiler.asm.visitor

import org.jelik.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.types.JVMBooleanType
import org.jelik.types.JVMIntType
import org.jelik.types.JVMNullType
import org.jelik.types.JVMObjectType
import org.jelik.types.Type
import org.jelik.types.jvm.JVMByteType
import org.jelik.types.jvm.JVMCharType
import org.jelik.types.jvm.JVMDoubleType
import org.jelik.types.jvm.JVMFloatType
import org.jelik.types.jvm.JVMLongType
import org.jelik.types.jvm.JVMShortType

/**
 * Visitor for getting element from array, i.e. IALOAD, FALOAD, AALOAD, DALOAD etc.
 *
 * @author Marcin Bukowiecki
 */
class ByteCodeArrayGetVisitor(private val mv: MethodVisitorAdapter) : TypeVisitor() {

    override fun visit(type: JVMIntType, compilationContext: CompilationContext) {
        mv.visitArrayIntGet()
    }

    override fun visit(type: JVMCharType, compilationContext: CompilationContext) {
        mv.visitArrayCharGet()
    }

    override fun visit(type: JVMBooleanType, compilationContext: CompilationContext) {
        mv.visitArrayIntGet()
    }

    override fun visit(type: JVMDoubleType, compilationContext: CompilationContext) {
        mv.visitArrayDoubleGet()
    }

    override fun visit(type: JVMLongType, compilationContext: CompilationContext) {
        mv.visitArrayLongGet()
    }

    override fun visit(type: JVMShortType, compilationContext: CompilationContext) {
        mv.visitArrayShortGet()
    }

    override fun visit(type: JVMByteType, compilationContext: CompilationContext) {
        mv.visitArrayByteGet()
    }

    override fun visit(type: JVMFloatType, compilationContext: CompilationContext) {
        mv.visitArrayFloatGet()
    }

    override fun visit(type: JVMObjectType, compilationContext: CompilationContext) {
        mv.visitArrayGet()
    }

    override fun visit(type: Type, compilationContext: CompilationContext) {
        mv.visitArrayGet()
    }
}
