package com.zaozao.comics.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;


/**
 * Created by 胡章孝 on 2016/8/5.
 */
public class AppConfig {

    private static AppConfig appConfig;
    private SQLiteDatabase db;
    private String sdCardPath;

    private AppConfig() {

    }

    public static synchronized AppConfig getInstance() {

        if (appConfig == null) {
            appConfig = new AppConfig();
        }
        return appConfig;
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public void clearCache(Context context) {
        db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().toString() + "_nohttp_cache_db.db", null);
        db.execSQL("drop table cache_table");
    }

    /**
     * 判断SD卡是否可用
     * 如果可用则返回SD卡的根目录路径
     *
     * @return
     */
    public static String sdCardPath() {
        String sdRoot = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            sdRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return sdRoot;
    }

    public void writeToSDCard(String folder, String file) {
        //查找SD卡
        File sdFile = new File(Environment.getExternalStorageDirectory().getPath());
        //在SD卡上建立文件夹
        File newFolder = new File(sdFile, folder);
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }
    }

    /**
     * 创建根文件夹
     * @param parent
     * @return
     */
    public File createRootFolder(String parent) {
        File file = new File(sdCardPath(), parent);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    /**
     * 创建子文件夹
     * @param parent
     * @param child
     * @return
     */
    public File createFolder(String parent ,String child){
        File file = new File(parent,child);
        if(!file.exists()){
            file.mkdirs();
        }
        return  file;
    }
}
