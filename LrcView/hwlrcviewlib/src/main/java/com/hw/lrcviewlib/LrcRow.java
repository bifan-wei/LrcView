package com.hw.lrcviewlib;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class LrcRow implements Comparable<LrcRow>{
	private List<LrcShowRow> ShowRows = new ArrayList<LrcShowRow>();//显示的行
	private String RowData;//行数据
	public String TimeText;//
	public int StartPositionY = 0;
	public int EndPositionY = 0;
	public int ContentHeight = 0;
	public long CurrentRowTime;//
	public LrcRowShowMode ShowMode = LrcRowShowMode.Normal;

	public LrcRow(String rowData,String timeText,long CurrentRowTime) {
		this.RowData = rowData;
		this.TimeText = timeText;
		this.CurrentRowTime = CurrentRowTime;
	}

	public Boolean hasData(){
		return !TextUtils.isEmpty(RowData);
	}

	public String getRowData() {
		return RowData;
	}
	public void setRowData(String rowData) {
		RowData = rowData;
	}
	public List<LrcShowRow> getShowRows() {
		return ShowRows;
	}
	public void setShowRows(List<LrcShowRow> showRows) {
		ShowRows = showRows;
	}
	public long getCurrentRowTime() {
		return CurrentRowTime;
	}
	public void setCurrentRowTime(long currentRowTime) {
		CurrentRowTime = currentRowTime;
	}//行时间

	@Override
	public int compareTo(LrcRow another) {
		return (int) (this.CurrentRowTime-another.CurrentRowTime);
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
	
	
