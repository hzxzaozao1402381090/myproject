package com.zaozao.download;

import android.app.Activity;

import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by 胡章孝 on 2016/7/30.
 */
public class HttpResponseListener implements OnResponseListener{

    private MainActivity activity;
    public HttpResponseListener(Activity activity){
        this.activity = (MainActivity) activity;
    }
    @Override
    public void onStart(int i) {

    }

    @Override
    public void onSucceed(int i, Response response) {
            activity.showToast("下载成功！");
    }

    @Override
    public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {

    }

    @Override
    public void onFinish(int i) {

    }
}
