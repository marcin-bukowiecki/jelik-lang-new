package org.jelik.compiler.mir.op

import org.jelik.compiler.mir.MIRValue

/**
 * @author Marcin Bukowiecki
 */
abstract class MIRBinOp(val left: MIRValue, val right: MIRValue) : MIRValue() {

    init {
        left.parent = this
        right.parent = this
    }
}
