package org.jelik.compiler.functions

import org.jelik.parser.ast.functions.ExtensionFunctionDeclaration
import org.jelik.parser.ast.functions.FunctionCall
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.utils.ASTUtils
import org.jelik.types.Type
import java.lang.IllegalArgumentException

/**
 * @author Marcin Bukowiecki
 */
object ExtensionFunctionRegister {

    private val register: MutableMap<Type, List<ExtFunctionEntry>> = mutableMapOf()

    fun insert(owner: Type, functionDeclaration: ExtensionFunctionDeclaration) {
        val classDeclaration = ASTUtils.getClassDeclaration(functionDeclaration)
            ?: throw IllegalArgumentException("function declaration without owning class")
        val entry = ExtFunctionEntry(classDeclaration, functionDeclaration)

        if (register[owner] == null) {
            register[owner] = listOf(entry)
        } else {
            register[owner] = register[owner]!! + listOf(entry)
        }
    }

    fun findForType(owner: Type, functionCall: FunctionCall): List<FunctionDeclaration> {
        val list = register[owner]
        if (list.isNullOrEmpty()) return emptyList()
        val classDeclaration = ASTUtils.getClassDeclaration(functionCall)
            ?: throw IllegalArgumentException("node without class owner")
        return list
            .filter { entry -> entry.place == classDeclaration }
            .map { entry -> entry.extFunction as FunctionDeclaration }
    }
}
