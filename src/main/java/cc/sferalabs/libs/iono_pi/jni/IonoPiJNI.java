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

package cc.sferalabs.libs.iono_pi.jni;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import cc.sferalabs.libs.iono_pi.DigitalInputListener;
import cc.sferalabs.libs.iono_pi.IonoPi.DigitalInput;

/**
 *
 * @author Giampiero Baggiani
 *
 */
public abstract class IonoPiJNI {

	private static final Map<Integer, InputListenerCouple> listeners = new HashMap<>(6);
	private static Boolean initialized = false;

	/**
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		synchronized (initialized) {
			if (!initialized) {
				loadNativeLibrary();
				if (!ionoPiSetup()) {
					throw new Exception("Iono Pi setup error");
				}
				initialized = true;
			}
		}
	}

	/**
	 * @throws IOException
	 */
	private static void loadNativeLibrary() throws IOException {
		Path tempLib = Files.createTempFile("ionoPiJNI", ".so");
		try (InputStream in = IonoPiJNI.class.getResourceAsStream("libionoPiJNI.so")) {
			Files.copy(in, tempLib, StandardCopyOption.REPLACE_EXISTING);
		}
		System.load(tempLib.toAbsolutePath().toString());
		Files.delete(tempLib);
	}

	private static native boolean ionoPiSetup();

	public static native void ionoPiPinMode(int pin, int mode);

	public static native void ionoPiDigitalWrite(int output, int value);

	public static native int ionoPiDigitalRead(int di);

	public static native int ionoPiAnalogRead(int ai);
	
	public static native float ionoPiVoltageRead(int ai);

	public static native int ionoPiDigitalInterrupt(int di, int mode, boolean enable);

	public static void ionoPiSetDigitalInputListener(DigitalInput di, int mode,
			DigitalInputListener listener) {
		if (listener == null) {
			listeners.put(di.pin, null);
			ionoPiDigitalInterrupt(di.pin, mode, false);
		} else {
			listeners.put(di.pin, new InputListenerCouple(di, listener));
			ionoPiDigitalInterrupt(di.pin, mode, true);
		}

	}

	private static void digitalInterruptCallback(int di, int value) {
		InputListenerCouple il = listeners.get(di);
		if (il != null) {
			il.listener.onChange(il.di, value == IonoPiJNIConstants.HIGH);
		}
	}

	public static native String[] ionoPi1WireBusGetDevices();

	public static native int ionoPi1WireBusReadTemperature(String deviceId, int attempts,
			int errorValue);

	public static native int[] ionoPi1WireMaxDetectRead(int ttl, int attempts);

	public static native boolean ionoPiWiegandMonitor(int wi, WiegandListenerJNIWrapper listener);

	public static native boolean ionoPiWiegandStop(int wInterface);

}
