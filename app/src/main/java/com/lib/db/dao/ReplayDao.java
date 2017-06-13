package com.lib.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lib.db.DBOpenHelper;
import com.ma.qqmsg.model.Replay;

import java.lang.reflect.Field;

/**
 * 会话 数据库操作
 */
public class ReplayDao extends BaseDao {
    /**
     */
    public static synchronized void updateOrInsert(Replay model) {
        SQLiteDatabase db = getDb();
        ContentValues values = new ContentValues();
        for (int i = 1; i < DBOpenHelper.ReplyCols.length; i++) {
            //从i == 1开始 ，就是不包含id到内部
            try {
                Class<?> clazz = model.getClass();
                Field field = clazz.getDeclaredField(DBOpenHelper.ReplyCols[i]);
                field.setAccessible(true);
                //数据置空
                String value = (String) field.get(model);
                value = value == null ? "" : value;
                values.put(DBOpenHelper.ReplyCols[i],value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long affectLines = db.update(DBOpenHelper.TbReply, values, DBOpenHelper.Col.TO_ID + "=?", transParams(model.getTo_id()));
        if (affectLines <= 0) {
            db.insert(DBOpenHelper.TbReply, null, values); //说明不存在，则要添加
        }
        //.......;
    }

    public static synchronized void delete(String to_id) {
        SQLiteDatabase db = getDb();
        db.delete(DBOpenHelper.TbReply, DBOpenHelper.Col.TO_ID + "=?", transParams(to_id));
        //.......;
    }

    public static synchronized Replay getReplayByToId(final String to_id, final Listener listener){
        Cursor cursor = null;
        SQLiteDatabase db = getDb();
        try {
            cursor = db.rawQuery("select * from " + DBOpenHelper.TbReply + " where " + DBOpenHelper.Col.TO_ID + " =?", transParams(to_id));
            if (cursor.moveToFirst()) {
                //进行数据的添加
                Class clazz = Replay.class;
                Replay model = (Replay) clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < DBOpenHelper.ReplyCols.length; i++) {
                    try {
                        Field f1 = clazz.getDeclaredField(DBOpenHelper.ReplyCols[i]);
                        f1.setAccessible(true);//设置是否可以操作私有属性
                        int columIndex = cursor.getColumnIndex(DBOpenHelper.ReplyCols[i]);
                        if (i == 0) {
                            f1.set(model, cursor.getInt(columIndex));
                        } else {
                            f1.set(model, cursor.getString(columIndex));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(listener != null){
                    listener.success(model);
                }
                return model;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            //......;
        }
        return null;
    }

    public static synchronized void enableAll(boolean isEnable) {
        SQLiteDatabase db = getDb();
        db.execSQL("update " + DBOpenHelper.TbReply + " set " + DBOpenHelper.Col.IS_ENABLE + " = "+(isEnable ? "1" : "0") +"");
        //.......;
    }

    public interface Listener {
        void success(Replay model);
        void fail(int errCode, String errMsg);
    }
}
