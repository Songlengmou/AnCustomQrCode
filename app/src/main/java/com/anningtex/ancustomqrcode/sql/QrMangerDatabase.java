package com.anningtex.ancustomqrcode.sql;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.anningtex.ancustomqrcode.bean.OrderNoAllDataBean;
import com.anningtex.ancustomqrcode.bean.QrMangerBean;
import com.anningtex.ancustomqrcode.sql.dao.OrderNoAllDataDao;
import com.anningtex.ancustomqrcode.sql.dao.QrMangerDao;
import com.anningtex.ancustomqrcode.sql.db.ConversionFactory;

/**
 * @Author Song
 * @Desc:识别到的二维码图片保存管理 / 获取所有orderNo
 */
@Database(entities = {QrMangerBean.class, OrderNoAllDataBean.class}, version = 1, exportSchema = false)
@TypeConverters({ConversionFactory.class})
public abstract class QrMangerDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "QrPicManger.db";

    public static QrMangerDatabase getDefault(Context context) {
        return buildDatabase(context);
    }

    private static QrMangerDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), QrMangerDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public abstract QrMangerDao getQrMangerDao();

    public abstract OrderNoAllDataDao getOrderNoAllDataDao();
}
