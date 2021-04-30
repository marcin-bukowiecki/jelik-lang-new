package org.jelik.compiler.mir.ret

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
class MIRFloatReturn(val subject: MIRValue) : MIRValue() {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitFloatReturn(this)
    }

    override fun toString(): String {
        return "$subject\nFRETURN"
    }
}
