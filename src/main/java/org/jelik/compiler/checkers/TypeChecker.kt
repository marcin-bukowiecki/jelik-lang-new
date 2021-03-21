package org.jelik.compiler.checkers

import org.jelik.CompilationContext
import org.jelik.compiler.exceptions.CompileException
import org.jelik.parser.ast.ReturnExpr
import org.jelik.parser.ast.locals.ValueDeclaration
import org.jelik.parser.ast.locals.VariableDeclaration
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
object TypeChecker : AstVisitor() {

    override fun visitValueDeclaration(valueDeclaration: ValueDeclaration, compilationContext: CompilationContext) {
        valueDeclaration.furtherExpressionOpt.ifPresent { expr -> expr.visit(this, compilationContext) }
    }

    override fun visitVariableDeclaration(variableDeclaration: VariableDeclaration, compilationContext: CompilationContext) {
        variableDeclaration.furtherExpressionOpt.ifPresent { expr -> expr.visit(this, compilationContext) }
    }

    override fun visitReturnExpr(re: ReturnExpr, compilationContext: CompilationContext) {
        re.furtherExpression.visit(this, compilationContext)
        val givenType = re.genericReturnType
        val currentFunction = compilationContext.currentFunction()
        val expectedType = currentFunction.returnType
        if (!givenType.isAssignableTo(expectedType, compilationContext)) {
            throw CompileException("Function must return ${expectedType}, given $givenType", re, compilationContext.currentModule)
        }
    }
}
