package org.jelik.compiler.mir.ret

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
class MIRVoidReturn : MIRValue() {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitVoidReturn(this)
    }

    override fun toString(): String {
        return "RETURN"
    }
}
