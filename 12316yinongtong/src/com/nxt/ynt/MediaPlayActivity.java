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
 * @author ��ѧ��
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
        //����ȫ����������Դ
        PanoramicMediaPlayView.loadLibrary();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����Ϣ��
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
            //ϵͳ�����ļ�ѡ�񲥷�������
            mPanoramicMediaPlayView.setDataSource(getIntent().getDataString());
            mPanoramicMediaPlayView.prepareAsync();

        }else {
            //��assetsĿ¼�Ĳ����ļ�
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

        //Ҳ�������������ַ�ʽ������Դ������򲥷���������
//        mPanoramicMediaPlayView.enableSensor(false);
//        mPanoramicMediaPlayView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPanoramicMediaPlayView.resumeMediaPlayerAndSensor();

        //Ҳ�������������ַ�ʽ������Դ������򲥷���������
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
                //�ѻ���������õ�seekbar�ĵڶ�������չʾ
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
                        //�����߿��Ը��ݾ���Ĵ�����������������������Ϳɲμ�Android�ٷ�
                        // MediaPlayer.OnErrorListener��onError��������
                        Toast.makeText(getApplicationContext(), "���ų�����", Toast.LENGTH_SHORT).show();
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
                    	 Toast.makeText(getApplicationContext(), "��������......", 100).show();
//                        Toast.makeText(getApplicationContext(), "��ʼ���壬��ͣ����", Toast.LENGTH_SHORT).show();
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //�����߿��Ը��ݾ���Ĵ�����������������info���Ϳɲμ�Android
                        //MediaPlayer.OnInfoListener��onInfo��������
//                        Toast.makeText(getApplicationContext(), "�������壬�Զ�����", Toast.LENGTH_SHORT).show();
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
                //����ǲ��Ź����е�seekTo���������ǾͲ���Ҫ����ʱ��Ƚ��ȣ���Ϊ���������Զ�����
                //onProgressChanged�������������������updatePlayProgress
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
                //�û�������ȫ��������
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
     * ���²��Ž���
     */
    private void updatePlayProgress(long currentPosition, long duration) {
        Log.e(TAG, "��ǰ����" + currentPosition + " �ܳ���" + duration);
        mSeekBarProgress.setProgress((int) currentPosition);
        //��ʽ��ʱ���ʽ
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
