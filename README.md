# LrcView

android歌词控件。仿网易云音乐自动滑动切换，手势切换歌词时显示歌词时间线



![imag](https://github.com/bifan-wei/LrcView/blob/master/lrcviewPic.png)


#使用方法：
<br>
<br>

## 依赖

```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}



dependencies {
	        compile 'com.github.bifan-wei:LrcView:v1.0'
	}
```


##代码使用

xml:

```java
 <com.hw.lrcviewlib.LrcView
  android:id="@+id/au_lrcView"
  android:layout_width="match_parent"
  android:layout_height="match_parent" />
```
<br>
<br>
设置数据：

```java
 //test lrc view data
        List<LrcRow> lrcRows = new LrcDataBuilder().BuiltFromAssets(this, "test2.lrc");
        //mLrcView.setTextSizeAutomaticMode(true);
        //init the lrcView
        mLrcView.getLrcSetting()
                .setTimeTextSize(40)//时间字体大小
                .setSelectLineColor(Color.parseColor("#ffffff"))//选中线颜色
                .setSelectLineTextSize(25)//选中线大小
                //.setHeightRowColor(Color.parseColor("#aaffffff"))//高亮字体颜色
                .setNormalRowTextSize(DisPlayUtil.sp2px(this, 17))//正常行字体大小
                .setHeightLightRowTextSize(DisPlayUtil.sp2px(this, 17))//高亮行字体大小
                .setTrySelectRowTextSize(DisPlayUtil.sp2px(this, 17))//尝试选中行字体大小
                .setTimeTextColor(Color.parseColor("#ffffff"))//时间字体颜色
                .setTrySelectRowColor(Color.parseColor("#55ffffff"));//尝试选中字体颜色



        mLrcView.setLrcData(lrcRows);
```
