
cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_VERBOSE_MAKEFILE ON)
message(STATUS  ${ANDROID_ABI})
set(opencv_root_dir ${CMAKE_SOURCE_DIR}/opencv)
set(INC_DIR ${opencv_root_dir}/include)
set(opencv_lib_dir ${opencv_root_dir}/prebuilt)
set(SOURCE_FILES    src/main/cpp/lang.h
                    src/main/cpp/face_detect.h
                   src/main/cpp/face_detect.cpp)
#添加头文件目录
include_directories(${INC_DIR})
add_library(face_detect SHARED ${SOURCE_FILES})
#添加外部动态链接库
add_library(opencv SHARED IMPORTED)

set_target_properties(opencv PROPERTIES IMPORTED_LOCATION
#    ${opencv_lib_dir}/${ANDROID_ABI}/libopencv_java3.so
        "/Users/bytedance/code/opensource/FaceRecognition/app/opencv/prebuilt/armeabi-v7a/libopencv_java3.so"
        )

target_link_libraries(face_detect  log )
target_link_libraries(face_detect  opencv)
