package org.jelik.parser.ast.resolvers.generics

import org.jelik.parser.ast.functions.FunctionCallExpr

/**
 * @author Marcin Bukowiecki
 */
object ConstructorTypeParametersMapper {

    fun mapTypeParameters(functionCallExpr: FunctionCallExpr) {
        val constructorReturnType = functionCallExpr.owner.deepGenericCopy()
        val expectedTypeParameters = functionCallExpr.targetFunctionCallProvider.methodData.expectedTypeParameters
        val providedTypes = functionCallExpr.literalExpr.typeParameterListNode.types

        expectedTypeParameters.forEachIndexed {
            index, expectedType ->
            val providedTypeNode = providedTypes[index]
            constructorReturnType.setTypeVariable(expectedType, providedTypeNode.type)
        }

        functionCallExpr.nodeContext.genericType = constructorReturnType
    }
}
