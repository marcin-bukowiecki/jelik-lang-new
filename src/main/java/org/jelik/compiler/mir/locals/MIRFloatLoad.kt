package org.jelik.compiler.mir.locals

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
class MIRFloatLoad(val index: Int) : MIRValue() {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitFloatLoad(this)
    }

    override fun toString(): String {
        return "FLOAD $index"
    }
}
