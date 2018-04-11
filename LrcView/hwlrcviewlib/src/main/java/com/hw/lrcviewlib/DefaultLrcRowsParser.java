package com.hw.lrcviewlib;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ： bifan-wei
 */

public class DefaultLrcRowsParser implements IRowsParser {
    private String tag = "DefaultLrcRowsParser";
    @Override
    public List<LrcRow> parse(String lrcRowDada) {
        try {
            // [01:15.33] 或者 [00:00]这种格式
            Boolean Form1 = lrcRowDada.indexOf("[") == 0 && lrcRowDada.indexOf("]") == 9;
            Boolean Form2 = lrcRowDada.indexOf("[") == 0 && lrcRowDada.indexOf("]") == 6;

            if (!Form1 && !Form2) {
                return null;
            }

            int lastIndexOfRightBracket = lrcRowDada.lastIndexOf("]");

            String content = lrcRowDada.substring(lastIndexOfRightBracket + 1, lrcRowDada.length());

            String times = lrcRowDada.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]",
                    "-");

            String arrTimes[] = times.split("-");
            List<LrcRow> listTimes = new ArrayList<LrcRow>();
            for (String temp : arrTimes) {
                if (temp.trim().length() == 0) {
                    continue;
                }

                LrcRow lrcRow = new LrcRow(content, temp, timeConvert(temp));
                listTimes.add(lrcRow);
            }
            return listTimes;
        } catch (Exception e) {
            Log.e(tag, "createRows exception:" + e.getMessage());
            return null;
        }
    }

    /**
     * 将解析得到的表示时间的字符转化为Long型
     */
    private long timeConvert(String timeString) {
        // 因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        // 将字符串 XX:XX.XX 转换为 XX:XX:XX
        timeString = timeString.replace('.', ':');
        // 将字符串 XX:XX:XX 拆分
        String[] times = timeString.split(":");
        // mm:ss:SS
        if (times.length == 3)
            return Integer.valueOf(times[0]) * 60 * 1000 + // 分
                    Integer.valueOf(times[1]) * 1000 + // 秒
                    Integer.valueOf(times[2]);// 毫秒
        // mm:ss
        return Integer.valueOf(times[0]) * 60 * 1000 + // 分
                Integer.valueOf(times[1]) * 1000;// 秒
    }
}
