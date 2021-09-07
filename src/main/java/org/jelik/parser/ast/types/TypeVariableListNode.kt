package org.jelik.parser.ast.types

import com.google.common.annotations.VisibleForTesting
import org.jelik.parser.token.CommaToken
import org.jelik.parser.token.operators.GreaterOperator
import org.jelik.parser.token.operators.LesserOperator

/**
 * @author Marcin Bukowiecki
 */
class TypeVariableListNode(
    left: LesserOperator,
    types: List<TypeNode>,
    commas: List<CommaToken>,
    right: GreaterOperator
) : TypeParameterListNode(left, types, commas, right) {

    @VisibleForTesting
    constructor(types: List<TypeNode>) :
            this(
                LesserOperator(-1),
                types,
                types.map { CommaToken(-1) },
                GreaterOperator(-1)
            ) {
    }

    companion object {
        val EMPTY = TypeVariableListNode(emptyList())
    }

    fun getText(): String {
        return if (types.isNullOrEmpty()) {
            ""
        } else {
            left.text + types.joinToString(separator = ",") + right.text
        }
    }
}
