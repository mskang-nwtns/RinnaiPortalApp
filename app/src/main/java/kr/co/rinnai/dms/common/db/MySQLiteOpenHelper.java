package kr.co.rinnai.dms.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.Nullable;
import android.util.Log;

import androidx.annotation.Nullable;
import kr.co.rinnai.dms.common.CommonValue;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = " CREATE TABLE   " + CommonValue.SQLITE_DB_TABLE_NAME_USER_INFO
                + " (id INTEGER  primary key autoincrement, "
                + CommonValue.SQLITE_DB_TABLE_USER_INFO_FILED_NAME_CLIENT_ID + " TEXT , "
                + CommonValue.SQLITE_DB_TABLE_USER_INFO_FILED_NAME_ACCESS_TOKEN + " TEXT, "
                + CommonValue.SQLITE_DB_TABLE_USER_INFO_FILED_NAME_REFRESH_TOKEN + " TEXT, "
                + CommonValue.SQLITE_DB_TABLE_USER_INFO_FILED_NAME_CODE + " TEXT, "
                + CommonValue.SQLITE_DB_TABLE_USER_INFO_FILED_NAME_ACCESS_TOKEN_CREATE_DATE + " INTEGER, "
                + CommonValue.SQLITE_DB_TABLE_USER_INFO_FILED_NAME_REFRESH_TOKEN_CREATE_DATE + " INTEGER, "
                + CommonValue.SQLITE_DB_TABLE_USER_INFO_FILED_NAME_EXPIRES_TIME + " INTEGER " + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);

        String primaryKey = String.format(" primary key( %s, %s, %s, %s ) " ,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
                CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
                CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE
                );

        sql = " CREATE TABLE  " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE + " CHAR(21)  NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME + " VARCHAR(64) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE + " CHAR(5) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME + " VARCHAR(64) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE + " CHAR(9) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE+ " CHAR(3) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS + " INTEGER NOT NULL,"
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS + " INTEGER NOT NULL, "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);

        primaryKey = String.format(" primary key( %s, %s, %s ) " ,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
                CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE
        );

        sql = " CREATE TABLE   " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE + " CHAR(21) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE + " CHAR(5) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE + " CHAR(9) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE + " CHAR(15) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS + " TEXT , "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);


        primaryKey = String.format(" primary key( %s ) " ,
                CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_ID
        );

        sql = " CREATE TABLE   " + CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_ID + " CHAR(1) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO + " VARCHAR(32) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NAME + " VARCHAR(16), "
                + CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_CODE + " VARCHAR(8), "
                + CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_NAME + " VARCHAR(32), "
                + CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO + " CHAR(1) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_GROUP_WARE_ID + " VARCHAR(32) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_LOGIN_ID_TYPE + " CHAR(1) NOT NULL, "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);


        primaryKey = String.format(" primary key( %s, %s, %s, %s ) " ,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
                CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
                CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE
        );

        sql = " CREATE TABLE  " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE + " CHAR(21)  NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME + " VARCHAR(64) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE + " CHAR(5) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME + " VARCHAR(64) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE + " CHAR(9) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE+ " CHAR(3) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS + " INTEGER NOT NULL,"
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS + " INTEGER NOT NULL, "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);


        primaryKey = String.format(" primary key( %s, %s, %s ) " ,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
                CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE
        );

        sql = " CREATE TABLE   " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE + " CHAR(21) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE + " CHAR(5) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE + " CHAR(9) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE + " CHAR(15) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS + " TEXT , "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);

        //COUPANG

        primaryKey = String.format(" primary key( %s, %s, %s, %s, %s ) " ,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
                CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
                CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE,
                CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK
        );

        sql = " CREATE TABLE  " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_COUPANG
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE + " CHAR(21)  NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME + " VARCHAR(64) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE + " CHAR(5) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME + " VARCHAR(64) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE + " CHAR(9) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE+ " CHAR(3) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS + " INTEGER NOT NULL,"
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS + " INTEGER NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK + " VARCHAR(64) NOT NULL, "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);


        primaryKey = String.format(" primary key( %s, %s, %s ) " ,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
                CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE
        );

        sql = " CREATE TABLE   " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE + " CHAR(21) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE + " CHAR(5) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE + " CHAR(9) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE + " CHAR(15) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS + " TEXT , "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ + " VARCHAR(4), "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK + " VARCHAR(64) NOT NULL, "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);

        primaryKey = String.format(" primary key( %s, %s, %s, %s ) " ,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
                CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
                CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE
        );

        sql = " CREATE TABLE  " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_RETAILER
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE + " CHAR(21)  NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME + " VARCHAR(64) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE + " CHAR(5) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME + " VARCHAR(64) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE + " CHAR(9) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE+ " CHAR(3) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS + " INTEGER NOT NULL,"
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS + " INTEGER NOT NULL, "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);


        primaryKey = String.format(" primary key( %s, %s, %s ) " ,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
                CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
                CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE
        );

        sql = " CREATE TABLE   " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_RETAILER
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE + " CHAR(21) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE + " CHAR(5) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE + " CHAR(9) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE + " CHAR(15) NOT NULL, "
                + CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS + " TEXT , "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);

        primaryKey = String.format(" primary key( %s ) " ,
                CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_USER_NO
        );

        sql = " CREATE TABLE   " + CommonValue.SQLITE_DB_TABLE_NAME_PRODUCT_MOVE_HANDLING
                + " ( "
                + CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_USER_NO + " CHAR(8) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_OUT_DATA + " CHAR(5) NOT NULL , "
                + CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_IN_DATA + " CHAR(9) NOT NULL, "
                + primaryKey + ");";
        Log.d("DB","sql : " + sql);
        db.execSQL(sql);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_USER_INFO + ";");
        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO + ";");
        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO + ";");
        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO + ";");
        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK + ";");
        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK + ";");

        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_COUPANG + ";");
        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG + ";");

        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_RETAILER + ";");
        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_RETAILER + ";");

        db.execSQL("DROP TABLE IF EXISTS " + CommonValue.SQLITE_DB_TABLE_NAME_PRODUCT_MOVE_HANDLING + ";");

        onCreate(db);
    }

}
