package org.jelik.compiler.asm.visitor;

import lombok.Getter;
import org.jelik.parser.ast.classes.ClassDeclaration;

/**
 * @author Marcin Bukowiecki
 */
public class ToByteCodeResult {

    @Getter
    private final ClassDeclaration classDeclaration;

    @Getter
    private final byte[] bytes;

    public ToByteCodeResult(ClassDeclaration classDeclaration, byte[] bytes) {
        this.classDeclaration = classDeclaration;
        this.bytes = bytes;
    }
}
