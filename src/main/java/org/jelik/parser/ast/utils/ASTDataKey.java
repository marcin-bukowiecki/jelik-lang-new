package org.jelik.parser.ast.utils;

import org.jelik.parser.ast.labels.LabelNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 *
 * @param <T> type of data
 */
public class ASTDataKey<T> {

    public static final ASTDataKey<Boolean> IS_IGNORED = new ASTDataKey<>(false);

    public static final ASTDataKey<Boolean> IS_ABSTRACT = new ASTDataKey<>(false);

    public static final ASTDataKey<LabelNode> LABEL_NODE = new ASTDataKey<>(null);

    private final T defaultValue;

    public ASTDataKey(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Nullable
    public T getDefaultValue() {
        return defaultValue;
    }

    public static @NotNull List<@NotNull ASTDataKey<?>> getAllKeys() {
        return Arrays.asList(
                IS_IGNORED,
                IS_ABSTRACT
        );
    }
}
