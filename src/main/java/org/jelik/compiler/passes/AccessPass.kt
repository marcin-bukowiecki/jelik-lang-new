package org.jelik.compiler.passes

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.functions.FunctionCallExpr

/**
 * @author Marcin Bukowiecki
 */
object AccessPass : BasePass() {

    override fun visitFunctionCall(functionCallExpr: FunctionCallExpr, compilationContext: CompilationContext) {
        super.visitFunctionCall(functionCallExpr, compilationContext)
        if (functionCallExpr.targetFunctionCallProvider.isConstructor) {
            if (functionCallExpr.owner.findClassData(compilationContext).isAbstract) {
                compilationContext.problemHolder.reportProblem("access.abstractTypeInit", functionCallExpr)
            }
        }
    }
}
