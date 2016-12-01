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

/**
 *
 * @author Giampiero Baggiani
 *
 */
public abstract class IonoPiJNIConstants {

	public static final int TTL1 = 7;
	public static final int TTL2 = 25;
	public static final int TTL3 = 28;
	public static final int TTL4 = 29;
	public static final int DI1 = 27;
	public static final int DI2 = 24;
	public static final int DI3 = 23;
	public static final int DI4 = 26;
	public static final int DI5 = 22;
	public static final int DI6 = 21;
	public static final int OC1 = 11;
	public static final int OC2 = 6;
	public static final int OC3 = 5;
	public static final int O1 = 0;
	public static final int O2 = 2;
	public static final int O3 = 3;
	public static final int O4 = 4;
	public static final int LED = 1;

	public static final int AI1 = 0b01000000; // MCP_CH1
	public static final int AI2 = 0b00000000; // MCP_CH0
	public static final int AI3 = 0b10000000; // MCP_CH2
	public static final int AI4 = 0b11000000; // MCP_CH3

	public static final int INPUT = 0;
	public static final int OUTPUT = 1;

	public static final int LOW = 0;
	public static final int HIGH = 1;

	public static final int ON = HIGH;
	public static final int OFF = LOW;

	public static final int CLOSED = HIGH;
	public static final int OPEN = LOW;

	public static final int INT_EDGE_FALLING = 1;
	public static final int INT_EDGE_RISING = 2;
	public static final int INT_EDGE_BOTH = 3;

	/**
	 * @throws IOException
	 */
	static void init() throws IOException {
		loadNativeLibrary();
		ionoPiSetup();
	}

	/**
	 * @throws IOException
	 */
	private static void loadNativeLibrary() throws IOException {
		Path tempLib = Files.createTempFile("ionoPiJNI", ".so");
		try (InputStream in = IonoPiJNIConstants.class.getResourceAsStream("libionoPiJNI.so")) {
			Files.copy(in, tempLib, StandardCopyOption.REPLACE_EXISTING);
		}
		System.load(tempLib.toAbsolutePath().toString());
		Files.delete(tempLib);
	}

	public static native int ionoPiSetup();

	public static native void ionoPiPinMode(int pin, int mode);

	public static native void ionoPiDigitalWrite(int output, int value);

	public static native int ionoPiDigitalRead(int di);

	public static native float ionoPiAnalogRead(int ai);

	/*
	 * public static native int ionoPiDigitalInterrupt(int di, void
	 * (*callBack)(int, int));
	 * 
	 * public static native int ionoPi1WireMaxDetectRead(const int ttl, const
	 * int attempts, int *temp, int *rh);
	 * 
	 * public static native int ionoPi1WireBusReadTemperature(const char*
	 * deviceId, int *temp);
	 * 
	 * public static native int ionoPiWiegandMonitor(int interface, int
	 * (*callBack)(int, int, uint64_t));
	 * 
	 * public static native int ionoPi1WireBusGetDevices(char*** ids);
	 */
}
