package org.jelik.parser.ast.labels;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Label;

/**
 * @author Marcin Bukowiecki
 */
public class LabelNode extends ASTNodeImpl {

    @Getter
    private final int id;

    @Getter
    private final String name;

    @Getter
    private final Label label = new Label();

    public LabelNode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "LabelNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", label=" + label +
                '}';
    }

    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitLabelNode(this, compilationContext);
    }
}
