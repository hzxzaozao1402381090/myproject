package com.zaozao.download;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.StorageReadWriteError;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView statusTextView;
    Button resultTextView;
    ContentLoadingProgressBar progressBar;
    DownloadRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        init();
    }

    public void findView() {
        statusTextView = (TextView) findViewById(R.id.status);
        resultTextView = (Button) findViewById(R.id.result);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressbar);
    }

    public void init() {
        NoHttp.initialize(getApplication());
        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile();
            }
        });
    }

    /**
     * 开始下载文件
     */
    public void downloadFile() {
        showToast("开始下载");
        /**
         * 如果当前正在现在，且还没有下载完成，则停止下载
         */
        if (request != null && request.isStarted() && !request.isFinished()) {
            request.cancel();
        } else if (request == null || request.isFinished()) {
            /**
             * 如果没有下载，或者下载已经完成，则重新开始下载
             */
            String url = "http://img5.imgtn.bdimg.com/it/u=222011730,2486072963&fm=23&gp=0.jpg";
            String folder = getFolderPath("image");
            String file = getFilePath(folder, getFileName());
            Log.i("DOWN",folder+","+file);
            request = NoHttp.createDownloadRequest(url, folder, file, true, true);
            CallServer.getDownLoadInstance().add(0, request, downloadListener);
        }
    }

    /**
     * 获得文件夹的路径
     *
     * @param folderName 文件夹的名称
     * @return
     */
    public String getFolderPath(String folderName) {
        Log.i("DOWN",sdCardAvailable()+"");
        File folder = null;
        if (sdCardAvailable()) {
            File sdCard = new File(Environment.getExternalStorageDirectory().getPath());
            folder = new File(sdCard, folderName);
            if (!folder.exists()) {
                folder.mkdir();
            }
        }
        return folder.getAbsolutePath();
    }

    /**
     * 获得文件的路径
     *
     * @param folderName 文件夹的名称
     * @param fileName   文件的名称
     * @return
     */
    public String getFilePath(String folderName, String fileName) {
        File file = new File(folderName, fileName);
        return file.getAbsolutePath();
    }

    /**
     * 判断sd卡是否可用
     *
     * @return
     */
    public boolean sdCardAvailable() {
        boolean isAvailable = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            isAvailable = true;
        }
        return isAvailable;
    }

    /**
     * 使用当期系统时间为下载的文件命名
     *
     * @return
     */
    public String getFileName() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        return format.format(date).toString();
    }

    public DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadError(int i, Exception e) {
           Log.i("DOWN",e.getMessage());

        }

        @Override
        public void onStart(int i, boolean b, long l, Headers headers, long l1) {
            Log.i("DOWN","下载开始");
        }

        @Override
        public void onProgress(int i, int i1, long l) {
            progressBar.setProgress(i);
        }

        @Override
        public void onFinish(int i, String s) {
            showToast("下载完成");
            Log.i("DOWN","下载完成");
        }

        @Override
        public void onCancel(int i) {
            Log.i("DOWN","下载取消");
        }
    };

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
