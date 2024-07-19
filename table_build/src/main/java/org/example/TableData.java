package org.example;

public class TableData {

    private String chineseName;
    private String lifeCycle;
    private String remarks;
    private String isArchived;

    private String deliveryMethod;
    private String tableAttributeSystemCode;
    private String previousVersionEndDate;


    public TableData(String chineseName, String lifeCycle, String remarks,
                     String isArchived, String deliveryMethod,
                     String tableAttributeSystemCode, String previousVersionEndDate) {
        this.chineseName = chineseName;
        this.lifeCycle = lifeCycle;
        this.remarks = remarks;
        this.isArchived = isArchived;

        this.deliveryMethod = deliveryMethod;
        this.tableAttributeSystemCode = tableAttributeSystemCode;
        this.previousVersionEndDate = previousVersionEndDate;

    }
    public TableData(){
        this.chineseName = "";
        this.lifeCycle = "";
        this.remarks = "";
        this.isArchived = "false";
        this.deliveryMethod = "01";
        this.tableAttributeSystemCode = "";
        this.previousVersionEndDate = "";
    }
    public TableData(TableData tableData) {
        this.chineseName = tableData.getChineseName();
        this.lifeCycle = tableData.getLifeCycle();
        this.remarks = tableData.getRemarks();
        this.isArchived = tableData.getIsArchived();
        this.deliveryMethod = tableData.getDeliveryMethod();
        this.tableAttributeSystemCode = tableData.getTableAttributeSystemCode();
        this.previousVersionEndDate = tableData.getPreviousVersionEndDate();
    }


    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(String isArchived) {
        this.isArchived = isArchived;
    }


    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getTableAttributeSystemCode() {
        return tableAttributeSystemCode;
    }

    public void setTableAttributeSystemCode(String tableAttributeSystemCode) {
        this.tableAttributeSystemCode = tableAttributeSystemCode;
    }

    public String getPreviousVersionEndDate() {
        return previousVersionEndDate;
    }

    public void setPreviousVersionEndDate(String previousVersionEndDate) {
        this.previousVersionEndDate = previousVersionEndDate;
    }
    public void printData(){
        System.out.println("chineseName: "+ getChineseName());
        System.out.println("lifeCycle: "+ getLifeCycle());
        System.out.println("remarks: "+ getRemarks());
        System.out.println("isArchived: "+ getIsArchived());
        System.out.println("deliveryMethod: "+ getDeliveryMethod());
        System.out.println("tableAttributeSystemCode: "+ getTableAttributeSystemCode());
        System.out.println("previousVersionEndDate: "+ getPreviousVersionEndDate());
    }

}
