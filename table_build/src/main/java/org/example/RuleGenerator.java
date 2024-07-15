package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RuleGenerator {
    String excelFilePath;
    String outputFolder;
    List<String> filenames;
    Map<String, Integer> headerIndexMap;

    public RuleGenerator(String excelFilePath, String outputFolder, List<String> filenames) {
        this.excelFilePath = excelFilePath;
        this.outputFolder = outputFolder;
        this.filenames = filenames;
        this.headerIndexMap = new HashMap<>();
    }

    public void generate() {
        try (FileInputStream excelFile = new FileInputStream(new File(excelFilePath))) {
            Workbook workbook = new HSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getLastRowNum() == 0 && sheet.getRow(0) == null) {
                Row headerRow = sheet.createRow(0);
                copyHeaderFromTemplate(headerRow, sheet);
            } else {
                // 获取表头索引
                getHeaderIndex(sheet.getRow(0));
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

    public static void copyHeaderFromTemplate(Row row, Sheet sheet) {
        // 假设模板的表头在第一个sheet的第一行
        Row templateHeaderRow = sheet.getRow(0);
        for (Cell cell : templateHeaderRow) {
            Cell newCell = row.createCell(cell.getColumnIndex());
            newCell.setCellValue(cell.getStringCellValue());
        }
    }

    public void getHeaderIndex(Row headerRow) {
        for (Cell cell : headerRow) {
            headerIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
    }

    public void updateSheetWithFilename(Sheet sheet, String filename) {
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

        // 使用模板构建路径，不替换日期占位符
        String srcPath = String.format(srcPathTemplate, outputFileName);
        String dstPath = String.format(dstPathTemplate, baseFolderName, outputFileName);
        String tmpPath = String.format(tmpPathTemplate, baseFolderName, outputFileName);

        // 创建新行并填充数据
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(headerIndexMap.get("owner")).setCellValue("BHAM_BDL_SHFH_PROJECT");
        row.createCell(headerIndexMap.get("srcPath")).setCellValue(srcPath); // srcPath
        row.createCell(headerIndexMap.get("dstPath")).setCellValue(dstPath); // dstPath
        row.createCell(headerIndexMap.get("tmpPath")).setCellValue(tmpPath); // tmpPath
        row.createCell(headerIndexMap.get("triggerCallbackName")).setCellValue(""); // triggerCallbackName
        row.createCell(headerIndexMap.get("ignore")).setCellValue("FALSE"); // ignore
        row.createCell(headerIndexMap.get("condition")).setCellValue("{\"startDate\":\"" + year + "-" + month + "-" + day + "\"}"); // condition
        row.createCell(headerIndexMap.get("alertTime")).setCellValue(""); // alertTime
        row.createCell(headerIndexMap.get("alertCount")).setCellValue(0); // alertCount
        row.createCell(headerIndexMap.get("alertInerval")).setCellValue(0); // alertInerval
        row.createCell(headerIndexMap.get("lzo")).setCellValue("FALSE"); // lzo
        row.createCell(headerIndexMap.get("transferCheckFile")).setCellValue("TRUE"); // transferCheckFile
        row.createCell(headerIndexMap.get("maxRetryCount")).setCellValue(1); // maxRetryCount
        row.createCell(headerIndexMap.get("schedule")).setCellValue("D"); // schedule
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
