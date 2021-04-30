package org.jelik.compiler.mir.ret

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
class MIRLongReturn(val subject: MIRValue) : MIRValue() {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitLongReturn(this)
    }

    override fun toString(): String {
        return "$subject\nLRETURN"
    }
}
