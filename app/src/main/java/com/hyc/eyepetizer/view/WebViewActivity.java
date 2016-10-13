package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/13.
 */
public class WebViewActivity extends BaseActivity {
    @BindView(R.id.wv_web)
    WebView wvWeb;
    @BindView(R.id.img_left)
    ImageView imgBack;
    @BindView(R.id.tv_head_title)
    CustomTextView tvTitle;
    private static final String TITLE="title";
    private static final String URL="url";
    private String mTitle;
    private String mUrl;

    public static Intent getIntent(Context context,String title,String url){
        Intent intent=new Intent(context,WebViewActivity.class);
        intent.putExtra(TITLE,title);
        intent.putExtra(URL,url);
        return intent;
    }
    @Override
    protected void handleIntent() {
        Intent intent=getIntent();
        mTitle = intent.getStringExtra(TITLE);
        mUrl = intent.getStringExtra(URL);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    protected void initPresenterAndData() {

    }

    @Override
    protected void initView() {
        wvWeb.loadUrl(mUrl);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText(mTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
