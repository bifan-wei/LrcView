package com.hw.lrcview;

import java.util.ArrayList;
import java.util.List;

import com.hw.lrcview.LrcViewContex.Mode;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LrcView extends View implements ILrcView {

	public String tag = "LrcView";

	private String Message = "加载歌词中";
	private LrcViewContex lvContex;
	private List<LrcRow> mRows;

	public LrcView(Context context) {
		super(context);
		lvContex = new LrcViewContex(context);

	}

	public LrcView(Context context, AttributeSet attr) {
		super(context, attr);
		lvContex = new LrcViewContex(context);

	}

	private long ActionDownTimeMoment = 0;
	private float ActionfirstY = 0;

	private float HightLightRowPositionY = 0;
	private float TrySeletRowPositionY = 0;
	private int HightLightRowPosition = 0;
	private int TrySeletRowPosition = 0;

	private float FirstRowPositionY = 0;
	private float DrawRowPositionY = 0;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mRows == null || mRows.size() == 0) {
			if (DoClick()) {
				return true;
			}
			return super.onTouchEvent(event);
		}

		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				ActionfirstY = event.getY();
				ActionDownTimeMoment = System.currentTimeMillis();
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				doSeek(event);
				break;
			case MotionEvent.ACTION_UP:

				if (lvContex.CurrentMode == Mode.Seeking) {
					seekToPosition();
				}

				lvContex.CurrentMode = Mode.normal;

				Boolean NeedPerformClick = System.currentTimeMillis() - ActionDownTimeMoment < 200;

				if (NeedPerformClick) {
					DoClick();
				}

				invalidate();
				break;
		}
		return true;
	}

	private void seekToPosition() {
		HightLightRowPositionY = TrySeletRowPositionY;
		HightLightRowPosition = TrySeletRowPosition;
		invalidate();
		if (mSeekListsner != null) {
			mSeekListsner.onSeek(mRows.get(HightLightRowPosition), mRows.get(HightLightRowPosition).CurrentRowTime);
		}
	}

	private void doSeek(MotionEvent event) {

		lvContex.CurrentMode = Mode.Seeking;

		float currenty = event.getY();

		float offesty = currenty - ActionfirstY;// 移动偏移量

		FirstRowPositionY = FirstRowPositionY + offesty;

		int rowOffset = Math.abs((int) (offesty / lvContex.NormalRowTextSize));

		if (offesty < 0) {
			TrySeletRowPosition += rowOffset;
		} else if (offesty > 0) {
			TrySeletRowPosition -= rowOffset;

		}

		TrySeletRowPosition = Math.max(0, TrySeletRowPosition);

		TrySeletRowPosition = Math.min(TrySeletRowPosition, mRows.size() - 1);

		MakeFirstRowPositionSecure();

		invalidate();

		ActionfirstY = currenty;
	}

	private void MakeFirstRowPositionSecure() {
		float defaultfirstrowy = getHeight() / 2;
		FirstRowPositionY = Math.min(FirstRowPositionY, defaultfirstrowy);
		if (FirstRowPositionY == 0) {
			FirstRowPositionY = defaultfirstrowy;
		}

	}

	private Boolean InitLrcRowDada = false;

	private Path TrianglePath = new Path();

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int Viewwidth = getViewWidth();
		int ViewHeight = getViewHeight();

		// 如果没有数据，画出信息文件
		if (ListIsEmpty(mRows)) {

			if (!TextUtils.isEmpty(Message)) {
				canvas.drawText(Message + "", Viewwidth / 2, ViewHeight / 2 - lvContex.MessagePaintTextSize / 2,
						lvContex.MessagePaint);
			}

			return;
		}

		if (!InitLrcRowDada) {
			InitLrcRowDada = true;
			InitLrcRowData(mRows);
		}

		float rowx = Viewwidth / 2;
		float timeliney = ViewHeight / 2;

		int linepadding = lvContex.HightlightRowTextSize + lvContex.LinePaddings;

		MakeFirstRowPositionSecure();

		DrawRowPositionY = FirstRowPositionY;

		// 滑动的话画出时间线
		if (lvContex.CurrentMode == Mode.Seeking) {

			LrcRow TrySelectRow = mRows.get(TrySeletRowPosition);

			String TrySelectTimeText = TrySelectRow.TimeText + "";

			// 画出时间线文字
			canvas.drawText(TrySelectTimeText, lvContex.TimeTextPaddingLeft, timeliney + lvContex.TimeTextSize / 2,
					lvContex.TimeTextPaint);

			float lstartX = lvContex.TimeTextPaddingRight+lvContex.TimeTextPaddingLeft
					+ MessureText(TrySelectTimeText, lvContex.TimeTextPaint);

			float lstartY = timeliney;
			float lstopX = Viewwidth;
			float lstopY = lstartY;

			canvas.drawLine(lstartX, lstartY, lstopX, lstopY, lvContex.SelectLinePaint);

			Trianglewidth = getViewWidth() / 45;

			TrianglePath.moveTo(lstopX, lstopY - Trianglewidth);
			TrianglePath.lineTo(lstopX - Trianglewidth, lstopY);
			TrianglePath.lineTo(lstopX + Trianglewidth, lstopY + Trianglewidth);

			canvas.drawPath(TrianglePath, lvContex.SelectLinePaint);

		}

		// 画出歌词
		for (int i = 0; i < mRows.size(); i++) {
			LrcRow lrcRow = mRows.get(i);
			if (lvContex.CurrentMode == Mode.normal) {
				if (i == HightLightRowPosition) {
					DrawHightLightRow(i, lrcRow, canvas, rowx);
				} else {
					DrawNormalRow(i, lrcRow, canvas, rowx);
				}

			} else {

				float timelineytop = timeliney - (linepadding / 2) * lrcRow.getShowRows().size();
				float timelineybottom = timeliney + (linepadding / 2) * lrcRow.getShowRows().size();

				if (i == HightLightRowPosition) {
					DrawHightLightRow(i, lrcRow, canvas, rowx);

				} else if ((DrawRowPositionY + linepadding) > timelineytop
						&& (DrawRowPositionY + linepadding) < timelineybottom) {

					DrawTrySelectRow(i, lrcRow, canvas, rowx);
				} else {
					DrawNormalRow(i, lrcRow, canvas, rowx);
				}
			}

		}

	}

	private void DrawNormalRow(int rawindex, LrcRow lrcRow, Canvas canvas, float rowx) {

		int linepadding = lvContex.NormalRowTextSize + lvContex.LinePaddings;
		List<LrcShowRow> showrows = lrcRow.getShowRows();
		for (LrcShowRow sr : showrows) {
			DrawRowPositionY += linepadding;
			sr.YPosition = DrawRowPositionY;
			canvas.drawText(sr.Data + "", rowx, DrawRowPositionY, lvContex.NormalRowPaint);
		}
	}

	private void DrawTrySelectRow(int rawindex, LrcRow lrcRow, Canvas canvas, float rowx) {
		int linepadding = lvContex.TrySelectRowTextSize + lvContex.LinePaddings;

		List<LrcShowRow> showrows = lrcRow.getShowRows();
		for (LrcShowRow sr : showrows) {
			DrawRowPositionY += linepadding;
			sr.YPosition = DrawRowPositionY;
			canvas.drawText(sr.Data + "", rowx, DrawRowPositionY, lvContex.TrySelectRowPaint);
		}

		TrySeletRowPosition = rawindex;
		TrySeletRowPositionY = DrawRowPositionY;
	}

	private void DrawHightLightRow(int rawindex, LrcRow lrcRow, Canvas canvas, float rowx) {

		int linepadding = lvContex.HightlightRowTextSize + lvContex.LinePaddings;

		List<LrcShowRow> showrows = lrcRow.getShowRows();
		for (LrcShowRow sr : showrows) {
			DrawRowPositionY += linepadding;
			sr.YPosition = DrawRowPositionY;
			canvas.drawText(sr.Data + "", rowx, DrawRowPositionY, lvContex.HightlightRowPaint);
		}

		if (showrows.size() > 0) {
			HightLightRowPositionY = showrows.get(showrows.size() - 1).YPosition;
		}
	}

	private float MessureText(String text, Paint paint) {

		if (TextUtils.isEmpty(text))
			return 0;
		return paint.measureText(text);
	}

	private Boolean DoClick() {
		if (mClickListener != null) {
			mClickListener.onClick(null);
			return true;
		}
		return false;
	}

	OnClickListener mClickListener;

	@Override
	public void setOnClickListener(OnClickListener l) {
		mClickListener = l;
	}

	@Override
	public void setLrcData(List<LrcRow> lrcRows) {
		InitLrcRowDada = false;
		mRows = lrcRows;
		postInvalidate();
	}

	int Trianglewidth = 0;

	private void InitLrcRowData(List<LrcRow> lrcRows) {
        initLrcView();

		if (lrcRows == null || lrcRows.size() == 0)
			return;
		float linewidth = getWidth() * 6 / 7;

		int index = 0;
		for (LrcRow r : lrcRows) {
			String content = r.getRowData();
			if (!TextUtils.isEmpty(content)) {
				List<String> lines = MakeSecureLines(content, lvContex.NormalRowPaint, linewidth);

				for (String line : lines) {
					LrcShowRow showRow = new LrcShowRow(index++, line, lvContex.NormalRowTextSize,
							lvContex.LinePaddings);
					r.getShowRows().add(showRow);
				}
			}

		}
	}

	private void initLrcView(){
		int textheight = getViewHeight() / 20 - lvContex.LinePaddings;
		Trianglewidth = getViewWidth() / 50;
		lvContex.NormalRowTextSize = textheight;
		lvContex.HightlightRowTextSize = textheight;
		lvContex.TrySelectRowTextSize = textheight;
		lvContex.TimeTextSize = textheight * 2 / 3;
		lvContex.LinePaddings = textheight;
		//lvContex.NormalRowColor =
		lvContex.initTextPaint();

	}


	private List<String> MakeSecureLines(String text, Paint mPaint, float securewidth) {

		List<String> lines = new ArrayList<String>();

		if (TextUtils.isEmpty(text)) {
			return lines;
		}

		float maxwidth = securewidth;

		int measurednum = mPaint.breakText(text, true, maxwidth, null);

		lines.add(text.substring(0, measurednum));
		String leftstr = text.substring(measurednum);

		while (leftstr.length() > 0) {
			measurednum = mPaint.breakText(leftstr, true, maxwidth, null);
			if (measurednum > 0) {
				lines.add(leftstr.substring(0, measurednum));
			}
			leftstr = leftstr.substring(measurednum);
		}

		return lines;
	}

	LrcViewSeekListener mSeekListsner;

	@Override
	public void setLrcViewSeekListener(LrcViewSeekListener seekListener) {
		this.mSeekListsner = seekListener;

	}

	@Override
	public void setLrcViewMessage(String messagetext) {
		this.Message = messagetext;

	}

	private Boolean OnAnimation = false;
	private int AutomaMoveAnimationDuration = 400;

	private void StartMoveAnimation(LrcRow tryselectrow, final int tryselectrowindex) {

		if (tryselectrowindex == HightLightRowPosition) {
			return;
		}

		if (tryselectrow == null) {
			return;
		}

		List<LrcShowRow> hightlightrs = mRows.get(HightLightRowPosition).getShowRows();
		List<LrcShowRow> tryrs = tryselectrow.getShowRows();

		if (ListIsEmpty(hightlightrs) || ListIsEmpty(tryrs))
			return;

		float hightlightrowshowY = getTimeLineYPosition() + lvContex.HightlightRowTextSize / 2;

		float tryselectrowY = tryrs.get(0).YPosition;

		final float distance = hightlightrowshowY - tryselectrowY;

		final float FirstRowPositionPreY = FirstRowPositionY;

		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, distance);
		valueAnimator.setDuration(AutomaMoveAnimationDuration);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (Float) animation.getAnimatedValue();
				FirstRowPositionY = FirstRowPositionPreY + value;
				invalidate();
			}
		});

		valueAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				HightLightRowPosition = tryselectrowindex;
				invalidate();
				OnAnimation = false;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		OnAnimation = true;
		valueAnimator.start();
	}

	public int getViewWidth() {
		return getWidth();
	}

	public int getViewHeight() {
		return getHeight();
	}

	private Boolean ListIsEmpty(List<?> list) {
		return list == null || list.size() == 0;
	}

	private int getTimeLineYPosition() {
		return getViewHeight() / 2;
	}

	@Override
	public void setCurrentTime(long time) {
		seekLrcToTime(time);
	}

	public void seekLrcToTime(long time) {

		if (ListIsEmpty(mRows)) {
			return;
		}

		if (lvContex.CurrentMode != Mode.normal) {
			return;
		}

		if (OnAnimation) {
			return;
		}

		for (int i = 0; i < mRows.size(); i++) {
			LrcRow current = mRows.get(i);
			LrcRow next = i + 1 == mRows.size() ? null : mRows.get(i + 1);
			if ((time >= current.CurrentRowTime && next != null && time < next.CurrentRowTime)
					|| (time > current.CurrentRowTime && next == null)) {
				StartMoveAnimation(current, i);
				return;
			}
		}
	}

}
