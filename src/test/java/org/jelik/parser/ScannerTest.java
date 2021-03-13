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
                .hasFieldOrPropertyWithValue("row", 1)
                .hasFieldOrPropertyWithValue("col", 1);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("row", 1)
                .hasFieldOrPropertyWithValue("col", 4);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "adder")
                .hasFieldOrPropertyWithValue("row", 1)
                .hasFieldOrPropertyWithValue("col", 5);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "\n")
                .hasFieldOrPropertyWithValue("row", 1)
                .hasFieldOrPropertyWithValue("col", 10);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "(")
                .hasFieldOrPropertyWithValue("row", 2)
                .hasFieldOrPropertyWithValue("col", 1);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "Int")
                .hasFieldOrPropertyWithValue("row", 2)
                .hasFieldOrPropertyWithValue("col", 2);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("row", 2)
                .hasFieldOrPropertyWithValue("col", 5);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "a")
                .hasFieldOrPropertyWithValue("row", 2)
                .hasFieldOrPropertyWithValue("col", 6);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", ",")
                .hasFieldOrPropertyWithValue("row", 2)
                .hasFieldOrPropertyWithValue("col", 7);


        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("row", 2)
                .hasFieldOrPropertyWithValue("col", 8);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "\n")
                .hasFieldOrPropertyWithValue("row", 2)
                .hasFieldOrPropertyWithValue("col", 9);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("row", 3)
                .hasFieldOrPropertyWithValue("col", 1);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", " ")
                .hasFieldOrPropertyWithValue("row", 3)
                .hasFieldOrPropertyWithValue("col", 2);

        next = scanner.next();
        Assertions.assertThat(next)
                .hasFieldOrPropertyWithValue("text", "Int")
                .hasFieldOrPropertyWithValue("row", 3)
                .hasFieldOrPropertyWithValue("col", 3);
    }
}
