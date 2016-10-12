package com.hyc.eyepetizer.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.model.VideoListInterface;
import com.hyc.eyepetizer.model.ViewDataListFactory;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.DateUtil;
import java.util.List;
import tv.danmaku.ijk.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoActivity extends BaseActivity implements
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
    private static int SIZE_DEFAULT = 0;
    private static int SIZE_4_3 = 1;
    private static int SIZE_16_9 = 2;


    static {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }


    private final String TAG = VideoActivity.class.getSimpleName();
    //重连次数
    private final int CONNECTION_TIMES = 5;
    @BindView(R.id.rl_title)
    RelativeLayout mTitleRL;
    @BindView(R.id.rl_all)
    RelativeLayout mParent;
    @BindView(R.id.ijk_view)
    IjkVideoView mVideoView;
    @BindView(R.id.rl_loading)
    RelativeLayout mLoading;
    @BindView(R.id.tv_tip)
    TextView mLoadingText;
    @BindView(R.id.voice_light)
    RelativeLayout mChange;
    @BindView(R.id.img_voice_light)
    ImageView mVoiceLightImg;
    @BindView(R.id.tv_voice_light)
    TextView mVoiceLightText;
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
    private GestureDetector gestureDetector;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mVolume = -1;
    private float mBrightness = -1;
    private long mNewPosition = -1;
    private boolean isToolbarShow = true;
    private boolean mBackPressed = false;
    private boolean isScrolling = false;
    private int currentSize = SIZE_16_9;
    private boolean isDragging;
    private int defaultTimeout = 3000;
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    private long mDuration;
    private int count = 0;
    private int mParentIndex;
    private int mIndex;
    private String mUrl;
    private String mTitle;
    private List<ViewData> mViewDatas;
    private ItemListData mCurrentData;
    private boolean formRelate;
    private int mVideoID;
    private VideoListInterface mModel;
    private int mFromType;
    private int time = 250;
    @SuppressWarnings("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_FADE_OUT:
                    if (!isDragging && !isScrolling) {
                        hideAll();
                    }
                    break;
                case MESSAGE_SHOW_PROGRESS:
                    updateProgress();
                    if (!isDragging && !isScrolling) {
                        //如果没有在手动滑动seekBar
                        msg = obtainMessage(MESSAGE_SHOW_PROGRESS);
                        removeMessages(MESSAGE_SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000);
                        updatePausePlay();
                    }
                    break;
                case MESSAGE_HIDE_CENTER_BOX:
                    mChange.setVisibility(View.GONE);
                    mLoading.setVisibility(View.GONE);
                    break;

                case MESSAGE_SEEK_NEW_POSITION:
                    mForwardText.setText("");
                    mVideoView.seekTo((int) mNewPosition);
                    mVideoView.start();
                    mNewPosition = -1;
                    break;
            }
        }
    };


    public static void startList(int type, Context context, int index, int parentIndex) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(INDEX, index);
        intent.putExtra(PARENT_INDEX, parentIndex);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }


    public static void startList(int type, Context context, int index, int parentIndex, boolean formRelate, int videoID) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(INDEX, index);
        intent.putExtra(PARENT_INDEX, parentIndex);
        intent.putExtra(FORM_RELATE, formRelate);
        intent.putExtra(VIDEO_ID,videoID);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }


    public static void startSingle(Context context, String url, String title) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    protected void initView(){

        mTitleText.setText(mCurrentData.getTitle());
        mUrl=mCurrentData.getPlayUrl();
        mSeekBar.setMax(1000);
        mSeekBar.setOnSeekBarChangeListener(this);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        gestureDetector = new GestureDetector(this, new PlayerGestureListener());
        mParent.setClickable(true);
        mParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }

                // 处理手势结束
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }
                return false;
            }
        });
        initVideo(mUrl);
    }


    @Override
    protected void handleIntent() {
        Intent intent=getIntent();
        mFromType = intent.getIntExtra(TYPE, -1);
        mParentIndex = intent.getIntExtra(PARENT_INDEX, -1);
        mIndex = intent.getIntExtra(INDEX, -1);
        formRelate=intent.getBooleanExtra(FORM_RELATE,false);
        mVideoID=intent.getIntExtra(VIDEO_ID,-1);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_video;
    }


    @Override
    protected void initPresenterAndData() {
        mModel = ViewDataListFactory.getModel(mFromType);
        mViewDatas=mModel.getVideoList(mVideoID,mParentIndex,new SparseArray<Integer>());
        mCurrentData=mViewDatas.get(mIndex).getData();
    }


    public void initVideo(final String path) {

        mScreenWidth = AppUtil.getScreenWidth(this);
        mScreenHeight = AppUtil.getScreenHeight(this);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mParent.getLayoutParams();
        lp.height = mScreenWidth * 9 / 16;
        lp.width = mScreenWidth;
        mParent.setLayoutParams(lp);

        hideAll();
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                Log.d(TAG, "onPrepared");
                mVideoView.start();
            }
        });

        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                Log.d(TAG, "setOnInfoListener");
                switch (what) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingText.setText(R.string.video_loading);
                        mLoading.setVisibility(View.VISIBLE);
                        updatePausePlay();
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        mDuration = mVideoView.getDuration();
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

        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                Log.d(TAG, "onCompletion");
                mLoading.setVisibility(View.VISIBLE);
                updatePausePlay();
                mVideoView.stopPlayback();
                mVideoView.release(true);
                mIndex++;
                mTimeText.setText(mViewDatas.get(mIndex).getData().getTitle());
                mUrl=mViewDatas.get(mIndex).getData().getPlayUrl();
                mVideoView.setVideoURI(Uri.parse(mUrl));
            }
        });

        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                if (count > CONNECTION_TIMES) {
                    new AlertDialog.Builder(VideoActivity.this)
                            .setMessage(R.string.video_loading_error)
                            .setPositiveButton(R.string.VideoView_error_button,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            VideoActivity.this.finish();
                                        }
                                    })
                            .setCancelable(false)
                            .show();
                } else {
                    mVideoView.stopPlayback();
                    mVideoView.release(true);
                    mVideoView.setVideoURI(Uri.parse(path));
                }
                count++;
                return false;
            }
        });

    }


    @OnClick(R.id.iv_back)
    protected void back() {
        finish();
    }


    @OnClick(R.id.iv_start_pause)
    protected void playOrPause() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        } else {
            mVideoView.start();
        }
        updatePausePlay();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            //切换到了横屏
