package org.jelik.compiler.exceptions;

import org.jelik.compiler.JelikCompiler;
import org.jelik.parser.CharPointer;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.token.Token;

import java.io.File;

/**
 * @author Marcin Bukowiecki
 */
public class CompileException extends RuntimeException {

    private final String absoluteFilePath;

    private final String message;

    private final int startOffset;

    private final int endOffset;

    public CompileException(String message, Token token, String absoluteFilePath) {
        super(message);
        this.absoluteFilePath = absoluteFilePath;
        this.message = message;
        this.startOffset = token.getStartOffset();
        this.endOffset = token.getEndOffset();
    }

    public CompileException(String message, ASTNode astNode, ClassDeclaration classDeclaration) {
        this.absoluteFilePath = classDeclaration.getAbsoluteFilePath();
        this.message = message;
        this.startOffset = astNode.getStartOffset();
        this.endOffset = astNode.getEndOffset();
    }

    public CompileException(String message, ASTNode astNode, String absoluteFilePath) {
        this.absoluteFilePath = absoluteFilePath;
        this.message = message;
        this.startOffset = astNode.getStartOffset();
        this.endOffset = astNode.getEndOffset();
    }

    public CompileException(String message, int startOffset, int endOffset, String absoluteFilePath) {
        super(message);
        this.absoluteFilePath = absoluteFilePath;
        this.message = message;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public String getAbsoluteFilePath() {
        return absoluteFilePath;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void printErrorMessage() {
        var errOut = JelikCompiler.INSTANCE.getErrOut();
        errOut.println("Error in " + this.absoluteFilePath);

        boolean markerMode = false;
        StringBuilder marker = new StringBuilder();
        StringBuilder line = new StringBuilder();

        var cp = new CharPointer(new File(this.absoluteFilePath));
        while (cp.hasNext()) {
            var ch = (char) cp.nextChar();
            line.append(ch);

            if (ch == '\n' || ch == '\r') {
                System.err.print(line);
                if (markerMode) {
                    System.err.println(marker);
                    System.err.println(marker.substring(0, marker.indexOf("^")) + message);
                    markerMode = false;
                }
                line = new StringBuilder();
                marker = new StringBuilder();
            }

            if (startOffset == cp.getOffset()) {
                markerMode = true;
            }
            if (markerMode) {
                marker.append("^");
            } else {
                if (ch != '\n' && ch != '\r') {
                    marker.append(" ");
                }
            }
        }
    }
}

