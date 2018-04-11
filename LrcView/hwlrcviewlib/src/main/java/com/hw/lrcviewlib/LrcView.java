package com.hw.lrcviewlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.hw.lrcviewlib.LrcViewState.Seeking;
import static com.hw.lrcviewlib.LrcViewState.normal;

public class LrcView extends View implements ILrcView {
    private String tag = "LrcView";
    private String NoDataMessage = "加载歌词中";
    private LrcViewContext lrcContext;//上下文
    private List<LrcRow> mRows;//行数据
    private Boolean TextSizeAutomaticMode = false;//是否文字自动适配界面

    public LrcView(Context context) {
        super(context);
        init();
    }

    public LrcView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    private void init() {
        lrcContext = new LrcViewContext(getContext());
        lrcContext.initTextPaint();
    }


    //手势
    private long ActionDownTimeMoment = 0;
    private float ActionFirstY = 0;

    private float HightLightRowPositionY = 0;//高亮行y位置
    private float TrySelectRowPositionY = 0;//尝试选择行y位置
    private int HeightLightRowPosition = 0;//高亮行位置
    private int TrySelectRowPosition = 0;//尝试选择行位置
    private float FirstRowPositionY = 0;//第一行y位置
    private float DragRowPositionY = 0;//拖动行位置

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRows == null || mRows.size() == 0) {
            DoClick();
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录点击下的位置与时刻
                ActionFirstY = event.getY();
                ActionDownTimeMoment = System.currentTimeMillis();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动
                doSeek(event);
                break;
            case MotionEvent.ACTION_UP:

                if (lrcContext.CurrentState == Seeking) {

                    seekToPosition();
                }
                lrcContext.CurrentState = normal;
                Boolean NeedPerformClick = System.currentTimeMillis() - ActionDownTimeMoment < 200;
                //判断到是否需要进行点击事件，如果滑动时间过短，那就是点击事件
                if (NeedPerformClick) {
                    DoClick();
                }
                invalidate();
                break;
        }
        return true;
    }

    /**
     * seek 到指定位置
     */
    private void seekToPosition() {
        HightLightRowPositionY = TrySelectRowPositionY;
        HeightLightRowPosition = TrySelectRowPosition;

        //获取高亮数据
        List<LrcShowRow> heightLightRs = mRows.get(HeightLightRowPosition).getShowRows();

        postInvalidate();
        if (mSeekListener != null) {
            mSeekListener.onSeek(mRows.get(HeightLightRowPosition), mRows.get(HeightLightRowPosition).CurrentRowTime);
        }
    }

    /**
     * @param event 执行拖动
     */
    private void doSeek(MotionEvent event) {
        lrcContext.CurrentState = Seeking;

        float currentY = event.getY();
        // y移动偏移量
        float offsetY = currentY - ActionFirstY;
        //计算出第一行显示的位置
        FirstRowPositionY = FirstRowPositionY + offsetY;
        //计算行数偏移量
        int rowOffset = Math.abs((int) (offsetY / lrcContext.setting.NormalRowTextSize));
        //
        if (offsetY < 0) {
            TrySelectRowPosition += rowOffset;
        } else if (offsetY > 0) {
            TrySelectRowPosition -= rowOffset;

        }

        TrySelectRowPosition = Math.max(0, TrySelectRowPosition);
        TrySelectRowPosition = Math.min(TrySelectRowPosition, mRows.size() - 1);
        makeFirstRowPositionSecure();
        invalidate();
        ActionFirstY = currentY;
    }

    /**
     * 获取文字高度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextFontHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * 如果第一行显示位置为0，那么第一行显示在中间
     */
    private void makeFirstRowPositionSecure() {
        float defaultFirstRowY = getHeight() / 2;
        FirstRowPositionY = Math.min(FirstRowPositionY, defaultFirstRowY);
        if (FirstRowPositionY == 0) {
            FirstRowPositionY = defaultFirstRowY;
        }

    }

    //
    private Boolean InitLrcRowDada = false;
    private Path TrianglePath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int ViewWidth = getViewWidth();
        int ViewHeight = getViewHeight();

        // 如果没有数据，画出信息文字
        if (listIsEmpty(mRows)) {
            if (!TextUtils.isEmpty(NoDataMessage)) {
                canvas.drawText(NoDataMessage + "", ViewWidth / 2, ViewHeight / 2 - lrcContext.setting.MessagePaintTextSize / 2,
                        lrcContext.MessagePaint);
            }
            return;
        }
        //没有初始化，进行初始化
        if (!InitLrcRowDada) {
            InitLrcRowDada = true;
            //初始化行数据
            initLrcRowData(mRows);
        }

        float rowX = ViewWidth / 2;
        float timeLineY = ViewHeight / 2;

        //如果第一行显示位置为0，那么第一行显示在中间,需要判断一下
        makeFirstRowPositionSecure();

        //开始绘制行位置，从第一行开始
        DragRowPositionY = FirstRowPositionY;

        // 滑动的话画出时间线
        if (lrcContext.CurrentState == Seeking) {

            LrcRow TrySelectRow = mRows.get(TrySelectRowPosition);
            String TrySelectTimeText = TrySelectRow.TimeText + "";

            // 画出时间文字
            int height = getTextFontHeight(lrcContext.TimeTextPaint, TrySelectTimeText);
            canvas.drawText(TrySelectTimeText,
                    getLrcSetting().TimeTextPaddingLeft,
                    timeLineY + height / 2,
                    lrcContext.TimeTextPaint);
            float lStartX =
                    +getLrcSetting().TimeTextPaddingRight
                            + getLrcSetting().TimeTextPaddingLeft
                            + MeasureText(TrySelectTimeText, lrcContext.TimeTextPaint);

            float lStartY = timeLineY;
            float lStopX = ViewWidth;
            float lStopY = lStartY;


            //画出时间线
            canvas.drawLine(lStartX, lStartY, lStopX, lStopY, lrcContext.SelectLinePaint);
            TriangleWidth = getViewWidth() / 45;

            //画出三角形
            TrianglePath.moveTo(lStopX, lStopY - TriangleWidth);
            TrianglePath.lineTo(lStopX - TriangleWidth, lStopY);
            TrianglePath.lineTo(lStopX + TriangleWidth, lStopY + TriangleWidth);
            canvas.drawPath(TrianglePath, lrcContext.SelectLinePaint);

        }

        int RowPositionTop;//该行的的top位置
        int RowPositionBottom = (int) DragRowPositionY;//该行的的bottom位置 用于判断是否是选中


        // 画出歌词
        for (int i = 0; i < mRows.size(); i++) {
            LrcRow lrcRow = mRows.get(i);
            if (i > 0) {
                RowPositionBottom = RowPositionBottom + getLrcSetting().LinePadding + lrcRow.ContentHeight;
            }
            RowPositionTop = RowPositionBottom - lrcRow.ContentHeight;


            if (lrcContext.CurrentState == LrcViewState.normal) {
                if (i == HeightLightRowPosition) {
                    drawHeightLightRow(i, lrcRow, canvas, rowX);
                } else {
                    drawNormalRow(i, lrcRow, canvas, rowX);
                }

            } else {
                int offset = getLrcSetting().LinePadding / 2;
                if (i == HeightLightRowPosition) {
                    drawHeightLightRow(i, lrcRow, canvas, rowX);
                } else {
                    if (RowPositionBottom + offset >= timeLineY
                            && RowPositionTop - offset <= timeLineY) {
                        drawTrySelectRow(i, lrcRow, canvas, rowX);
                    } else {
                        drawNormalRow(i, lrcRow, canvas, rowX);
                    }
                }
            }

        }


    }

    private void drawNormalRow(int rawIndex, LrcRow lrcRow, Canvas canvas, float rowX) {
        List<LrcShowRow> showRows = lrcRow.getShowRows();
        for (LrcShowRow sr : showRows) {
            sr.YPosition = DragRowPositionY;
            canvas.drawText(sr.Data + "", rowX, sr.YPosition, lrcContext.NormalRowPaint);
            DragRowPositionY += sr.RowHeight + sr.RowPadding;
        }
    }

    private void drawTrySelectRow(int rawIndex, LrcRow lrcRow, Canvas canvas, float rowX) {
        List<LrcShowRow> showRows = lrcRow.getShowRows();
        for (LrcShowRow sr : showRows) {
            sr.YPosition = DragRowPositionY;
            canvas.drawText(sr.Data + "", rowX, sr.YPosition, lrcContext.TrySelectRowPaint);
            DragRowPositionY += sr.RowHeight + sr.RowPadding;
        }

        TrySelectRowPosition = rawIndex;
        TrySelectRowPositionY = DragRowPositionY;
    }

    private void drawHeightLightRow(int rawIndex, LrcRow lrcRow, Canvas canvas, float rowX) {
        List<LrcShowRow> showRows = lrcRow.getShowRows();
        for (LrcShowRow sr : showRows) {
            sr.YPosition = DragRowPositionY;
            canvas.drawText(sr.Data + "", rowX, sr.YPosition, lrcContext.HeightLightRowPaint);
            DragRowPositionY += sr.RowHeight + sr.RowPadding;
        }

        if (showRows.size() > 0) {
            HightLightRowPositionY = showRows.get(showRows.size() - 1).YPosition;
        }
    }

    private float MeasureText(String text, Paint paint) {
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

    private OnClickListener mClickListener;

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

    int TriangleWidth = 0;

    /**
     * @param lrcRows
     */
    private void initLrcRowData(List<LrcRow> lrcRows) {
        initLrcView();
        if (!listIsEmpty(lrcRows)) {
            //显示行宽度
            float lineWidth = getWidth() * 6 / 7;
            int index = 0;
            for (LrcRow r : lrcRows) {
                String content = r.getRowData();
                if (!TextUtils.isEmpty(content.trim())) {
                    List<String> lines = makeSecureLines(content, lrcContext.NormalRowPaint, lineWidth);
                    for (String line : lines) {
                        int showRowHeight = getTextFontHeight(lrcContext.NormalRowPaint, line);
                        r.ContentHeight = r.ContentHeight + showRowHeight + getLrcSetting().LinePadding;
                        LrcShowRow showRow = new LrcShowRow(index++, line, showRowHeight, getLrcSetting().LinePadding);
                        r.getShowRows().add(showRow);
                    }

                } else {
                    //分隔空格
                    int showRowHeight = getTextFontHeight(lrcContext.NormalRowPaint, "A") * 2;
                    r.ContentHeight = r.ContentHeight + showRowHeight + getLrcSetting().LinePadding;
                    LrcShowRow showRow = new LrcShowRow(index++, " ", showRowHeight, getLrcSetting().LinePadding);
                    r.getShowRows().add(showRow);
                }
                if (r.ContentHeight > 0) {
                    r.ContentHeight = r.ContentHeight - getLrcSetting().LinePadding;
                }

            }
        }
    }


    private void initLrcView() {
        if (TextSizeAutomaticMode) {
            int textHeight = getViewHeight() / 20 - getLrcSetting().LinePadding;
            TriangleWidth = getViewWidth() / 50;
            getLrcSetting().NormalRowTextSize = textHeight;
            getLrcSetting().HeightLightRowTextSize = textHeight;
            getLrcSetting().TrySelectRowTextSize = textHeight;
            getLrcSetting().TimeTextSize = textHeight * 2 / 3;
            getLrcSetting().LinePadding = textHeight;
        }
        lrcContext.initTextPaint();
    }


    private List<String> makeSecureLines(String text, Paint mPaint, float secureLineWidth) {
        List<String> lines = new ArrayList<String>();
        if (!TextUtils.isEmpty(text)) {
            float maxWidth = secureLineWidth;
            int measuredNum = mPaint.breakText(text, true, maxWidth, null);
            lines.add(text.substring(0, measuredNum));
            String leftStr = text.substring(measuredNum);

            while (leftStr.length() > 0) {
                measuredNum = mPaint.breakText(leftStr, true, maxWidth, null);
                if (measuredNum > 0) {
                    lines.add(leftStr.substring(0, measuredNum));
                }
                leftStr = leftStr.substring(measuredNum);
            }
        }
        return lines;
    }

    private ILrcViewSeekListener mSeekListener;

    @Override
    public void setLrcViewSeekListener(ILrcViewSeekListener seekListener) {
        this.mSeekListener = seekListener;

    }

    @Override
    public void setLrcViewMessage(String messageText) {
        this.NoDataMessage = messageText;

    }

    private Boolean OnAnimation = false;
    private int automaticMoveAnimationDuration = 400;
    private ValueAnimator valueAnimator;

    /**
     * 执行移动动画
     *
     * @param trySelectRow
     * @param trySelectRowIndex
     */
    private void StartMoveAnimation(LrcRow trySelectRow, final int trySelectRowIndex) {

        //如果当前是高亮的话，不操作
        if (trySelectRowIndex == HeightLightRowPosition) {
            return;
        }
        //为空不操作
        if (trySelectRow == null) {
            return;
        }

        //获取高亮数据
        List<LrcShowRow> heightLightRs = mRows.get(HeightLightRowPosition).getShowRows();
        //获取尝试选中数据
        List<LrcShowRow> tryRs = trySelectRow.getShowRows();

        if (listIsEmpty(heightLightRs) || listIsEmpty(tryRs)) {
            return;
        }

        float heightLightRowShowY = getTimeLineYPosition() + getLrcSetting().HeightLightRowTextSize / 2;

        float trySelectRowY = tryRs.get(0).YPosition;

        final float distance = heightLightRowShowY - trySelectRowY;

        final float FirstRowPositionPreY = FirstRowPositionY;

        //cancel pre animation
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        //start animation
        valueAnimator = ValueAnimator.ofFloat(0, distance);
        valueAnimator.setDuration(automaticMoveAnimationDuration);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                FirstRowPositionY = FirstRowPositionPreY + value;
                invalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                HeightLightRowPosition = trySelectRowIndex;
                valueAnimator = null;
                invalidate();
                OnAnimation = false;
            }

        });
        OnAnimation = true;
        valueAnimator.start();
    }

    /**
     * @param time 拖动到指定时间
     */
    public void seekLrcToTime(long time) {

        //no data do nothing
        if (listIsEmpty(mRows)) {
            return;
        }
        // false state do nothing
        if (lrcContext.CurrentState != normal) {
            return;
        }

        //current is on animation ,cancel it
        if (OnAnimation && valueAnimator != null) {
            valueAnimator.cancel();
            OnAnimation = false;
        }
        //find the data and excuse the animation
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


    /**
     * @return 显示宽度
     */
    public int getViewWidth() {
        return getWidth();
    }

    /**
     * @return 显示高度
     */
    public int getViewHeight() {
        return getHeight();
    }

    public boolean hasData() {
        return !listIsEmpty(mRows);
    }

    /**
     * @param noDataMessage 设置显示信息文字
     */
    public void setNoDataMessage(String noDataMessage) {
        NoDataMessage = noDataMessage;
        postInvalidate();
    }

    /**
     * @param list
     * @return
     */
    private Boolean listIsEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * @return
     */
    private int getTimeLineYPosition() {
        return getViewHeight() / 2;
    }

    /**
     * @param time
     */
    @Override
    public void smoothScrollToTime(long time) {
        seekLrcToTime(time);
    }

    /**
     * @return 获取设置
     */
    public LrcViewSetting getLrcSetting() {
        return lrcContext.setting;
    }

    /**
     * 提交设置
     */
    public void commitLrcSettings() {
        lrcContext.initTextPaint();
    }


    /**
     * @param textSizeAutomaticMode 是否使歌词自动文字大小自动适配
     */
    public void setTextSizeAutomaticMode(Boolean textSizeAutomaticMode) {
        TextSizeAutomaticMode = textSizeAutomaticMode;
        initLrcView();
    }


    /**
     * @param automaticMoveAnimationDuration 歌词自动滑动时间速度
     */
    public void setAutomaticMoveAnimationDuration(int automaticMoveAnimationDuration) {
        this.automaticMoveAnimationDuration = automaticMoveAnimationDuration;
    }

    /**
     *
     */
    public void onDestroy() {
        lrcContext.onDestroy();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        OnAnimation = false;
    }

    /**
     * @return 获取歌词自动移动时间
     */
    public int getAutomaticMoveAnimationDuration() {
        return automaticMoveAnimationDuration;
    }

    /**
     * @return 获取相关数据
     */
    public LrcViewContext getLrcContext() {
        return lrcContext;
    }
}
