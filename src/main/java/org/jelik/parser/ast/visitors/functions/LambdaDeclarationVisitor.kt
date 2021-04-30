package org.jelik.parser.ast.visitors.functions

import org.jelik.compiler.exceptions.SyntaxException
import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.ast.functions.FunctionBody
import org.jelik.parser.ast.functions.FunctionBodyBlock
import org.jelik.parser.ast.functions.FunctionParameter
import org.jelik.parser.ast.functions.InferredLambdaParameter
import org.jelik.parser.ast.functions.LambdaDeclaration
import org.jelik.parser.ast.functions.LambdaParameterList
import org.jelik.parser.ast.visitors.TypeNodeVisitor
import org.jelik.parser.token.ArrowToken
import org.jelik.parser.token.CommaToken
import org.jelik.parser.token.LeftCurlToken
import org.jelik.parser.token.LiteralToken
import org.jelik.parser.token.keyword.LamKeyword

/**
 * @author Marcin Bukowiecki
 */
class LambdaDeclarationVisitor(private val lamKeyword: LamKeyword) : TokenVisitor<LambdaDeclaration> {

    private var functionBody: FunctionBody? = null

    override fun visit(parseContext: ParseContext): LambdaDeclaration {
        val lexer = parseContext.lexer
        val params = mutableListOf<FunctionParameter>()

        var nextToken = lexer.nextToken()
        while (lexer.hasNextToken() && nextToken !is ArrowToken) {
            val name = nextToken as LiteralToken
            nextToken = lexer.nextToken()
            if (nextToken is ArrowToken) {
                params.add(InferredLambdaParameter(name, null))
                break
            } else if (nextToken is CommaToken) {
                params.add(InferredLambdaParameter(name, nextToken))
            }
            val type = TypeNodeVisitor(nextToken).visit(parseContext)
            nextToken = lexer.nextToken()
            if (nextToken is CommaToken) {
                params.add(FunctionParameter(type, name, nextToken))
            } else if (nextToken is ArrowToken) {
                params.add(FunctionParameter(type, name, null))
                break
            } else {
                throw SyntaxException("Unexpected token", nextToken, parseContext.currentFilePath)
            }
        }
        val arrow = lexer.current as ArrowToken

        nextToken = lexer.nextToken()
        nextToken.accept(this, parseContext)

        return parseContext.addLambdaDeclaration(LambdaDeclaration(lamKeyword,
                LambdaParameterList(params),
                arrowToken = arrow,
                functionBody ?: FunctionBodyBlock.createEmpty()))
    }

    override fun visitLeftCurl(leftCurlToken: LeftCurlToken, parseContext: ParseContext) {
        this.functionBody = FunctionBodyVisitor(leftCurlToken)
            .visit(parseContext)
    }
}
