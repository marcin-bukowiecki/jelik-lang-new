package org.jelik.compiler.mir.locals

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
class MIRDoubleStore(val index: Int, val subject: MIRValue) : MIRValue() {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitDoubleStore(this)
    }

    override fun toString(): String {
        return "$subject\nDSTORE $index"
    }
}
