package org.jelik.parser.ast.functions

import org.jelik.compiler.CompilationContext
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
        return lambdaDeclaration.functionType
    }

    override fun getGenericReturnType(): Type {
        return lambdaDeclaration.functionType
    }

    override fun getType(): Type {
        return lambdaDeclaration.functionType
    }

    override fun getGenericType(): Type {
        return lambdaDeclaration.functionType
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitLambdaDeclarationExpression(this, compilationContext)
    }

    override fun createInferredTypeRef(): InferredTypeRef {
        return InferredLambdaTypeRef(this)
    }

    override fun setTargetFunction(methodData: MethodData) {

    }

    override fun getPossibleFunctionsToCall(): List<MethodData> {
        return listOf(lambdaDeclaration)
    }

    override fun getStartOffset(): Int {
        return lambdaDeclaration.startOffset
    }

    override fun getEndOffset(): Int {
        return lambdaDeclaration.endOffset
    }
}
