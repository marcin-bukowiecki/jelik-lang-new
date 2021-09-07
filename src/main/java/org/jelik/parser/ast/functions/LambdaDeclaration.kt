package org.jelik.parser.ast.functions

import org.jelik.parser.ast.types.InferredTypeNode
import org.jelik.parser.ast.types.TypeVariableListNode
import org.jelik.parser.token.ArrowToken
import org.jelik.parser.token.LiteralToken
import org.jelik.parser.token.Token
import org.jelik.types.FunctionType
import org.jelik.types.FunctionType.Companion.getFunctionType
import org.jelik.types.JVMVoidType

/**
 * @author Marcin Bukowiecki
 */
class LambdaDeclaration(
    keyword: Token,
    functionParameterList: LambdaParameterList,
    arrowToken: ArrowToken,
    functionBody: FunctionBody
) : FunctionDeclaration(
    keyword,
    LiteralToken("lam"),
    functionParameterList,
    FunctionReturn(arrowToken, InferredTypeNode()),
    functionBody,
    TypeVariableListNode.EMPTY) {

    override fun getFunctionType(): FunctionType {
        if (returnType == null) {
            return getFunctionType(parameterTypes.size, JVMVoidType.INSTANCE)
        }
        return getFunctionType(parameterTypes.size, returnType)
    }

    override fun getStartOffset(): Int {
        return keyword.startOffset
    }

    override fun getEndOffset(): Int {
        return functionBody.endOffset
    }
}
