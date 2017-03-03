###先看两张效果图
######1、垂直滑动

![onegif.gif](http://upload-images.jianshu.io/upload_images/3523210-8c8caf8839bf4685.gif?imageMogr2/auto-orient/strip)
######2、水平滑动


![twoGIF.gif](http://upload-images.jianshu.io/upload_images/3523210-161de76bbe8cb67e.gif?imageMogr2/auto-orient/strip)
####先看使用方法
####1、AndroidStudio 引入
````
Project.gradle
 repositories {
        、、、、
        maven { url 'https://jitpack.io' }
    }
````
````
app.gradle
dependencies {
    、、、、
   compile 'com.github.ithedan:Group_Horizontal-and-vertical-Scrolling:v1.0'
    、、、、
   
}
````
####2、用法
######先介绍对外提供的几个方法
|方法名|使用方式|备注|
|----|----|----|
|setHorizontalOrVertical()|setHorizontalOrVertical(true)|设置滚动方向true:横向滚动 false: 纵向滚动|
|setStartEndScroll()|setStartEndScroll(true)|设置边缘是否可以滚动true: 可以滚动 false :不可以滚动|
|setScrollEdge()|setScrollEdge(width/4)|设置滚动下一页边界，width为屏幕的宽度|
|setDuration()|setDuration(1000)|设置滚动时间，默认是800毫秒|
|setInvalidate()|setInvalidate()|设置重绘，必须设置|
|etOnPageChangeListener()|etOnPageChangeListener(new onPageChangeListener(){})|监听滑动到第几页|
######Activity.java
````
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        ScrollGroup mScrollGroup = (ScrollGroup) findViewById(R.id.mScrollGroup);
        mScrollGroup.setHorizontalOrVertical(true)//设置滚动方向为横向滚动 
                .setStartEndScroll(true)//设置边缘可以滚动
                .setScrollEdge(width/4)//设置滚动下一页边界
                .setDuration(1000)//设置滚动时间
                .setInvalidate();//设置重绘

       /* mScrollGroup.setOnPageChangeListener(new ScrollGroup.onPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                Toast.makeText(MainActivity.this, "第" + currentPage + "页", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

````
######xml（pager可以是单独的View，或是一个单独的布局，pager必须要设置android:clickable="true"）
````
 <LinearLayout
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:orientation="vertical"
      android:clickable="true"
      >
    、、、、、
    、、、、、
  </LinearLayout>

 <LinearLayout
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:orientation="vertical"
      android:clickable="true"
      >
    、、、、、
    、、、、、
  </LinearLayout>


 <LinearLayout
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:orientation="vertical"
      android:clickable="true"
      >
    、、、、、
    、、、、、
  </LinearLayout>

 <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" 
       android:clickable="true"
/>
 
````
####说完使用方法，再说说本文所涉及到的知识点有scrollTo与ScrollBy，Scroller，ViewGroup的事件分发机制，，这几种学习内容就不贴了，自行百度 ，源码地址https://github.com/ithedan/Group_Horizontal-and-vertical-Scrolling
####如有什么问题，敬请提出，十分感谢！希望越来越好，谢谢！如果喜欢，还请点击start，喜欢支持一下了，谢谢O(∩_∩)O~
