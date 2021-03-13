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
class WrapperCastVisitor(private val mv: MethodVisitorAdapter) : TypeVisitor() {

    override fun visit(type: JVMIntType, compilationContext: CompilationContext) {
        mv.invokeStatic("java/lang/Integer", "valueOf",
                "(I)Ljava/lang/Integer;",
                Type.of(Integer::class.java), listOf(JVMIntType.INSTANCE), false)
    }

    override fun visit(type: JVMCharType, compilationContext: CompilationContext) {
        mv.invokeStatic("java/lang/Char", "valueOf",
                "(C)Ljava/lang/Char;",
                Type.of(Integer::class.java), listOf(JVMCharType.INSTANCE), false)
    }

    override fun visit(type: JVMBooleanType, compilationContext: CompilationContext) {
        mv.invokeStatic("java/lang/Boolean", "valueOf",
                "(Z)Ljava/lang/Boolean;",
                Type.of(Boolean::class.java), listOf(JVMBooleanType.INSTANCE), false)
    }

    override fun visit(type: JVMDoubleType, compilationContext: CompilationContext) {
        mv.invokeStatic("java/lang/Double", "valueOf",
            "(D)Ljava/lang/Double;",
            Type.of(Integer::class.java), listOf(JVMDoubleType.INSTANCE), false)
    }

    override fun visit(type: JVMLongType, compilationContext: CompilationContext) {
        mv.invokeStatic("java/lang/Long", "valueOf",
                "(J)Ljava/lang/Long;",
                Type.of(Long::class.java), listOf(JVMLongType.INSTANCE), false)
    }

    override fun visit(type: JVMShortType, compilationContext: CompilationContext) {
        mv.invokeStatic("java/lang/Short", "valueOf",
                "(S)Ljava/lang/Short;",
                Type.of(Short::class.java), listOf(JVMShortType.INSTANCE), false)
    }

    override fun visit(type: JVMByteType, compilationContext: CompilationContext) {
        mv.invokeStatic("java/lang/Byte", "valueOf",
                "(I)Ljava/lang/Byte;",
                Type.of(Byte::class.java), listOf(JVMByteType.INSTANCE), false)
    }

    override fun visit(type: JVMFloatType, compilationContext: CompilationContext) {
        mv.invokeStatic("java/lang/Float", "valueOf",
                "(F)Ljava/lang/Float;",
                Type.of(Float::class.java), listOf(JVMFloatType.INSTANCE), false)
    }

    override fun visit(type: JVMObjectType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: Type, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }
}
