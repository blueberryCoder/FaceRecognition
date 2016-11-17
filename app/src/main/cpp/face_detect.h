//
// Created by Administrator on 11/16/2016.
//

#ifndef FACERECOGNITION_FACE_DETECT_H
#define FACERECOGNITION_FACE_DETECT_H

#include "lang.h"
#include <opencv2/core.hpp>
#include <opencv2/objdetect.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL
        Java_com_blueberry_facerec_FaceDetector_init(JNIEnv *env, jobject instance,jstring path_);


JNIEXPORT jobjectArray JNICALL
        Java_com_blueberry_facerec_FaceDetector_detect(JNIEnv *env, jobject instance,
                                                       jstring path_);

JNIEXPORT void JNICALL
        Java_com_blueberry_facerec_FaceDetector_delete(JNIEnv *env, jobject instance, jlong cptr);

#ifdef  __cplusplus
};
#endif

#endif //FACERECOGNITION_FACE_DETECT_H
