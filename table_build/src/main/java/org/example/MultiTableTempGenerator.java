package org.example;

import java.util.List;

public class MultiTableTempGenerator {
    List<String> okFiles;
    String templateFilePath;
    String outputFolderPath;
    TableData tableDataSetting;

    public MultiTableTempGenerator(List<String> okFiles, String templateFilePath, String outputFolderPath, TableData tableDataSetting) {
        this.okFiles = okFiles;
        this.templateFilePath = templateFilePath;
        this.outputFolderPath = outputFolderPath;
        this.tableDataSetting = tableDataSetting;
    }
    public void generate() {
        TableTemplateGenerator generator;
        for (String okFile : okFiles) {
            generator = new TableTemplateGenerator(okFile,templateFilePath, outputFolderPath, tableDataSetting);
            generator.generate();
        }
    }
}
