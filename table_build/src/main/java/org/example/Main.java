package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static String directoryPath = "src/main/output";
    public static String fexTemplatePath = "src/main/templates/fexTemplateEmpty.csv";
    public static String tableTemplatePath = "src/main/templates/addTableTemplate.xlsx";
    public static String ruleTemplatePath = "src/main/templates/fileMonitorRules.xls";
    public static String fexSettingsFilePath = "cfg/fexSettings.ser";
    public static String tableSettingsFilePath = "cfg/tableSettings.ser";
    public static String ruleSettingFilePath = "cfg/ruleSetting.ser";

    public static void main(String[] args) {
        FexFileData fexFileDataSetting = loadFexSettings();
        TableData tableDataSetting = loadTableSettings();
        RuleData ruleDataSetting = loadRuleSetting();

        if (args.length == 0) {
            System.out.println("No command line arguments provided.");
            return;
        }

        switch (args[0]) {
            case "-help":
                printHelp();
                break;

            case "-rsetting":
                if (args.length != 3) {
                    System.out.println("Invalid number of arguments.");
                    return;
                }
                try {
                    changeRuleSetting(ruleDataSetting, args[1], args[2]);
                    System.out.println("Rule setting changed.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "-rule":
                try {
                    List<String> filenames = new ArrayList<>();
                    for (int i = 1; i < args.length; i++) {
                        filenames.add(args[i]);
                    }
                    RuleGenerator ruleGenerator = new RuleGenerator(ruleTemplatePath, directoryPath, filenames, ruleDataSetting);
                    ruleGenerator.generate();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Usage: -rule <filename> <filename> <filename> <filename>");
                }
                break;

            case "-multitb":
                try {
                    List<String> okFiles = OkFileReader.readOkFiles(args[1]);
                    MultiTableTempGenerator multiTableTempGenerator = new MultiTableTempGenerator(okFiles, tableTemplatePath, directoryPath, tableDataSetting);
                    multiTableTempGenerator.generate();
                    System.out.println("Successfully generated multi table temp file.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Usage: -multitb okFilesPath");
                }
                break;

            case "-tbsetting":
                if (args.length != 3) {
                    System.out.println("Usage: -tbsetting <variable_name> <value>, Example: -tbsetting chineseName ExampleName");
                    return;
                }
                try {
                    changeTableSetting(tableDataSetting, args[1], args[2]);
                    saveTableSettings(tableDataSetting);
                    System.out.println("Table setting updated successfully.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "-table":
                if (args.length != 2) {
                    System.out.println("Wrong number of arguments. Usage: -table <okFilePath>");
                    return;
                }
                try {
                    TableTemplateGenerator tableTemplateGenerator = new TableTemplateGenerator(args[1], tableTemplatePath, directoryPath, tableDataSetting);
                    tableTemplateGenerator.generate();
                    System.out.println("Generated table template.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Usage: -table <okFilePath>");
                }
                break;

            case "-view":
                System.out.println("Settings:");
                System.out.println("    Fex setting: ");
                fexFileDataSetting.printAllVariables();
                System.out.println("fexTemplatePath = " + fexTemplatePath);
                System.out.println("    Table setting:");
                tableDataSetting.printData();
                System.out.println("    Rule setting:");
                ruleDataSetting.printSetting();
                break;

            case "-fxsetting":
                if (args.length != 3) {
                    System.out.println("Usage: -fxsetting <variable_name> <value>, Example: -fxsetting serviceProviderSystemCode 12.18.1");
                    return;
                }
                try {
                    changeFexSetting(fexFileDataSetting, args[1], args[2]);
                    saveFexSettings(fexFileDataSetting);
                    System.out.println("Setting updated successfully.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "-fexmultigen":
                if (args.length != 3) {
                    System.out.println("Usage: -fexmultigen <value>,<value>,<value> <landingName>,<landingName>,<landingName>");
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

            case "-fexgen":
                if (args.length != 3) {
                    System.out.println("Usage: -fexgen sourceName landingName");
                    return;
                }
                try {
                    List<FexFileData> inputFexFileDataList = new ArrayList<>();
                    FexFileData tempFexFileData = new FexFileData(fexFileDataSetting);
                    tempFexFileData.setFileReceivePath(args[1]);
                    tempFexFileData.setLandingFileName(args[2]);
                    inputFexFileDataList.add(tempFexFileData);
                    FexTemplateGenerator generator = new FexTemplateGenerator(fexTemplatePath, inputFexFileDataList, directoryPath);
                    generator.generate();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Usage: -fexgen sourceName landingName");
                }
                break;

            default:
                System.out.println("Invalid command.");
                printHelp();
                break;
        }
    }

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("    -help                      Display this help message.");
        System.out.println("    -rsetting <var> <value>    Change a rule setting.");
        System.out.println("    -rule <file1> <file2> ...  Generate rule files.");
        System.out.println("    -multitb <okFilesPath>     Generate multi table temp file.");
        System.out.println("    -tbsetting <var> <value>   Change a table setting.");
        System.out.println("    -table <okFilePath>        Generate a table template.");
        System.out.println("    -view                      View current settings.");
        System.out.println("    -fxsetting <var> <value>   Change an Fex setting.");
        System.out.println("    -fexmultigen <values> <landingNames> Generate multiple Fex templates.");
        System.out.println("    -fexgen <sourceName> <landingName> Generate a Fex template.");
    }

    private static void changeFexSetting(FexFileData fexFileDataSetting, String variableName, String value) {
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

    private static void changeTableSetting(TableData tableDataSetting, String variableName, String value) {
        switch (variableName) {
            case "chineseName":
                tableDataSetting.setChineseName(value);
                break;
            case "lifeCycle":
                tableDataSetting.setLifeCycle(value);
                break;
            case "remarks":
                tableDataSetting.setRemarks(value);
                break;
            case "isArchived":
                tableDataSetting.setIsArchived(value);
                break;
            case "deliveryMethod":
                tableDataSetting.setDeliveryMethod(value);
                break;
            case "tableAttributeSystemCode":
                tableDataSetting.setTableAttributeSystemCode(value);
                break;
            case "previousVersionEndDate":
                tableDataSetting.setPreviousVersionEndDate(value);
                break;
            default:
                System.out.println("Invalid variable name.");
                break;
        }
    }

    private static void changeRuleSetting(RuleData ruleDataSetting, String variableName, String value) {
        switch (variableName) {
            case "schedule":
                ruleDataSetting.setSchedule(value);
                break;
            case "maxRetryCount":
                ruleDataSetting.setMaxRetryCount(value);
                break;
            case "transferCheckFile":
                ruleDataSetting.setTransferCheckFile(value);
                break;
            case "lzo":
                ruleDataSetting.setLzo(value);
                break;
            case "alertInerval":
                ruleDataSetting.setAlertInerval(value);
                break;
            case "alertCount":
                ruleDataSetting.setAlertCount(value);
                break;
            case "alertTime":
                ruleDataSetting.setAlertTime(value);
                break;
            case "conditionTemplate":
                ruleDataSetting.setConditionTemplate(value);
                break;
            case "ignore":
                ruleDataSetting.setIgnore(value);
                break;
            case "triggerCallbackName":
                ruleDataSetting.setTriggerCallbackName(value);
                break;
            case "srcPathTemplate":
                ruleDataSetting.setSrcPathTemplate(value);
                break;
            case "dstPathTemplate":
                ruleDataSetting.setDstPathTemplate(value);
                break;
            case "tmpPathTemplate":
                ruleDataSetting.setTmpPathTemplate(value);
                break;
            case "owner":
                ruleDataSetting.setOwner(value);
                break;
        }
    }

    private static void saveRuleSetting(RuleData ruleDataSetting) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruleSettingFilePath))) {
            oos.writeObject(ruleDataSetting);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void saveFexSettings(FexFileData fexFileDataSetting) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fexSettingsFilePath))) {
            oos.writeObject(fexFileDataSetting);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void saveTableSettings(TableData tableDataSetting) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tableSettingsFilePath))) {
            oos.writeObject(tableDataSetting);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static RuleData loadRuleSetting() {
        File file = new File(ruleSettingFilePath);
        if (!file.exists()) {
            return new RuleData();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruleSettingFilePath))) {
            return (RuleData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return new RuleData();
        }
    }

    private static FexFileData loadFexSettings() {
        File file = new File(fexSettingsFilePath);
        if (!file.exists()) {
            return new FexFileData("", "");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fexSettingsFilePath))) {
            return (FexFileData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return new FexFileData("", "");
        }
    }

    private static TableData loadTableSettings() {
        File file = new File(tableSettingsFilePath);
        if (!file.exists()) {
            return new TableData();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tableSettingsFilePath))) {
            return (TableData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return new TableData();
        }
    }

    public static String okPathToDtf(String okPath) {
        String[] array = okPath.split("/");
        return array[array.length - 1].replace(".ok", ".dtf");
    }
}
