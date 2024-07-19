package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static String directoryPath = "src/main/output";
    public static String fexTemplatePath = "src/main/templates/fexTemplateEmpty.csv";
    public static String settingsFilePath = "settings.ser";

    public static void main(String[] args) {
        FexFileData fexFileDataSetting = loadSettings();

        if (args.length == 0) {
            System.out.println("No command line arguments provided.");
            return;
        }

        switch (args[0]) {
            case "-view":
                System.out.println("Settings:");
                System.out.println("    fex:");
                fexFileDataSetting.printAllVariables();
                System.out.println("fexTemplatePath = " + fexTemplatePath);
                break;
            case "-csetting":
                if (args.length != 3) {
                    System.out.println("Usage: -csetting <variable_name> <value>, Example: -csetting serviceProviderSystemCode 12.18.1");
                    return;
                }
                try {
                    changeSetting(fexFileDataSetting, args[1], args[2]);
                    saveSettings(fexFileDataSetting);
                    System.out.println("Setting updated successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "-multigen": // -multigen tablePath1,tablePath2,tablePath3
                if (args.length != 3) {
                    System.out.println("Usage: -multigen <value>,<value>,<value> <landingName>,<landingName>,<landingName>");
                    return;
                }
                String[] receivingNames = args[1].split(",");
                String[] landingNames = args[2].split(",");
                List<FexFileData> multiInputFexFileDataList = new ArrayList<>();
                for (int i = 0; i < receivingNames.length; i++) {
                    FexFileData tempFexFileData = new FexFileData(fexFileDataSetting);
                    tempFexFileData.setFileReceivePath(receivingNames[i]);
                    tempFexFileData.setLandingFileName(landingNames[i]);
                    multiInputFexFileDataList.add(tempFexFileData);
                }
                FexTemplateGenerator multiGenerator = new FexTemplateGenerator(fexTemplatePath, multiInputFexFileDataList, directoryPath);
                multiGenerator.generate();
                break;
            case "-gen": // -defaultgen sourceName landingName
                if (args.length != 3) {
                    System.out.println("need 3 arguments provided. Example: -gen Table.dtf Table.dtf");
                    return;
                }
                List<FexFileData> inputFexFileDataList = new ArrayList<>();
                FexFileData tempFexFileData = new FexFileData(fexFileDataSetting);
                tempFexFileData.setFileReceivePath(args[1]);
                tempFexFileData.setLandingFileName(args[2]);
                inputFexFileDataList.add(tempFexFileData);
                FexTemplateGenerator generator = new FexTemplateGenerator(fexTemplatePath, inputFexFileDataList, directoryPath);
                generator.generate();
                break;
            default:
                System.out.println("Invalid command.");
                break;
        }
    }

    private static void changeSetting(FexFileData fexFileDataSetting, String variableName, String value) {
        switch (variableName) {
            case "operationFlag":
                fexFileDataSetting.setOperationFlag(value);
                break;
            case "serviceProviderSystemChineseName":
                fexFileDataSetting.setServiceProviderSystemChineseName(value);
                break;
            case "serviceProviderSystemCode":
                fexFileDataSetting.setServiceProviderSystemCode(value);
                break;
            case "serviceProviderSystemIP":
                fexFileDataSetting.setServiceProviderSystemIP(value);
                break;
            case "sourceFileLandingPath":
                fexFileDataSetting.setSourceFileLandingPath(value);
                break;
            case "sourceFileName":
                fexFileDataSetting.setSourceFileName(value);
                break;
            case "initializationID":
                fexFileDataSetting.setInitializationID(value);
                break;
            case "consumerSystemChineseName":
                fexFileDataSetting.setConsumerSystemChineseName(value);
                break;
            case "consumerSystemCode":
                fexFileDataSetting.setConsumerSystemCode(value);
                break;
            case "consumerSystemIP":
                fexFileDataSetting.setConsumerSystemIP(value);
                break;
            case "fileReceivePath":
                fexFileDataSetting.setFileReceivePath(value);
                break;
            case "landingFileName":
                fexFileDataSetting.setLandingFileName(value);
                break;
            case "issuingSystemFlag":
                fexFileDataSetting.setIssuingSystemFlag(value);
                break;
            case "splitType":
                fexFileDataSetting.setSplitType(value);
                break;
            case "initializationDate":
                fexFileDataSetting.setInitializationDate(value);
                break;
            case "dataUnloadFrequency":
                fexFileDataSetting.setDataUnloadFrequency(value);
                break;
            case "organizationName":
                fexFileDataSetting.setOrganizationName(value);
                break;
            case "organizationCode":
                fexFileDataSetting.setOrganizationCode(value);
                break;
            default:
                System.out.println("Invalid variable name.");
                break;
        }
    }

    private static void saveSettings(FexFileData fexFileDataSetting) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingsFilePath))) {
            oos.writeObject(fexFileDataSetting);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FexFileData loadSettings() {
        File file = new File(settingsFilePath);
        if (!file.exists()) {
            return new FexFileData("", "");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(settingsFilePath))) {
            return (FexFileData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new FexFileData("", "");
        }
    }

    public static String okPathToDtf(String okPath) {
        String[] array = okPath.split("/");
        return array[array.length - 1].replace(".ok", ".dtf");
    }
}
