package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OkFileReader {

    public static List<String> readOkFiles(String directoryPath) {
        List<String> okFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".ok"));

            if (files != null) {
                for (File file : files) {
                    okFiles.add(file.getName());
                }
            }
        } else {
            System.out.println("The provided directory path is invalid.");
        }

        return okFiles;
    }

    public static void main(String[] args) {
        String directoryPath = "path/to/your/directory"; // 请替换为你的目录路径
        List<String> okFiles = readOkFiles(directoryPath);

        System.out.println("Found .ok files:");
        for (String fileName : okFiles) {
            System.out.println(fileName);
        }
    }
}
