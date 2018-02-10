package com.hw.lrcviewlib;

public class LrcShowRow implements Comparable<LrcShowRow> {
	 String Data;// 行数据
	 float RowHeight;// 行高度
	 float RowPadding;// 行间距
	 float YPosition =0;//绘画y位置
	 int Index;//所在行坐标

	public LrcShowRow(int index, String data, float rowHeight, float rowPadding) {
		this.Data = data;
		this.RowHeight = rowHeight;
		this.RowPadding = rowPadding;
		this.Index = index;
	}

	@Override
	public String toString() {
		return "LrcShowRow [Data=" + Data + ", RowHeight=" + RowHeight + ", RowPadding=" + RowPadding + ", YPosition="
				+ YPosition + ", Index=" + Index + "]";
	}

	@Override
	public int compareTo(LrcShowRow another) {
		return this.Index - another.Index;
	}

}
