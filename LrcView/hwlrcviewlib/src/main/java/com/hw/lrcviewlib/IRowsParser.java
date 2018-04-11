package com.hw.lrcviewlib;

import java.util.List;

/**
 * created by ： bifan-wei
 * 歌词行解析器，将一句歌词字符串转换为行数据
 * 如果默认的歌词解析器不能完成你的数据解析
 * 你可以在LrcDataBuilder传入你的歌词行解析器
 */

public interface IRowsParser {
    /**
     * @param lrcData        歌词字符串数据
     * @return 解析后的歌词数据
     */
    List<LrcRow> parse(String lrcData);
}
