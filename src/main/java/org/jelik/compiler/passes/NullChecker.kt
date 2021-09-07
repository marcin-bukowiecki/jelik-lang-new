package org.jelik.compiler.passes

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.exceptions.CompileException
import org.jelik.compiler.utils.Stateless
import org.jelik.parser.ast.ReturnExpr
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.types.MaybeTypeNode
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
@Stateless
object NullChecker : BasePass() {

    override fun visitReturnExpr(re: ReturnExpr, compilationContext: CompilationContext) {
        super.visitReturnExpr(re, compilationContext)
        val givenType = re.genericReturnType
        val expectedType = checkForMaybe(compilationContext.currentFunction())
        if (!givenType.isNullAssignableTo(expectedType, compilationContext)) {
            throw CompileException("Function must return ${expectedType}, given ${givenType.forErrorMessage()}",
                re,
                compilationContext.currentModule)
        }
    }

    private fun checkForMaybe(functionDeclaration: FunctionDeclaration): Type {
        if (!functionDeclaration.functionReturn.isVoid) {
            val typeNode = functionDeclaration.functionReturn.typeNode
            if (typeNode is MaybeTypeNode) {
                return typeNode.getWrappedMaybeType()
            }
        }
        return functionDeclaration.returnType
    }
}
