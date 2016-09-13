package com.hyc.eyepetizer.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.hyc.eyepetizer.R;

/**
 * Created by Administrator on 2016/9/1.
 */
public class TestActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }


    @Override protected void onPostResume() {
        super.onPostResume();
    }

}
