package com.blueberry.facerec;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by blueberry on 11/17/2016.
 */

public class FileUtil {

    public static void close(Closeable closeable){
        if (closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
