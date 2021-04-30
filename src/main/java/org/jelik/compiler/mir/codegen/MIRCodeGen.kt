package org.jelik.compiler.mir.codegen

import org.jelik.compiler.asm.MethodVisitorAdapter
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
import org.jelik.compiler.mir.visitor.MIRVisitor

/**
 * @author Marcin Bukowiecki
 */
class MIRCodeGen(val mv: MethodVisitorAdapter) : MIRVisitor() {

    override fun visitIntAnd(mirIntAdd: MIRIntAdd) {
        mirIntAdd.left.accept(this)
        mirIntAdd.right.accept(this)
        mv.visitIntAdd()
    }

    override fun visitIntSub(mirIntSub: MIRIntSub) {
        mirIntSub.left.accept(this)
        mirIntSub.right.accept(this)
        mv.visitIntSub()
    }

    override fun visitIntDiv(mirIntDiv: MIRIntDiv) {
        mirIntDiv.left.accept(this)
        mirIntDiv.right.accept(this)
        mv.visitIntDiv()
    }

    override fun visitIntMul(mirIntMul: MIRIntMul) {
        mirIntMul.left.accept(this)
        mirIntMul.right.accept(this)
        mv.visitIntMul()
    }

    override fun visitIntReturn(mirIntReturn: MIRIntReturn) {
        mirIntReturn.subject.accept(this)
        mv.visitIReturn()
    }

    override fun visitLongReturn(mirLongReturn: MIRLongReturn) {
        mirLongReturn.subject.accept(this)
        mv.visitLReturn()
    }

    override fun visitDoubleReturn(mirDoubleReturn: MIRDoubleReturn) {
        mirDoubleReturn.subject.accept(this)
        mv.visitDReturn()
    }

    override fun visitFloatReturn(mirFloatReturn: MIRFloatReturn) {
        mirFloatReturn.subject.accept(this)
        mv.visitFReturn()
    }

    override fun visitVoidReturn(mirVoidReturn: MIRVoidReturn) {
        mv.visitReturn()
    }

    override fun visitRefReturn(mirRefReturn: MIRRefReturn) {
        mirRefReturn.subject.accept(this)
        mv.visitAReturn()
    }

    override fun visitALoad(miraLoad: MIRALoad) {
        mv.aload(miraLoad.index)
    }

    override fun visitAStore(miraStore: MIRAStore) {
        miraStore.subject.accept(this)
        mv.objectStore(miraStore.index)
    }

    override fun visitDoubleStore(mirDoubleStore: MIRDoubleStore) {
        mirDoubleStore.subject.accept(this)
        mv.objectStore(mirDoubleStore.index)
    }

    override fun visitFloatStore(mirFloatStore: MIRFloatStore) {
        mirFloatStore.subject.accept(this)
        mv.objectStore(mirFloatStore.index)
    }

    override fun visitFloatLoad(mirFloatLoad: MIRFloatLoad) {
        mv.floatLoad(mirFloatLoad.index)
    }


    override fun visitFloatSub(mirFloatSub: MIRFloatSub) {
        super.visitFloatSub(mirFloatSub)
        mv.visitFloatSub()
    }


    override fun visitIntLoad(mirIntLoad: MIRIntLoad) {
        mv.intLoad(mirIntLoad.index)
    }
}