//            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mParent.getLayoutParams();
//            lp.height = AppUtil.getScreenHeight(this);
//            lp.width = AppUtil.getScreenWidth(this);
//            mParent.setLayoutParams(lp);
//        } else {
//            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mParent.getLayoutParams();
//            lp.height = AppUtil.getScreenWidth(this) * 9 / 16;
//            lp.width = AppUtil.getScreenWidth(this);
//            mParent.setLayoutParams(lp);
//        }
//        setScreenRate(currentSize);
//    }

//
//    public void setScreenRate(int rate) {
//        mScreenWidth = AppUtil.getScreenWidth(this);
//        mScreenHeight = AppUtil.getScreenHeight(this);
//        int width = 0;
//        int height = 0;
//        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            // 横屏
//            if (rate == SIZE_DEFAULT) {
//                width = mVideoView.getmVideoWidth();
//                height = mVideoView.getmVideoHeight();
//            } else if (rate == SIZE_4_3) {
//                width = mScreenHeight / 3 * 4;
//                height = mScreenHeight;
//            } else if (rate == SIZE_16_9) {
//                width = mScreenHeight / 9 * 16;
//                height = mScreenHeight;
//            }
//        } else {
//            if (rate == SIZE_DEFAULT) {
//                width = mVideoView.getmVideoWidth();
//                height = mVideoView.getmVideoHeight();
//            } else if (rate == SIZE_4_3) {
//                width = mScreenWidth;
//                height = mScreenWidth * 3 / 4;
//            } else if (rate == SIZE_16_9) {
//                width = mScreenWidth;
//                height = mScreenWidth * 9 / 16;
//            }
//        }
//        if (width > 0 && height > 0) {
//            FrameLayout.LayoutParams vlp = (FrameLayout.LayoutParams) mVideoView.getmRenderView()
//                    .getView()
//                    .getLayoutParams();
//            vlp.width = width;
//            vlp.height = height;
//            mVideoView.getmRenderView().getView().setLayoutParams(vlp);
//        }
//    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (!fromUser) {
            return;
        }
        Log.d(TAG, "onProgressChanged");
        int newPosition = (int) ((mDuration * progress * 1.0) / 1000);
        mVideoView.seekTo(newPosition);
        mCurrentTime.setText(DateUtil.generateTime(newPosition));
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        isDragging = false;
        handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_FADE_OUT), defaultTimeout);
        handler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
    }


    private long updateProgress() {
        if (isDragging || isScrolling) {
            return 0;
        }

        long position = mVideoView.getCurrentPosition();
        long duration = mVideoView.getDuration();
        if (mSeekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mSeekBar.setProgress((int) pos);
            }
            int percent = mVideoView.getBufferPercentage();
            //缓存的进度，比播放的速度快
            mSeekBar.setSecondaryProgress(percent * 10);
        }
        mCurrentTime.setText(DateUtil.generateTime(position));
        return position;
    }


    /**
     * 改变播放、暂停的按钮
     */
    private void updatePausePlay() {
        if (mVideoView.isPlaying()) {
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


    private void showAll(int timeout) {
        if (!isToolbarShow) {
            Log.e("hyc-test1", System.currentTimeMillis() + "--start");
//            getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
            showToolbar(mTitleRL);
            showFloor(mFloor);
            isToolbarShow = true;
            handler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
            handler.removeMessages(MESSAGE_FADE_OUT);
            if (timeout != 0) {
                handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_FADE_OUT), timeout);
            }
            Log.e("hyc-test1", System.currentTimeMillis() + "--end");

        }
    }


    public void hideFloor(final View v) {
        v.animate().y(AppUtil.getScreenHeight(this)).setDuration(time).start();
    }


    public void showFloor(final View v) {
        v.animate().y(AppUtil.getScreenHeight(this) - v.getHeight()).setDuration(time).start();

    }


    public void hideToolbar(final View v) {
        v.animate().y(-v.getHeight()).setDuration(time).start();
    }


    public void showToolbar(final View v) {
        v.animate().y(0).setDuration(time).start();
    }


    /**
     * 滑动改变声音大小
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0) {
                mVolume = 0;
            }
        }
        hideAll();

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "off";
        }
        // 显示
        mVoiceLightImg.setImageResource(
                i == 0 ? R.drawable.ic_volume_off_white_36dp : R.drawable.ic_volume_up_white_36dp);
        mVoiceLightText.setText(s);
        mChange.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
    }


    /**
     * 滑动改变亮度
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f) {
                mBrightness = 0.50f;
            } else if (mBrightness < 0.01f) {
                mBrightness = 0.01f;
            }
        }
        //Log.d(this.getClass().getSimpleName(), "mBrightness:" + mBrightness + ",percent:" + percent);
        mChange.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        mVoiceLightText.setText(((int) (lpa.screenBrightness * 100)) + "%");
        mVoiceLightImg.setImageResource(R.drawable.ic_brightness_6_white_36dp);
        getWindow().setAttributes(lpa);
    }


    private void onProgressSlide(float percent) {
        long position = mVideoView.getCurrentPosition();
        long duration = mVideoView.getDuration();
        //long deltaMax = Math.min(100 * 1000, mDuration - position);
        long deltaMax = duration;
        long delta = (long) (deltaMax * percent);

        mNewPosition = delta + position;
        if (mNewPosition > duration) {
            mNewPosition = duration;
        } else if (mNewPosition <= 0) {
            mNewPosition = 0;
            delta = -position;
        }
        int showDelta = (int) delta / 1000;
        if (showDelta != 0) {
            if (mSeekBar != null) {
                if (duration > 0) {
                    long pos = 1000L * mNewPosition / duration;
                    mSeekBar.setProgress((int) pos);
                }
                int percents = mVideoView.getBufferPercentage();
                //缓存的进度，比播放的速度快
                mSeekBar.setSecondaryProgress(percents * 10);
            }
            mVideoView.pause();
            String text = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
            mForwardText.setText(text + "s");
            mCurrentTime.setText(DateUtil.generateTime(mNewPosition));
            mTimeText.setText(DateUtil.generateTime(duration));
        }
    }


    /**
     * 手势结束
     */
    private void endGesture() {
        isScrolling = false;
        mVolume = -1;
        mBrightness = -1f;
        if (mNewPosition >= 0) {
            handler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            handler.sendEmptyMessage(MESSAGE_SEEK_NEW_POSITION);
            handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_FADE_OUT), defaultTimeout);
            handler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
        }
        handler.removeMessages(MESSAGE_HIDE_CENTER_BOX);
        handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CENTER_BOX, 500);
    }


    public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean volumeControl;
        private boolean toSeek;


        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            isScrolling = false;
            mVideoView.toggleAspectRatio();
            return true;
        }


        @Override
        public boolean onDown(MotionEvent e) {
            isScrolling = false;
            firstTouch = true;
            return super.onDown(e);
        }


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            isScrolling = false;
            //点击显示和隐藏进度条、时间等
            if (isToolbarShow) {
                hideAll();
                Log.e("hyc-test1","hideAll");
            } else {
                Log.e("hyc-test1","showAll");
                showAll(0);
            }
            return true;
        }


        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            isScrolling = true;
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            if (firstTouch) {
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl = mOldX > getResources().getDisplayMetrics().widthPixels * 0.5f;
                firstTouch = false;
            }

            if (toSeek) {

                onProgressSlide(-deltaX / mVideoView.getWidth());
            } else {
                float percent = deltaY / mVideoView.getHeight();
                if (volumeControl) {
                    onVolumeSlide(percent);
                } else {
                    onBrightnessSlide(percent);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

}