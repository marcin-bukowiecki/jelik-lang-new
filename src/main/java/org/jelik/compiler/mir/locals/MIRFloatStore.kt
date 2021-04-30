package org.jelik.compiler.mir.locals

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
class MIRFloatStore(val index: Int, val subject: MIRValue) : MIRValue() {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitFloatStore(this)
    }

    override fun toString(): String {
        return "$subject\nFSTORE $index"
    }
}
