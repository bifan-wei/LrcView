package com.hw.lrcview;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.hw.lrcview.ILrcView.LrcViewSeekListener;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import lrcview.hw.com.lrcviewdemo.R;

public class MainActivity extends Activity {
	LrcView mLrcView;
	int mProgress;
	SeekBar mSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mLrcView = (LrcView) findViewById(R.id.lrcview);
		mSeekBar = (SeekBar) findViewById(R.id.seekbar);

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				long time = mPlayer.getDuration() * mProgress / 100;
				mLrcView.seekLrcToTime(time);
				mPlayer.seekTo((int) time);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mProgress = progress;
			}
		});

		List<LrcRow> lrcRows = new LrcDataBuilder().BuiltFromAssets(this, "test2.lrc");

		mLrcView.setLrcData(lrcRows);

		// 开始播放歌曲并同步展示歌词
		beginLrcPlay();

		// 设置自定义的LrcView上下拖动歌词时监听
		mLrcView.setLrcViewSeekListener(new LrcViewSeekListener() {

			@Override
			public void onSeek(com.hw.lrcview.LrcRow currentlrcrow, long Currenselectrowtime) {
				if (mPlayer != null) {
					mPlayer.seekTo((int) Currenselectrowtime);
				}
			}
		});
	}

	// 更新歌词的频率，每秒更新一次
	private int mPalyTimerDuration = 1000;
	// 更新歌词的定时器
	private Timer mTimer;
	// 更新歌词的定时任务
	private TimerTask mTask;
	private MediaPlayer mPlayer;

	/**
	 * 开始播放歌曲并同步展示歌词
	 */
	public void beginLrcPlay() {

		mPlayer = new MediaPlayer();



		try {
			AssetFileDescriptor afd = getAssets().openFd("test2.mp3");
			mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
			// 准备播放歌曲监听
			mPlayer.setOnPreparedListener(new OnPreparedListener() {
				// 准备完毕
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					if (mTimer == null) {
						mTimer = new Timer();
						mTask = new LrcTask();
						mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
					}
				}
			});
			// 歌曲播放完毕监听
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					stopLrcPlay();
				}
			});
			// 准备播放歌曲
			mPlayer.prepare();
			// 开始播放歌曲
			mPlayer.start();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}

	}

	/**
	 * 停止展示歌曲
	 */
	public void stopLrcPlay() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	/**
	 * 展示歌曲的定时任务
	 */
	class LrcTask extends TimerTask {
		@Override
		public void run() {
			// 获取歌曲播放的位置
			final long timePassed = mPlayer.getCurrentPosition();
			MainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// 滚动歌词
					mLrcView.seekLrcToTime(timePassed);
					int p = 0;
					if (mPlayer != null && mPlayer.getDuration() > 0) {
						p = mPlayer.getCurrentPosition() * 100 / mPlayer.getDuration();
						mSeekBar.setProgress(p);
					}
				}
			});

		}
	};
}
