package org.jelik.parser.ast.labels;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Label;

/**
 * @author Marcin Bukowiecki
 */
public class LabelNode extends ASTNodeImpl {

    private final int id;

    private final String name;

    private final Label label = new Label();

    public LabelNode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Label getLabel() {
        return label;
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
