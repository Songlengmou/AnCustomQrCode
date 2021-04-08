package com.anningtex.ancustomqrcode.act;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.anningtex.ancustomqrcode.R;
import com.anningtex.ancustomqrcode.bean.QrMangerBean;
import com.anningtex.ancustomqrcode.dialog.QrMangerEditDialog;
import com.anningtex.ancustomqrcode.sql.QrMangerDatabase;
import com.anningtex.ancustomqrcode.sql.dao.QrMangerDao;
import com.anningtex.ancustomqrcode.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.syp.library.BaseRecycleAdapter;

import java.util.List;

/**
 * @author Administrator
 * desc:数据库的二维码管理值
 */
public class QrCodeMangerActivity extends AppCompatActivity implements View.OnClickListener {
    private QrMangerDao qrMangerDao;
    private RecyclerView mRvQrCode;
    private ImageButton mBtnBack;
    private TextView mTxtTitle, mTvNull;
    private Button mBtnAlbum, mBtnDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_qr_code_manger);
        initView();
    }

    private void initView() {
        mRvQrCode = findViewById(R.id.rv_qr_code);
        mBtnBack = findViewById(R.id.btn_back);
        mTxtTitle = findViewById(R.id.txt_title);
        mBtnAlbum = findViewById(R.id.btn_album);
        mTvNull = findViewById(R.id.tv_null);
        mBtnDeleteAll = findViewById(R.id.btn_delete_all);
        mBtnBack.setOnClickListener(this);
        mBtnDeleteAll.setOnClickListener(this);
        mTxtTitle.setText("Data Details");
        mBtnAlbum.setVisibility(View.GONE);
        qrMangerDao = QrMangerDatabase.getDefault(getApplicationContext()).getQrMangerDao();
        queryQrManger();
    }

    private void queryQrManger() {
        if (qrMangerDao != null) {
            List<QrMangerBean> qrMangerBeanAll = qrMangerDao.getQrMangerBeanAll();
            if (qrMangerBeanAll != null && qrMangerBeanAll.size() > 0) {
                mTvNull.setVisibility(View.GONE);
                mRvQrCode.setVisibility(View.VISIBLE);
                Log.e("666", "queryPhoneSize: " + qrMangerBeanAll.size());
                BaseRecycleAdapter recycleAdapter = new BaseRecycleAdapter(R.layout.item_qr_code_manger, qrMangerBeanAll);
                recycleAdapter.setOnDataToViewListener((helper, item, position) -> {
                    QrMangerBean qrMangerBean = (QrMangerBean) item;
                    helper.setText(R.id.tv_phone_num, "工位号: " + qrMangerBean.getPhoneNum());
                    helper.setText(R.id.tv_date, DateUtils.getDate(qrMangerBean.getDate()));
                    String orderNo = qrMangerBean.getOrderNo();
                    if (TextUtils.isEmpty(orderNo)) {
                        helper.setText(R.id.tv_orderNo, "暂无");
                    } else {
                        helper.setText(R.id.tv_orderNo, orderNo);
                    }
                    helper.setText(R.id.tv_qrCode, "Code: " + qrMangerBean.getQrCode());
                    helper.setText(R.id.tv_qrCodePicPath, qrMangerBean.getQrCodePicPath());
                    helper.setText(R.id.tv_olid, qrMangerBean.getOlid());
                    ImageView ivPic = helper.getView(R.id.iv_pic);
                    Glide.with(helper.itemView.getContext())
                            .load(qrMangerBean.getQrCodePicPath())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(ivPic);
                    ivPic.setOnClickListener(view -> {
                        Intent intent = new Intent(this, CropActivity.class);
                        intent.putExtra("orderNo", orderNo != null ? orderNo : "");
                        intent.putExtra("code", qrMangerBean.getQrCode());
                        intent.putExtra("urlPath", qrMangerBean.getQrCodePicPath());
                        startActivity(intent);
                    });
                });
                mRvQrCode.setAdapter(recycleAdapter);
                recycleAdapter.setOnItemClickListener((adapter, view, position) -> {
                    QrMangerBean qrMangerBean = (QrMangerBean) adapter.getData().get(position);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("QrMangerBean", qrMangerBean);
                    QrMangerEditDialog dialog = new QrMangerEditDialog();
                    dialog.show(this, mBundle, "edit");
                    dialog.setOnRefreshDataListener(() -> queryQrManger());
                });
            } else {
                mTvNull.setVisibility(View.VISIBLE);
                mRvQrCode.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_delete_all:
                new AlertDialog.Builder(QrCodeMangerActivity.this)
                        .setTitle("确认")
                        .setMessage("确认删除所有数据?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", (dialog, which) -> {
                            qrMangerDao.deleteQrMangerBeanAll();
                            Toast.makeText(this, "All Delete Success", Toast.LENGTH_SHORT).show();
                            queryQrManger();
                        })
                        .create().show();
                break;
            default:
                break;
        }
    }
}