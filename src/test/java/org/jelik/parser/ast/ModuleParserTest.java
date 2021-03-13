package org.jelik.parser.ast;

import org.assertj.core.api.Assertions;
import org.jelik.parser.CharPointer;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.Scanner;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class ModuleParserTest {

    @Test
    public void parseAdderModule() {
        final String content = "fun adder(a Int, b Int) -> Int { ret a + b }";
        ModuleParser moduleParser = new ModuleParser("DUMMY_FILE");
        Lexer lexer = new Lexer(new Scanner(new CharPointer(content)));
        ParseContext parseContext = new ParseContext(lexer);
        ClassDeclaration md = moduleParser.visit(parseContext);
        Assertions.assertThat(md.getFunctionDeclarations())
                .hasSize(1);
    }
}
