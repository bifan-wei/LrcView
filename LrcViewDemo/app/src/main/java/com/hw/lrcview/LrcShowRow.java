package com.hw.lrcview;

public class LrcShowRow implements Comparable<LrcShowRow> {

	public String Data;// 行数据
	public float RowHeight;// 行高度
	public float RowPadding;// 行间距
	public float YPosition =0;//绘画y位置
	public int Index;//所在行坐标

	public LrcShowRow(int index, String data, float rowheight, float rowpadding) {
		this.Data = data;
		this.RowHeight = rowheight;
		this.RowPadding = rowpadding;
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
