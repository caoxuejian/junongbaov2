package com.nxt.ynt;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.ynt.utils.UnitConvertUtils;
import com.perfant.panoramicplay.widget.PanoramicMediaPlayView;
/**
 * 
 * @author 曹学建
 *
 */
public class MediaPlayActivity extends Activity {

    private static final String TAG = MediaPlayActivity.class.getSimpleName();
    private ImageButton mBtnPlay;
    private ImageButton mBtnViewport;
    private PanoramicMediaPlayView mPanoramicMediaPlayView;
    private SeekBar mSeekBarProgress;
    private TextView mTvTime;
    private View mLayoutControl;
	private RelativeLayout rl_pro;

    static {
        //加载全景播放器资源
        PanoramicMediaPlayView.loadLibrary();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);

        mBtnPlay = (ImageButton) findViewById(R.id.btn_play);
        mBtnViewport = (ImageButton) findViewById(R.id.btn_viewport);
        mLayoutControl = findViewById(R.id.layout_control);
        mPanoramicMediaPlayView = (PanoramicMediaPlayView) findViewById(R.id.panoramic_media_play_view);
        mSeekBarProgress = (SeekBar) findViewById(R.id.seekbar_progress);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        rl_pro=(RelativeLayout)findViewById(R.id.VR_pro);
        initListener();

        if(Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            //系统播放文件选择播放器过来
            mPanoramicMediaPlayView.setDataSource(getIntent().getDataString());
            mPanoramicMediaPlayView.prepareAsync();

        }else {
            //打开assets目录的测试文件
//            try {
//                AssetFileDescriptor assetFileDescriptor = getAssets().openFd("test.mp4");
//                mPanoramicMediaPlayView.setDataSource(assetFileDescriptor.getFileDescriptor());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            mPanoramicMediaPlayView.setDataSource(getIntent().getStringExtra("uri"));
            mPanoramicMediaPlayView.prepareAsync();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPanoramicMediaPlayView.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPanoramicMediaPlayView.pauseMediaPlayerAndSensor();

        //也可以用下面这种方式单独针对传感器或播放器来处理
//        mPanoramicMediaPlayView.enableSensor(false);
//        mPanoramicMediaPlayView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPanoramicMediaPlayView.resumeMediaPlayerAndSensor();

        //也可以用下面这种方式单独针对传感器或播放器来处理
//        mPanoramicMediaPlayView.enableSensor(true);
//        mPanoramicMediaPlayView.play();
    }

    private void initListener() {
        mBtnViewport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPanoramicMediaPlayView.switchViewPort();
                mBtnViewport.setSelected(!mBtnViewport.isSelected());
            }
        });

        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPanoramicMediaPlayView.isPlaying()) {
                    mPanoramicMediaPlayView.pause();
                }else {
                    mPanoramicMediaPlayView.play();
                }
            }
        });

        mPanoramicMediaPlayView.setOnMediaPlayerStateListener(new PanoramicMediaPlayView.OnMediaPlayerStateListener() {
            @Override
            public void onPrepared(PanoramicMediaPlayView panoramicMediaPlayView) {
            	rl_pro.setVisibility(View.GONE);
                mSeekBarProgress.setMax((int) mPanoramicMediaPlayView.getDuration());
                mPanoramicMediaPlayView.play();
            }

            @Override
            public void onCompletion(PanoramicMediaPlayView panoramicMediaPlayView) {
//                panoramicMediaPlayView.reset();
                finish();
            }

            @Override
            public void onBufferingUpdate(PanoramicMediaPlayView panoramicMediaPlayView, int percent) {
                //把缓冲进度设置到seekbar的第二进度做展示
                mSeekBarProgress.setSecondaryProgress(percent);
            }

            @Override
            public boolean onError(PanoramicMediaPlayView panoramicMediaPlayView, int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_ERROR_IO:
                    case IMediaPlayer.MEDIA_ERROR_MALFORMED:
                    case IMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    case IMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    case IMediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    case IMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    case IMediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        //开发者可以根据具体的错误类型做处理，具体错误类型可参见Android官方
                        // MediaPlayer.OnErrorListener的onError方法介绍
                        Toast.makeText(getApplicationContext(), "播放出错啦", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                return false;
            }

            @Override
            public boolean onInfo(PanoramicMediaPlayView panoramicMediaPlayView, int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    	if(mLayoutControl.getVisibility() == View.VISIBLE) {
                            mLayoutControl.setVisibility(View.GONE);
                        }else {
                            mLayoutControl.setVisibility(View.VISIBLE);
                        }
                    	 Toast.makeText(getApplicationContext(), "即将播放......", 100).show();
//                        Toast.makeText(getApplicationContext(), "开始缓冲，暂停播放", Toast.LENGTH_SHORT).show();
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //开发者可以根据具体的错误类型做处理，具体info类型可参见Android
                        //MediaPlayer.OnInfoListener的onInfo方法介绍
//                        Toast.makeText(getApplicationContext(), "结束缓冲，自动播放", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }

            @Override
            public void onProgressChanged(long currentPosition, long duration) {
                updatePlayProgress(currentPosition, duration);
            }

            @Override
            public void onPause() {
                mBtnPlay.setSelected(false);
            }

            @Override
            public void onPlay() {
                mBtnPlay.setSelected(true);
            }

            @Override
            public void onStop() {
                mBtnPlay.setSelected(false);
            }

            @Override
            public void onSeekComplete(PanoramicMediaPlayView panoramicMediaPlayView) {
                //如果是播放过程中的seekTo操作，我们就不需要更新时间等进度，因为播放器会自动调用
                //onProgressChanged方法，否则就主动调用updatePlayProgress
                if(!mPanoramicMediaPlayView.isPlaying()) {
                    updatePlayProgress(mPanoramicMediaPlayView.getCurrentPosition(),
                            mPanoramicMediaPlayView.getDuration());
                }
            }

            @Override
            public void onVideoSizeChanged(PanoramicMediaPlayView panoramicMediaPlayView, int width, int height) {

            }
        });

        mPanoramicMediaPlayView.setOnPanoramicMediaPlayViewListener(new PanoramicMediaPlayView.OnPanoramicMediaPlayViewListener() {
            @Override
            public void onSurfaceTextureValid(boolean isValid) {
//                mPanoramicMediaPlayView.setViewPort(ViewportMode.DOUBLE_EYE);
            }

            @Override
            public void onSingleTapConfirmed() {
                //用户单击了全景播放器
                if(mLayoutControl.getVisibility() == View.VISIBLE) {
                    mLayoutControl.setVisibility(View.GONE);
                }else {
                    mLayoutControl.setVisibility(View.VISIBLE);
                }
            }
        });

        mSeekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    mPanoramicMediaPlayView.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 更新播放进度
     */
    private void updatePlayProgress(long currentPosition, long duration) {
        Log.e(TAG, "当前进度" + currentPosition + " 总长度" + duration);
        mSeekBarProgress.setProgress((int) currentPosition);
        //格式化时间格式
        String length = UnitConvertUtils.formatTime(duration);
        String time = UnitConvertUtils.formatTime(currentPosition > duration ? duration : currentPosition);

        String str = String.format("%s / %s",time,length);
        mTvTime.setText(str);
    }

    public static void startActivity(Context context1, String uri) {
        Intent intent = new Intent(context1, MediaPlayActivity.class);
        intent.putExtra("uri", uri);
        context1.startActivity(intent);
    }
}
