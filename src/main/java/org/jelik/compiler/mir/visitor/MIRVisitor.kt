package org.jelik.compiler.mir.visitor

import org.jelik.compiler.mir.locals.MIRALoad
import org.jelik.compiler.mir.locals.MIRAStore
import org.jelik.compiler.mir.locals.MIRDoubleStore
import org.jelik.compiler.mir.locals.MIRFloatLoad
import org.jelik.compiler.mir.locals.MIRFloatStore
import org.jelik.compiler.mir.locals.MIRIntLoad
import org.jelik.compiler.mir.op.MIRFloatSub
import org.jelik.compiler.mir.op.MIRIntAdd
import org.jelik.compiler.mir.op.MIRIntDiv
import org.jelik.compiler.mir.op.MIRIntMul
import org.jelik.compiler.mir.op.MIRIntSub
import org.jelik.compiler.mir.ret.MIRDoubleReturn
import org.jelik.compiler.mir.ret.MIRFloatReturn
import org.jelik.compiler.mir.ret.MIRIntReturn
import org.jelik.compiler.mir.ret.MIRLongReturn
import org.jelik.compiler.mir.ret.MIRRefReturn
import org.jelik.compiler.mir.ret.MIRVoidReturn

/**
 * @author Marcin Bukowiecki
 */
abstract class MIRVisitor {

    open fun visitIntAnd(mirIntAdd: MIRIntAdd) {
        mirIntAdd.left.accept(this)
        mirIntAdd.right.accept(this)
    }

    open fun visitIntSub(mirIntSub: MIRIntSub) {
        mirIntSub.left.accept(this)
        mirIntSub.right.accept(this)
    }

    open fun visitIntDiv(mirIntDiv: MIRIntDiv) {
        mirIntDiv.left.accept(this)
        mirIntDiv.right.accept(this)
    }

    open fun visitIntMul(mirIntMul: MIRIntMul) {
        mirIntMul.left.accept(this)
        mirIntMul.right.accept(this)
    }

    open fun visitIntReturn(mirIntReturn: MIRIntReturn) {
        mirIntReturn.subject.accept(this)
    }

    open fun visitDoubleReturn(mirDoubleReturn: MIRDoubleReturn) {
        mirDoubleReturn.subject.accept(this)
    }

    open fun visitVoidReturn(mirVoidReturn: MIRVoidReturn) {

    }

    open fun visitRefReturn(mirRefReturn: MIRRefReturn) {
        mirRefReturn.subject.accept(this)
    }

    open fun visitLongReturn(mirLongReturn: MIRLongReturn) {
        mirLongReturn.subject.accept(this)
    }

    open fun visitFloatReturn(mirFloatReturn: MIRFloatReturn) {
        mirFloatReturn.subject.accept(this)
    }

    open fun visitALoad(miraLoad: MIRALoad) {

    }

    open fun visitAStore(miraStore: MIRAStore) {
        miraStore.subject.accept(this)
    }

    open fun visitDoubleStore(mirDoubleStore: MIRDoubleStore) {
        mirDoubleStore.subject.accept(this)
    }

    open fun visitFloatStore(mirFloatStore: MIRFloatStore) {
        mirFloatStore.subject.accept(this)
    }

    open fun visitFloatSub(mirFloatSub: MIRFloatSub) {
        mirFloatSub.left.accept(this)
        mirFloatSub.right.accept(this)
    }

    open fun visitFloatLoad(mirFloatLoad: MIRFloatLoad) {

    }

    open fun visitIntLoad(mirIntLoad: MIRIntLoad) {

    }
}
