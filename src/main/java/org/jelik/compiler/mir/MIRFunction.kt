package org.jelik.compiler.mir

import org.jelik.compiler.mir.visitor.MIRVisitor
import org.jelik.parser.ast.functions.FunctionDeclaration

/**
 * @author Marcin Bukowiecki
 */
class MIRFunction(private val functionDeclaration: FunctionDeclaration,
                  private val values: MutableList<MIRValue>): MIRValue() {

    override fun accept(visitor: MIRVisitor) {

    }
}
