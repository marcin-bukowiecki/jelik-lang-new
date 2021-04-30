package org.jelik.compiler.helper

import org.jelik.compiler.exceptions.SyntaxException
import org.jelik.compiler.exceptions.TypeCompileException
import org.jelik.compiler.utils.MessagesBundle
import org.jelik.parser.ParseContext
import org.jelik.parser.ast.ASTNode
import org.jelik.parser.ast.classes.ClassDeclaration
import org.jelik.parser.ast.utils.ASTUtils
import org.jelik.types.Type
import java.text.MessageFormat

/**
 * @author Marcin Bukowiecki
 */
object CompilerHelper {

    fun reportError(key: String, node: ASTNode) {
        throw TypeCompileException(MessagesBundle.message(key), node, ASTUtils.getClassDeclaration(node))
    }

    fun raiseTypeCompileError(key: String, node: ASTNode, where: ClassDeclaration, vararg args: Type) {
        throw TypeCompileException(createMessage(key, *args), node, where)
    }

    fun raiseTypeCompileError(key: String, node: ASTNode, vararg args: Type) {
        throw TypeCompileException(createMessage(key, *args), node, ASTUtils.getClassDeclaration(node));
    }

    private fun createMessage(key: String, vararg args: Any): String {
        return MessageFormat.format(MessagesBundle.message(key), *args)
    }

    fun raiseSyntaxError(key: String, parseContext: ParseContext) {
        throw SyntaxException(MessagesBundle.message(key), parseContext.lexer.current, parseContext.currentFilePath)
    }

    fun raiseSyntaxError(key: String, parseContext: ParseContext, arg: Any) {
        throw SyntaxException(MessagesBundle.message(key, arg), parseContext.lexer.current, parseContext.currentFilePath)
    }
}
