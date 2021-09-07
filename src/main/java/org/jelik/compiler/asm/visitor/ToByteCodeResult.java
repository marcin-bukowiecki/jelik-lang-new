package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.asm.utils.ByteCodeLogger;
import org.jelik.types.Type;

import java.io.File;

/**
 * @author Marcin Bukowiecki
 */
public class ToByteCodeResult {

    private final Type type;

    private final byte[] bytes;

    private final boolean main;

    public ToByteCodeResult(Type type, byte[] bytes, boolean main) {
        this.bytes = bytes;
        this.type = type;
        this.main = main;
    }

    public boolean isMain() {
        return main;
    }

    public String getPackageAsPath() {
        final String canonicalName = type.getCanonicalName();
        final int i = canonicalName.lastIndexOf('.');
        if (i == -1) {
            return "";
        } else {
            return canonicalName.substring(0, i).replace('.', '/');
        }
    }

    public String getSimpleName() {
        return type.getName();
    }

    public String getForClassFile() {
        return type.getCanonicalName().replace(".", File.separator) + ".class";
    }

    public String getNameForClassLoader() {
        return type.getCanonicalName();
    }

    public void printByteCode() {
        ByteCodeLogger.logASM(this.bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Type getType() {
        return type;
    }
}
