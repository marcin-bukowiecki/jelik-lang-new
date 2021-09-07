package org.jelik.compiler;

import org.assertj.core.api.Assertions;
import org.jelik.compiler.data.ClassData;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Marcin Bukowiecki
 */
public class JelikCompilerTest {

    @Test
    public void compileAdderModule() throws Exception {
        final String content = "fun adder(a Int, b Int) -> Int { ret a + b }";
        Path path = Paths.get("./src/test/resources/temp/TempModule.jlk");
        File file = Files.writeString(path, content).toFile();
        JelikCompiler.INSTANCE.compile(Collections.singletonList(file));
    }

    @Test
    public void createJavaClassData() {
        CompilationContext compilationContext = new CompilationContext();
        ClassData javaClassData = JelikCompiler.INSTANCE.createJavaClassData(ArrayList.class, compilationContext);
        assert javaClassData != null;
        Assertions.assertThat(javaClassData.findByName("stream", compilationContext))
                .isNotEmpty();
    }
}
