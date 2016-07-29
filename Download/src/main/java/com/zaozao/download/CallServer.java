package com.zaozao.download;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadQueue;
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
}
