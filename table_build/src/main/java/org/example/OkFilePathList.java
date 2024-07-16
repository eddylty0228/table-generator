package org.example;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OkFilePathList {
    // 定义一个方法，将Excel文件中的数据读取到List中
    public static List<String> readExcelFileToList(String filePath) {
        List<String> filePaths = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) { // 打开Excel文件

            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            Iterator<Row> rowIterator = sheet.iterator(); // 获取行迭代器

            // 跳过表头
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(0); // 假设数据在第一列
                if (cell != null) {
                    filePaths.add(cell.getStringCellValue()); // 将单元格的值添加到列表中
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePaths;
    }

    public static void main(String[] args) {
        String filePath = "src/main/resources/okFilePath_test.xlsx"; // Excel文件的路径
        List<String> filePaths = readExcelFileToList(filePath);

        // 打印列表以验证输出
        for (String path : filePaths) {
            System.out.println(path);
        }
    }
}
