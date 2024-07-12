package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleGenerator {
    String excelFilePath;
    String outputFolder;
    List<String> filenames;

    public RuleGenerator(String excelFilePath, String outputFolder, List<String> filenames) {
        this.excelFilePath = excelFilePath;
        this.outputFolder = outputFolder;
        this.filenames = filenames;
    }

    public void generate() {
        try (FileInputStream excelFile = new FileInputStream(new File(excelFilePath))) {
            Workbook workbook = new HSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getLastRowNum() == 0 && sheet.getRow(0) == null) {
                Row headerRow = sheet.createRow(0);
                createHeaderRow(headerRow);
            }

            for (String filename : filenames) {
                updateSheetWithFilename(sheet, filename);
            }

            // 创建输出文件夹（如果不存在）
            File outputDir = new File(outputFolder);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // 保存更新后的文件到指定的输出文件夹
            try (FileOutputStream outFile = new FileOutputStream(new File(outputFolder, "updated_fileMonitorRules.xls"))) {
                workbook.write(outFile);
            }

            workbook.close();

            System.out.println("Excel 文件更新成功，保存到：" + outputFolder + File.separator + "updated_fileMonitorRules.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createHeaderRow(Row row) {
        row.createCell(0).setCellValue("owner");
        row.createCell(1).setCellValue("srcPath");
        row.createCell(2).setCellValue("dstPath");
        row.createCell(3).setCellValue("tmpPath");
        row.createCell(4).setCellValue("triggerCallbackName");
        row.createCell(5).setCellValue("ignore");
        row.createCell(6).setCellValue("condition");
        row.createCell(7).setCellValue("alertTime");
        row.createCell(8).setCellValue("alertCount");
        row.createCell(9).setCellValue("alertInerval");
        row.createCell(10).setCellValue("lzo");
        row.createCell(11).setCellValue("transferCheckFile");
        row.createCell(12).setCellValue("maxRetryCount");
        row.createCell(13).setCellValue("schedule");
    }

    public static void updateSheetWithFilename(Sheet sheet, String filename) {
        // 从文件名中提取日期组件
        Pattern pattern = Pattern.compile("_(\\d{8})\\.dtf$");
        Matcher matcher = pattern.matcher(filename);
        if (!matcher.find()) {
            throw new IllegalArgumentException("文件名不包含有效的日期模式");
        }

        String dateStr = matcher.group(1);
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(4, 6);
        String day = dateStr.substring(6, 8);

        // 构建新文件名部分
        String baseFileName = filename.substring(0, filename.length() - 13);

        String outputFileName = baseFileName.replace("_TJ_A_UTF", "_A_UTF");
        String baseFolderName = outputFileName.replace("_A_UTF", "");

        // 构建路径模板
        String srcPathTemplate = "/nasbham_sh/input/day/${YYYY}${MM}${DD}/%s_${YYYY}${MM}${DD}.dtf";
        String dstPathTemplate = "hdfs://hdfs-ha/bdbham/bdbham_SHFH/input/day/data/%s/${YYYY}${MM}/${DD}/A/%s_${YYYY}${MM}${DD}.dtf";
        String tmpPathTemplate = "hdfs://hdfs-ha/user/pipline/tmp/bdbham_SHFH/input/day/data/%s/${YYYY}${MM}/${DD}/A/%s_${YYYY}${MM}${DD}.dtf";

        // 使用模板和日期组件构建路径
        String srcPath = String.format(srcPathTemplate, outputFileName);
        String dstPath = String.format(dstPathTemplate, baseFolderName, outputFileName);
        String tmpPath = String.format(tmpPathTemplate, baseFolderName, outputFileName);

        // 创建新行并填充数据
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(0).setCellValue("BHAM_BDL_SHFH_PROJECT");
        row.createCell(1).setCellValue(srcPath); // srcPath
        row.createCell(2).setCellValue(dstPath); // dstPath
        row.createCell(3).setCellValue(tmpPath); // tmpPath
        row.createCell(4).setCellValue(""); // triggerCallbackName
        row.createCell(5).setCellValue("FALSE"); // ignore
        row.createCell(6).setCellValue("{\"startDate\":\"" + year + "-" + month + "-" + day + "\"}"); // condition
        row.createCell(7).setCellValue(""); // alertTime
        row.createCell(8).setCellValue(0); // alertCount
        row.createCell(9).setCellValue(0); // alertInerval
        row.createCell(10).setCellValue("FALSE"); // lzo
        row.createCell(11).setCellValue("TRUE"); // transferCheckFile
        row.createCell(12).setCellValue(1); // maxRetryCount
        row.createCell(13).setCellValue("D"); // schedule
    }

    public static void main(String[] args) {
        // 示例调用
        List<String> filenames = Arrays.asList(
                "TJMIS_MDM_001_A_TEST_TABLE_TJ_A_UTF_20240708.dtf",
                "SHMIS_MDM_002_A_TEST_TABLE_TJ_A_UTF_20240709.dtf"
        );
        RuleGenerator updater = new RuleGenerator("src/main/resources/fileMonitorRules.xls", "src/main/rule", filenames);
        updater.generate();
    }
}
