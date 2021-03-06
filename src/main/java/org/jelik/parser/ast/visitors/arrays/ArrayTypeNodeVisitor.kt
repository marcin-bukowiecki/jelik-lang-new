package org.jelik.parser.ast.visitors.arrays

import org.jelik.compiler.exceptions.SyntaxException
import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.ast.types.ArrayTypeNode
import org.jelik.parser.ast.types.TypeNode
import org.jelik.parser.ast.visitors.TypeNodeVisitor
import org.jelik.parser.token.ElementType
import org.jelik.parser.token.LeftBracketToken
import org.jelik.parser.token.RightBracketToken

/**
 * @author Marcin Bukowiecki
 */
class ArrayTypeNodeVisitor(private val leftBracket: LeftBracketToken) :
    TokenVisitor<ArrayTypeNode> {

    lateinit var arrayTypeNode: ArrayTypeNode

    private lateinit var rightBracketToken: RightBracketToken

    private lateinit var innerType: TypeNode

    override fun visit(parseContext: ParseContext): ArrayTypeNode {
        val nextToken = parseContext.lexer.nextToken()
        if (nextToken.tokenType != ElementType.rightBracket) {
            throw SyntaxException("Expected right bracket", nextToken, parseContext.currentFilePath)
        }
        this.rightBracketToken = nextToken as RightBracketToken
        this.innerType = TypeNodeVisitor(parseContext.lexer.nextToken()).visit(parseContext)
        return ArrayTypeNode(leftBracket, rightBracketToken, innerType)
    }
}
