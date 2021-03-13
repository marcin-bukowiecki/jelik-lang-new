package org.jelik.compiler.exceptions;

import org.apache.commons.lang3.StringUtils;
import org.jelik.compiler.JelikCompiler;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.token.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class CompileException extends RuntimeException {

    private final String fileName;

    private final String message;

    private final int startCol;

    private final int startRow;

    private final int endCol;

    private final int endRow;

    public CompileException(String message, Token token, String fileName) {
        super(message);
        this.fileName = fileName;
        this.message = message;
        this.startCol = token.getCol();
        this.startRow = token.getRow();
        this.endCol = token.getCol() + token.toString().length();
        this.endRow = token.getRow();
    }

    public CompileException(String message, ASTNode astNode, ClassDeclaration classDeclaration) {
        this.fileName = classDeclaration.getAbsoluteFilePath();
        this.message = message;
        this.startCol = astNode.getStartCol();
        this.startRow = astNode.getStartRow();
        this.endCol = astNode.getEndCol();
        this.endRow = astNode.getEndRow();
    }

/*
    public CompileException(String message, ASTNode astNode, String fileName) {
        super(message);
        this.fileName = fileName;
        this.message = message;
        //this.startCol = astNode.getStartCol();
        //this.startRow = astNode.getLineNumber();
        //this.endCol = astNode.getEndCol();
        //this.endRow = astNode.getEndRow();
    }
*/
    public CompileException(String message, int startRow, int startCol, int endRow, int endCol, String fileName) {
        super(message);
        this.fileName = fileName;
        this.message = message;
        this.startCol = startCol;
        this.startRow = startRow;
        this.endCol = endCol;
        this.endRow = endRow;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndCol() {
        return endCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public void printErrorMessage() {
        var errOut = JelikCompiler.INSTANCE.getErrOut();
        errOut.println("Compiler error in " + this.fileName);
        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            int noOfLines = lines.size();
            int width = String.valueOf(noOfLines).length();

            int rowIndex = 1;
            int topOffset = 5;
            int bottomOffset = 5;

            errOut.println("...");
            for (String line : lines) {
                if (rowIndex == startRow) {
                    errOut.printf("%" + width + "s: " + line + "%n", rowIndex);
                    errOut.println(StringUtils.repeat(" ", width + 2) +
                            StringUtils.repeat(" ", startCol - 1) +
                            StringUtils.repeat("^", endCol - startCol));
                    errOut.println(StringUtils.repeat(" ", width + 2) +
                            StringUtils.repeat(" ", startCol - 1) +
                            this.message);
                } else {
                    if (rowIndex <= startRow && rowIndex >= startRow - topOffset) {
                        errOut.printf("%" + width + "s: " + line + "%n", rowIndex);
                    }
                    if (rowIndex >= endRow && rowIndex <= endRow + bottomOffset) {
                        errOut.printf("%" + width + "s: " + line + "%n", rowIndex);
                    }
                }
                if (rowIndex > endRow + bottomOffset) { //stop iterating
                    break;
                }
                rowIndex++;
            }
            errOut.println("...");
        } catch (IOException e) {
            throw new RuntimeException(fileName + " not found for error message printing");
        }
    }
}

