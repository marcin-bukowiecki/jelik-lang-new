package org.jelik.parser.ast.resolvers.types

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.helper.CompilerHelper
import org.jelik.parser.ast.types.CompositeTypeNode
import org.jelik.types.Type
import java.lang.Exception

/**
 * @author Marcin Bukowiecki
 */
class CompositeTypeResolver(private val compilationContext: CompilationContext) {

    fun resolve(compositeTypeNode: CompositeTypeNode): Type? {
        compositeTypeNode.paths.firstOrNull()?.let {
            firstPath ->
            if (firstPath.text == "java") {
                try {
                    val clazz = Class
                        .forName(
                            compositeTypeNode.paths.joinToString(separator = ".") +
                                    "." +
                                    compositeTypeNode.typeNode.symbol
                        )
                    return Type.of(clazz)
                } catch (ex: Exception) {
                    CompilerHelper.raiseTypeCompileError("type.unresolved", compositeTypeNode)
                }
            }
        }
        return null
    }
}
