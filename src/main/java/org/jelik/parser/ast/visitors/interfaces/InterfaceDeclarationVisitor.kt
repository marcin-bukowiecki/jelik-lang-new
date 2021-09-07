package org.jelik.parser.ast.visitors.interfaces

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.ast.classes.InterfaceDeclaration
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.functions.MethodDeclaration
import org.jelik.parser.ast.types.TypeVariableListNode
import org.jelik.parser.ast.visitors.TypeParameterListVisitor
import org.jelik.parser.ast.visitors.functions.FunctionDeclarationVisitor
import org.jelik.parser.ast.visitors.functions.MethodDeclarationVisitor
import org.jelik.parser.token.EofTok
import org.jelik.parser.token.LeftCurlToken
import org.jelik.parser.token.LiteralToken
import org.jelik.parser.token.RightCurlToken
import org.jelik.parser.token.keyword.FunKeyword
import org.jelik.parser.token.keyword.InterfaceKeyword
import org.jelik.parser.token.keyword.MetKeyword
import org.jelik.parser.token.keyword.Modifier
import org.jelik.parser.token.keyword.PrivateKeyword
import org.jelik.parser.token.keyword.PublicKeyword
import org.jelik.parser.token.operators.LesserOperator
import java.util.*
import kotlin.collections.ArrayList

/**
 * Visitor for class declaration, starts by class keyword
 *
 * @author Marcin Bukowiecki
 */
class InterfaceDeclarationVisitor(val keyword: InterfaceKeyword) : TokenVisitor<InterfaceDeclaration> {

    private lateinit var name: LiteralToken

    private var leftCurl: LeftCurlToken? = null

    private var rightCurl: RightCurlToken? = null

    private val functionDeclarations = ArrayList<FunctionDeclaration>()

    private val methodDeclarations = ArrayList<MethodDeclaration>()

    private var typeParameterList = TypeVariableListNode.EMPTY

    override fun visit(parseContext: ParseContext): InterfaceDeclaration {
        val modifiers = parseContext.modifiersAndReset
        val nextToken = parseContext.lexer.nextToken()
        nextToken.accept(this, parseContext)
        return InterfaceDeclaration(
            parseContext.currentFilePath,
            modifiers,
            keyword,
            name,
            typeParameterList,
            functionDeclarations,
            methodDeclarations
        )
    }

    override fun visitPrivateKeyword(privateKeyword: PrivateKeyword, parseContext: ParseContext) {
        parseContext.modifiersStack.add(privateKeyword)
        parseContext.lexer.nextToken()
    }

    override fun visitPublicKeyword(publicKeyword: PublicKeyword, parseContext: ParseContext) {
        parseContext.modifiersStack.add(publicKeyword)
        parseContext.lexer.nextToken()
    }

    override fun visitLiteral(literalToken: LiteralToken, parseContext: ParseContext) {
        this.name = literalToken
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitLesser(lesserOperator: LesserOperator, parseContext: ParseContext) {
        this.typeParameterList = TypeParameterListVisitor(lesserOperator).visit(parseContext)
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitLeftCurl(leftCurlToken: LeftCurlToken, parseContext: ParseContext) {
        this.leftCurl = leftCurlToken
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitMetKeyword(metKeyword: MetKeyword, parseContext: ParseContext) {
        if (leftCurl == null) {
            super.visitMetKeyword(metKeyword, parseContext)
        }
        methodDeclarations.add(
            InterfaceMethodVisitor(
                metKeyword
            ).visit(parseContext) as MethodDeclaration
        )
        parseContext.lexer.current.accept(this, parseContext)
    }

    override fun visitFunKeyword(funKeyword: FunKeyword, parseContext: ParseContext) {
        if (leftCurl == null) {
            super.visitFunKeyword(funKeyword, parseContext)
        }
        functionDeclarations.add(
            FunctionDeclarationVisitor(
                funKeyword
            ).visit(parseContext)
        )
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visit(eofTok: EofTok?, parseContext: ParseContext?) {

    }

    override fun visitRightCurl(rightCurlToken: RightCurlToken, parseContext: ParseContext) {
        this.rightCurl = rightCurlToken
    }
}
