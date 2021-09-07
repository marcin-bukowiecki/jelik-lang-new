package org.jelik.compiler.types

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.functions.FunctionCallExpr
import org.jelik.parser.ast.functions.LambdaDeclarationExpression
import org.jelik.parser.ast.functions.providers.LambdaTargetFunctionCallProvider
import org.jelik.parser.ast.resolvers.types.TypeResolver
import org.jelik.parser.ast.types.InferredTypeNode
import org.jelik.parser.ast.utils.ASTDataKey
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
object LambdaTypeInference : AstVisitor() {

    fun resolve(caller: LambdaTargetFunctionCallProvider, functionCallExpr: FunctionCallExpr, ctx: CompilationContext) {
        val argumentTypes = functionCallExpr.argumentTypes
        val ref = caller.functionReference()
        ref.putData(ASTDataKey.PROVIDED_ARGUMENT_TYPES, argumentTypes)
        ref.accept(this, ctx)
    }

    override fun visitLambdaDeclarationExpression(
        lambdaDeclarationExpression: LambdaDeclarationExpression,
        ctx: CompilationContext
    ) {
        val providedArgumentTypes = lambdaDeclarationExpression.getData(ASTDataKey.PROVIDED_ARGUMENT_TYPES)
        for ((index, functionParam) in lambdaDeclarationExpression.lambdaDeclaration.functionParameterList.functionParameters.withIndex()) {
            val typeNode = functionParam.typeNode
            if (typeNode.isInferred) {
                val inferredTypeNode = typeNode as InferredTypeNode
                val providedType = providedArgumentTypes[index]
                inferredTypeNode.type = providedType
                inferredTypeNode.genericType = providedType.deepGenericCopy()
            }
        }
        val lambdaDeclaration = lambdaDeclarationExpression.lambdaDeclaration
        lambdaDeclaration.accept(TypeResolver(), ctx)
        val lambdaReturnType = lambdaDeclaration.functionBody.basicBlock.genericReturnType
        lambdaDeclaration.functionReturn.typeNode.type = lambdaReturnType
        lambdaDeclaration.functionReturn.typeNode.genericType = lambdaReturnType.deepGenericCopy()
    }
}