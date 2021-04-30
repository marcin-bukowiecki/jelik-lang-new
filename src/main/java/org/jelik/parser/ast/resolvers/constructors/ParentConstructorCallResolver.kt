package org.jelik.parser.ast.resolvers.constructors

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ReturnExpr
import org.jelik.parser.ast.arguments.ArgumentList
import org.jelik.parser.ast.functions.ConstructorDeclaration
import org.jelik.parser.ast.functions.SuperCallExpr
import org.jelik.parser.ast.resolvers.FunctionCallResolver
import org.jelik.parser.token.keyword.SuperKeyword

/**
 * Class for resolving parent constructor call
 *
 * @author Marcin Bukowiecki
 */
object ParentConstructorCallResolver {

    fun resolveCall(constructorDeclaration: ConstructorDeclaration, compilationContext: CompilationContext) {
        constructorDeclaration.functionBody.basicBlock.let { bb ->
            if (bb.expressions.isEmpty()) {
                constructorDeclaration.functionBody.basicBlock.prependExpression(ReturnExpr.voidReturn())
                val superCall = SuperCallExpr(SuperKeyword(-1, -1),
                        ArgumentList.EMPTY,
                        constructorDeclaration.owner
                        .getParentType(compilationContext).get())
                constructorDeclaration.functionBody.basicBlock.prependExpression(superCall)

                FunctionCallResolver().resolveCall(superCall, compilationContext).ifPresent {
                    targetCall -> superCall.targetFunctionCall = targetCall
                }
                return
            }
        }
        val owner = constructorDeclaration.owner
        owner.getParentType(compilationContext).ifPresentOrElse({parentType ->

        }, {

        })
    }
}
