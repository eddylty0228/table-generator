package org.example;

import java.util.List;

public class MultiTableTempGenerator {
    List<String> okFiles;
    String templateFilePath;
    String outputFolderPath;

    public MultiTableTempGenerator(List<String> okFiles, String templateFilePath, String outputFolderPath) {
        this.okFiles = okFiles;
        this.templateFilePath = templateFilePath;
        this.outputFolderPath = outputFolderPath;
    }
    public void generate() {
        TableTemplateGenerator generator;
        for (String okFile : okFiles) {
            generator = new TableTemplateGenerator(okFile,templateFilePath, outputFolderPath);
            generator.generate();
        }
    }
}
