package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FexTemplateGenerator {
    String templatePath;
    List<String> inputFileNames;
    String outputDirectory;
    private static final Logger logger = LogManager.getLogger(FexTemplateGenerator.class);

    public FexTemplateGenerator(String templatePath, List<String> inputFileNames, String outputFileDirectory) {
        this.templatePath = templatePath;
        this.inputFileNames = inputFileNames;
        this.outputDirectory = outputFileDirectory;
    }

    public void generate() {
        String outputFileName = generateOutputFileName(outputDirectory);

        try {
            generateCsvFromTemplate(templatePath, outputFileName, inputFileNames);
            logger.info("File generated successfully: {}", outputFileName);
        } catch (IOException e) {
            logger.error("Error generating file", e);
        }
    }

    public static String generateOutputFileName(String outputDirectory) {
        String outputFileName = "combined_fexTemplate.csv";
        return Paths.get(outputDirectory, outputFileName).toString();
    }

    public static void generateCsvFromTemplate(String templatePath, String outputFileName, List<String> inputFileNames) throws IOException {
        // Create output directory if it doesn't exist
        java.nio.file.Path outputPath = Paths.get(outputFileName).getParent();
        if (outputPath != null && !Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(templatePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

            String line;
            boolean isHeader = true;
            String templateLine = null;

            // Read template content
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    writer.write(line);
                    writer.newLine();
                    isHeader = false;
                } else {
                    templateLine = line;
                    break; // Only read the second line as template
                }
            }

            // Process each input file name
            for (String inputFileName : inputFileNames) {
                if (templateLine != null) {
                    String processedLine = templateLine.replace("源文件名称", generateSourceFileName(inputFileName))
                            .replace("落地文件名称", generateLandingFileName(inputFileName));
                    writer.write(processedLine);
                    writer.newLine();
                }
            }
        }
    }

    private static String generateSourceFileName(String inputFileName) {
        String baseFileName = inputFileName.substring(0, inputFileName.length() - 13);
        return baseFileName + "_YYYYMMDD.dtf";
    }

    private static String generateLandingFileName(String inputFileName) {
        String baseFileName = inputFileName.substring(0, inputFileName.length() - 13);
        return baseFileName.replace("_TJ_A_UTF", "_A_UTF") + "_YYYYMMDD.dtf";
    }

    public static void main(String[] args) {
        // 模板路径
        String templatePath = "src/main/resources/fexTemplate.csv";
        // 输入文件名列表
        List<String> inputFileNames = Arrays.asList(
                "SHMIS_GDM_NNGB_TJ_A_UTF_20240708.dtf",
                "TJMIS_GDM_SHBU_TJ_A_UTF_20240709.dtf",
                "SHMIS_GDM_SHBU_TJ_A_UTF_20240710.dtf"
        );
        // 输出目录
        String outputDirectory = "src/main/tubeTemplate";

        // 创建并运行FexTemplateGenerator
        FexTemplateGenerator generator = new FexTemplateGenerator(templatePath, inputFileNames, outputDirectory);
        generator.generate();
    }
}
