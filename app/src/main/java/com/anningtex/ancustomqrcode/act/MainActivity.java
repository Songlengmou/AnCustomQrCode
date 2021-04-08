package com.anningtex.ancustomqrcode.act;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anningtex.ancustomqrcode.R;
import com.anningtex.ancustomqrcode.bean.OrderNoAllDataBean;
import com.anningtex.ancustomqrcode.bean.OrderNoMangerBean;
import com.anningtex.ancustomqrcode.camera.CameraManager;
import com.anningtex.ancustomqrcode.sql.QrMangerDatabase;
import com.anningtex.ancustomqrcode.sql.dao.OrderNoAllDataDao;
import com.anningtex.ancustomqrcode.utils.Constant;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Song
 * desc:一直处于扫描返回中
 */
public class MainActivity extends AppCompatActivity {
    private final long EXPIRED = 3600000 * 24 * 2;
    private OrderNoAllDataDao orderNoAllDataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        CameraManager.init(getApplication());
        findViewById(R.id.input_scan).setOnClickListener(view -> requestPermission());
        findViewById(R.id.input_speech).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SpeechSoundActivity.class)));
        orderNoAllDataDao = QrMangerDatabase.getDefault(getApplicationContext()).getOrderNoAllDataDao();
        orderNoMangerJsonData();
    }

    private void orderNoMangerJsonData() {
        InputStream inputStream;
        OrderNoMangerBean orderNoMangerBean;
        try {
            inputStream = getApplicationContext().getAssets().open("orderNoManger.json");
            orderNoMangerBean = new GsonBuilder().create().fromJson(new InputStreamReader(inputStream), OrderNoMangerBean.class);
            OrderNoMangerBean.DataBean data = orderNoMangerBean.getData();
            List<OrderNoMangerBean.DataBean.ListBean> list = data.getList();
            for (OrderNoMangerBean.DataBean.ListBean listBean : list) {
                OrderNoAllDataBean orderNoAllDataBean = new OrderNoAllDataBean();
                orderNoAllDataBean.setOlid(listBean.getId());
                orderNoAllDataBean.setOrderNo(listBean.getOrder_no());
                orderNoAllDataDao.addOrderNoAllData(orderNoAllDataBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_CAMERA_OR_EXTERNAL_STORAGE);
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            }
        } else {
            startActivity(new Intent(MainActivity.this, CaptureActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, CaptureActivity.class));
                }
            }
            default:
                break;
        }
    }
}