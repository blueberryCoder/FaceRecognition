package com.blueberry.facerec;

/**
 * Created by blueberry on 11/16/2016.
 * <p>
 * 人脸检测
 */
public class FaceDetector {

    static {
        System.loadLibrary("face_detect");
    }

    private long cPtr;

    public FaceDetector(String path) {
        cPtr = init(path);
    }

    /**
     * 初始化CascadeClassifier,需要传入xml文件的路径，CascadeClassifier会根据这个xml文件进行识别。
     *
     * @param path xml文件的路径
     * @return CascadeClassifier的路径
     */
    public final native long init(String path);

    /**
     * 人脸检测
     *
     * @param path 人脸图片文件的路径
     * @return 检测到人脸数据的集合
     */
    public final native Face[] detect(String path);

    /**
     * 删除CascadeClassifier
     *
     * @param cPtr CascadeClassifier的指针
     */
    private final native void delete(long cPtr);

    @Override
    protected void finalize() throws Throwable {
        delete(cPtr);
    }


}
