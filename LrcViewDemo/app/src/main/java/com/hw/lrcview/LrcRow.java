package com.hw.lrcview;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class LrcRow implements Comparable<LrcRow>{

	private String RowData;//行数据
	private List<LrcShowRow> ShowRows = new ArrayList<LrcShowRow>();//显示的行
	public String TimeText;
	public long CurrentRowTime;
	public LrcRowShowMode ShowMode = LrcRowShowMode.Normal;


	public LrcRow(String rowdata,String timetext,long CurrentRowTime) {
		this.RowData = rowdata;
		this.TimeText = timetext;
		this.CurrentRowTime = CurrentRowTime;

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

	public Boolean HasData(){
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

	public enum LrcRowShowMode{
		Normal,
		HightLight,
		TrySelected

	}

	@Override
	public int compareTo(LrcRow another) {

		return (int) (this.CurrentRowTime-another.CurrentRowTime);
	}

}
	
	
