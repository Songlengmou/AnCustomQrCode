package com.anningtex.ancustomqrcode.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * @Author Song
 * @Desc:识别到的二维码图片保存管理
 */
@Entity
public class QrMangerBean implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;
    /**
     * 手机工位号、识别到的保存的图片路径、日期
     */
    @ColumnInfo(name = "PHONE_NUM")
    private String phoneNum;

    @ColumnInfo(name = "QR_CODE_PIC_PATH")
    private String qrCodePicPath;

    @ColumnInfo(name = "DATE")
    private Date date;

    public QrMangerBean() {

    }

    @Ignore
    public QrMangerBean(String phoneNum, String qrCodePicPath, Date date) {
        this.phoneNum = phoneNum;
        this.qrCodePicPath = qrCodePicPath;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getQrCodePicPath() {
        return qrCodePicPath;
    }

    public void setQrCodePicPath(String qrCodePicPath) {
        this.qrCodePicPath = qrCodePicPath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.phoneNum);
        dest.writeString(this.qrCodePicPath);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    protected QrMangerBean(Parcel in) {
        this.id = in.readInt();
        this.phoneNum = in.readString();
        this.qrCodePicPath = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<QrMangerBean> CREATOR = new Creator<QrMangerBean>() {
        @Override
        public QrMangerBean createFromParcel(Parcel source) {
            return new QrMangerBean(source);
        }

        @Override
        public QrMangerBean[] newArray(int size) {
            return new QrMangerBean[size];
        }
    };
}
