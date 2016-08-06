package com.zaozao.comics.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.dialog.WaitDialog;
import com.zaozao.comics.services.DownLoadService;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DownLoadManagerActivity extends AppCompatActivity {

    @InjectView(R.id.back_arrow)
    ImageView backArrow;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.other)
    TextView other;
    @InjectView(R.id.download_recycle)
    RecyclerView downloadRecycle;
    private ArrayList<BookChapter> chapterList;
    String comicName;
    static String action = "com.zaozao.comics.detail.downloadmanageactivity";
    LinearLayoutManager llManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_manager);
        ButterKnife.inject(this);
        init();
        getIntentData();
        startDownload();
        setAdapterListener();
    }

    /**
     * 初始化
     */
    public void init() {
        llManager = new LinearLayoutManager(this);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        other.setText("删除");
    }

    /**
     * 获取Intent传来的数据
     * 其中包含了每个章节的所有图片地址
     */
    public void getIntentData() {
        Intent intent = getIntent();
        chapterList = intent.getParcelableArrayListExtra(Constant.CHAPTER_LIST);
        comicName = intent.getStringExtra(Constant.COMICS_NAME);
    }

    /**
     * 开启一个后台服务来下载文件
     */
    public void startDownload() {
        Intent intent = new Intent(this, DownLoadService.class);
        intent.putParcelableArrayListExtra(Constant.CHAPTER_LIST, chapterList);
        intent.putExtra(Constant.COMICS_NAME, comicName);
        startService(intent);
    }


    public void setAdapterListener() {
        downloadRecycle.setLayoutManager(llManager);
        downloadRecycle.setAdapter(new DownLoadRecyAdapter());
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownLoadManagerActivity.this.finish();
            }
        });
    }

    class DownLoadRecyAdapter extends RecyclerView.Adapter {

        MyHolder holder;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.download_recy_item, parent, false);
            holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof  MyHolder){
                ((MyHolder) holder).chapter.setText(comicName);
            }
        }

        @Override
        public int getItemCount() {
            return chapterList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView chapter;
            TextView download_state;
            ProgressBar progressBar;
            ImageButton options;

            public MyHolder(View itemView) {
                super(itemView);
                chapter = (TextView) itemView.findViewById(R.id.download_title);
                download_state = (TextView) itemView.findViewById(R.id.download_state);
                progressBar = (ProgressBar) itemView.findViewById(R.id.download_progress);
                options = (ImageButton) itemView.findViewById(R.id.download_options);
            }
        }
    }

    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (action.equals(intent.getAction())) {

            }
        }
    }
}
