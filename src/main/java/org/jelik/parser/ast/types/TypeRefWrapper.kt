package org.jelik.parser.ast.types

import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class TypeRefWrapper(private var wrappedType: Type) : AbstractTypeRef() {

    override fun getType(): Type {
        return wrappedType
    }

    override fun getGenericType(): Type {
        return wrappedType
    }

    override fun setType(type: Type) {
        wrappedType = type
    }

    override fun setGenericType(type: Type) {
        wrappedType = type
    }
}
