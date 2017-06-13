package com.lib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ma.qqmsg.MyApplication;

/**
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static int DbVersion = 1;//版本1  （版本更替记录）
    private static String DbName = "im.db";

    public static String TbReply = "TbReply";//自动回复
    /**
     * 消息表
     */
    public static String[] ReplyCols= {
            Col.ID,//ID不能被移动
            Col.TO_ID,
            Col.CONTENT,
            Col.IS_ENABLE,
            Col.IS_ENABLE_AI,
    };//新增字段往后添加，不往中间插入

    private static DBOpenHelper mInstance = null;

    public synchronized static DBOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBOpenHelper(context);
        }
        return mInstance;
    }

    public synchronized static DBOpenHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DBOpenHelper(MyApplication.getInstance());
        }
        return mInstance;
    }

    public DBOpenHelper(Context context) {
        super(context, DbName, null, DbVersion);//之后会检查数据库是否存在，
        //如果存在看版本号是否相同，如果不同则调用onUpgrade方法
        //如果相同则不做任何修改。如果是第一次创建，则调用onCreate方法
        // "itcast.db"数据库名称，2为版本号
        //存放的位子为：
        //<包>/databases/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //是在数据库第一次被创建的时候调用的
        createTb(TbReply, ReplyCols, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createTb(TbReply, ReplyCols, db);
    }


    private void createTb(String table, String[] colums, SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
        StringBuilder createSql = new StringBuilder("");
        for (int i = 0; i < colums.length; i++) {
            if (i == 0) {
                createSql.append(" " + colums[i] + " integer primary key autoincrement");
            } else {
                createSql.append(", " + colums[i] + " varchar DEFAULT NULL ");
            }
        }
        String sql = "CREATE TABLE " + table + "(" + createSql.toString() + ")";
        Log.e("DBHelper", sql);
        db.execSQL(sql);
    }

    public interface Col {
        /*integer*/ String ID = "id"; // 主键
        /*varchar*/ String TO_ID = "to_id"; //针对设置的唯一id
        /*varchar*/ String CONTENT = "content";//内容
        /*varchar*/ String IS_ENABLE ="is_enable";//是否生效  1 : 是 0 未fou
        /*varchar*/ String IS_ENABLE_AI ="is_enable_ai";//是否生效智能回复  1 : 是 0 未fou

    }
}
