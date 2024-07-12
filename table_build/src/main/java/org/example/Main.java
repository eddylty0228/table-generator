package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static String directoryPath = "src/main/templates";
    public static void main(String[] args) {
        String fexTemplatePath = "src/main/resources/fexTemplate.csv";
        // 输入文件名列表
        List<String> inputFileNames = Arrays.asList(
                "SHMIS_GDM_NNGB_TJ_A_UTF_20240708.dtf",
                "TJMIS_GDM_SHBU_TJ_A_UTF_20240709.dtf",
                "SHMIS_GDM_SHBU_TJ_A_UTF_20240710.dtf"
        );


        FexTemplateGenerator fexTemplateGenerator = new FexTemplateGenerator(fexTemplatePath,inputFileNames,directoryPath);
        fexTemplateGenerator.generate();


        List<String> okFiles = Collections.singletonList("src/main/resources/SHMIS_MDM_001_A_TEST_TABLE_TJ_A_UTF_20240708.ok");
        String tableTemplateFilePath = "src/main/resources/addTableTemplate.xlsx";

        MultiTableTempGenerator multiTableTempGenerator = new MultiTableTempGenerator(okFiles,tableTemplateFilePath,directoryPath);
        multiTableTempGenerator.generate();

        String ruleTemplatePath = "src/main/resources/fileMonitorRules.xls";

        RuleGenerator ruleGenerator = new RuleGenerator(ruleTemplatePath,directoryPath,inputFileNames);
        ruleGenerator.generate();
    }
}