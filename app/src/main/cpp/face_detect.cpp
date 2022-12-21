//
// Created by Administrator on 11/16/2016.
//

#ifndef FACERECOGNITION_FACE_DETECT_CPP
#define FACERECOGNITION_FACE_DETECT_CPP

#include "face_detect.h"

using namespace cv;
using namespace std;

CascadeClassifier *g_CascadeClassifier;

JNIEXPORT jlong JNICALL
Java_com_blueberry_facerec_FaceDetector_init(JNIEnv *env, jobject instance, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, NULL);
    g_CascadeClassifier = new CascadeClassifier(path);
    long ptr = reinterpret_cast<long>(g_CascadeClassifier);
    env->ReleaseStringUTFChars(path_, path);
    LOGD("cascadeClassifier initialized");
    return ptr;
}

JNIEXPORT jobjectArray JNICALL
Java_com_blueberry_facerec_FaceDetector_detect(JNIEnv *env, jobject instance, jstring path_) {

    if (!g_CascadeClassifier) {
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "CascadeClassifier not initialized!");
    }
    // 图片的路径
    const char *path = env->GetStringUTFChars(path_, 0);
    //单通道模式,读取图片
    Mat srcImage = imread(path, IMREAD_GRAYSCALE);
    //直方图均值化
    equalizeHist(srcImage, srcImage);
    vector<Rect> vectors;
//    g_CascadeClassifier->detectMultiScale(srcImage, vectors);
    LOGD("detected.");
    jclass objectClass = env->FindClass("com/blueberry/facerec/Face");
    // 返回的人脸数组
    jobjectArray result = env->NewObjectArray(vectors.size(), objectClass, NULL);
    jmethodID cId = env->GetMethodID(objectClass, "<init>", "()V");
    jfieldID xId = env->GetFieldID(objectClass, "x", "I");
    jfieldID yId = env->GetFieldID(objectClass, "y", "I");
    jfieldID widthId = env->GetFieldID(objectClass, "width", "I");
    jfieldID heightId = env->GetFieldID(objectClass, "height", "I");
    for (int i = 0; i < vectors.size(); i++) {
        jobject obj = env->NewObject(objectClass, cId);
        env->SetIntField(obj, xId, vectors[i].x);
        env->SetIntField(obj, yId, vectors[i].y);
        env->SetIntField(obj, widthId, vectors[i].width);
        env->SetIntField(obj, heightId, vectors[i].height);
        env->SetObjectArrayElement(result, i, obj);
    }
    env->ReleaseStringUTFChars(path_, path);
    return result;
}

JNIEXPORT void JNICALL
Java_com_blueberry_facerec_FaceDetector_delete(JNIEnv *env, jobject instance, jlong cptr) {
    cv::CascadeClassifier *cascadeClassifier = reinterpret_cast<cv::CascadeClassifier *>(cptr);
    delete cascadeClassifier;
    return;
}

#endif //FACERECOGNITION_FACE_DETECT_CPP
