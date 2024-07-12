package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
///
public class TableTemplateGenerator {
    private String okFilePath;
    private String templateFilePath;
    private String outputFolderPath;
    private Map<String, Integer> templateMappings;

    // 构造函数，初始化路径
    public TableTemplateGenerator(String okFilePath, String templateFilePath, String outputFolderPath) {
        this.okFilePath = okFilePath;
        this.templateFilePath = templateFilePath;
        this.outputFolderPath = outputFolderPath;
    }

    // 生成Excel文件
    public void generate() {
        // 从模板文件中提取模板映射
        templateMappings = extractTemplateMappings(templateFilePath);
        if (templateMappings == null) {
            System.err.println("提取模板映射时出错。");
            return;
        }

        // 从OK文件中提取基础数据和表结构数据
        Map<String, String> basicData = extractBasicDataFromOKFile(okFilePath);
        List<String[]> tableStructData = extractTableStructDataFromOKFile(okFilePath);
        if (basicData != null && tableStructData != null) {
            try {
                String okFileName = new File(okFilePath).getName();
                String baseFileName = okFileName.substring(0, okFileName.lastIndexOf('.'));
                String outputFileName = baseFileName + "_table_template.xlsx";
                String outputFilePath = Paths.get(outputFolderPath, outputFileName).toString();

                // 用提取的数据填充模板并生成新Excel文件
                fillTemplateWithData(templateFilePath, outputFilePath, basicData, tableStructData);
                System.out.println("新Excel文件生成成功: " + outputFilePath);
            } catch (IOException e) {
                System.err.println("生成Excel文件时出错: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("提取数据时出错。");
        }
    }

    // 从OK文件中提取基础数据
    private static Map<String, String> extractBasicDataFromOKFile(String filePath) {
        Map<String, String> data = new HashMap<>();
        try {
            // 从完整文件路径中读取文件
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("文件未找到: " + filePath);
            }

            InputStream inputStream = new FileInputStream(file);

            // 提取文件名信息
            String fileName = file.getName();
            String fileBaseName = fileName.substring(0, fileName.indexOf("_TJ")).toLowerCase();
            String upperBaseName = fileBaseName.toUpperCase();
            String versionDate = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));

            // 填充基础数据
            data.put("*英文名", fileBaseName);
            data.put("中文名/实体名", "");
            data.put("生命周期", "");
            data.put("备注", "");
            data.put("是否归档", "false");
            data.put("源表名", upperBaseName);
            data.put("接口文件名", upperBaseName);
            data.put("下发方式", "01");
            data.put("表属性系统四位编码", "");
            data.put("上一版本结束日期", "");
            data.put("本版本数据开始日期", versionDate);

            // 解析XML内容
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            // 提取encoding
            NodeList encodingNodes = doc.getElementsByTagName("encoding");
            if (encodingNodes.getLength() > 0) {
                data.put("源文件编码", encodingNodes.item(0).getTextContent());
            }

            // 提取format
            NodeList formatNodes = doc.getElementsByTagName("format");
            if (formatNodes.getLength() > 0) {
                data.put("源文件格式", formatNodes.item(0).getTextContent());
            }
            data.put("表类型", "MANAGED");

        } catch (Exception e) {
            System.err.println("提取基础数据时出错: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return data;
    }

    // 从OK文件中提取表结构数据
    private static List<String[]> extractTableStructDataFromOKFile(String filePath) {
        List<String[]> data = new ArrayList<>();
        try {
            // 从完整文件路径中读取文件
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("文件未找到: " + filePath);
            }

            InputStream inputStream = new FileInputStream(file);

            // 解析XML内容
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            // 提取table_struct
            NodeList tableStructNodes = doc.getElementsByTagName("table_struct");
            if (tableStructNodes.getLength() > 0) {
                String[] rows = tableStructNodes.item(0).getTextContent().split("\n");
                for (String row : rows) {
                    String[] columns = row.split(";");
                    // 如果有空的字段，用""填充，确保每行有14个值
                    List<String> columnList = new ArrayList<>(Arrays.asList(columns));
                    while (columnList.size() < 14) {
                        columnList.add("");
                    }
                    data.add(columnList.toArray(new String[0]));
                }
            }
        } catch (Exception e) {
            System.err.println("提取表结构数据时出错: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return data;
    }

    // 用提取的数据填充模板并生成新Excel文件
    private void fillTemplateWithData(String templatePath, String outputPath, Map<String, String> basicData, List<String[]> tableStructData) throws IOException {
        // 从完整文件路径中读取模板文件
        File templateFile = new File(templatePath);
        if (!templateFile.exists()) {
            throw new FileNotFoundException("模板文件未找到: " + templatePath);
        }

        try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(templateFile.toPath()));
             FileOutputStream fos = new FileOutputStream(outputPath)) {

            Sheet sheet = workbook.getSheetAt(0);

            // 使用映射信息填充基础数据
            for (Map.Entry<String, Integer> entry : templateMappings.entrySet()) {
                String key = entry.getKey();
                Integer cellIndex = entry.getValue();
                String value = basicData.get(key);
                Row row = sheet.getRow(4);
                if (row == null) {
                    row = sheet.createRow(4);
                }
                updateCell(row, cellIndex, value);
            }

            // 填充table_struct数据从第10行开始
            int startingRow = 9;
            for (int i = 0; i < tableStructData.size(); i++) {
                Row row = sheet.getRow(startingRow + i);
                if (row == null) {
                    row = sheet.createRow(startingRow + i);
                }
                String[] rowData = tableStructData.get(i);
                try {
                    updateCell(row, 0, getValueSafely(rowData, 5));  // 字段*英文名
                    updateCell(row, 1, getValueSafely(rowData, 6));  // 字段中文名
                    updateCell(row, 2, getValueSafely(rowData, 7));  // 字段类型
                    if ("DECIMAL".equals(getValueSafely(rowData, 7))) {
                        updateCell(row, 3, getValueSafely(rowData, 8) + "," + getValueSafely(rowData, 10));
                    } else {
                        updateCell(row, 3, getValueSafely(rowData, 8));
                    }
                    updateCell(row, 4, !getValueSafely(rowData, 12).isEmpty() ? "true" : "false");
                    updateCell(row, 6, "N".equals(getValueSafely(rowData, 11)) ? "true" : "false");
                    updateCell(row, 7, "1");
                    updateCell(row, 8, "0");
                    updateCell(row, 11, "false");
                    updateCell(row, 14, "false");
                } catch (Exception e) {
                    System.err.println("填充表结构数据时出错: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 在最后添加新行
            Row newRow = sheet.createRow(startingRow + tableStructData.size());
            String[] newRowData = {"dt", "", "STRING", "10", "false", "", "false", "1", "0", "", "", "true", "1", "", "false"};
            for (int j = 0; j < newRowData.length; j++) {
                updateCell(newRow, j, newRowData[j]);
            }

            // 将工作簿内容写入输出文件
            workbook.write(fos);
        } catch (Exception e) {
            System.err.println("写入Excel文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 从模板文件中提取模板映射
    private Map<String, Integer> extractTemplateMappings(String templatePath) {
        Map<String, Integer> mappings = new HashMap<>();
        try {
            File templateFile = new File(templatePath);
            if (!templateFile.exists()) {
                throw new FileNotFoundException("模板文件未找到: " + templatePath);
            }

            try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(templateFile.toPath()))) {
                Sheet sheet = workbook.getSheetAt(0);
                Row headerRow = sheet.getRow(2); // 假设第一行是标题行

                for (Cell cell : headerRow) {
                    String cellValue = cell.getStringCellValue();
                    switch (cellValue) {
                        case "*英文名":
                            mappings.put("*英文名", cell.getColumnIndex());
                            break;
                        case "中文名/实体名":
                            mappings.put("中文名/实体名", cell.getColumnIndex());
                            break;
                        case "生命周期":
                            mappings.put("生命周期", cell.getColumnIndex());
                            break;
                        case "备注":
                            mappings.put("备注", cell.getColumnIndex());
                            break;
                        case "是否归档":
                            mappings.put("是否归档", cell.getColumnIndex());
                            break;
                        case "源表名":
                            mappings.put("源表名", cell.getColumnIndex());
                            break;
                        case "接口文件名":
                            mappings.put("接口文件名", cell.getColumnIndex());
                            break;
                        case "下发方式":
                            mappings.put("下发方式", cell.getColumnIndex());
                            break;
                        case "表属性系统四位编码":
                            mappings.put("表属性系统四位编码", cell.getColumnIndex());
                            break;
                        case "源文件编码":
                            mappings.put("源文件编码", cell.getColumnIndex());
                            break;
                        case "源文件格式":
                            mappings.put("源文件格式", cell.getColumnIndex());
                            break;
                        case "表类型":
                            mappings.put("表类型", cell.getColumnIndex());
                            break;
                        case "上一版本结束日期":
                            mappings.put("上一版本结束日期", cell.getColumnIndex());
                            break;
                        case "本版本数据开始日期":
                            mappings.put("本版本数据开始日期", cell.getColumnIndex());
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("提取模板映射时出错: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return mappings;
    }

    // 安全获取数组元素值的方法
    private static String getValueSafely(String[] array, int index) {
        return index < array.length ? array[index] : "";
    }

    // 更新单元格内容的方法
    private static void updateCell(Row row, int cellIndex, String value) {
        try {
            Cell cell = row.getCell(cellIndex);
            if (cell == null) {
                cell = row.createCell(cellIndex);
            }
            if (value != null && !value.isEmpty()) {
                cell.setCellValue(value);
            }
        } catch (Exception e) {
            System.err.println("更新单元格时出错 (行: " + row.getRowNum() + ", 列: " + cellIndex + "): " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 示例用法
    public static void main(String[] args) {
        // 示例文件路径
        String okFilePath = "C:/Users/lty22/table_build/src/main/resources/SHMIS_MDM_001_A_TEST_TABLE_TJ_A_UTF_20240708.ok";
        String templateFilePath = "src/main/resources/addTableTemplate.xlsx";
        String outputFolderPath = "src/main/addTableTemplate";

        // 创建生成器对象并调用生成方法
        TableTemplateGenerator generator = new TableTemplateGenerator(okFilePath, templateFilePath, outputFolderPath);
        generator.generate();
    }
}

