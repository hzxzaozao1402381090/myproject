package com.zaozao.comics.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zaozao.comics.R;

public class SettingActivity extends Activity implements View.OnClickListener{

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = (ImageView)findViewById(R.id.set_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.set_back){
            finish();
        }
    }
}
