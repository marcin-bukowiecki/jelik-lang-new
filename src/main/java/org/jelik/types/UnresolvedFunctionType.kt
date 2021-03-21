package org.jelik.types

import org.jelik.compiler.data.MethodData

/**
 * @author Marcin Bukowiecki
 */
class UnresolvedFunctionType(val functions: List<MethodData>)
    : FunctionType("Unresolved", "Unresolved") {


}
