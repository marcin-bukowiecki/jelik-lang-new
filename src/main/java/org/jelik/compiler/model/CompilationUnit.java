package org.jelik.compiler.model;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.resolvers.FindSymbolResult;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.types.Type;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public interface CompilationUnit {

    FindSymbolResult findSymbol(String text, CompilationContext compilationContext);

    Optional<Type> findType(SingleTypeNode typeNode, CompilationContext compilationContext);
}
