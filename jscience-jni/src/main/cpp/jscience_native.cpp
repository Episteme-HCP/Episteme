#include <jni.h>
#include <iostream>
#include "org_jscience_jni_NativeDeviceBridge.h"

// Mock implementation of hardware interaction

extern "C" {

JNIEXPORT jboolean JNICALL Java_org_jscience_jni_NativeDeviceBridge_connectDevice
  (JNIEnv *env, jobject obj, jstring deviceId) {
    const char *nativeString = env->GetStringUTFChars(deviceId, 0);
    std::cout << "Native: Connecting to device " << nativeString << std::endl;
    env->ReleaseStringUTFChars(deviceId, nativeString);
    return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_org_jscience_jni_NativeDeviceBridge_disconnectDevice
  (JNIEnv *env, jobject obj, jstring deviceId) {
    const char *nativeString = env->GetStringUTFChars(deviceId, 0);
    std::cout << "Native: Disconnecting from device " << nativeString << std::endl;
    env->ReleaseStringUTFChars(deviceId, nativeString);
}

JNIEXPORT jdouble JNICALL Java_org_jscience_jni_NativeDeviceBridge_readSensorValue
  (JNIEnv *env, jobject obj, jstring deviceId) {
    // Mock random value
    return 42.0 + (rand() % 100) / 10.0;
}

}
