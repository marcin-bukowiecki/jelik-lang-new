package org.jelik.parser.ast.types

import org.jelik.parser.ast.functions.LambdaDeclarationExpression
import org.jelik.types.Type

/**
 * Container referencing lambda function
 *
 * @author Marcin Bukowiecki
 */
class InferredLambdaTypeRef(ref: LambdaDeclarationExpression) : InferredTypeRef(ref) {

    override fun isFunctionReference(): Boolean {
        return true
    }

    override fun getType(): Type {
        return (ref as LambdaDeclarationExpression).lambdaDeclaration.functionType
    }
}
