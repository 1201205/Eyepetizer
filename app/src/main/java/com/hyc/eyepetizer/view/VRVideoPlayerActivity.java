package com.hyc.eyepetizer.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asha.md360player4android.CustomProjectionFactory;
import com.asha.md360player4android.MD360PlayerActivity;
import com.asha.md360player4android.MediaPlayerWrapper;
import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.BarrelDistortionConfig;
import com.hyc.eyepetizer.utils.DateUtil;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by hzqiujiadi on 16/4/5.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class VRVideoPlayerActivity extends MD360PlayerActivity implements
        SeekBar.OnSeekBarChangeListener{

    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    RelativeLayout mTitleRL;
    RelativeLayout mLoading;
    TextView mLoadingText;
    RelativeLayout mChange;
    ImageView mVoiceLightImg;
    TextView mVoiceLightText;
    TextView mForwardText;
    LinearLayout mFloor;
    TextView mCurrentTime;
    TextView mTimeText;
    TextView mTitleText;
    ImageView mPlayButton;
    SeekBar mSeekBar;
    private boolean isToolbarShow = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                cancelBusy();
                mMediaPlayerWrapper.seekTo(30);

            }
        });

        mMediaPlayerWrapper.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                String error = String.format("Play Error what=%d extra=%d", what, extra);
                Toast.makeText(VRVideoPlayerActivity.this, error, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mMediaPlayerWrapper.getPlayer()
            .setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                    getVRLibrary().onTextureResize(width, height);
                }
            });

        Uri uri = getUri();
        if (uri != null) {
            mMediaPlayerWrapper.openRemoteFile(uri.toString());
            mMediaPlayerWrapper.prepare();
            mMediaPlayerWrapper.seekTo(30);
        }

    }
    private void initView() {
        mTitleRL = (RelativeLayout) findViewById(com.asha.md360player4android.R.id.rl_title);
        mLoading = (RelativeLayout) findViewById(com.asha.md360player4android.R.id.rl_loading);
        mLoadingText = (TextView) findViewById(com.asha.md360player4android.R.id.tv_tip);
        mChange = (RelativeLayout) findViewById(com.asha.md360player4android.R.id.voice_light);
        mVoiceLightImg = (ImageView) findViewById(com.asha.md360player4android.R.id.img_voice_light);
        mVoiceLightText = (TextView) findViewById(com.asha.md360player4android.R.id.tv_voice_light);
        mForwardText = (TextView) findViewById(com.asha.md360player4android.R.id.tv_forward);
        mFloor = (LinearLayout) findViewById(com.asha.md360player4android.R.id.ll_floor);
        mCurrentTime = (TextView) findViewById(com.asha.md360player4android.R.id.tv_current_time);
        mTimeText = (TextView) findViewById(com.asha.md360player4android.R.id.tv_time);
        mTitleText = (TextView) findViewById(com.asha.md360player4android.R.id.tv_title);
        mPlayButton = (ImageView) findViewById(com.asha.md360player4android.R.id.iv_start_pause);
        mSeekBar = (SeekBar) findViewById(com.asha.md360player4android.R.id.sb_time);
        mSeekBar.setMax(1000);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
            .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
            .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
            .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                @Override
                public void onSurfaceReady(Surface surface) {
                    mMediaPlayerWrapper.getPlayer().setSurface(surface);
                }
            })
            .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                @Override
                public void onNotSupport(int mode) {
                    String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                                 ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                    Toast.makeText(VRVideoPlayerActivity.this, tip, Toast.LENGTH_SHORT).show();
                }
            })
            .pinchEnabled(true)
            .projectionFactory(new CustomProjectionFactory())
            .barrelDistortionConfig(
                new BarrelDistortionConfig().setDefaultEnabled(false).setScale(0.95f))
            .build(com.asha.md360player4android.R.id.gl_view);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayerWrapper.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayerWrapper.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayerWrapper.onResume();
    }
    private void hideAll() {
        if (isToolbarShow) {
            getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
            hideToolbar(mTitleRL);
            hideFloor(mFloor);
            isToolbarShow = false;
        }
    }

    public void hideFloor(final View v) {
        v.animate().y(getScreenHeight(this)).setDuration(time).start();
    }


    public void showFloor(final View v) {
        v.animate().y(getScreenHeight(this) - v.getHeight()).setDuration(time).start();

    }


    public void hideToolbar(final View v) {
        v.animate().y(-v.getHeight()).setDuration(time).start();
    }


    public void showToolbar(final View v) {
        v.animate().y(0).setDuration(time).start();
    }
    public int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenHeight = dm.heightPixels;
        return mScreenHeight;
    }
    private int time = 250;
    private long mDuration;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (!fromUser) {
            return;
        }
        int newPosition = (int) ((mDuration * progress * 1.0) / 1000);
        mMediaPlayerWrapper.seekTo(newPosition);
        mCurrentTime.setText(DateUtil.generateTime(newPosition));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
