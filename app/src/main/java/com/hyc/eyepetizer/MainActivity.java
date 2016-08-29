package com.hyc.eyepetizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.eyepetizer.beans.Selection;
import com.hyc.eyepetizer.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.test)
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        Requests.getApi().getSelection().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Selection>() {
            @Override
            public void call(Selection selection) {
                Log.e("selection",System.currentTimeMillis()+"");
                List<ViewData> datas=new ArrayList<ViewData>();
                int i=selection.getSectionList().size();
                for (int j=0;j<i;j++) {
                    datas.addAll(selection.getSectionList().get(j).getItemList());
                }
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.setAdapter(new TestAdapter(MainActivity.this,datas));
                Log.e("selection",System.currentTimeMillis()+"");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
