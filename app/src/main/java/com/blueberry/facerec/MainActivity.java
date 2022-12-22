package com.blueberry.facerec;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

// http://www.milbo.org/stasm-files/stasm4.pdf
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final int SELECT_IMAGE = 200;
    private String xmlPath;

    private String fileName = "lbpcascade_frontalface.xml";

    //测试图片的存位置
    private String picPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "test.jpg";

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picPath = this.getExternalCacheDir().getAbsolutePath() + File.pathSeparator + "test.jpg";
        setContentView(R.layout.activity_main);
        xmlPath = getCacheDir().getPath();
        new Thread(new WriteXmlRunnable()).start();
        findViewById(R.id.btn).setOnClickListener(new MyOnClickListener());
        tv = (TextView) findViewById(R.id.tv);

        checkPermission();
    }

    /**
     * 6.0 权限申请
     */
    private void checkPermission() {
        if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE}, 100);
        }
    }


    public void selectImage(int requestCode) {
        final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE}, 100);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            Uri uri = data.getData();
            String path = MediaStoreUtils.getPathFromUri(this, uri);
            new Thread() {
                @Override
                public void run() {
                    final Face[] faces = new FaceDetector(xmlPath + "/" + fileName)
                            .detect(path);
                    Log.d(TAG, "run: detect:" + Arrays.toString(faces));
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(Arrays.toString(faces));
                        }
                    });
                }
            }.start();
        }
    }

class MyOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(final View v) {
        selectImage(SELECT_IMAGE);
    }
}



class WriteXmlRunnable implements Runnable {

    @Override
    public void run() {
        writeXml();
    }

    private void writeXml() {
        File dstFile = new File(xmlPath, fileName);
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        OutputStream out = null;
        try {
            inputStream = assetManager.open(fileName);
            out = new FileOutputStream(dstFile);
            int len = -1;
            byte[] bytes = new byte[8096];
            while ((len = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.close(inputStream);
            FileUtil.close(out);
        }
    }
}

}
