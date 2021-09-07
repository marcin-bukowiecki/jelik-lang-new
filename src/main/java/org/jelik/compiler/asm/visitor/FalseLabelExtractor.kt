package org.jelik.compiler.asm.visitor

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr
import org.jelik.parser.ast.operators.AndExpr
import org.jelik.parser.ast.operators.EqualExpr
import org.jelik.parser.ast.operators.GreaterExpr
import org.jelik.parser.ast.operators.LesserExpr
import org.jelik.parser.ast.operators.NotEqualExpr
import org.jelik.parser.ast.operators.OrExpr
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
class FalseLabelExtractor : AstVisitor() {

    lateinit var result: LabelNode

    override fun visitAbstractLogicalOpExpr(expr: AbstractLogicalOpExpr, compilationContext: CompilationContext) {
        result = expr.falseLabelNode
        expr.left.accept(this, compilationContext)
        expr.right.accept(this, compilationContext)
    }

    override fun visit(greaterExpr: GreaterExpr, compilationContext: CompilationContext) {
        this.visitAbstractLogicalOpExpr(greaterExpr as AbstractLogicalOpExpr, compilationContext)
    }

    override fun visit(lesserExpr: LesserExpr, compilationContext: CompilationContext) {
        this.visitAbstractLogicalOpExpr(lesserExpr as AbstractLogicalOpExpr, compilationContext)
    }

    override fun visit(op: EqualExpr, compilationContext: CompilationContext) {
        this.visitAbstractLogicalOpExpr(op as AbstractLogicalOpExpr, compilationContext)
    }

    override fun visit(op: NotEqualExpr, compilationContext: CompilationContext) {
        this.visitAbstractLogicalOpExpr(op as AbstractLogicalOpExpr, compilationContext)
    }

    override fun visitOrExpr(orExpr: OrExpr, compilationContext: CompilationContext) {
        this.visitAbstractLogicalOpExpr(orExpr as AbstractLogicalOpExpr, compilationContext)
    }

    override fun visit(andExpr: AndExpr, compilationContext: CompilationContext) {
        this.visitAbstractLogicalOpExpr(andExpr as AbstractLogicalOpExpr, compilationContext)
    }
}
