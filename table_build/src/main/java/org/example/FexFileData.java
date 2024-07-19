package org.example;

import java.io.Serializable;

public class FexFileData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String operationFlag;
    private String serviceProviderSystemChineseName;
    private String serviceProviderSystemCode;
    private String serviceProviderSystemIP;
    private String sourceFileLandingPath;
    private String sourceFileName;
    private String initializationID;
    private String consumerSystemChineseName;
    private String consumerSystemCode;
    private String consumerSystemIP;
    private String fileReceivePath;
    private String landingFileName;
    private String issuingSystemFlag;
    private String splitType;
    private String initializationDate;
    private String dataUnloadFrequency;
    private String organizationName;
    private String organizationCode;

    public FexFileData(String sourceFileName, String landingFileName) {
        this.operationFlag = "I";
        this.serviceProviderSystemChineseName = "数据湖分行业务领域集市";
        this.serviceProviderSystemCode = "SHFH";
        this.serviceProviderSystemIP = "28.6.12.3";
        this.sourceFileLandingPath = "/tmp/YYYYMMDD";
        this.sourceFileName = sourceFileName;
        this.initializationID = "";
        this.consumerSystemChineseName = "数据湖分行业务领域集市";
        this.consumerSystemCode = "BDPS";
        this.consumerSystemIP = "28.4.185.120";
        this.fileReceivePath = "/nasbham_sh/input/day/YYYYMMDD";
        this.landingFileName = landingFileName;
        this.issuingSystemFlag = "Y";
        this.splitType = "N";
        this.initializationDate = "";
        this.dataUnloadFrequency = "9";
        this.organizationName = "";
        this.organizationCode = "";
    }

    // Copy constructor
    public FexFileData(FexFileData fexFileDataSetting) {
        this.operationFlag = fexFileDataSetting.getOperationFlag();
        this.serviceProviderSystemChineseName = fexFileDataSetting.getServiceProviderSystemChineseName();
        this.serviceProviderSystemCode = fexFileDataSetting.getServiceProviderSystemCode();
        this.serviceProviderSystemIP = fexFileDataSetting.getServiceProviderSystemIP();
        this.sourceFileLandingPath = fexFileDataSetting.getSourceFileLandingPath();
        this.sourceFileName = fexFileDataSetting.getSourceFileName();
        this.initializationID = fexFileDataSetting.getInitializationID();
        this.consumerSystemChineseName = fexFileDataSetting.getConsumerSystemChineseName();
        this.consumerSystemCode = fexFileDataSetting.getConsumerSystemCode();
        this.consumerSystemIP = fexFileDataSetting.getConsumerSystemIP();
        this.fileReceivePath = fexFileDataSetting.getFileReceivePath();
        this.landingFileName = fexFileDataSetting.getLandingFileName();
        this.issuingSystemFlag = fexFileDataSetting.getIssuingSystemFlag();
        this.splitType = fexFileDataSetting.getSplitType();
        this.initializationDate = fexFileDataSetting.getInitializationDate();
        this.dataUnloadFrequency = fexFileDataSetting.getDataUnloadFrequency();
        this.organizationName = fexFileDataSetting.getOrganizationName();
        this.organizationCode = fexFileDataSetting.getOrganizationCode();
    }

    // Getters and setters for all fields
    public String getOperationFlag() { return operationFlag; }
    public void setOperationFlag(String operationFlag) { this.operationFlag = operationFlag; }
    public String getServiceProviderSystemChineseName() { return serviceProviderSystemChineseName; }
    public void setServiceProviderSystemChineseName(String serviceProviderSystemChineseName) { this.serviceProviderSystemChineseName = serviceProviderSystemChineseName; }
    public String getServiceProviderSystemCode() { return serviceProviderSystemCode; }
    public void setServiceProviderSystemCode(String serviceProviderSystemCode) { this.serviceProviderSystemCode = serviceProviderSystemCode; }
    public String getServiceProviderSystemIP() { return serviceProviderSystemIP; }
    public void setServiceProviderSystemIP(String serviceProviderSystemIP) { this.serviceProviderSystemIP = serviceProviderSystemIP; }
    public String getSourceFileLandingPath() { return sourceFileLandingPath; }
    public void setSourceFileLandingPath(String sourceFileLandingPath) { this.sourceFileLandingPath = sourceFileLandingPath; }
    public String getSourceFileName() { return sourceFileName; }
    public void setSourceFileName(String sourceFileName) { this.sourceFileName = sourceFileName; }
    public String getInitializationID() { return initializationID; }
    public void setInitializationID(String initializationID) { this.initializationID = initializationID; }
    public String getConsumerSystemChineseName() { return consumerSystemChineseName; }
    public void setConsumerSystemChineseName(String consumerSystemChineseName) { this.consumerSystemChineseName = consumerSystemChineseName; }
    public String getConsumerSystemCode() { return consumerSystemCode; }
    public void setConsumerSystemCode(String consumerSystemCode) { this.consumerSystemCode = consumerSystemCode; }
    public String getConsumerSystemIP() { return consumerSystemIP; }
    public void setConsumerSystemIP(String consumerSystemIP) { this.consumerSystemIP = consumerSystemIP; }
    public String getFileReceivePath() { return fileReceivePath; }
    public void setFileReceivePath(String fileReceivePath) { this.fileReceivePath = fileReceivePath; }
    public String getLandingFileName() { return landingFileName; }
    public void setLandingFileName(String landingFileName) { this.landingFileName = landingFileName; }
    public String getIssuingSystemFlag() { return issuingSystemFlag; }
    public void setIssuingSystemFlag(String issuingSystemFlag) { this.issuingSystemFlag = issuingSystemFlag; }
    public String getSplitType() { return splitType; }
    public void setSplitType(String splitType) { this.splitType = splitType; }
    public String getInitializationDate() { return initializationDate; }
    public void setInitializationDate(String initializationDate) { this.initializationDate = initializationDate; }
    public String getDataUnloadFrequency() { return dataUnloadFrequency; }
    public void setDataUnloadFrequency(String dataUnloadFrequency) { this.dataUnloadFrequency = dataUnloadFrequency; }
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    public String getOrganizationCode() { return organizationCode; }
    public void setOrganizationCode(String organizationCode) { this.organizationCode = organizationCode; }

    // Method to print all variables
    public void printAllVariables() {
        System.out.println("Operation Flag: " + getOperationFlag());
        System.out.println("Service Provider System Chinese Name: " + getServiceProviderSystemChineseName());
        System.out.println("Service Provider System Code: " + getServiceProviderSystemCode());
        System.out.println("Service Provider System IP: " + getServiceProviderSystemIP());
        System.out.println("Source File Landing Path: " + getSourceFileLandingPath());
        System.out.println("Source File Name: " + getSourceFileName());
        System.out.println("Initialization ID: " + getInitializationID());
        System.out.println("Consumer System Chinese Name: " + getConsumerSystemChineseName());
        System.out.println("Consumer System Code: " + getConsumerSystemCode());
        System.out.println("Consumer System IP: " + getConsumerSystemIP());
        System.out.println("File Receive Path: " + getFileReceivePath());
        System.out.println("Landing File Name: " + getLandingFileName());
        System.out.println("Issuing System Flag: " + getIssuingSystemFlag());
        System.out.println("Split Type: " + getSplitType());
        System.out.println("Initialization Date: " + getInitializationDate());
        System.out.println("Data Unload Frequency: " + getDataUnloadFrequency());
        System.out.println("Organization Name: " + getOrganizationName());
        System.out.println("Organization Code: " + getOrganizationCode());
    }
}
