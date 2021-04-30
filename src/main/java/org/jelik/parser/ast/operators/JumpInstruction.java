package org.jelik.parser.ast.operators;

import org.objectweb.asm.Opcodes;

import java.util.Collections;
import java.util.List;

/**
 * Enum class to provide proper ASM instructions to handle condition expressions
 *
 * @author Marcin Bukowiecki
 */
public enum JumpInstruction {

    isTrue(List.of(Opcodes.IFNE) /* != 0 */, List.of(Opcodes.IFEQ) /* == 0 */),

    isFalse(List.of(Opcodes.IFEQ) /* == 0 */, List.of(Opcodes.IFNE) /* != 0 */),

    if_icmpeq(List.of(Opcodes.IF_ICMPEQ), List.of(Opcodes.IF_ICMPNE)),

    refs_equal(List.of(Opcodes.IF_ACMPEQ), List.of(Opcodes.IF_ACMPNE)),

    refs_not_equal(List.of(Opcodes.IF_ACMPNE), List.of(Opcodes.IF_ACMPEQ)),

    if_icmpne(List.of(Opcodes.IF_ICMPNE), List.of(Opcodes.IF_ICMPEQ)),

    if_icmpgt(List.of(Opcodes.IF_ICMPGT), List.of(Opcodes.IF_ICMPLE)),

    if_icmplt(List.of(Opcodes.IF_ICMPLT), List.of(Opcodes.IF_ICMPGE)),

    isNull(List.of(Opcodes.IFNULL), List.of(Opcodes.IFNONNULL)),

    isNotNull(List.of(Opcodes.IFNONNULL), List.of(Opcodes.IFNULL)),

    lcmp(List.of(Opcodes.LCMP, Opcodes.IFEQ), List.of(Opcodes.LCMP, Opcodes.IFNE)),

    dcmpl(List.of(Opcodes.DCMPL, Opcodes.IFEQ), List.of(Opcodes.DCMPL, Opcodes.IFNE)),

    dcmpg(List.of(Opcodes.DCMPL, Opcodes.IFNE), List.of(Opcodes.DCMPL, Opcodes.IFEQ)),

    floatGreater(List.of(Opcodes.FCMPL, Opcodes.IFGE), List.of(Opcodes.FCMPL, Opcodes.IFLE)),

    floatLesser(List.of(Opcodes.FCMPG, Opcodes.IFLE), List.of(Opcodes.FCMPG, Opcodes.IFGE)),

    intGreaterOrEqual(List.of(Opcodes.IF_ICMPGE), List.of(Opcodes.IF_ICMPLT)),

    intLesserOrEqual(List.of(Opcodes.IF_ICMPLE), List.of(Opcodes.IF_ICMPGT)),

    isLessThanZero(List.of(Opcodes.IFLT), List.of(Opcodes.IFGE)),

    isGreaterThanZero(List.of(Opcodes.IFGT), List.of(Opcodes.IFLE)),

    ;

    private final List<Integer> value;

    private final List<Integer> negated;

    JumpInstruction(List<Integer> value, List<Integer> negated) {
        this.value = value;
        this.negated = negated;
    }

    public List<Integer> getValue() {
        return value;
    }

    public List<Integer> getNegated() {
        return negated;
    }
}
