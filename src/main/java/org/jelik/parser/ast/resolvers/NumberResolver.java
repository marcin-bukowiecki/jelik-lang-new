package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.numbers.Float32Node;
import org.jelik.parser.ast.numbers.Int64Node;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.numbers.Int32Node;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class NumberResolver extends AstVisitor {

    private final LiteralExpr literalExpr;

    private Expression newNode;

    public NumberResolver(LiteralExpr literalExpr) {
        this.literalExpr = literalExpr;
    }

    public NumberResolver resolve(CompilationContext compilationContext) {
        visit(this.literalExpr, compilationContext);
        return this;
    }

    public Expression getNewNode() {
        return newNode;
    }

    @Override
    public void visit(@NotNull LiteralExpr literalExpr, @NotNull CompilationContext compilationContext) {
        var text = literalExpr.getLiteralToken().getText();
        try {
            var intValue = Integer.parseInt(text);
            var newNode = new Int32Node(literalExpr.getLiteralToken(), intValue);
            newNode.setParent(literalExpr.getParent());
            //literalExprgetFurtherExpressionOpt().ifPresent(newNode::setFurtherExpression);
            literalExpr.getParent().replaceWith(literalExpr, newNode);
            this.newNode = newNode;
        } catch (NumberFormatException ignored1) {
            try {
                var floatValue = Float.parseFloat(text);
                var newNode = new Float32Node(literalExpr.getLiteralToken(), floatValue);
                newNode.setParent(literalExpr.getParent());
                //literalExpr.getFurtherExpressionOpt().ifPresent(newNode::setFurtherExpression);
                literalExpr.getParent().replaceWith(literalExpr, newNode);
                this.newNode = newNode;
            } catch (NumberFormatException ignored2) {
                try {
                    if (text.endsWith("L")) {
                        text = text.substring(0, text.lastIndexOf('L'));
                    }
                    var longValue = Long.parseLong(text);
                    var newNode = new Int64Node(literalExpr.getLiteralToken(), longValue);
                    newNode.setParent(literalExpr.getParent());
                    //literalExpr.getFurtherExpressionOpt().ifPresent(newNode::setFurtherExpression);
                    literalExpr.getParent().replaceWith(literalExpr, newNode);
                    this.newNode = newNode;
                } catch (NumberFormatException ignored3) {

                }
            }
        }
    }
}
