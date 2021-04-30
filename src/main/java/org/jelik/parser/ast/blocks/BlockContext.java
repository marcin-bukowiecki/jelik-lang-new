package org.jelik.parser.ast.blocks;

import com.google.common.collect.Maps;
import lombok.Data;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.context.NodeContext;
import org.jelik.parser.ast.labels.LabelNode;
import org.objectweb.asm.Label;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class BlockContext extends NodeContext {

    private Map<String, LocalVariable> mappedLocals = Maps.newHashMap();

    public void addLocal(String name, LocalVariable localVariable) {
        mappedLocals.put(name, localVariable);
    }

    public Optional<LocalVariable> findLocal(String name) {
        return Optional.ofNullable(mappedLocals.get(name));
    }

    public Map<String, LocalVariable> getMappedLocals() {
        return Collections.unmodifiableMap(mappedLocals);
    }
}
