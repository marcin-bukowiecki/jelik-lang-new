package org.jelik.compiler;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class JelikCompilerTest {

    @Test
    public void compileAdderModule() throws Exception {
        final String content = "fun adder(a Int, b Int) -> Int { ret a + b }";
        Path path = Paths.get("./src/test/resources/TempModule.jlk");
        File file = Files.writeString(path, content).toFile();
        JelikCompiler.INSTANCE.compile(Collections.singletonList(file));
    }
}
