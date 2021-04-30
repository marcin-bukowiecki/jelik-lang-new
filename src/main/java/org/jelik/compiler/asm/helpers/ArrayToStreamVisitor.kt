package org.jelik.compiler.asm.helpers

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.TypeVisitor
import org.jelik.parser.ast.expression.Expression
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
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
class ArrayToStreamVisitor(private val mv: MethodVisitorAdapter, private val subject: Expression) : TypeVisitor() {

    override fun visit(type: JVMIntType, compilationContext: CompilationContext) {
        mv.invokeStatic(
                Type.of(Arrays::class.java).internalName,
                "stream",
                "([I)Ljava/util/stream/IntStream;"
        )
        mv.invokeInstance(
                "java/util/stream/IntStream",
                "boxed",
                "()Ljava/util/stream/Stream;",
                true
        )
    }

    override fun visit(type: JVMCharType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMBooleanType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMDoubleType, compilationContext: CompilationContext) {
        mv.invokeStatic(
                Type.of(Arrays::class.java).internalName,
                "stream",
                "([D)Ljava/util/stream/DoubleStream;"
        )
        mv.invokeInstance(
                "java/util/stream/DoubleStream",
                "boxed",
                "()Ljava/util/stream/Stream;",
                true
        )
    }

    override fun visit(type: JVMLongType, compilationContext: CompilationContext) {
        mv.invokeStatic(
                Type.of(Arrays::class.java).internalName,
                "stream",
                "([J)Ljava/util/stream/LongStream;"
        )
        mv.invokeInstance(
                "java/util/stream/LongStream",
                "boxed",
                "()Ljava/util/stream/Stream;",
                true
        )
    }

    override fun visit(type: JVMShortType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMByteType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMFloatType, compilationContext: CompilationContext) {
        throw UnsupportedOperationException()
    }

    override fun visit(type: JVMObjectType, compilationContext: CompilationContext) {
        mv.invokeStatic(
                Type.of(Arrays::class.java).internalName,
                "stream",
                "([Ljava/lang/Object;)Ljava/util/stream/Stream;"
        )
    }

    override fun visit(type: Type, compilationContext: CompilationContext) {
        mv.invokeStatic(
                Type.of(Arrays::class.java).internalName,
                "stream",
                "([Ljava/lang/Object;)Ljava/util/stream/Stream;"
        )
    }
}
