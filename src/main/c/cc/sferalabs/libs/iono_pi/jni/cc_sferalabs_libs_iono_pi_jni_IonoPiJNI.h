/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class cc_sferalabs_libs_iono_pi_jni_IonoPiJNI */

#ifndef _Included_cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
#define _Included_cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiSetup
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiSetup
  (JNIEnv *, jclass);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiPinMode
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiPinMode
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiDigitalWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiDigitalWrite
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiDigitalRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiDigitalRead
  (JNIEnv *, jclass, jint);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiAnalogRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiAnalogRead
  (JNIEnv *, jclass, jint);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiVoltageRead
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiVoltageRead
  (JNIEnv *, jclass, jint);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiDigitalInterrupt
 * Signature: (IIZ)I
 */
JNIEXPORT jint JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiDigitalInterrupt
  (JNIEnv *, jclass, jint, jint, jboolean);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPi1WireBusGetDevices
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPi1WireBusGetDevices
  (JNIEnv *, jclass);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPi1WireBusReadTemperature
 * Signature: (Ljava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPi1WireBusReadTemperature
  (JNIEnv *, jclass, jstring, jint, jint);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPi1WireMaxDetectRead
 * Signature: (II)[I
 */
JNIEXPORT jintArray JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPi1WireMaxDetectRead
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiWiegandMonitor
 * Signature: (ILcc/sferalabs/libs/iono_pi/jni/WiegandListenerJNIWrapper;)Z
 */
JNIEXPORT jboolean JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiWiegandMonitor
  (JNIEnv *, jclass, jint, jobject);

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiWiegandStop
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiWiegandStop
  (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
