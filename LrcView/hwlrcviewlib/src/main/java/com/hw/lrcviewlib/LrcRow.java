package com.hw.lrcviewlib;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class LrcRow implements Comparable<LrcRow> {
    //一行的数据，但是如果数据比较长的话需要多行显示，所以需要ShowRows，存储显示行数据集合
    //它是RowData的拆分
    private List<LrcShowRow> ShowRows = new ArrayList<LrcShowRow>();//显示的行

    private String RowData;//行数据，基础数据
    public String TimeText;//时间数据字符串形式，就是滑动是显示的数据，基础数据
    public long CurrentRowTime;//数据数据long形式，基础数据

    public int StartPositionY = 0;//行绘制开始坐标，用于计算
    public int EndPositionY = 0;//行绘制的结束坐标，用于绘制
    public int ContentHeight = 0;//当前显示行全部内容高度，用于计算

    public LrcRowShowMode ShowMode = LrcRowShowMode.Normal;


    /**
     * @param rowData        歌词数据
     * @param timeText       歌词显示时间
     * @param CurrentRowTime 歌词时间long形式
     */
    public LrcRow(String rowData, String timeText, long CurrentRowTime) {
        this.RowData = rowData;
        this.TimeText = timeText;
        this.CurrentRowTime = CurrentRowTime;
    }

    /**
     * @return 是否有显示数据
     */
    public Boolean hasData() {
        return !TextUtils.isEmpty(RowData);
    }

    /**
     * @return 获取数据
     */
    public String getRowData() {
        return RowData;
    }

    /**
     * @param rowData
     */
    public void setRowData(String rowData) {
        RowData = rowData;
    }

    /**
     * @return 获取显示行数据
     */
    public List<LrcShowRow> getShowRows() {
        return ShowRows;
    }

    /**
     * @param showRows
     */
    public void setShowRows(List<LrcShowRow> showRows) {
        ShowRows = showRows;
    }

    /**
     * @return
     */
    public long getCurrentRowTime() {
        return CurrentRowTime;
    }

    /**
     * @param currentRowTime
     */
    public void setCurrentRowTime(long currentRowTime) {
        CurrentRowTime = currentRowTime;
    }//行时间

    @Override
    public int compareTo(LrcRow another) {
        return (int) (this.CurrentRowTime - another.CurrentRowTime);
    }

    @Override
    public String toString() {
        return "LrcRow{" +
                "RowData='" + RowData + '\'' +
                ", ShowRows=" + ShowRows +
                ", TimeText='" + TimeText + '\'' +
                ", CurrentRowTime=" + CurrentRowTime +
                ", ShowMode=" + ShowMode +
                '}';
    }

}

	
