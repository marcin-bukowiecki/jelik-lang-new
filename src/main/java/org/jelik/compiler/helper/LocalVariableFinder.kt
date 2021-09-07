package org.jelik.compiler.helper

import org.apache.commons.collections4.iterators.ReverseListIterator
import org.jelik.compiler.CompilationContext
import org.jelik.compiler.locals.LocalVariable
import org.jelik.compiler.locals.OuterLocalVariable
import org.jelik.parser.ast.blocks.BasicBlock
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
object LocalVariableFinder {

    fun tryFindLocalVariable(name: String, compilationContext: CompilationContext): Optional<LocalVariable> {
        if (!compilationContext.blockStack.isEmpty()) {
            val codeEventReverseListIterator = ReverseListIterator(compilationContext.blockStack)
            while (codeEventReverseListIterator.hasNext()) {
                val next = codeEventReverseListIterator.next()
                val local = next.blockContext.findLocal(name)
                if (local.isPresent) {
                    return Optional.of(isOuter(local.get(), next, compilationContext.currentBlock()) ?: return local)
                }
            }
        }
        return Optional.empty()
    }

    private fun isOuter(localVariable: LocalVariable,
                        foundInBlock: BasicBlock,
                        currentBlock: BasicBlock?): OuterLocalVariable? {
        val owningFunction = foundInBlock.owningFunction
        if (owningFunction != currentBlock?.owningFunction) {
            return OuterLocalVariable(localVariable)
        }
        return null
    }
}
