package com.zaozao.download;

import android.app.Activity;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by 胡章孝 on 2016/7/29.
 */
public class CallServer {



    private static CallServer callServer;
    /**
     * 请求队列
     */
    private static RequestQueue requestQueue;
    /**
     * 下载队列
     */
    private static DownloadQueue downloadQueue;
    /**
     * 私有构造函数
     */
    private  CallServer(){
        requestQueue = NoHttp.newRequestQueue();
    }
    public synchronized static CallServer getRequestInstance(){
        if(callServer == null){
            callServer = new CallServer();
        }
        return  callServer;
    }
    public static DownloadQueue getDownLoadInstance(){
        if(downloadQueue == null){
            downloadQueue = NoHttp.newDownloadQueue();
        }
        return downloadQueue;
    }

    /**
     * 添加一个请求到请求队列
     * @param what 请求类别
     * @param request 请求对象
     * @param activity 请求回调的Activity
     */
    public void add(int what, Request request,Activity activity){
        requestQueue.add(what,request,new HttpResponseListener(activity));
    }
}
