package com.zaozao.comics.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.util.AsyncListUtil;
import android.util.Log;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.tools.ImageDownloader;
import com.zaozao.comics.Constant;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.detail.DownLoadImage;
import com.zaozao.comics.detail.LoadPageData;
import com.zaozao.comics.dialog.WaitDialog;
import com.zaozao.comics.http.HttpURL;
import com.zaozao.comics.utils.AppConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownLoadService extends IntentService implements LoadPageData.DataCallback {


    ArrayList<BookChapter> list;
    LoadPageData loadPageData;
    List<ArrayList<String>> imgList;
    String comicName;
    AppConfig config;
    String downloadDir = "tycomics";

    public DownLoadService() {
        super("DownLoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loadPageData = new LoadPageData(this, HttpURL.COMICS_CHAPTER_CONTENT, this);
        list = intent.getParcelableArrayListExtra(Constant.CHAPTER_LIST);
        comicName = intent.getStringExtra(Constant.COMICS_NAME);
        init();
    }

    public void init() {
        config = AppConfig.getInstance();
        imgList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            loadPageData.addRequestParams(HttpURL.APP_KEY, comicName, list.get(i).getId());
            loadPageData.addTaskInService(list.get(i).getId());
        }
    }

    @Override
    public void getData(ArrayList<String> imageList) {
        Log.i("REQUEST", imageList.size() + "");
        if (imageList != null) {
            this.imgList.add(imageList);
            Log.i("REQUEST", imgList.size() + "," + list.size());
            if (imgList.size() == list.size()) {
                ImageDownloader loader = ImageDownloader.getInstance();
                File root = config.createRootFolder(downloadDir);
                for (int i = 0; i < imgList.size(); i++) {
                    loader.setCachePath(config.createFolder(root.getPath(),comicName).getPath());
                    for (int j = i; j < imgList.get(i).size(); j++) {
                        loader.downloadImage(imgList.get(i).get(j), new ImageDownLoadListener(), true, null);
                    }
                }
            }
        }
    }

    class ImageDownLoadListener implements ImageDownloader.OnImageDownListener {

        @Override
        public void onDownFinish(String s, String s1, boolean b, Object o) {
            Intent intent = new Intent();
            intent.setAction("com.zaozao.comics.detail.downloadmanageactivity");
            sendBroadcast(intent);
        }
    }
}
