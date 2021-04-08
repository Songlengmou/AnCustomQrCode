package com.anningtex.ancustomqrcode.act;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.anningtex.ancustomqrcode.R;
import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

/**
 * @author Administrator
 * desc:图片放大
 */
public class CropActivity extends AppCompatActivity {
    private PhotoView mPhotoView;
    private String url, code, orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        initView();
    }

    private void initView() {
        orderNo = getIntent().getStringExtra("orderNo");
        code = getIntent().getStringExtra("code");
        url = getIntent().getStringExtra("urlPath");
        Log.e("666", "CropUrl: " + url);
        setTitle(orderNo + " ( " + code + " )");
        mPhotoView = findViewById(R.id.photoView);
        Glide.with(this).load(url).into(mPhotoView);
        mPhotoView.enable();
        Info info = mPhotoView.getInfo();
        mPhotoView.animaFrom(info);
        mPhotoView.setMaxScale(10f);
    }
}