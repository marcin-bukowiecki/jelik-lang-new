package org.jelik.compiler.mir.op

import org.jelik.compiler.mir.MIRValue
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
open class MIRFloatSub(left: MIRValue, right: MIRValue) : MIRBinOp(left, right) {

    override fun accept(visitor: MIRVisitor) {
        visitor.visitFloatSub(this)
    }

    override fun toString(): String {
        return "$left\n$right\nFSUB"
    }
}
