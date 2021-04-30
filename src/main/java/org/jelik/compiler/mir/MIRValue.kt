package org.jelik.compiler.mir

import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
abstract class MIRValue {

    var parent: MIRValue? = null

    abstract fun accept(visitor: MIRVisitor)
}
