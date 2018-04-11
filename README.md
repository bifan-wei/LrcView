# LrcView
<br>
<br>

这是使用非常简单而且效果还不错的Android歌词控件，只需要传递歌词文件，即可实现类似网易云音乐的歌词效果
<br>
<br>

 ## 支持效果有：
 
 1.柔和的滑动到指定时间歌词。<br>
 2.手势拖动歌词控件，释放手势跳转歌词。<br>
 3. 提供自定义歌词大小字体颜色等，方便自定义界面。<br>
 4.对于超长歌词换行显示。
 
 <br>
 <br>
 
![imag](https://github.com/bifan-wei/LrcView/blob/master/lrcView.gif)

![imag](https://github.com/bifan-wei/LrcView/blob/master/lrcviewPic.png)

<br>
<br>

# 使用方法：

## 依赖

```java 
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}



dependencies {
	        compile 'com.github.bifan-wei:LrcView:V1.1'
	}
```


## 代码使用

xml:

```java
 <com.hw.lrcviewlib.LrcView
  android:id="@+id/au_lrcView"
  android:layout_width="match_parent"
  android:layout_height="match_parent" />
```
<br>
<br>

## 设置数据：
```java
 
        List<LrcRow> lrcRows = new LrcDataBuilder().BuiltFromAssets(this, "test2.lrc");
         //ro  List<LrcRow> lrcRows = new LrcDataBuilder().Build(file);
        //mLrcView.setTextSizeAutomaticMode(true);//是否自动适配文字大小
	
        //init the lrcView
        mLrcView.getLrcSetting()
                .setTimeTextSize(40)//时间字体大小
                .setSelectLineColor(Color.parseColor("#ffffff"))//选中线颜色
                .setSelectLineTextSize(25)//选中线大小
                .setHeightRowColor(Color.parseColor("#aaffffff"))//高亮字体颜色
                .setNormalRowTextSize(DisPlayUtil.sp2px(this, 17))//正常行字体大小
                .setHeightLightRowTextSize(DisPlayUtil.sp2px(this, 17))//高亮行字体大小
                .setTrySelectRowTextSize(DisPlayUtil.sp2px(this, 17))//尝试选中行字体大小
                .setTimeTextColor(Color.parseColor("#ffffff"))//时间字体颜色
                .setTrySelectRowColor(Color.parseColor("#55ffffff"));//尝试选中字体颜色
		
        mLrcView.commitLrcSettings()；
        mLrcView.setLrcData(lrcRows);
```
<br>
<br>
## 歌词拖动监听：
```java

        mLrcView.setLrcViewSeekListener(new ILrcViewSeekListener() {
            @Override
            public void onSeek(LrcRow currentLrcRow, long CurrentSelectedRowTime) {
                //在这里执行播放器控制器控制播放器跳转到指定时间
                mController.seekTo((int) CurrentSelectedRowTime);
            }
        });
```
<br>
<br>
## 歌词跳柔和滑动转到指定时间：
```java
//播放器播放时，时间更新后调用这个时歌词数据更新到当前对应的歌词，播放器一般时间更新以秒为频率更新
mLrcView.smoothScrollToTime(time)//传递的数据是播放器的时间格式转化为long数据
```
