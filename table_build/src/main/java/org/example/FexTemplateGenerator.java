package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FexTemplateGenerator {
    String templatePath;
    List<FexFileData> inputFexFileDataList;
    String outputDirectory;
    private static final Logger logger = LogManager.getLogger(FexTemplateGenerator.class);

    public FexTemplateGenerator(String templatePath, List<FexFileData> inputFexFileDataList, String outputFileDirectory) {
        this.templatePath = templatePath;
        this.inputFexFileDataList = inputFexFileDataList;
        this.outputDirectory = outputFileDirectory;
    }

    public void generate() {
        String outputFileName = generateOutputFileName(outputDirectory);

        try {
            generateCsvFromTemplate(templatePath, outputFileName, inputFexFileDataList);
            logger.info("文件生成成功: {}", outputFileName);
        } catch (IOException e) {
            logger.error("生成文件时出错", e);
        }
    }

    public static String generateOutputFileName(String outputDirectory) {
        String outputFileName = "combined_fexTemplate.csv";
        return Paths.get(outputDirectory, outputFileName).toString();
    }

    public static void generateCsvFromTemplate(String templatePath, String outputFileName, List<FexFileData> inputFexFileDataList) throws IOException {
        // 如果输出目录不存在，则创建它
        java.nio.file.Path outputPath = Paths.get(outputFileName).getParent();
        if (outputPath != null && !Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(templatePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

            String line;
            boolean isHeader = true;
            String headerLine = null;

            // 读取模板头部
            if ((headerLine = reader.readLine()) != null) {
                writer.write(headerLine);
                writer.newLine();
            }

            // 处理每个输入文件数据
            for (FexFileData fexFileData : inputFexFileDataList) {
                String processedLine = fexFileData.getOperationFlag() + "," +
                        fexFileData.getServiceProviderSystemChineseName() + "," +
                        fexFileData.getServiceProviderSystemCode() + "," +
                        fexFileData.getServiceProviderSystemIP() + "," +
                        fexFileData.getSourceFileLandingPath() + "," +
                        fexFileData.getSourceFileName() + "," +
                        fexFileData.getInitializationID() + "," +
                        fexFileData.getConsumerSystemChineseName() + "," +
                        fexFileData.getConsumerSystemCode() + "," +
                        fexFileData.getConsumerSystemIP() + "," +
                        fexFileData.getFileReceivePath() + "," +
                        fexFileData.getLandingFileName() + "," +
                        fexFileData.getIssuingSystemFlag() + "," +
                        fexFileData.getSplitType() + "," +
                        fexFileData.getInitializationDate() + "," +
                        fexFileData.getDataUnloadFrequency() + "," +
                        fexFileData.getOrganizationName() + "," +
                        fexFileData.getOrganizationCode();
                writer.write(processedLine);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) {
        // 模板路径
        String templatePath = "src/main/resources/fexTemplate.csv";
        // 输出目录
        String outputDirectory = "src/main/tubeTemplate";
        List<FexFileData> inputFexFileDataList = new ArrayList<>();
        FexFileData fexFileData01 = new FexFileData("TEST_TABLE_001","TEST_TABLE_001");
        FexFileData fexFileData02 = new FexFileData("TEST_TABLE_002","TEST_TABLE_002");

        inputFexFileDataList.add(fexFileData01);
        inputFexFileDataList.add(fexFileData02);
        FexTemplateGenerator generator = new FexTemplateGenerator(templatePath, inputFexFileDataList, outputDirectory);
        generator.generate();
    }
}

