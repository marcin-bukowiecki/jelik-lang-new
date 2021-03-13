/*
 * Copyright 2019 Marcin Bukowiecki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jelik.parser;

import lombok.Getter;
import org.jelik.parser.exceptions.SyntaxException;
import org.jelik.parser.token.LiteralToken;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Marcin Bukowiecki
 */
public class CharPointer {

    private int lineNumber = 1;

    private int columnNumber = 0;

    private BufferedReader br;

    private int current;

    @Getter
    private final String fileAbsolutePath;

    public CharPointer(File file) {
        try {
            this.br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.fileAbsolutePath = file.getAbsolutePath();
    }

    public CharPointer(String content) {
        this.br = new BufferedReader(new CharArrayReader(content.toCharArray()));
        this.fileAbsolutePath = "DUMMY_FILE";
    }

    public int nextChar() {
        try {
            current = br.read();
        } catch (IOException e) {
            throw new SyntaxException("Unexpected token",
                    new LiteralToken(lineNumber, columnNumber - 3, "eof"),
                    fileAbsolutePath);
        }

        columnNumber++;

        return current;
    }

    public int current() {
        return current;
    }

    public void incrLineNumber() {
        this.columnNumber = 1;
        this.lineNumber++;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void finish() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNext() {
        switch (current()) {
            case 65535:
            case -1:
                finish();
                return false;
            default:
                return true;
        }
    }
}
