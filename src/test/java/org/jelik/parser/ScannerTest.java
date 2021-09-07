package org.jelik.parser;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.jelik.parser.token.Token;
import org.junit.Test;

import java.util.List;

public class ScannerTest {

    @Test
    public void scanFunction() {
        final String content = "fun adder(Int a, Int b) -> Int { ret a + b }";
        final CharPointer charPointer = new CharPointer(content);
        final List<Token> acc = Lists.newArrayList();
        final Scanner scanner = new Scanner(charPointer);

        while (scanner.hasNext()) {
            acc.add(scanner.next());
        }
        for (Token token : acc) {
            System.out.print(token);
        }
        Assertions.assertThat(acc)
                .hasSize(29);
    }

    @Test
    public void correctColsAndRows() {
        final String content = "fun adder\n(Int a, \n  Int b) -> Int { ret a + b }";
        final CharPointer charPointer = new CharPointer(content);
        final Scanner scanner = new Scanner(charPointer);

        Token next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "fun")
                .hasFieldOrPropertyWithValue("startOffset", 0);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("startOffset", 3);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "adder")
                .hasFieldOrPropertyWithValue("startOffset", 4);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "\n")
                .hasFieldOrPropertyWithValue("startOffset", 9);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "(")
                .hasFieldOrPropertyWithValue("startOffset", 10);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "Int")
                .hasFieldOrPropertyWithValue("startOffset", 11);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("startOffset", 14);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "a")
                .hasFieldOrPropertyWithValue("startOffset", 15);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", ",")
                .hasFieldOrPropertyWithValue("startOffset", 16);


        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("startOffset", 17);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "\n")
                .hasFieldOrPropertyWithValue("startOffset", 18);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("startOffset", 19);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("startOffset", 20);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "Int")
                .hasFieldOrPropertyWithValue("startOffset", 21);
    }
}
