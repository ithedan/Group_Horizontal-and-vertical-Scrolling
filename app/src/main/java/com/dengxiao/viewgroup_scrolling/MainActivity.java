package com.dengxiao.viewgroup_scrolling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.dengxiao.scroll_viewgroup_library.ScrollGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        ScrollGroup mScrollGroup = (ScrollGroup) findViewById(R.id.mScrollGroup);
        mScrollGroup.setHorizontalOrVertical(true)//设置滚动方向true:横向滚动 false: 纵向滚动
                .setStartEndScroll(true)//设置边缘是否可以滚动true: 可以滚动 false :不可以滚动
                .setScrollEdge(width/2)//设置滚动下一页标记
                .setDuration(1000)//设置滚动时间
                .setInvalidate();//设置重绘

       /* mScrollGroup.setOnPageChangeListener(new ScrollGroup.onPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                Toast.makeText(MainActivity.this, "第" + currentPage + "页", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
