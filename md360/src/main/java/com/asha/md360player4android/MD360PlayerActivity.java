package com.asha.md360player4android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.MDPosition;
import com.asha.vrlib.plugins.IMDHotspot;
import com.asha.vrlib.plugins.MDAbsPlugin;
import com.asha.vrlib.texture.MD360BitmapTexture;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * using MD360Renderer
 * <p/>
 * Created by hzqiujiadi on 16/1/22.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class MD360PlayerActivity extends Activity {

    private static final String TAG = "MD360PlayerActivity";

    private static final SparseArray<String> sDisplayMode = new SparseArray<>();
    private static final SparseArray<String> sInteractiveMode = new SparseArray<>();
    private static final SparseArray<String> sProjectionMode = new SparseArray<>();
    private static final SparseArray<String> sAntiDistortion = new SparseArray<>();


    static {
        sDisplayMode.put(MDVRLibrary.DISPLAY_MODE_NORMAL, "NORMAL");
        sDisplayMode.put(MDVRLibrary.DISPLAY_MODE_GLASS, "GLASS");

        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_MOTION, "MOTION");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_TOUCH, "TOUCH");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH, "M & T");

        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_SPHERE, "SPHERE");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_DOME180, "DOME 180");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_DOME230, "DOME 230");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_DOME180_UPPER, "DOME 180 UPPER");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_DOME230_UPPER, "DOME 230 UPPER");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_STEREO_SPHERE, "STEREO SPHERE");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_PLANE_FIT, "PLANE FIT");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_PLANE_CROP, "PLANE CROP");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_PLANE_FULL, "PLANE FULL");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_MULTI_FISH_EYE_HORIZONTAL,
                "MULTI FISH EYE HORIZONTAL");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_MULTI_FISH_EYE_VERTICAL,
                "MULTI FISH EYE VERTICAL");
        sProjectionMode.put(CustomProjectionFactory.CUSTOM_PROJECTION_FISH_EYE_RADIUS_VERTICAL,
                "CUSTOM MULTI FISH EYE");

        sAntiDistortion.put(1, "ANTI-ENABLE");
        sAntiDistortion.put(0, "ANTI-DISABLE");
    }


    private MDVRLibrary mVRLibrary;
    private List<MDAbsPlugin> plugins = new LinkedList<>();
    private MDPosition logoPosition = MDPosition.newInstance().setY(-8.0f).setYaw(-90.0f);
    private MDPosition[] positions = new MDPosition[]{
            MDPosition.newInstance().setZ(-8.0f).setYaw(-45.0f),
            MDPosition.newInstance().setZ(-18.0f).setYaw(15.0f).setAngleX(15),
            MDPosition.newInstance().setZ(-10.0f).setYaw(-10.0f).setAngleX(-15),
            MDPosition.newInstance().setZ(-10.0f).setYaw(30.0f).setAngleX(30),
            MDPosition.newInstance().setZ(-10.0f).setYaw(-30.0f).setAngleX(-30),
            MDPosition.newInstance().setZ(-5.0f).setYaw(30.0f).setAngleX(60),
            MDPosition.newInstance().setZ(-3.0f).setYaw(15.0f).setAngleX(-45),
            MDPosition.newInstance().setZ(-3.0f).setYaw(15.0f).setAngleX(-45).setAngleY(45),
            MDPosition.newInstance().setZ(-3.0f).setYaw(0.0f).setAngleX(90),
    };

    public static void startVideo(Context context, Uri uri) {
        start(context, uri, VideoPlayerActivity.class);
    }


    public static void startVideo(Context context, String uri) {
        start(context, Uri.parse(uri), VideoPlayerActivity.class);
    }


    public static void startBitmap(Context context, Uri uri) {
        start(context, uri, BitmapPlayerActivity.class);
    }


    private static void start(Context context, Uri uri, Class<? extends Activity> clz) {
        Intent i = new Intent(context, clz);
        i.setData(uri);
        context.startActivity(i);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set content view
        setContentView(R.layout.activity_md_using_surface_view);
        // init VR Library
        mVRLibrary = createVRLibrary();

        final List<View> hotspotPoints = new LinkedList<>();
        hotspotPoints.add(findViewById(R.id.hotspot_point1));
        hotspotPoints.add(findViewById(R.id.hotspot_point2));
        getVRLibrary().setEyePickChangedListener(new MDVRLibrary.IEyePickListener() {
            @Override
            public void onHotspotHit(IMDHotspot hotspot, long hitTimestamp) {
//                String text = hotspot == null
//                        ? "nop"
//                        : String.format(Locale.CHINESE, "%s  %fs", hotspot.getTitle(),
//                        (System.currentTimeMillis() - hitTimestamp) / 1000.0f);

                if (System.currentTimeMillis() - hitTimestamp > 5000) {
                    getVRLibrary().resetEyePick();
                }
            }
        });
    }


    abstract protected MDVRLibrary createVRLibrary();


    public MDVRLibrary getVRLibrary() {
        return mVRLibrary;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVRLibrary.onDestroy();
    }


    protected Uri getUri() {
        Intent i = getIntent();
        if (i == null || i.getData() == null) {
            return null;
        }
        return i.getData();
    }


    public void cancelBusy() {
//        findViewById(R.id.progress).setVisibility(View.GONE);
    }


    private class AndroidDrawableProvider implements MDVRLibrary.IBitmapProvider {

        private int res;


        public AndroidDrawableProvider(int res) {
            this.res = res;
        }


        @Override
        public void onProvideBitmap(MD360BitmapTexture.Callback callback) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), this.res);
            callback.texture(bitmap);
        }
    }
}