package org.jelik.compiler.model;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.resolvers.FindSymbolResult;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.types.Type;

import java.util.Map;
import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public interface CompilationUnit {

    Optional<FindSymbolResult> findSymbol(String text, CompilationContext compilationContext);

    Optional<Type> findType(SingleTypeNode typeNode, CompilationContext compilationContext);

    Map<String, TypeNode> getTypeParametersMappings();

    void addTypeParameterMapping(String symbol, TypeNode typeNode);

    Map<String, TypeNode> getGenericTypeParametersMappings();

    void addGenericTypeParameterMapping(String symbol, TypeNode typeNode);
}
