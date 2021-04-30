package org.jelik.compiler.passes

import org.jelik.compiler.utils.MessagesBundle
import org.jelik.parser.ast.ASTNode
import org.jelik.parser.ast.classes.ClassDeclaration
import org.jelik.parser.ast.utils.ASTUtils
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
data class ProblemDescriptor(val node: ASTNode, val messageKey: String, val types: Array<out Type>) {

    fun getClassDeclaration(): ClassDeclaration? {
        return ASTUtils.getClassDeclaration(node)
    }

    fun getMessageText(): String {
        return MessagesBundle.message(messageKey)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProblemDescriptor

        if (node != other.node) return false
        if (messageKey != other.messageKey) return false
        if (!types.contentEquals(other.types)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = node.hashCode()
        result = 31 * result + messageKey.hashCode()
        result = 31 * result + types.contentHashCode()
        return result
    }
}
