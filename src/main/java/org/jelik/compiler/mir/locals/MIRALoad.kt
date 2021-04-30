package org.jelik.compiler.mir.locals

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
class MIRALoad(val index: Int) : MIRValue() {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitALoad(this)
    }

    override fun toString(): String {
        return "ALOAD $index"
    }
}
