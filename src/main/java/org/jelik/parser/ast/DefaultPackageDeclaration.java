package org.jelik.parser.ast;

import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.PackageKeyword;

import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class DefaultPackageDeclaration extends PackageDeclaration {

    public static final DefaultPackageDeclaration INSTANCE = new DefaultPackageDeclaration(new PackageKeyword(-1,-1), Collections.emptyList());


    public DefaultPackageDeclaration(PackageKeyword packageKeyword, List<Token> tokens) {
        super(packageKeyword, tokens);
    }

    @Override
    public void setParent(ASTNode parent) {

    }

    @Override
    public ASTNode getParent() {
        throw new UnsupportedOperationException();
    }
}
