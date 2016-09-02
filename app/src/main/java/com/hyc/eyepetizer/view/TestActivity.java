package com.hyc.eyepetizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.widget.AnimateTextView;

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
        ButterKnife.bind(this);
    }


    @Override protected void onPostResume() {
        super.onPostResume();
    }


    @Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        t1.setAnimText("111");
        t2.setAnimText("12345678900123456789");
        t3.setAnimText("1234567");
        t1.animateChar();
        t2.animateChar();
        t3.animateChar();
    }


    @Override
    protected void onResume() {
        super.onResume();

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
