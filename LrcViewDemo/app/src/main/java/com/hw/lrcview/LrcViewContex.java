package com.hw.lrcview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class LrcViewContex {

	public Context context;
	public Mode CurrentMode = Mode.normal;

	public int LinePaddings = 20;// in px
	public int RowPaddingLeft = 10;
	public int RowPaddingRight = 10;

	public int TimeTextPaddingRight = 10;// 时间文字距右距离
	public int TimeTextPaddingLeft = 10;// 时间文字距左距离
	public int SelectLinePaddingTextTop = 10;// 时间线距离时间文字
	public int SelectLinePaddingTextLeft = 10;// 时间线距离时间文字

	public int NormalRowTextSize = 27;// in px
	public int HightlightRowTextSize = 27;// in px
	public int TrySelectRowTextSize = 27;// in px
	public int MessagePaintTextSize = 27;// in px
	public int SelectLineTextSize = 27;// in px
	public int TimeTextSize = 27;// in px

	public int NormalRowColor = Color.WHITE;// 正常行字体颜色
	public int HightRowColor = Color.YELLOW;// 高亮行字体颜色
	public int TrySelectRowColor = Color.GRAY;// 尝试选择行字体颜色
	public int MessageColor = Color.YELLOW;// 信息字体颜色
	public int SelectLineColor = Color.GRAY;// 选择线颜色
	public int TimeTextColor = Color.GRAY;// 选择线颜色

	public Paint NormalRowPaint;// 正常行画笔
	public Paint HightlightRowPaint;// 当前高亮行画笔
	public Paint TrySelectRowPaint;// 滑动时将要选择的行画笔
	public Paint MessagePaint;// 滑动时将要选择的行画笔
	public Paint SelectLinePaint;
	public Paint TimeTextPaint;

	public LrcViewContex(Context context) {
		this.context = context;

	}

	public void initTextPaint(){
		initHightlightRowPaint();
		initNormalRowPaint();
		initTrySelectRowPaint();
		initMessagePaint();
		initSelectLinePaint();
		initTimeText();
	}

	private void initTimeText() {
		if (TimeTextPaint == null) {
			TimeTextPaint = new Paint();
			TimeTextPaint.setAntiAlias(true);
			TimeTextPaint.setTextAlign(Align.LEFT);
		}
		TimeTextPaint.setTextSize(TimeTextSize);
		TimeTextPaint.setColor(TimeTextColor);
	}

	public void initSelectLinePaint() {
		if (SelectLinePaint == null) {
			SelectLinePaint = new Paint();
			SelectLinePaint.setAntiAlias(true);
			SelectLinePaint.setTextAlign(Align.LEFT);
		}
		SelectLinePaint.setTextSize(SelectLineTextSize);
		SelectLinePaint.setColor(SelectLineColor);
	}

	public void initMessagePaint() {
		if (MessagePaint == null) {
			MessagePaint = new Paint();
			MessagePaint.setAntiAlias(true);
			MessagePaint.setTextAlign(Align.CENTER);
		}
		MessagePaint.setTextSize(MessagePaintTextSize);
		MessagePaint.setColor(MessageColor);
	}

	public void initNormalRowPaint() {
		if (NormalRowPaint == null) {
			NormalRowPaint = new Paint();
			NormalRowPaint.setAntiAlias(true);
			NormalRowPaint.setTextAlign(Align.CENTER);
		}
		NormalRowPaint.setTextSize(NormalRowTextSize);
		NormalRowPaint.setColor(NormalRowColor);

	}

	public void initHightlightRowPaint() {
		if (HightlightRowPaint == null) {
			HightlightRowPaint = new Paint();
			HightlightRowPaint.setAntiAlias(true);
			HightlightRowPaint.setTextAlign(Align.CENTER);
		}
		HightlightRowPaint.setTextSize(HightlightRowTextSize);
		HightlightRowPaint.setColor(HightRowColor);

	}

	public void initTrySelectRowPaint() {
		if (TrySelectRowPaint == null) {
			TrySelectRowPaint = new Paint();
			TrySelectRowPaint.setAntiAlias(true);
			TrySelectRowPaint.setTextAlign(Align.CENTER);
		}

		TrySelectRowPaint.setTextSize(TrySelectRowTextSize);
		TrySelectRowPaint.setColor(TrySelectRowColor);

	}

	public enum Mode {
		normal, Seeking
	}
}
