package org.jelik.compiler.functions

import org.jelik.parser.ast.classes.ClassDeclaration
import org.jelik.parser.ast.functions.ExtensionFunctionDeclaration

/**
 * @author Marcin Bukowiecki
 */
class ExtFunctionEntry(val place: ClassDeclaration,
                       val extFunction: ExtensionFunctionDeclaration
)
