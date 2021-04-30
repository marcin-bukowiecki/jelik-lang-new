package org.jelik.parser;

import com.google.common.collect.Lists;
import org.jelik.parser.ast.functions.LambdaDeclaration;
import org.jelik.parser.token.keyword.Modifier;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class ParseContext {

    public List<Modifier> modifiersStack = Lists.newArrayList();

    public List<LambdaDeclaration> lambdaDeclarations = Lists.newArrayList();

    private final Lexer lexer;

    public ParseContext(Lexer lexer) {
        this.lexer = lexer;
    }

    public ParseContext(String content) {
        this.lexer = new Lexer(new Scanner(new CharPointer(content)));
    }

    public Lexer getLexer() {
        return lexer;
    }

    public String getCurrentFilePath() {
        return lexer.getScanner().getCp().getFileAbsolutePath();
    }

    public LambdaDeclaration addLambdaDeclaration(LambdaDeclaration lambdaDeclaration) {
        this.lambdaDeclarations.add(lambdaDeclaration);
        return lambdaDeclaration;
    }

    public List<Modifier> getModifiersAndReset() {
        var temp = modifiersStack;
        this.modifiersStack = Lists.newArrayList();
        return temp;
    }
}
