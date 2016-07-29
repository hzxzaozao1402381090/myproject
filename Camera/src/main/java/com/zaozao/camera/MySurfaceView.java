package com.zaozao.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    Camera myCamera;
    public MySurfaceView(Context context) {
        super(context);
        holder = this.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(myCamera == null){
            myCamera = Camera.open();
            try {
                myCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        myCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        myCamera.stopPreview();
        myCamera.release();
        myCamera = null;
    }
}
