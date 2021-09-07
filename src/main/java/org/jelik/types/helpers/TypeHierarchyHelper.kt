package org.jelik.types.helpers

import org.apache.commons.lang3.mutable.MutableInt
import org.jelik.compiler.CompilationContext
import org.jelik.types.JVMObjectType
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
object TypeHierarchyHelper {

    fun getTypeHierarchyScore(myGiven: Type,
                              myTarget: Type,
                              myCompilationContext: CompilationContext
    ): Int {
        if (myGiven == myTarget) {
            return -1
        }
        if (myGiven.isAssignableTo(myTarget, myCompilationContext)) {
            return Int.MAX_VALUE
        }
        return getTypeHierarchyScore(myGiven, myTarget, MutableInt(0), myCompilationContext)
    }

    private fun getTypeHierarchyScore(myGiven: Type,
                                      myTarget: Type,
                                      myDepth: MutableInt,
                                      myCompilationContext: CompilationContext
    ): Int {
        if (myGiven == JVMObjectType.INSTANCE) {
            return myDepth.value
        }
        val myParentType = myGiven.getParentType(myCompilationContext).orElseThrow()
        if (myParentType == myTarget) {
            return myDepth.value
        } else {
            for (interfaceType in myGiven.getInterfaceTypes(myCompilationContext)) {
                if (interfaceType == myTarget) {
                    return myDepth.value
                }
            }
            myDepth.increment()
            return getTypeHierarchyScore(myGiven, myParentType, myDepth, myCompilationContext)
        }
    }
}
