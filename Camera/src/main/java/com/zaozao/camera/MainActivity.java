package com.zaozao.camera;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MySurfaceView2 surfaceView2;
    private boolean isClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // surfaceView2 = new MySurfaceView2(this);
      //  setContentView(R.layout.activity_main);
       // setContentView(new MySurfaceView(this));
       // setContentView(surfaceView2);
        setContentView(R.layout.activity_main);
        surfaceView2 = (MySurfaceView2)findViewById(R.id.surface);
        surfaceView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isClick){
                    isClick =true;
                    surfaceView2.takePicture();
                }else{
                    isClick = false;
                    surfaceView2.setPreview();
                }
            }
        });
    }

    /**
     * 不对图片处理
     * @param view
     */
    public void Cancel(View view){
        surfaceView2.setPreview();
    }

    /**
     * 保存图片
     * @param view
     */
    public void Save(View view){
        surfaceView2.takePicture();
    }
    /**
     * 调用系统相机
     * @param view
     */
  /*  public void start(View view){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }*/
}
