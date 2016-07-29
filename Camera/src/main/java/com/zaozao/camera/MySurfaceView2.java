package com.zaozao.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 胡章孝 on 2016/7/23.
 */
public class MySurfaceView2 extends SurfaceView implements SurfaceHolder.Callback {

    private Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.i("TAG","SHUTTER");
        }
    };
    private Camera.PictureCallback raw = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.i("TAG","PICTURE");
        }
    };
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.i("TAG","PICTURE_JPEG");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            try {
                String filePath = Environment.getExternalStorageDirectory().getPath()+"/finger/";
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
                String filename = format.format(date) + ".jpg";
                File fileFolder = new File(filePath);
                if(!fileFolder.exists()){
                    fileFolder.mkdir();
                }
                File fileJpg = new File(fileFolder,filename);
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileJpg));
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    SurfaceHolder holder;
    Camera myCamera;
    public MySurfaceView2(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);

    }

    public MySurfaceView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        myCamera = Camera.open();
        try {
            myCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
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
    public void takePicture(){
        myCamera.takePicture(shutter,raw,jpeg);
    }
    public void setPreview(){
        myCamera.startPreview();
    }
}
