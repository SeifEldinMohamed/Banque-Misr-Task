//
// Created by 96654 on 8/30/2022.
//
#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_package_name_ApiHelper_baseUrlFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string baseURL = "https://domainname/functiontype/";
    return env->NewStringUTF(baseURL.c_str());
}

