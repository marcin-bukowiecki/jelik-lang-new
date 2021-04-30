package org.jelik.compiler.utils;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.compiler.helper.CompilerHelper;
import org.jelik.parser.ParseContext;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.FunKeyword;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Marcin Bukowiecki
 */
public class TokenUtils {

    public static boolean isNewLine(@NotNull Token left, @NotNull Token right) {
        return left.getRow() != right.getRow();
    }

    public static void checkExpectedToken(@NotNull ElementType expected,
                                          @NotNull Token given,
                                          @NotNull String expectedText,
                                          @NotNull ParseContext parseContext) {
        if (given.getTokenType() != expected) {
            throw new SyntaxException("Expected " + expectedText + " token", given, parseContext);
        }
    }

    public static boolean nextNotMatching(@NotNull ParseContext parseContext, @NotNull Class<?> currentClazz) {
        return !currentClazz.isInstance(parseContext.getLexer().peekNext());
    }


    public static boolean currentNotMatching(@NotNull ParseContext parseContext, @NotNull Class<?> currentClazz) {
        return !currentClazz.isInstance(parseContext.getLexer().getCurrent());
    }

    public static void checkCurrentNotMatching(@NotNull String messageKey,
                                               @NotNull ParseContext parseContext,
                                               @NotNull Class<?> currentClazz) {
        if (currentNotMatching(parseContext, currentClazz)) {
            CompilerHelper.INSTANCE.raiseSyntaxError(messageKey, parseContext);
        }
    }

    public static void checkCurrentNotMatching(@NotNull ParseContext parseContext,
                                               @NotNull Class<?> ...currentClazz) {
        if (Arrays.stream(currentClazz).noneMatch(clazz -> currentNotMatching(parseContext, clazz))) {
            CompilerHelper.INSTANCE.raiseSyntaxError("token.unexpected", parseContext);
        }
    }

    public static void checkCurrentNotMatching(@NotNull ParseContext parseContext,
                                               @NotNull ElementType ...elementTypes) {
        if (Arrays.stream(elementTypes).noneMatch(t -> parseContext.getLexer().getCurrent().getTokenType() == t)) {
            CompilerHelper.INSTANCE.raiseSyntaxError("token.unexpected", parseContext, parseContext.getLexer().getCurrent());
        }
    }

    public static void checkCurrentNotMatching(@NotNull ParseContext parseContext,
                                               @NotNull @Nls String messageKey,
                                               @NotNull ElementType ...elementTypes) {
        if (Arrays.stream(elementTypes).noneMatch(t -> parseContext.getLexer().getCurrent().getTokenType() == t)) {
            CompilerHelper.INSTANCE.raiseSyntaxError(messageKey, parseContext, parseContext.getLexer().getCurrent());
        }
    }

    public static void checkNext(@NotNull String messageKey,
                                 @NotNull ParseContext parseContext,
                                 @NotNull Class<?> currentClazz) {
        if (nextNotMatching(parseContext, currentClazz)) {
            parseContext.getLexer().nextToken();
            CompilerHelper.INSTANCE.raiseSyntaxError(messageKey, parseContext);
        }
    }

    public static void checkCurrent(@NotNull @Nls String messageKey,
                                    @NotNull ParseContext parseContext,
                                    @NotNull Class<?> clazz) {
        if (!clazz.isInstance(parseContext.getLexer().getCurrent())) {
            CompilerHelper.INSTANCE.raiseSyntaxError(messageKey, parseContext);
        }
    }
}
