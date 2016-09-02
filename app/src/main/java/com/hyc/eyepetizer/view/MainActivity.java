package com.hyc.eyepetizer.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.hyc.eyepetizer.view.adapter.FragmentAdapter;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.fragment.TestFragment;
import com.hyc.eyepetizer.widget.CustomTextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.test1)
    ViewPager mPager;
    @BindView(R.id.tv_title)
    CustomTextView mTitle;
    @BindView(R.id.rg_tab)
    RadioGroup mTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        ButterKnife.bind(this);

        mTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.TEMP));
        mTitle.setText(R.string.app_name);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragments);
        mPager.setAdapter(adapter);
        for (int i=0;i<mTab.getChildCount();i++) {
            RadioButton button=(RadioButton)mTab.getChildAt(i);
            button.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.NORMAL));
            final int finalI = i;
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        mPager.setCurrentItem(finalI,true);
                    }
                }
            });
        }
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) mTab.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
