package org.jelik.parser.ast.utils

import java.util.*

/**
 * @author Marcin Bukowiecki
 */
class ASTDataHolder {

    private val data: IdentityHashMap<ASTDataKey<*>, Any> = IdentityHashMap(0)

    fun <T> putData(key: ASTDataKey<T>, value: T) {
        this.data[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: ASTDataKey<T>): T? {
        return data[key] as T ?: key.defaultValue
    }
}
