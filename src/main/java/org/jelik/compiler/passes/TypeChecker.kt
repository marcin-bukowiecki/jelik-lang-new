package org.jelik.compiler.passes

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.helper.CompilerHelper
import org.jelik.compiler.utils.Stateless
import org.jelik.parser.ast.ReturnExpr
import org.jelik.parser.ast.locals.ValueDeclaration
import org.jelik.parser.ast.locals.VariableDeclaration
import org.jelik.parser.ast.loops.ForEachLoop

/**
 * @author Marcin Bukowiecki
 */
@Stateless
object TypeChecker : BasePass() {

    override fun visitValueDeclaration(valueDeclaration: ValueDeclaration,
                                       compilationContext: CompilationContext
    ) {
        valueDeclaration.expression.accept(this, compilationContext)
    }

    override fun visitVariableDeclaration(variableDeclaration: VariableDeclaration,
                                          compilationContext: CompilationContext
    ) {
        variableDeclaration.expression.accept(this, compilationContext)
    }

    override fun visitReturnExpr(re: ReturnExpr, compilationContext: CompilationContext) {
        re.expression.accept(this, compilationContext)
        val givenType = re.genericReturnType
        val currentFunction = compilationContext.currentFunction()
        val expectedType = currentFunction.returnType
        if (!givenType.isAssignableTo(expectedType, compilationContext)) {
            compilationContext
                .problemHolder
                .reportProblem("type.checker.functionReturnType", re, expectedType, givenType)
        }
    }

    override fun visitForEachLoop(forEachloop: ForEachLoop, compilationContext: CompilationContext) {
        super.visitForEachLoop(forEachloop, compilationContext)
        val iterExprType = forEachloop.getIterExpression().genericReturnType
        if (!iterExprType.isArray && !iterExprType.isCollection(compilationContext)) {
            CompilerHelper.raiseTypeCompileError("type.checker.exception.expected.collection",
                    forEachloop.getIterExpression(),
                    compilationContext.currentModule,
                    iterExprType)
        }
    }
}
