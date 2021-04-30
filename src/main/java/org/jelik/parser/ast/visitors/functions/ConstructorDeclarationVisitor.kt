package org.jelik.parser.ast.visitors.functions

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.ast.functions.ConstructorDeclaration
import org.jelik.parser.ast.functions.FunctionBody
import org.jelik.parser.ast.functions.FunctionParameterList
import org.jelik.compiler.exceptions.SyntaxException
import org.jelik.parser.token.LeftCurlToken
import org.jelik.parser.token.LeftParenthesisToken
import org.jelik.parser.token.keyword.ConstructorKeyword

/**
 * @author Marcin Bukowiecki
 */
class ConstructorDeclarationVisitor(private val constructorKeyword: ConstructorKeyword) :
    TokenVisitor<ConstructorDeclaration> {

    private var parameterList: FunctionParameterList? = null

    private var body: FunctionBody? = null

    override fun visit(parseContext: ParseContext): ConstructorDeclaration {
        parseContext.lexer.nextToken().accept(this, parseContext)
        if (body == null) {
            throw SyntaxException("Expected constructor body", constructorKeyword, parseContext.currentFilePath)
        }
        return ConstructorDeclaration(constructorKeyword, parameterList ?: FunctionParameterList.createEmpty(), body)
    }

    override fun visitLeftParenthesis(leftParenthesisToken: LeftParenthesisToken, parseContext: ParseContext) {
        this.parameterList = FunctionParameterListVisitor(
            leftParenthesisToken
        ).visit(parseContext)
        parseContext.lexer.nextToken().accept(this, parseContext)
    }

    override fun visitLeftCurl(leftCurlToken: LeftCurlToken, parseContext: ParseContext) {
        this.body = FunctionBodyVisitor(leftCurlToken).visit(parseContext)
    }
}
