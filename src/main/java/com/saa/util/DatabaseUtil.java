package com.saa.util;

import java.io.File;

public class DatabaseUtil {


    public static String getPath() {
        String userHome = System.getProperty("user.home");
        String appFolder = ".saa";

        File dir = new File(userHome, appFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dbFile = new File(dir, "data.db");
        return dbFile.getAbsolutePath();
    }


}
