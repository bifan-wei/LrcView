package com.hw.lrcviewlib;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LrcDataBuilder {
    private String tag = "LrcDataBuilder";

    public List<LrcRow> BuiltFromAssets(Context context, String fileName) {
        return Build(LoadContentFromAssets(context, fileName));
    }

    public List<LrcRow> Buile(File lrcFile) {
        return Build(LoadContentFromFile(lrcFile));

    }

    public List<LrcRow> Build(String rawLrc) {

        if (TextUtils.isEmpty(rawLrc)) {
            Log.e(tag, " lrcflie do not exist");
            return null;
        }

        StringReader reader = new StringReader(rawLrc);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        List<LrcRow> rows = new ArrayList<LrcRow>();
        try {
            // 循环地读取歌词的每一行
            do {
                line = br.readLine();

                if (line != null && line.trim().length() > 0) {
                    // 解析每一行歌词 得到每行歌词的集合，因为有些歌词重复有多个时间，就可以解析出多个歌词行来
                    List<LrcRow> lrcRows = createRows(line);
                    if (lrcRows != null && lrcRows.size() > 0) {
                        for (LrcRow row : lrcRows) {
                            rows.add(row);
                        }
                    }
                }
            } while (line != null);

            if (rows.size() > 0) {
                // 根据歌词行的时间排序
                Collections.sort(rows);
                for (LrcRow lrcRow : rows) {
                    Log.d(tag, "lrcRow:" + lrcRow.toString());
                }

            }else{
                Log.d(tag, "rows.size() ==0:" );
            }
        } catch (Exception e) {
            Log.e(tag, "parse exceptioned:" + e.getMessage());
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        }
        return rows;
    }

    private List<LrcRow> createRows(String standardLrcLine) {

        try {
            // [01:15.33] 或者 [00:00]这种格式
            Boolean Form1 = standardLrcLine.indexOf("[") == 0 && standardLrcLine.indexOf("]") == 9;
            Boolean Form2 = standardLrcLine.indexOf("[") == 0 && standardLrcLine.indexOf("]") == 6;

            if (!Form1 && !Form2) {
                return null;
            }

            int lastIndexOfRightBracket = standardLrcLine.lastIndexOf("]");

            String content = standardLrcLine.substring(lastIndexOfRightBracket + 1, standardLrcLine.length());


            String times = standardLrcLine.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]",
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

    private String LoadContentFromFile(File file) {
        try {
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            @SuppressWarnings("resource")
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String LoadContentFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
