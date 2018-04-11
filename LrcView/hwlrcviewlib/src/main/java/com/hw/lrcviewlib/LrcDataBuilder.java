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

    /**
     * @param context
     * @param fileName 文件名称
     * @param parser 解析器
     * @return
     */
    public List<LrcRow> BuiltFromAssets(Context context, String fileName, IRowsParser parser) {
        return Build(LoadContentFromAssets(context, fileName), parser);
    }

    /**
     * @param lrcFile 文件
     * @param parser 解析器
     * @return
     */
    public List<LrcRow> Build(File lrcFile, IRowsParser parser) {
        return Build(LoadContentFromFile(lrcFile), parser);

    }


    /**
     * @param context
     * @param fileName
     * @return
     */
    public List<LrcRow> BuiltFromAssets(Context context, String fileName) {
        return BuiltFromAssets(context, fileName, new DefaultLrcRowsParser());
    }

    /**
     * @param lrcFile
     * @return
     */
    public List<LrcRow> Build(File lrcFile) {
        return Build(lrcFile, new DefaultLrcRowsParser());

    }

    /**
     * @param rawLrcStrData 歌词数据字符串
     * @return 如果没有数据，返回null
     */
    public List<LrcRow> Build(String rawLrcStrData, IRowsParser parser) {
        if (TextUtils.isEmpty(rawLrcStrData)) {
            Log.e(tag, " lrcFile do not exist");
            return null;
        }

        StringReader reader = new StringReader(rawLrcStrData);
        BufferedReader br = new BufferedReader(reader);
        String line;
        List<LrcRow> rows = new ArrayList<LrcRow>();
        try {
            // 循环地读取歌词的每一行
            do {
                line = br.readLine();

                if (line != null && line.trim().length() > 0) {
                    // 解析每一行歌词 得到每行歌词的集合，因为有些歌词重复有多个时间，就可以解析出多个歌词行来
                    List<LrcRow> lrcRows = parser.parse(line);
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
            } else {
                Log.d(tag, "rows.size() ==0:");
            }
        } catch (Exception e) {
            Log.e(tag, "歌词解析异常:" + e.getMessage());
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


    /**
     * @param file
     * @return
     */
    private String LoadContentFromFile(File file) {
        try {
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
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

    /**
     * @param context
     * @param fileName
     * @return
     */
    private String LoadContentFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
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
