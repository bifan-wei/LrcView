package com.hw.lrcviewlib;

import android.graphics.Color;

/**
 * created by ： bifan-wei
 */

public class LrcViewSetting {

    public int LinePadding = 40;// in px
    public int RowPaddingLeft = 10;//行左padding
    public int RowPaddingRight = 10;//行右padding

    public int TimeTextPaddingRight = 10;// 时间文字距右距离
    public int TimeTextPaddingLeft = 10;// 时间文字距左距离
    public int SelectLinePaddingTextTop = 10;// 时间线距离时间文字
    public int SelectLinePaddingTextLeft = 10;// 时间线距离时间文字

    public int NormalRowTextSize = 47;// in px
    public int HeightLightRowTextSize = 67;// in px
    public int TrySelectRowTextSize = 67;// in px
    public int MessagePaintTextSize = 27;// in px
    public int SelectLineTextSize = 27;// in px
    public int TimeTextSize = 27;// in px


    public int NormalRowColor = Color.WHITE;// 正常行字体颜色
    public int HeightRowColor = Color.YELLOW;// 高亮行字体颜色
    public int TrySelectRowColor = Color.GRAY;// 尝试选择行字体颜色
    public int MessageColor = Color.YELLOW;// 信息字体颜色
    public int SelectLineColor = Color.GRAY;// 选择线颜色
    public int TimeTextColor = Color.GRAY;// 选择线颜色


    public LrcViewSetting setLinePadding(int linePadding) {
        LinePadding = linePadding;
        return this;
    }

    public LrcViewSetting setRowPaddingLeft(int rowPaddingLeft) {
        RowPaddingLeft = rowPaddingLeft;
        return this;
    }

    public LrcViewSetting setRowPaddingRight(int rowPaddingRight) {
        RowPaddingRight = rowPaddingRight;
        return this;
    }

    public LrcViewSetting setTimeTextPaddingRight(int timeTextPaddingRight) {
        TimeTextPaddingRight = timeTextPaddingRight;
        return this;
    }

    public LrcViewSetting setTimeTextPaddingLeft(int timeTextPaddingLeft) {
        TimeTextPaddingLeft = timeTextPaddingLeft;
        return this;
    }

    public LrcViewSetting setSelectLinePaddingTextTop(int selectLinePaddingTextTop) {
        SelectLinePaddingTextTop = selectLinePaddingTextTop;
        return this;
    }

    public LrcViewSetting setSelectLinePaddingTextLeft(int selectLinePaddingTextLeft) {
        SelectLinePaddingTextLeft = selectLinePaddingTextLeft;
        return this;
    }

    public LrcViewSetting setNormalRowTextSize(int normalRowTextSize) {
        NormalRowTextSize = normalRowTextSize;
        return this;
    }

    public LrcViewSetting setHeightLightRowTextSize(int heightLightRowTextSize) {
        HeightLightRowTextSize = heightLightRowTextSize;
        return this;
    }

    public LrcViewSetting setTrySelectRowTextSize(int trySelectRowTextSize) {
        TrySelectRowTextSize = trySelectRowTextSize;
        return this;
    }

    public LrcViewSetting setMessagePaintTextSize(int messagePaintTextSize) {
        MessagePaintTextSize = messagePaintTextSize;
        return this;
    }

    public LrcViewSetting setSelectLineTextSize(int selectLineTextSize) {
        SelectLineTextSize = selectLineTextSize;
        return this;
    }

    public LrcViewSetting setTimeTextSize(int timeTextSize) {
        TimeTextSize = timeTextSize;
        return this;
    }

    public LrcViewSetting setNormalRowColor(int normalRowColor) {
        NormalRowColor = normalRowColor;
        return this;
    }

    public LrcViewSetting setHeightRowColor(int heightRowColor) {
        HeightRowColor = heightRowColor;
        return this;
    }

    public LrcViewSetting setTrySelectRowColor(int trySelectRowColor) {
        TrySelectRowColor = trySelectRowColor;
        return this;
    }

    public LrcViewSetting setMessageColor(int messageColor) {
        MessageColor = messageColor;
        return this;
    }

    public LrcViewSetting setSelectLineColor(int selectLineColor) {
        SelectLineColor = selectLineColor;
        return this;
    }

    public LrcViewSetting setTimeTextColor(int timeTextColor) {
        TimeTextColor = timeTextColor;
        return this;
    }


}
