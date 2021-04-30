package org.jelik.parser.ast.classes

import org.jelik.parser.ast.types.TypeNode

/**
 * @author Marcin Bukowiecki
 */
class ClassContext(private val classDeclaration: ClassDeclaration) {

    private val typeParametersMap = mutableMapOf<String, TypeNode>()

    private val genericTypeParametersMap = mutableMapOf<String, TypeNode>()

    fun getTypeParametersMappings(): Map<String, TypeNode> {
        return this.typeParametersMap
    }

    fun getGenericTypeParametersMappings(): Map<String, TypeNode> {
        return this.genericTypeParametersMap
    }

    fun addGenericTypeParameterMapping(symbol: String, typeNode: TypeNode) {
        this.genericTypeParametersMap[symbol] = typeNode
    }

    fun addTypeParameterMapping(symbol: String, typeNode: TypeNode) {
        this.typeParametersMap[symbol] = typeNode
    }
}
