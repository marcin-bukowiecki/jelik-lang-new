package org.jelik.parser.ast.classes

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.types.TypeNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LiteralToken
import org.jelik.parser.token.keyword.Modifier
import org.jelik.parser.token.operators.AssignOperator

/**
 * @author Marcin Bukowiecki
 */
class FieldDeclaration(private val modifiers: List<Modifier>,
                       val name: LiteralToken,
                       val typeNode: TypeNode,
                       val assignOperator: AssignOperator?,
                       val expression: Expression) : ASTNodeImpl() {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitFieldDeclaration(this, compilationContext)
    }

    override fun toString(): String {
        return (modifiers.joinToString(",") + " " + name + " " +
                "" + typeNode + " " +
                "" + (assignOperator?.toString() ?: "") + " " +
                "" + expression).trim()
    }
}
