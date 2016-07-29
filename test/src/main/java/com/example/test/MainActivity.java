package com.example.test;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout topView;
    LinearLayout bottomView;
    boolean isshowing;
    RecyclerView recyclerView;
    ImageView back;
    TextView hxTextView, ldTextView;
    LinearLayoutManager llManager;
    FrameLayout frameLayout;
    PopupWindow p;
    int[] imgIds = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5};
    private int screenWidth, screenHeight;
    float density;
    int values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenSize();
        findView();
        initView();
        setAdapterListener();
    }

    /**
     * 查找控件
     */
    public void findView() {
        topView = (RelativeLayout) findViewById(R.id.top);
        bottomView = (LinearLayout) findViewById(R.id.bottom);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        back = (ImageView) findViewById(R.id.back);
        hxTextView = (TextView) findViewById(R.id.hx);
        ldTextView = (TextView) findViewById(R.id.ld);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
    }

    /**
     * 获得屏幕参数
     */
    public void getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        density = metrics.density;
    }

    /**
     * 初始化控件
     */
    public void initView() {
        llManager = new LinearLayoutManager(this);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llManager);
    }

    /**
     * 设置控件的监听器和适配器
     */
    public void setAdapterListener() {
        recyclerView.setAdapter(new RecycleAdapter(imgIds));
        back.setOnClickListener(this);
        hxTextView.setOnClickListener(this);
        ldTextView.setOnClickListener(this);
    }

    /**
     * 打开隐藏布局
     */
    public void showView() {
        topView.setVisibility(View.VISIBLE);
        bottomView.setVisibility(View.VISIBLE);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_in);
        topView.setAnimation(topAnim);

        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_in);
        bottomView.setAnimation(bottomAnim);
    }

    /**
     * 关闭隐藏布局
     */
    public void hideView() {
        topView.setVisibility(View.GONE);
        bottomView.setVisibility(View.GONE);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_out);
        topView.setAnimation(topAnim);

        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_out);
        bottomView.setAnimation(bottomAnim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.hx:
                if ("横向翻页".equals(hxTextView.getText().toString())) {
                    llManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(llManager);
                    hxTextView.setText("纵向翻页");
                } else if ("纵向翻页".equals(hxTextView.getText().toString())) {
                    llManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llManager);
                    hxTextView.setText("横向翻页");
                }
                break;
            case R.id.ld:
                showPopupWindow(v);
                break;
        }
    }

    /**
     * 调节屏幕亮度的弹窗
     */
    public void showPopupWindow(View v) {
        if (p == null) {
            p = new PopupWindow(this);
        }
        View view = getLayoutInflater().inflate(R.layout.pop_bright, null);
        AppCompatSeekBar seekBar = (AppCompatSeekBar) view.findViewById(R.id.seek);
        ContentResolver cr = MainActivity.this.getContentResolver();
        try {
            values = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
            seekBar.setProgress(values*100);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        addScreenBrightListener(seekBar);
        p.setWidth((int) (screenWidth - 50 * density));
        p.setHeight((int) (50 * density));
        p.setContentView(view);
        p.setOutsideTouchable(true);
        p.setBackgroundDrawable(new BitmapDrawable());
        if (isshowing) {
            isshowing = false;
            p.showAtLocation(frameLayout, Gravity.CENTER, 0, 0);
        } else if (p != null && p.isShowing() && !isshowing) {
            isshowing = true;
            p.dismiss();
            p = null;
        }
    }

    /**
     * 屏幕亮度调节的监听
     *
     * @param seekBar
     */
    public void addScreenBrightListener(AppCompatSeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("SEEKBAR", progress + "");
                WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
                params.screenBrightness = progress / 100;
                MainActivity.this.getWindow().setAttributes(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("SEEKBAR", "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("SEEKBAR", "onStopTrackingTouch");
            }
        });
    }

    class RecycleAdapter extends RecyclerView.Adapter {

        MyHolder myHolder;
        List<String> imgList;
        int[] imgIdList;

        public RecycleAdapter(int[] imgIdList) {
            this.imgIdList = imgIdList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.recycler_item, parent, false);
            myHolder = new MyHolder(itemView);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyHolder) {
                ((MyHolder) holder).image.setImageResource(imgIdList[position]);
            }
        }

        @Override
        public int getItemCount() {
            return imgIdList.length;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            ImageView image;

            public MyHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.recycler_item_image);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Log.i("TAG", "ACTION_UP-ACTIVITY");
                if (!isshowing) {
                    isshowing = true;
                    showView();
                } else {
                    isshowing = false;
                    hideView();
                }
                break;
        }
        return false;
    }

}
