package com.anningtex.ancustomqrcode.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anningtex.ancustomqrcode.R;
import com.anningtex.ancustomqrcode.bean.QrMangerBean;
import com.anningtex.ancustomqrcode.sql.QrMangerDatabase;

import java.util.Date;

/**
 * @author Song
 * desc:修改和删除数据库内容
 */
public class QrMangerEditDialog extends BaseDialog implements View.OnClickListener {
    private TextView mTvQrCode;
    private EditText mNumEdit, mOrderNoEdit;
    private QrMangerBean mQrMangerBean;
    private OnRefreshDataListener mListener;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_edit_qr_code_manger;
    }

    @Override
    protected void initView(View view) {
        mTvQrCode = view.findViewById(R.id.tv_qr_code);
        mNumEdit = view.findViewById(R.id.update_num_edit);
        mOrderNoEdit = view.findViewById(R.id.update_orderNo_edit);
        view.findViewById(R.id.update_button).setOnClickListener(this);
        view.findViewById(R.id.delete_button).setOnClickListener(this);
    }

    @Override
    protected void loadData(Bundle bundle) {
        if (bundle != null) {
            mQrMangerBean = bundle.getParcelable("QrMangerBean");
            mTvQrCode.setText(mQrMangerBean.getQrCode());
            mNumEdit.setText(mQrMangerBean.getPhoneNum());
            mOrderNoEdit.setText(mQrMangerBean.getOrderNo());
            mNumEdit.setSelection(mNumEdit.length());
        }
    }

    @Override
    public void onClick(View v) {
        String mNum = mNumEdit.getText().toString();
        String mOrder = mOrderNoEdit.getText().toString();
        switch (v.getId()) {
            case R.id.update_button:
                updateQrManger(mNum, mOrder);
                break;
            case R.id.delete_button:
                deleteQrManger();
                break;
            default:
                break;
        }
    }

    private void updateQrManger(String name, String phone) {
        mQrMangerBean.setPhoneNum(name);
        mQrMangerBean.setOrderNo(phone);
        mQrMangerBean.setDate(new Date());
        QrMangerDatabase.getDefault(getActivity().getApplicationContext()).getQrMangerDao().updateQrMangerBean(mQrMangerBean);
        dismiss();
        if (mListener != null) {
            mListener.onRefresh();
        }
    }

    private void deleteQrManger() {
        QrMangerDatabase.getDefault(getActivity().getApplicationContext()).getQrMangerDao().deleteQrMangerBean(mQrMangerBean);
        dismiss();
        if (mListener != null) {
            mListener.onRefresh();
        }
    }

    public void setOnRefreshDataListener(OnRefreshDataListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshDataListener {
        void onRefresh();
    }
}
