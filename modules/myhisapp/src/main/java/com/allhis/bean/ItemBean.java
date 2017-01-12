package com.allhis.bean;

import java.util.List;
import java.util.Map;

/**

 * Created by ljy on 16/9/2.
 * ok
 */
public class ItemBean {
    private int itemId;
    private String itemName;
    private String itemContent;
    private int itemType;
    private int startYear;
    private int startYearNDFlag;
    private int endYear;
    private int endYearNDFlag;
    private String startYearDes;
    private String endYearDes;
    private String startTime;
    private String endTime;
    private List<Map<String,Object>> itemTipMapList;
    private List<Map<String,Object>> itemFileMapList;

    public ItemBean() {
        this.itemName = "";
        this.itemContent = "";
        this.startTime = "";
        this.endTime = "";
        this.itemType = 0;
        this.startYear = 0;
        this.endYear = 0;
        this.startYearDes = "";
        this.endYearDes = "";
        this.startYearNDFlag = 0;
        this.endYearNDFlag = 0;

    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Map<String, Object>> getItemTipMapList() {
        return itemTipMapList;
    }

    public void setItemTipMapList(List<Map<String, Object>> itemTipMapList) {
        this.itemTipMapList = itemTipMapList;
    }

    public List<Map<String, Object>> getItemFileMapList() {
        return itemFileMapList;
    }

    public void setItemFileMapList(List<Map<String, Object>> itemFileMapList) {
        this.itemFileMapList = itemFileMapList;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public String getStartYearDes() {
        return startYearDes;
    }

    public void setStartYearDes(String startYearDes) {
        this.startYearDes = startYearDes;
    }

    public String getEndYearDes() {
        return endYearDes;
    }

    public void setEndYearDes(String endYearDes) {
        this.endYearDes = endYearDes;
    }

    public int getStartYearNDFlag() {
        return startYearNDFlag;
    }

    public void setStartYearNDFlag(int startYearNDFlag) {
        this.startYearNDFlag = startYearNDFlag;
    }

    public int getEndYearNDFlag() {
        return endYearNDFlag;
    }

    public void setEndYearNDFlag(int endYearNDFlag) {
        this.endYearNDFlag = endYearNDFlag;
    }

}
