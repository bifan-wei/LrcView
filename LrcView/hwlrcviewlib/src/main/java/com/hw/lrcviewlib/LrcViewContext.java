package com.hw.lrcviewlib;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class LrcViewContext {

    public Context context;
    public LrcViewState CurrentState= LrcViewState.normal;
    public LrcViewSetting setting = new LrcViewSetting();

    Paint NormalRowPaint;// 正常行画笔
    Paint HeightLightRowPaint;// 当前高亮行画笔
    Paint TrySelectRowPaint;// 滑动时将要选择的行画笔
    Paint MessagePaint;// 滑动时将要选择的行画笔
    Paint SelectLinePaint;
    Paint TimeTextPaint;

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
        TimeTextPaint.setAntiAlias(true);
        TimeTextPaint.setTextAlign(Align.LEFT);
        TimeTextPaint.setTextSize(setting.TimeTextSize);
        TimeTextPaint.setColor(setting.TimeTextColor);
    }

    private void initSelectLinePaint() {
        SelectLinePaint = new Paint();
        SelectLinePaint.setAntiAlias(true);
        SelectLinePaint.setTextAlign(Align.LEFT);
        SelectLinePaint.setTextSize(setting.SelectLineTextSize);
        SelectLinePaint.setColor(setting.SelectLineColor);
    }

    private void initMessagePaint() {
        MessagePaint = new Paint();
        MessagePaint.setAntiAlias(true);
        MessagePaint.setTextAlign(Align.CENTER);
        MessagePaint.setTextSize(setting.MessagePaintTextSize);
        MessagePaint.setColor(setting.MessageColor);
    }

    private void initNormalRowPaint() {
        NormalRowPaint = new Paint();
        NormalRowPaint.setAntiAlias(true);
        NormalRowPaint.setTextAlign(Align.CENTER);
        NormalRowPaint.setTextSize(setting.NormalRowTextSize);
        NormalRowPaint.setColor(setting.NormalRowColor);

    }

    private void initHeightLightRowPaint() {
        HeightLightRowPaint = new Paint();
        HeightLightRowPaint.setAntiAlias(true);
        HeightLightRowPaint.setTextAlign(Align.CENTER);
        HeightLightRowPaint.setTextSize(setting.HeightLightRowTextSize);
        HeightLightRowPaint.setColor(setting.HeightRowColor);

    }

    private void initTrySelectRowPaint() {
        TrySelectRowPaint = new Paint();
        TrySelectRowPaint.setAntiAlias(true);
        TrySelectRowPaint.setTextAlign(Align.CENTER);
        TrySelectRowPaint.setTextSize(setting.TrySelectRowTextSize);
        TrySelectRowPaint.setColor(setting.TrySelectRowColor);

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
