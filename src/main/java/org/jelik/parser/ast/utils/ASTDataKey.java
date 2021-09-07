package org.jelik.parser.ast.utils;

import org.apache.commons.lang3.mutable.MutableInt;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.types.Type;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
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

    public static final ASTDataKey<LabelNode> NULL_LABEL = new ASTDataKey<>(null);

    public static final ASTDataKey<LabelNode> NOT_NULL_LABEL = new ASTDataKey<>(null);

    public static final ASTDataKey<List<Type>> PROVIDED_ARGUMENT_TYPES = new ASTDataKey<>(Collections.emptyList());

    public static final ASTDataKey<MutableInt> LAMBDA_ID_COUNTER = new ASTDataKey<>(null);

    private final T defaultValue;

    public ASTDataKey(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Nullable
    public T getDefaultValue() {
        return defaultValue;
    }
}
