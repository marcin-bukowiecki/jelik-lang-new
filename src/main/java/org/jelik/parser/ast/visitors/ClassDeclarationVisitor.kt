package org.jelik.parser.ast.visitors

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.ParseVisitor
import org.jelik.parser.ast.classes.ClassDeclaration
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.types.TypeParameterListNode
import org.jelik.parser.token.LeftCurlToken
import org.jelik.parser.token.LiteralToken
import org.jelik.parser.token.keyword.ClassKeyword
import org.jelik.parser.token.operators.LesserOperator
import java.util.*
import kotlin.collections.ArrayList

/**
 * Visitor for class declaration, starts by class keyword
 *
 * @author Marcin Bukowiecki
 */
class ClassDeclarationVisitor(val keyword: ClassKeyword) : ParseVisitor<ClassDeclaration> {

    private lateinit var name: LiteralToken

    private val functionDeclarations = ArrayList<FunctionDeclaration>()

    private var typeParameterList = TypeParameterListNode.EMPTY

    override fun visit(parseContext: ParseContext): ClassDeclaration {
        val nextToken = parseContext.lexer.nextToken()
        nextToken.visit(this, parseContext)

        return ClassDeclaration(parseContext.currentFilePath,
                name,
                functionDeclarations, Collections.emptyList())
    }

    override fun visitLiteral(literalToken: LiteralToken, parseContext: ParseContext) {
        this.name = literalToken
        parseContext.lexer.nextToken().visit(this, parseContext)
    }

    override fun visit(lesserOperator: LesserOperator, parseContext: ParseContext) {
        this.typeParameterList = TypeParameterListVisitor(lesserOperator).visit(parseContext)
    }

    override fun visitLeftCurl(leftCurlToken: LeftCurlToken, parseContext: ParseContext) {
        parseContext.lexer.nextToken()
    }
}
