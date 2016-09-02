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
    private String startTime;
    private String endTime;
    private List<Map<String,Object>> itemTipMapList;
    private List<Map<String,Object>> itemFileMapList;

    public ItemBean() {
        this.itemName = "";
        this.itemContent = "";
        this.startTime = "";
        this.endTime = "";
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
}
