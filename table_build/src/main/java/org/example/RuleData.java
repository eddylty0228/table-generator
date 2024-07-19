package org.example;

public class RuleData {
    private String owner;
    private String srcPathTemplate;
    private String dstPathTemplate;
    private String tmpPathTemplate;
    private String triggerCallbackName;
    private String ignore;
    private String conditionTemplate;
    private String alertTime;
    private int alertCount;
    private int alertInerval;
    private String lzo;
    private String transferCheckFile;
    private int maxRetryCount;
    private String schedule;
    public RuleData(String owner, String srcPathTemplate, String dstPathTemplate, String tmpPathTemplate, String triggerCallbackName, String ignore, String conditionTemplate, String alertTime,int alertCount, int alertInerval, String lzo, String transferCheckFile, int maxRetryCount, String schedule) {
        this.owner = owner;
        this.srcPathTemplate = srcPathTemplate;
        this.dstPathTemplate = dstPathTemplate;
        this.tmpPathTemplate = tmpPathTemplate;
        this.triggerCallbackName = triggerCallbackName;
        this.ignore = ignore;
        this.conditionTemplate = conditionTemplate;
        this.alertTime = alertTime;
        this.alertCount = alertCount;
        this.alertInerval = alertInerval;
        this.lzo = lzo;
        this.transferCheckFile = transferCheckFile;
        this.maxRetryCount = maxRetryCount;
        this.schedule = schedule;
    }
    public RuleData(){
        this.owner = "BHAM_BDL_SHFH_PROJECT";
        this.srcPathTemplate = "/nasbham_sh/input/day/${YYYY}${MM}${DD}/%s_${YYYY}${MM}${DD}.dtf";
        this.dstPathTemplate = "hdfs://hdfs-ha/bdbham/bdbham_SHFH/input/day/data/%s/${YYYY}${MM}/${DD}/A/%s_${YYYY}${MM}${DD}.dtf";
        this.tmpPathTemplate = "hdfs://hdfs-ha/user/pipline/tmp/bdbham_SHFH/input/day/data/%s/${YYYY}${MM}/${DD}/A/%s_${YYYY}${MM}${DD}.dtf";
        this.triggerCallbackName = "";
        this.ignore = "FALSE";
        this.conditionTemplate = "{\"startDate\":\"${YYYY}-${MM}-${DD}\"}";
        this.alertTime = "";
        this.alertCount = 0;
        this.alertInerval = 0;
        this.lzo = "FALSE";
        this.transferCheckFile = "TRUE";
        this.maxRetryCount = 1;
        this.schedule = "D";
    }

    // Getters and setters for each field
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSrcPathTemplate() {
        return srcPathTemplate;
    }

    public void setSrcPathTemplate(String srcPathTemplate) {
        this.srcPathTemplate = srcPathTemplate;
    }

    public String getDstPathTemplate() {
        return dstPathTemplate;
    }

    public void setDstPathTemplate(String dstPathTemplate) {
        this.dstPathTemplate = dstPathTemplate;
    }

    public String getTmpPathTemplate() {
        return tmpPathTemplate;
    }

    public void setTmpPathTemplate(String tmpPathTemplate) {
        this.tmpPathTemplate = tmpPathTemplate;
    }

    public String getTriggerCallbackName() {
        return triggerCallbackName;
    }

    public void setTriggerCallbackName(String triggerCallbackName) {
        this.triggerCallbackName = triggerCallbackName;
    }

    public String getIgnore() {
        return ignore;
    }

    public void setIgnore(String ignore) {
        this.ignore = ignore;
    }

    public String getConditionTemplate() {
        return conditionTemplate;
    }

    public void setConditionTemplate(String conditionTemplate) {
        this.conditionTemplate = conditionTemplate;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public int getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }

    public int getAlertInerval() {
        return alertInerval;
    }

    public void setAlertInerval(int alertInerval) {
        this.alertInerval = alertInerval;
    }

    public String getLzo() {
        return lzo;
    }

    public void setLzo(String lzo) {
        this.lzo = lzo;
    }

    public String getTransferCheckFile() {
        return transferCheckFile;
    }

    public void setTransferCheckFile(String transferCheckFile) {
        this.transferCheckFile = transferCheckFile;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
