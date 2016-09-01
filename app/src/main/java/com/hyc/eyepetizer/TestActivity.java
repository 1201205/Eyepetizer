package com.hyc.eyepetizer;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.hyc.eyepetizer.widget.AnimateTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/9/1.
 */
public class TestActivity extends AppCompatActivity {
    @BindView(R.id.t1)
    AnimateTextView t1;
    @BindView(R.id.t2)
    AnimateTextView t2;
    @BindView(R.id.t3)
    AnimateTextView t3;
    @BindView(R.id.b3)
    Button b3;
    @BindView(R.id.b2)
    Button b2;
    @BindView(R.id.b1)
    Button b1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onResume() {
        super.onResume();
        t1.setAnimText("111");
        t2.setAnimText("12345678900123456789");
        t3.setAnimText("1234567");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.animateChar();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t2.animateChar();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t3.animateChar();
            }
        });
    }
}
