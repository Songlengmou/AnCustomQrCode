package com.anningtex.ancustomqrcode.sql.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.anningtex.ancustomqrcode.bean.OrderNoAllDataBean;

/**
 * @Author Song
 * desc:获取所有orderNo
 */
@Dao
public interface OrderNoAllDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addOrderNoAllData(OrderNoAllDataBean allData);

    /**
     * 根据olid查询对应的orderNo
     */
    @Query("SELECT * FROM OrderNoAllDataBean WHERE olid=:olid")
    OrderNoAllDataBean queryFromOlidToOrderNo(int olid);

    @Delete()
    void deleteOrderNoAllData(OrderNoAllDataBean allData);

    /**
     * 总数量
     */
    @Query("SELECT count(*) FROM OrderNoAllDataBean")
    Integer queryOrderNoAllData();
}
