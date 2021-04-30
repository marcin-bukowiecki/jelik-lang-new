package org.jelik.parser.ast.functions

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.data.FunctionReferenceMethodData
import org.jelik.compiler.data.MethodData
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.types.InferredLambdaTypeRef
import org.jelik.parser.ast.types.InferredTypeRef
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class LambdaDeclarationExpression(val lambdaDeclaration: LambdaDeclaration) : ASTNodeImpl(), Expression, FunctionReferenceNode {

    override fun getReturnType(): Type {
        TODO("Not yet implemented")
    }

    override fun getGenericReturnType(): Type {
        TODO("Not yet implemented")
    }

    override fun getType(): Type {
        TODO("Not yet implemented")
    }

    override fun getGenericType(): Type {
        TODO("Not yet implemented")
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitLambdaDeclarationExpression(this, compilationContext)
    }

    override fun createInferredTypeRef(): InferredTypeRef {
        return InferredLambdaTypeRef(this)
    }

    override fun setTargetFunction(methodData: MethodData) {

    }

    override fun setFunctionReferenceMethod(data: FunctionReferenceMethodData) {

    }

    override fun getPossibleFunctionsToCall(): List<MethodData> {
        return listOf(lambdaDeclaration)
    }

    override fun getFunctionReferenceMethod(): FunctionReferenceMethodData? {
        TODO("Not yet implemented")
    }
}
