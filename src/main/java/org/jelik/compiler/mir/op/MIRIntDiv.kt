package org.jelik.compiler.mir.op

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
open class MIRIntDiv(left: MIRValue, right: MIRValue) : MIRBinOp(left, right) {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitIntDiv(this)
    }

    override fun toString(): String {
        return "$left\n$right\nIDIV"
    }
}
