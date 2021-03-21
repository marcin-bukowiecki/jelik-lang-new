package org.jelik.parser.ast.visitors

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.ParseVisitor
import org.jelik.parser.ast.classes.ClassDeclaration
import org.jelik.parser.ast.classes.FieldDeclaration
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.types.TypeParameterListNode
import org.jelik.compiler.exceptions.SyntaxException
import org.jelik.parser.token.ElementType
import org.jelik.parser.token.EofTok
import org.jelik.parser.token.LeftCurlToken
import org.jelik.parser.token.LiteralToken
import org.jelik.parser.token.RightCurlToken
import org.jelik.parser.token.keyword.ClassKeyword
import org.jelik.parser.token.keyword.ValKeyword
import org.jelik.parser.token.keyword.VarKeyword
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

    private val fieldDeclarations = ArrayList<FieldDeclaration>()

    private var typeParameterList = TypeParameterListNode.EMPTY

    override fun visit(parseContext: ParseContext): ClassDeclaration {
        val nextToken = parseContext.lexer.nextToken()
        nextToken.visit(this, parseContext)

        return ClassDeclaration(parseContext.currentFilePath,
                keyword,
                name,
                fieldDeclarations,
                functionDeclarations,
                Collections.emptyList())
    }

    override fun visitLiteral(literalToken: LiteralToken, parseContext: ParseContext) {
        this.name = literalToken
        val peekedNext = parseContext.lexer.peekNext()
        if (peekedNext.tokenType == ElementType.eof) {
            return
        }
        if (peekedNext.tokenType != ElementType.leftCurl) {
            if (peekedNext.row != literalToken.row) {
                return
            } else {
                throw SyntaxException("Unexpected token", peekedNext, parseContext.currentFilePath)
            }
        }
        parseContext.lexer.nextToken().visit(this, parseContext)
    }

    override fun visit(lesserOperator: LesserOperator, parseContext: ParseContext) {
        this.typeParameterList = TypeParameterListVisitor(lesserOperator).visit(parseContext)
    }

    override fun visitLeftCurl(leftCurlToken: LeftCurlToken, parseContext: ParseContext) {
        parseContext.lexer.nextToken().visit(this, parseContext)
    }

    override fun visitValKeyword(valKeyword: ValKeyword, parseContext: ParseContext) {
        parseContext.modifiersStack.add(valKeyword)
        fieldDeclarations.add(FieldVisitor().visit(parseContext))
        parseContext.lexer.nextToken().visit(this, parseContext)
    }

    override fun visitVarKeyword(varKeyword: VarKeyword, parseContext: ParseContext) {
        parseContext.modifiersStack.add(varKeyword)
        fieldDeclarations.add(FieldVisitor().visit(parseContext))
        parseContext.lexer.nextToken().visit(this, parseContext)
    }

    override fun visit(eofTok: EofTok?, parseContext: ParseContext?) {

    }

    override fun visitRightCurl(rightCurlToken: RightCurlToken, parseContext: ParseContext) {

    }
}
