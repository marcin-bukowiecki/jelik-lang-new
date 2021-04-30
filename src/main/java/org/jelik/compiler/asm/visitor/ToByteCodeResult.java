package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.asm.utils.ByteCodeLogger;
import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public class ToByteCodeResult {

    private final Type type;

    private final byte[] bytes;

    public ToByteCodeResult(Type type, byte[] bytes) {
        this.bytes = bytes;
        this.type = type;
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
