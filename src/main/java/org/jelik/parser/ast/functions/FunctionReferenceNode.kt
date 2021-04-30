package org.jelik.parser.ast.functions

import org.jelik.compiler.data.FunctionReferenceMethodData
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.ASTNode

/**
 * @author Marcin Bukowiecki
 */
interface FunctionReferenceNode : ASTNode {

    fun setTargetFunction(methodData: MethodData);

    fun setFunctionReferenceMethod(data: FunctionReferenceMethodData)

    fun getPossibleFunctionsToCall(): List<MethodData>

    fun getFunctionReferenceMethod(): FunctionReferenceMethodData?
}
