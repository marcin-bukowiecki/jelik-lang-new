package org.jelik.parser.ast;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModuleContext {

    private String canonicalName;

    private String fileAbsolutePath;
}
