package org.jelik.parser.ast.types

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.QuestionMarkToken
import org.jelik.types.MaybeType
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class MaybeTypeNode(private val inner: TypeNode, private val questionMarkToken: QuestionMarkToken) : TypeNode() {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        inner.accept(astVisitor, compilationContext)
    }

    fun getWrappedMaybeType(): Type {
        return MaybeType(inner.type)
    }

    override fun getType(): Type? {
        return inner.type
    }

    override fun getGenericType(): Type? {
        return inner.genericType
    }

    override fun getStartCol(): Int {
        return inner.startCol
    }

    override fun getStartRow(): Int {
        return inner.startRow
    }

    override fun getEndRow(): Int {
        return questionMarkToken.endRow
    }

    override fun getEndCol(): Int {
        return questionMarkToken.endCol
    }

    override fun getSymbol(): String {
        return inner.symbol
    }

    fun getInner(): TypeNode {
        return inner
    }

    override fun setType(type: Type) {
        inner.type = type
        nodeContext.type = MaybeType(type)
    }

    override fun setGenericType(type: Type) {
        inner.genericType = type
        nodeContext.genericType = MaybeType(type)
    }

    override fun isMaybe(): Boolean {
        return true;
    }

    fun liftToWrapper() {
        setType(type!!.wrapperType)
        setGenericType(type!!.wrapperType)
    }

    override fun toString(): String {
        return "$inner?"
    }
}
