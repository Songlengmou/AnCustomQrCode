package com.anningtex.ancustomqrcode.sql.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.anningtex.ancustomqrcode.bean.QrMangerBean;

import java.util.List;

/**
 * @Author Song
 * @Desc:识别到的二维码图片保存管理
 */
@Dao
public interface QrMangerDao {

    @Query("SELECT * FROM QrMangerBean")
    List<QrMangerBean> getQrMangerBeanAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQrMangerBean(QrMangerBean qrMangerBean);

    @Update()
    void updateQrMangerBean(QrMangerBean qrMangerBean);

    @Delete()
    void deleteQrMangerBean(QrMangerBean qrMangerBean);
}
