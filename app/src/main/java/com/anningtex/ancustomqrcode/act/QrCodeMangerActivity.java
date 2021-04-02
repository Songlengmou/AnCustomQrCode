package com.anningtex.ancustomqrcode.act;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.anningtex.ancustomqrcode.R;
import com.anningtex.ancustomqrcode.bean.QrMangerBean;
import com.anningtex.ancustomqrcode.sql.QrMangerDatabase;
import com.anningtex.ancustomqrcode.sql.dao.QrMangerDao;
import com.syp.library.BaseRecycleAdapter;

import java.util.List;

/**
 * @author Administrator
 * desc:数据库的二维码管理值
 */
public class QrCodeMangerActivity extends AppCompatActivity {
    private QrMangerDao qrMangerDao;
    private RecyclerView mRvQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_manger);
        initView();
    }

    private void initView() {
        mRvQrCode = findViewById(R.id.rv_qr_code);
        qrMangerDao = QrMangerDatabase.getDefault(getApplicationContext()).getQrMangerDao();
        queryQrManger();
    }

    private void queryQrManger() {
        if (qrMangerDao != null) {
            List<QrMangerBean> qrMangerBeanAll = qrMangerDao.getQrMangerBeanAll();
            if (qrMangerBeanAll != null && qrMangerBeanAll.size() > 0) {
                Log.e("666TAG", "queryPhoneSize: " + qrMangerBeanAll.size());
                BaseRecycleAdapter adapter = new BaseRecycleAdapter(R.layout.item_qr_code_manger, qrMangerBeanAll);
                adapter.setOnDataToViewListener((helper, item, position) -> {
                    QrMangerBean qrMangerBean = (QrMangerBean) item;
                    helper.setText(R.id.tv_phone_num, qrMangerBean.getPhoneNum());
                    helper.setText(R.id.tv_date, qrMangerBean.getDate() + "");
                    helper.setText(R.id.tv_qrCode, qrMangerBean.getQrCode());
                    helper.setText(R.id.tv_qrCodePicPath, qrMangerBean.getQrCodePicPath());
                });
                mRvQrCode.setAdapter(adapter);
            }
        }
    }
}