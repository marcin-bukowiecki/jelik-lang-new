package org.jelik.parser.ast.visitors

import org.jelik.compiler.asm.visitor.TypeVisitor
import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.ast.classes.ClassDeclaration
import org.jelik.parser.ast.classes.FieldDeclaration
import org.jelik.parser.ast.functions.ConstructorDeclaration
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.functions.MethodDeclaration
import org.jelik.parser.ast.types.TypeNode
import org.jelik.parser.ast.types.TypeVariableListNode
import org.jelik.parser.ast.visitors.functions.ConstructorDeclarationVisitor
import org.jelik.parser.ast.visitors.functions.FunctionDeclarationVisitor
import org.jelik.parser.ast.visitors.functions.MethodDeclarationVisitor
import org.jelik.parser.token.*
import org.jelik.parser.token.keyword.AbstractKeyword
import org.jelik.parser.token.keyword.ClassKeyword
import org.jelik.parser.token.keyword.ConstructorKeyword
import org.jelik.parser.token.keyword.FunKeyword
import org.jelik.parser.token.keyword.MetKeyword
import org.jelik.parser.token.keyword.PrivateKeyword
import org.jelik.parser.token.keyword.PublicKeyword
import org.jelik.parser.token.keyword.ValKeyword
import org.jelik.parser.token.keyword.VarKeyword
import org.jelik.parser.token.operators.LesserOperator

/**
 * Visitor for class declaration, starts by class keyword
 *
 * @author Marcin Bukowiecki
 */
class ClassDeclarationVisitor(val keyword: ClassKeyword) : TokenVisitor<ClassDeclaration> {

    private lateinit var name: LiteralToken

    private var leftCurl: LeftCurlToken? = null

    private var rightCurl: RightCurlToken? = null

    private var colon: ColonToken? = null

    private val functionDeclarations = ArrayList<FunctionDeclaration>()

    private val methodDeclarations = ArrayList<MethodDeclaration>()

    private val constructorDeclarations = ArrayList<ConstructorDeclaration>()

    private val fieldDeclarations = ArrayList<FieldDeclaration>()

    private var typeParameterList = TypeVariableListNode.EMPTY

    override fun visit(parseContext: ParseContext): ClassDeclaration {
        val modifiers = parseContext.modifiersAndReset
        val nextToken = parseContext.lexer.nextToken()
        nextToken.accept(this, parseContext)
        return ClassDeclaration(
            parseContext.currentFilePath,
            modifiers,
            keyword,
            name,
            typeParameterList,
            fieldDeclarations,
            functionDeclarations,
            methodDeclarations,
            constructorDeclarations
        )
    }

    override fun visitAbstractKeyword(abstractKeyword: AbstractKeyword, parseContext: ParseContext) {
        parseContext.modifiersStack.add(abstractKeyword)
        parseContext.lexer.nextToken()
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

    override fun visitColon(colonToken: ColonToken, parseContext: ParseContext) {
        this.colon = colonToken

        val extendsList = mutableListOf<TypeNode>()
        while (parseContext.lexer.hasNextToken()) {
            val typeNodeVisitor = TypeNodeVisitor(parseContext.lexer.nextToken())
            val extends = typeNodeVisitor.visit(parseContext)
            extendsList.add(extends)
            val next = parseContext.lexer.nextToken()
            if (next is LeftCurlToken) {
                break
            }
            if (next is CommaToken) {
                continue
            }
        }
    }

    override fun visitLesser(lesserOperator: LesserOperator, parseContext: ParseContext) {
        this.typeParameterList = TypeParameterListVisitor(lesserOperator).visit(parseContext)
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitLeftCurl(leftCurlToken: LeftCurlToken, parseContext: ParseContext) {
        this.leftCurl = leftCurlToken
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitValKeyword(valKeyword: ValKeyword, parseContext: ParseContext) {
        parseContext.modifiersStack.add(valKeyword)
        fieldDeclarations.add(FieldVisitor().visit(parseContext))
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitVarKeyword(varKeyword: VarKeyword, parseContext: ParseContext) {
        parseContext.modifiersStack.add(varKeyword)
        fieldDeclarations.add(FieldVisitor().visit(parseContext))
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitConstructorKeyword(constructorKeyword: ConstructorKeyword, parseContext: ParseContext) {
        if (leftCurl == null) {
            super.visitConstructorKeyword(constructorKeyword, parseContext)
        }
        constructorDeclarations.add(ConstructorDeclarationVisitor(constructorKeyword).visit(parseContext))
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitMetKeyword(metKeyword: MetKeyword, parseContext: ParseContext) {
        if (leftCurl == null) {
            super.visitMetKeyword(metKeyword, parseContext)
        }
        methodDeclarations.add(
            MethodDeclarationVisitor(
                metKeyword
            ).visit(parseContext) as MethodDeclaration
        )
        parseContext.lexer.nextToken().accept(this, parseContext)
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
