/*-
 * +======================================================================+
 * Iono Pi Java library
 * ---
 * Copyright (C) 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

#include <ionoPi.h>
#include "cc_sferalabs_libs_iono_pi_jni_IonoPiJNI.h"

static JavaVM *jvm;
static jclass thisClassCached;
static jmethodID digitalInterruptCallbackCached;

static jobject wiegandListener[2];
static JNIEnv *wiegandEnv[2];
static jmethodID wiegandDataCallbackCached;

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiSetup
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiSetup(
		JNIEnv *env, jclass thisClass) {
	return ionoPiSetup();
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiPinMode
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiPinMode(
		JNIEnv *env, jclass thisClass, jint pin, jint mode) {
	ionoPiPinMode(pin, mode);
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiDigitalWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiDigitalWrite
(JNIEnv *env, jclass thisClass, jint output, jint value) {
	ionoPiDigitalWrite(output, value);
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiDigitalRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiDigitalRead(
		JNIEnv *env, jclass thisClass, jint di) {
	return ionoPiDigitalRead(di);
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiAnalogRead
 * Signature: (I)F
 */
JNIEXPORT jint JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiAnalogRead(
		JNIEnv *env, jclass thisClass, jint ai) {
	return ionoPiAnalogRead(ai);
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiVoltageRead
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiVoltageRead(
		JNIEnv *env, jclass thisClass, jint ai) {
	return ionoPiVoltageRead(ai);
}

void callDdigitalInterruptListener(int di, int val) {
	if (NULL != thisClassCached && NULL != digitalInterruptCallbackCached) {
		JNIEnv *env;
		jint rs = (*jvm)->AttachCurrentThread(jvm, (void**) &env, NULL);
		if (rs != JNI_OK) {
			return;
		}

		(*env)->CallStaticVoidMethod(env, thisClassCached,
				digitalInterruptCallbackCached, di, val);

		(*jvm)->DetachCurrentThread(jvm);
	}
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiDigitalInterrupt
 * Signature: (IIZ)I
 */
JNIEXPORT jint JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiDigitalInterrupt(
		JNIEnv *env, jclass thisClass, jint di, jint mode, jboolean enable) {
	if (NULL == jvm) {
		jint rs = (*env)->GetJavaVM(env, &jvm);
		if (rs != JNI_OK) {
			return FALSE;
		}
	}

	if (NULL == thisClassCached) {
		thisClassCached = (*env)->NewGlobalRef(env, thisClass);
	}

	if (NULL == digitalInterruptCallbackCached) {
		digitalInterruptCallbackCached = (*env)->GetStaticMethodID(env,
				thisClassCached, "digitalInterruptCallback", "(II)V");
		if (NULL == digitalInterruptCallbackCached) {
			return FALSE;
		}
	}

	return ionoPiDigitalInterrupt(di, mode,
			enable ? callDdigitalInterruptListener : NULL);
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPi1WireBusGetDevices
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPi1WireBusGetDevices(
		JNIEnv *env, jclass thisClass) {
	char** ids = NULL;
	int count = ionoPi1WireBusGetDevices(&ids);
	if (count < 0) {
		return NULL;
	}

	jclass classString = (*env)->FindClass(env, "java/lang/String");
	jobjectArray outJNIArray = (*env)->NewObjectArray(env, count, classString,
			NULL);

	int i;
	for (i = 0; i < count; ++i) {
		jstring id = (*env)->NewStringUTF(env, ids[i]);
		(*env)->SetObjectArrayElement(env, outJNIArray, i, id);
	}

	return outJNIArray;
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPi1WireBusReadTemperature
 * Signature: (Ljava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPi1WireBusReadTemperature(
		JNIEnv *env, jclass thisClass, jstring deviceId, jint attempts,
		jint errorValue) {
	const char *id = (*env)->GetStringUTFChars(env, deviceId, NULL);
	if (NULL == id) {
		return errorValue;
	}

	int temp;
	if (!ionoPi1WireBusReadTemperature(id, attempts, &temp)) {
		temp = errorValue;
	}
	(*env)->ReleaseStringUTFChars(env, deviceId, id);

	return temp;
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPi1WireMaxDetectRead
 * Signature: (II)[I
 */
JNIEXPORT jintArray JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPi1WireMaxDetectRead(
		JNIEnv *env, jclass thisClass, jint ttl, jint attempts) {
	int temp;
	int rh;
	if (ionoPi1WireMaxDetectRead(ttl, attempts, &temp, &rh)) {
		jintArray ret = (*env)->NewIntArray(env, 2);
		if (NULL == ret) {
			return NULL;
		}
		jint temp_rh[] = { temp, rh };
		(*env)->SetIntArrayRegion(env, ret, 0, 2, temp_rh);
		return ret;
	} else {
		return NULL;
	}
}

int callWiegandListener(int interface, int bitCount, uint64_t data) {
	int idx = interface - 1;
	jobject listener = wiegandListener[idx];
	JNIEnv *env = wiegandEnv[idx];
	if (NULL != listener) {
		return (*env)->CallBooleanMethod(env, listener,
				wiegandDataCallbackCached, interface, bitCount, data);
	}
	return FALSE;
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiWiegandMonitor
 * Signature: (ILcc/sferalabs/libs/iono_pi/jni/WiegandListenerJNI;)Z
 */
JNIEXPORT jboolean JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiWiegandMonitor(
		JNIEnv *env, jclass thisClass, jint interface, jobject listener) {
	if (NULL == wiegandDataCallbackCached) {
		jclass listenerClass = (*env)->GetObjectClass(env, listener);
		if (NULL == listenerClass) {
			return FALSE;
		}
		wiegandDataCallbackCached = (*env)->GetMethodID(env, listenerClass,
				"onData", "(IIJ)Z");
		if (NULL == wiegandDataCallbackCached) {
			return FALSE;
		}
		(*env)->DeleteLocalRef(env, listenerClass);
	}
	int idx = interface - 1;
	if (idx != 0 && idx != 1) {
		return FALSE;
	}
	wiegandEnv[idx] = env;
	wiegandListener[idx] = listener;
	return ionoPiWiegandMonitor(interface, callWiegandListener);
}

/*
 * Class:     cc_sferalabs_libs_iono_pi_jni_IonoPiJNI
 * Method:    ionoPiWiegandStop
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_cc_sferalabs_libs_iono_1pi_jni_IonoPiJNI_ionoPiWiegandStop(
		JNIEnv *env, jclass thisClass, jint interface) {
	return ionoPiWiegandStop(interface);
}
