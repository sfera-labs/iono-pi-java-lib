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

package cc.sferalabs.libs.iono_pi.onewire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.sferalabs.libs.iono_pi.IonoPi.DigitalIO;
import cc.sferalabs.libs.iono_pi.jni.IonoPiJNI;

/**
 *
 * @author Giampiero Baggiani
 *
 */
public class OneWire {

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<OneWireBusDevice> getBusDevices() throws IOException {
		String[] ids = IonoPiJNI.ionoPi1WireBusGetDevices();
		if (ids == null) {
			throw new IOException("Devices discovery error. Have you enabled the 1-wire bus?");
		}
		List<OneWireBusDevice> list = new ArrayList<>(ids.length);
		for (String id : ids) {
			list.add(new OneWireBusDevice(id));
		}
		return list;
	}

	/**
	 * 
	 * @param ttl
	 * @param attempts
	 * @return
	 * @throws IOException
	 */
	public int[] maxDetectRead(DigitalIO ttl, int attempts) throws IOException {
		int[] temp_rh = IonoPiJNI.ionoPi1WireMaxDetectRead(ttl.pin, attempts);
		if (temp_rh == null) {
			throw new IOException("Could not read");
		}
		return temp_rh;
	}

}
