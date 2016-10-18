package com.hyc.eyepetizer.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
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
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VRVideoPlayerActivity extends MD360PlayerActivity implements
        SeekBar.OnSeekBarChangeListener {
    private static final int MESSAGE_SHOW_PROGRESS = 1;
    private static final int MESSAGE_FADE_OUT = 2;
    private static final int MESSAGE_SEEK_NEW_POSITION = 3;
    private static final int MESSAGE_HIDE_CENTER_BOX = 4;
    private static final String PARENT_INDEX = "parent_index";
    private static final String INDEX = "index";
    private static final String URL = "url";
    private static final String TITLE = "title";
    private static final String FORM_RELATE = "form_relate";
    private static final String VIDEO_ID = "video_id";
    private static final String TYPE = "type";
    private final int CONNECTION_TIMES = 5;
    @BindView(R.id.rl_title)
    RelativeLayout mTitleRL;
    @BindView(R.id.fl_all)
    FrameLayout mParent;
    @BindView(R.id.rl_loading)
    RelativeLayout mLoading;
    @BindView(R.id.tv_tip)
    TextView mLoadingText;
    @BindView(R.id.tv_forward)
    TextView mForwardText;
    @BindView(R.id.ll_floor)
    LinearLayout mFloor;
    @BindView(R.id.tv_current_time)
    TextView mCurrentTime;
    @BindView(R.id.tv_time)
    TextView mTimeText;
    @BindView(R.id.tv_title)
    TextView mTitleText;
    @BindView(R.id.iv_start_pause)
    ImageView mPlayButton;
    @BindView(R.id.sb_time)
    SeekBar mSeekBar;
    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    private long mNewPosition = -1;
    private boolean isToolbarShow = true;
    private boolean isScrolling = false;
    private int defaultTimeout = 3000;
    private int count = 0;
    private int time = 250;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_FADE_OUT:
                    if (!isScrolling) {
                        hideAll();
                    }
                    break;
                case MESSAGE_SHOW_PROGRESS:
                    updateProgress();
                    if (!isScrolling) {
                        //如果没有在手动滑动seekBar
                        msg = obtainMessage(MESSAGE_SHOW_PROGRESS);
                        removeMessages(MESSAGE_SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000);
                        updatePausePlay();
                    }
                    break;
                case MESSAGE_HIDE_CENTER_BOX:
                    mLoading.setVisibility(View.GONE);
                    break;

                case MESSAGE_SEEK_NEW_POSITION:
                    mForwardText.setText("");
                    mMediaPlayerWrapper.seekTo((int) mNewPosition);
                    mMediaPlayerWrapper.onResume();
                    mNewPosition = -1;
                    break;
            }
        }
    };
    private long mDuration;
    private float mLastX;
    private float mLastY;
    private int mTouchSlop;

    public static void start(Context context, String uri, String title) {
        Intent i = new Intent(context, VRVideoPlayerActivity.class);
        i.setData(Uri.parse(uri));
        i.putExtra(TITLE, title);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mLoadingText.setText(R.string.video_loading);
        mLoading.setVisibility(View.VISIBLE);
        updatePausePlay();
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.getPlayer().setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int i1) {
                switch (what) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingText.setText(R.string.video_loading);
                        mLoading.setVisibility(View.VISIBLE);
                        updatePausePlay();
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        mDuration = mMediaPlayerWrapper.getDuration();
                        mTimeText.setText(DateUtil.generateTime(mDuration));

                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mLoading.setVisibility(View.GONE);
                        showAll(defaultTimeout);
                        updatePausePlay();
                        break;
                }
                return false;
            }
        });
        mMediaPlayerWrapper.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                if (count > CONNECTION_TIMES) {
                    new AlertDialog.Builder(VRVideoPlayerActivity.this)
                            .setMessage(R.string.video_loading_error)
                            .setPositiveButton(R.string.VideoView_error_button,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            VRVideoPlayerActivity.this.finish();
                                        }
                                    })
                            .setCancelable(false)
                            .show();
                } else {
                    mMediaPlayerWrapper.stopPlayback();
                }
                count++;
                return false;
            }
        });

        mMediaPlayerWrapper.getPlayer()
                .setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                        getVRLibrary().onTextureResize(width, height);
                    }
                });
        mMediaPlayerWrapper.getPlayer().setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                finish();
            }
        });

        Uri uri = getUri();
        if (uri != null) {
            mMediaPlayerWrapper.openRemoteFile(uri.toString());
            mMediaPlayerWrapper.prepare();
            mMediaPlayerWrapper.seekTo(30);
        }
        hideAll();
        initView();

    }

    private void initView() {
        mTitleText.setText(getIntent().getStringExtra(TITLE));
        mTouchSlop = ViewConfiguration.getTouchSlop();
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

    @OnClick(R.id.iv_back)
    protected void back() {
        finish();
    }

    @OnClick(R.id.iv_start_pause)
    protected void playOrPause() {
        if (mMediaPlayerWrapper.isPlaying()) {
            mMediaPlayerWrapper.onPause();
        } else {
            mMediaPlayerWrapper.onResume();
        }
        updatePausePlay();
    }

    private void updatePausePlay() {
        if (mMediaPlayerWrapper.isPlaying()) {
            mPlayButton.setImageResource(R.drawable.ic_stop);
        } else {
            mPlayButton.setImageResource(R.drawable.ic_play);
        }
    }

    private void hideAll() {
        if (isToolbarShow) {
            getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
            hideToolbar(mTitleRL);
            hideFloor(mFloor);
            isToolbarShow = false;
        }
    }

    private long updateProgress() {
        long position = mMediaPlayerWrapper.getCurrentPosition();
        long duration = mMediaPlayerWrapper.getDuration();
        if (mSeekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mSeekBar.setProgress((int) pos);
            }
            int percent = mMediaPlayerWrapper.getBufferPercentage();
            //缓存的进度，比播放的速度快
            mSeekBar.setSecondaryProgress(percent * 10);
        }
        mCurrentTime.setText(DateUtil.generateTime(position));
        return position;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getRawX();
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (Math.abs(ev.getRawX() - mLastX) < mTouchSlop && Math.abs(ev.getRawY() - mLastY) < mTouchSlop) {
                    onSingleTapUp();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showAll(int timeout) {
        if (!isToolbarShow) {
            showToolbar(mTitleRL);
            showFloor(mFloor);
            isToolbarShow = true;
            handler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
            handler.removeMessages(MESSAGE_FADE_OUT);
            if (timeout != 0) {
                handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_FADE_OUT), timeout);
            }

        }
    }

    public void onSingleTapUp() {
        isScrolling = false;
        if (isToolbarShow) {
            hideAll();
        } else {
            showAll(defaultTimeout);
        }
    }
}
