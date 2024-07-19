package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleGenerator {
    private String excelFilePath;
    private String outputFolder;
    private List<String> filenames;
    private Map<String, Integer> headerIndexMap;
    private RuleData ruleData;

    public RuleGenerator(String excelFilePath, String outputFolder, List<String> filenames, RuleData ruleData) {
        this.excelFilePath = excelFilePath;
        this.outputFolder = outputFolder;
        this.filenames = filenames;
        this.headerIndexMap = new HashMap<>();
        this.ruleData = ruleData;
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

        // 保留指定的代码段
        String baseFileName = filename.substring(0, filename.length() - 13);
        String outputFileName = baseFileName.replace("_TJ_A_UTF", "_A_UTF");
        String baseFolderName = outputFileName.replace("_A_UTF", "");

        String srcPath = String.format(ruleData.getSrcPathTemplate(), outputFileName);
        String dstPath = String.format(ruleData.getDstPathTemplate(), baseFolderName, outputFileName);
        String tmpPath = String.format(ruleData.getTmpPathTemplate(), baseFolderName, outputFileName);

        // 创建新行并填充数据
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(headerIndexMap.get("owner")).setCellValue(ruleData.getOwner());
        row.createCell(headerIndexMap.get("srcPath")).setCellValue(srcPath);
        row.createCell(headerIndexMap.get("dstPath")).setCellValue(dstPath);
        row.createCell(headerIndexMap.get("tmpPath")).setCellValue(tmpPath);
        row.createCell(headerIndexMap.get("triggerCallbackName")).setCellValue(ruleData.getTriggerCallbackName());
        row.createCell(headerIndexMap.get("ignore")).setCellValue(ruleData.getIgnore());
        row.createCell(headerIndexMap.get("condition")).setCellValue(ruleData.getConditionTemplate().replace("${YYYY}", year).replace("${MM}", month).replace("${DD}", day));
        row.createCell(headerIndexMap.get("alertTime")).setCellValue(ruleData.getAlertTime());
        row.createCell(headerIndexMap.get("alertCount")).setCellValue(ruleData.getAlertCount());
        row.createCell(headerIndexMap.get("alertInerval")).setCellValue(ruleData.getAlertInerval());
        row.createCell(headerIndexMap.get("lzo")).setCellValue(ruleData.getLzo());
        row.createCell(headerIndexMap.get("transferCheckFile")).setCellValue(ruleData.getTransferCheckFile());
        row.createCell(headerIndexMap.get("maxRetryCount")).setCellValue(ruleData.getMaxRetryCount());
        row.createCell(headerIndexMap.get("schedule")).setCellValue(ruleData.getSchedule());
    }
}
