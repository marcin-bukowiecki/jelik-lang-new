package org.jelik.compiler.utils

import java.text.MessageFormat
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
object MessagesBundle {

    private var bundle: ResourceBundle = ResourceBundle.getBundle("messages", Locale.getDefault(),
        this.javaClass.classLoader,
        ResourceBundleControl)

    fun message(key: String): String {
        return bundle.getString(key)
    }

    fun message(key: String, arg: Any): String {
        return MessageFormat(bundle.getString(key)).format(arrayOf(arg))
    }

    fun message(key: String, vararg args: Any): String {
        return MessageFormat(bundle.getString(key)).format(args)
    }
}
