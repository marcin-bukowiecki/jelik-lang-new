package org.jelik.parser.ast.visitors

import com.google.common.collect.Lists
import org.jelik.parser.ParseContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.ast.classes.FieldDeclaration
import org.jelik.parser.ast.expression.EmptyExpression
import org.jelik.parser.ast.types.TypeNode
import org.jelik.parser.ast.types.UndefinedTypeNode
import org.jelik.compiler.exceptions.SyntaxException
import org.jelik.parser.token.EofTok
import org.jelik.parser.token.LiteralToken
import org.jelik.parser.token.keyword.ValKeyword
import org.jelik.parser.token.operators.AssignOperator

/**
 * @author Marcin Bukowiecki
 */
class FieldVisitor : TokenVisitor<FieldDeclaration> {

    private var expr: Expression = EmptyExpression()

    private var type: TypeNode = UndefinedTypeNode.UNDEFINED_TYPE_NODE

    private var assignOp: AssignOperator? = null

    override fun visit(parseContext: ParseContext): FieldDeclaration {
        val lexer = parseContext.lexer
        val name = lexer.nextToken()
        if (name !is LiteralToken) {
            throw SyntaxException("Expected field name", name, parseContext.currentFilePath)
        }
        lexer.nextToken().accept(this, parseContext)
        val modifiersStack = parseContext.modifiersStack
        if (modifiersStack.last() is ValKeyword && expr is EmptyExpression) {
            throw SyntaxException("Expected value definition", name, parseContext.currentFilePath)
        }
        if (assignOp != null && expr is EmptyExpression) {
            if (modifiersStack.last() is ValKeyword) {
                throw SyntaxException("Expected value definition", name, parseContext.currentFilePath)
            } else {
                throw SyntaxException("Expected variable definition", name, parseContext.currentFilePath)
            }
        }
        parseContext.modifiersStack = Lists.newArrayList()
        return FieldDeclaration(modifiersStack, name, type, assignOp, expr)
    }

    override fun visitAssign(assignOperator: AssignOperator, parseContext: ParseContext) {
        this.assignOp = assignOperator
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitLiteral(literalToken: LiteralToken, parseContext: ParseContext) {
        if (type !is UndefinedTypeNode || assignOp != null) {
            this.expr = ExpressionVisitor(literalToken).visit(parseContext)
        } else {
            this.type = TypeNodeVisitor(literalToken).visit(parseContext)
            parseContext.lexer.nextToken().accept(this, parseContext)
        }
    }

    override fun visit(eofTok: EofTok?, parseContext: ParseContext?) {

    }
}
