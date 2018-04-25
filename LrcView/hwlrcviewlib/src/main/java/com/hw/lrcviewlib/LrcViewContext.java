package com.hw.lrcviewlib;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class LrcViewContext {

    public Context context;
    public LrcViewState CurrentState = LrcViewState.normal;
    public LrcViewSetting setting = new LrcViewSetting();

    public Paint NormalRowPaint;// 正常行画笔
    public Paint HeightLightRowPaint;// 当前高亮行画笔
    public Paint TrySelectRowPaint;// 滑动时将要选择的行画笔
    public Paint MessagePaint;// 滑动时将要选择的行画笔
    public Paint SelectLinePaint;//尝试选择行画笔
    public Paint TimeTextPaint;//时间画笔

    public LrcViewContext(Context context) {
        this.context = context;
    }

    public void initTextPaint() {
        initHeightLightRowPaint();
        initNormalRowPaint();
        initTrySelectRowPaint();
        initMessagePaint();
        initSelectLinePaint();
        initTimeText();
    }

    private void initTimeText() {
        TimeTextPaint = new Paint();
        setPaint(TimeTextPaint, Align.LEFT, setting.TimeTextSize, setting.TimeTextColor);
    }

    private void initSelectLinePaint() {
        SelectLinePaint = new Paint();
        setPaint(SelectLinePaint, Align.CENTER, setting.SelectLineTextSize, setting.SelectLineColor);
    }

    private void initMessagePaint() {
        MessagePaint = new Paint();
        setPaint(MessagePaint, Align.CENTER, setting.MessagePaintTextSize, setting.MessageColor);
    }

    private void initNormalRowPaint() {
        NormalRowPaint = new Paint();
        setPaint(NormalRowPaint, Align.CENTER, setting.NormalRowTextSize, setting.NormalRowColor);

    }

    private void initHeightLightRowPaint() {
        HeightLightRowPaint = new Paint();
        setPaint(HeightLightRowPaint, Align.CENTER, setting.HeightLightRowTextSize, setting.HeightRowColor);

    }

    private void initTrySelectRowPaint() {
        TrySelectRowPaint = new Paint();
        setPaint(TrySelectRowPaint, Align.CENTER, setting.TrySelectRowTextSize, setting.TrySelectRowColor);

    }


    private void setPaint(Paint paint, Align align, float textSize, int color) {
        paint.setAntiAlias(true);
        paint.setTextAlign(align);
        paint.setTextSize(textSize);
        paint.setColor(color);

    }

    public void onDestroy() {
        NormalRowPaint = null;
        HeightLightRowPaint = null;
        TrySelectRowPaint = null;
        MessagePaint = null;
        SelectLinePaint = null;
        TimeTextPaint = null;
    }
}
