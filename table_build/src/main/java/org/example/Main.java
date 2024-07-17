package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.example.OkFilePathList.readExcelFileToList;


public class Main {
    public static String directoryPath = "src/main/templates";
    public static void main(String[] args) {
        String fexTemplatePath = "src/main/resources/fexTemplate.csv";
        // 输入文件名列表
        List<String> inputFileNames = new ArrayList<>();




        // ok文件路径
        String filePath = "src/main/resources/okFilePath_test.xlsx"; // Excel文件的路径
        List<String> okFiles = readExcelFileToList(filePath);
        for (String fileName : okFiles) {
            inputFileNames.add(okPathToDtf(fileName));
        }

        FexTemplateGenerator fexTemplateGenerator = new FexTemplateGenerator(fexTemplatePath,inputFileNames,directoryPath);
        fexTemplateGenerator.generate();

        String tableTemplateFilePath = "src/main/resources/addTableTemplate.xlsx";

        MultiTableTempGenerator multiTableTempGenerator = new MultiTableTempGenerator(okFiles,tableTemplateFilePath,directoryPath);
        multiTableTempGenerator.generate();

        String ruleTemplatePath = "src/main/resources/fileMonitorRules.xls";

        RuleGenerator ruleGenerator = new RuleGenerator(ruleTemplatePath,directoryPath,inputFileNames);
        ruleGenerator.generate();
    }
    public static String okPathToDtf(String okPath) {
        String[] array = okPath.split("/");
        return array[array.length-1].replace(".ok",".dtf");
    }
}