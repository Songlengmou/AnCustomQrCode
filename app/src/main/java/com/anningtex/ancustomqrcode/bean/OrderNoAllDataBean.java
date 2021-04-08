package com.anningtex.ancustomqrcode.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * @Author Song
 * desc:获取所有orderNo
 */
@Entity
public class OrderNoAllDataBean {
    @NotNull
    @PrimaryKey(autoGenerate = true)
    private int posId;
    private int olid;
    private String orderNo;

    public OrderNoAllDataBean() {
    }

    @Ignore
    public OrderNoAllDataBean(int olid, String orderNo) {
        this.olid = olid;
        this.orderNo = orderNo;
    }

    @Ignore
    public OrderNoAllDataBean(int olid) {
        this.olid = olid;
    }

    public int getPosId() {
        return posId;
    }

    public void setPosId(int posId) {
        this.posId = posId;
    }

    public int getOlid() {
        return olid;
    }

    public void setOlid(int olid) {
        this.olid = olid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "OrderNoAllData{" +
                "posId=" + posId +
                ", olid=" + olid +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}
