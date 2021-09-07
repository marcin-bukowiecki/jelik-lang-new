package org.jelik.parser.ast.branching;

/**
 * @author Marcin Bukowiecki
 */
public class IfNodeContext extends ElseNodeContext {

    private boolean jumpOver;

    public boolean isJumpOver() {
        return jumpOver;
    }

    public void setJumpOver(boolean jumpOver) {
        this.jumpOver = jumpOver;
    }
}
