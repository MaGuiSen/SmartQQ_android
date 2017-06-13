package com.lib.db.dao;

import android.database.sqlite.SQLiteDatabase;

import com.lib.db.DBOpenHelper;

public class BaseDao {
    static SQLiteDatabase db;
    public static synchronized SQLiteDatabase getDb() {
        if(db == null || !db.isOpen()) {
            db = DBOpenHelper.getInstance().getWritableDatabase();
        }
        return db;
    }

    public static synchronized void closeDb() {
        if(db != null) {
            db.close();
        }
    }

    /**
     * 转换参数，处理null转换成"" 空字符
     * @return
     */
    public static String[] transParams(String... values){
        for(int i=0;i<values.length;i++){
            String value = values[i];
            //数据置空
            values[i] = value == null ? "" : value;
        }
        return values;
    }
}
