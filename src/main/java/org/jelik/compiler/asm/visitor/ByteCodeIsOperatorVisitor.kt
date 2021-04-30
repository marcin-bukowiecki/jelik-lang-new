package org.jelik.compiler.asm.visitor

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.parser.ast.operators.IsExpr
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
 * Visitor instanceof operator
 *
 * @author Marcin Bukowiecki
 */
class ByteCodeIsOperatorVisitor(private val mv: MethodVisitorAdapter, private val subject: IsExpr) : TypeVisitor() {

    override fun visit(type: JVMIntType, compilationContext: CompilationContext) {
        if (subject.left.genericReturnType.isPrimitive) {
            subject.left.genericReturnType.accept(WrapperCastVisitor(mv), compilationContext)
        }
        mv.instanceOf(type.wrapperType)
    }

    override fun visit(type: JVMCharType, compilationContext: CompilationContext) {
        if (subject.left.genericReturnType.isPrimitive) {
            subject.left.genericReturnType.accept(WrapperCastVisitor(mv), compilationContext)
        }

        mv.instanceOf(type.wrapperType)
    }

    override fun visit(type: JVMBooleanType, compilationContext: CompilationContext) {
        if (subject.left.genericReturnType.isPrimitive) {
            subject.left.genericReturnType.accept(WrapperCastVisitor(mv), compilationContext)
        }

        mv.instanceOf(type.wrapperType)
    }

    override fun visit(type: JVMDoubleType, compilationContext: CompilationContext) {
        if (subject.left.genericReturnType.isPrimitive) {
            subject.left.genericReturnType.accept(WrapperCastVisitor(mv), compilationContext)
        }

        mv.instanceOf(type.wrapperType)
    }

    override fun visit(type: JVMLongType, compilationContext: CompilationContext) {
        if (subject.left.genericReturnType.isPrimitive) {
            subject.left.genericReturnType.accept(WrapperCastVisitor(mv), compilationContext)
        }

        mv.instanceOf(type.wrapperType)
    }

    override fun visit(type: JVMShortType, compilationContext: CompilationContext) {
        if (subject.left.genericReturnType.isPrimitive) {
            subject.left.genericReturnType.accept(WrapperCastVisitor(mv), compilationContext)
        }

        mv.instanceOf(type.wrapperType)
    }

    override fun visit(type: JVMByteType, compilationContext: CompilationContext) {
        if (subject.left.genericReturnType.isPrimitive) {
            subject.left.genericReturnType.accept(WrapperCastVisitor(mv), compilationContext)
        }

        mv.instanceOf(type.wrapperType)
    }

    override fun visit(type: JVMFloatType, compilationContext: CompilationContext) {
        if (subject.left.genericReturnType.isPrimitive) {
            subject.left.genericReturnType.accept(WrapperCastVisitor(mv), compilationContext)
        }

        mv.instanceOf(type.wrapperType)
    }

    override fun visit(type: JVMObjectType, compilationContext: CompilationContext) {
        mv.instanceOf(type);
    }

    override fun visit(type: Type, compilationContext: CompilationContext) {
        mv.instanceOf(type);
    }
}
