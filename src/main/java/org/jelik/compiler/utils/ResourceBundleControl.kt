package org.jelik.compiler.utils

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
object ResourceBundleControl : ResourceBundle.Control() {

    override fun getFormats(baseName: String?): MutableList<String> {
        return FORMAT_PROPERTIES
    }

    override fun newBundle(
        baseName: String?,
        locale: Locale?,
        format: String?,
        loader: ClassLoader?,
        reload: Boolean
    ): ResourceBundle {
        if (baseName.isNullOrEmpty()) throw IllegalArgumentException("baseName can't be null or empty")
        if (loader == null) throw IllegalArgumentException("class loader is null")

        val bundleName = getBundleName(baseName, locale)
        val resourceName = "META-INF/$bundleName.properties"

        val stream = loader.getResourceAsStream(resourceName) ?: throw UnsupportedOperationException("resource not found")

        stream.use {
            return PropertyResourceBundle(InputStreamReader(it, StandardCharsets.UTF_8))
        }
    }

    private fun getBundleName(baseName: String?, locale: Locale?): String? {
        return baseName
    }
}
